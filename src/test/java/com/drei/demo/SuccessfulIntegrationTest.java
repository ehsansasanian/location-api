package com.drei.demo;

import com.drei.demo.api.request.CreateLocationRequest;
import com.drei.demo.api.response.LocationResponse;
import com.drei.demo.domain.Location;
import com.drei.demo.domain.Type;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.drei.demo.domain.Type.premium;
import static com.drei.demo.domain.Type.standard;
import static org.assertj.core.api.Assertions.assertThat;

public class SuccessfulIntegrationTest extends InitHelper {
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final static String URL = "/location";

    @Test
    public void createLocation_validBody_shouldReturnCreated() {
        double lat = 44.123456;
        double lng = 21.123456;
        Type type = standard;
        ResponseEntity<LocationResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(
                new CreateLocationRequest("createLocation_validBody", lat, lng, type)), LocationResponse.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<LocationResponse> locationsByType = locationDao.findLocationsByType(standard.toString());
        LocationResponse createdLoc = locationsByType.stream()
                .filter(l -> l.getName().equals("createLocation_validBody"))
                .findAny()
                .orElseThrow(AssertionError::new);

        LocationResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getName()).isEqualTo(createdLoc.getName());
        assertThat(body.getLatitude()).isEqualTo(lat);
        assertThat(body.getLongitude()).isEqualTo(lng);
        assertThat(body.getType()).isEqualTo(type.toString());
    }

    @Test
    public void findLocation_byType_shouldReturnValueSuccessful() {
        double lat_1 = 48.291528, lng_1 = 15.524527;
        var point_1 = geometryFactory.createPoint(new Coordinate(lat_1, lng_1));

        locationRepository.save(new Location("findLocation_byType-1", premium, lat_1, lng_1, point_1));
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(
                URL + "/premium", LocationResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<LocationResponse> locationResponses = Arrays.asList(response.getBody());
        Optional<LocationResponse> optionalLocation = locationResponses.stream()
                .filter(l -> l.getName().equals("findLocation_byType-1"))
                .filter(l -> l.getType().equals(premium.toString()))
                .filter(l -> l.getLatitude().equals(lat_1))
                .filter(l -> l.getLongitude().equals(lng_1))
                .findFirst();
        assertThat(optionalLocation.isPresent()).isTrue();
    }

    @Test
    public void findLocation_byArea_shouldReturnValueSuccessful() {
        double lat_2 = -6.840598, lng_2 = -54.070016;
        var point_2 = geometryFactory.createPoint(new Coordinate(lat_2, lng_2));
        locationRepository.save(new Location("findLocation_byArea-2", standard, lat_2, lng_2, point_2));

        double p1_lat = -6.785757, p1_long = -54.150233;
        double p2_lat = -6.919470, p2_long = -53.991330;
        String target_url = String.format("/location/area?p1Lat=%s&p1Long=%s&p2Lat=%s&p2Long=%s", p1_lat, p1_long, p2_lat, p2_long);
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(
                target_url, LocationResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<LocationResponse> body = Arrays.asList(response.getBody());
        Optional<LocationResponse> optionalLocation = body.stream()
                .filter(l -> l.getName().equals("findLocation_byArea-2"))
                .filter(l -> l.getType().equals(standard.toString()))
                .filter(l -> l.getLatitude().equals(lat_2))
                .filter(l -> l.getLongitude().equals(lng_2))
                .findFirst();
        assertThat(optionalLocation.isPresent()).isTrue();
    }

    @Test
    public void findLocation_byAreaAdnType_shouldReturnValueSuccessful() {
        double lat_3 = -6.840598, lng_3 = -54.070016;
        var point_3 = geometryFactory.createPoint(new Coordinate(lat_3, lng_3));
        locationRepository.save(new Location("findLocation_byAreaAdnType-3", premium, lat_3, lng_3, point_3));

        double p1_lat = -6.785757, p1_long = -54.150233;
        double p2_lat = -6.919470, p2_long = -53.991330;
        String target_url = String.format(
                "/location/premium/area?p1Lat=%s&p1Long=%s&p2Lat=%s&p2Long=%s", p1_lat, p1_long, p2_lat, p2_long);
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(
                target_url, LocationResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<LocationResponse> body = Arrays.asList(response.getBody());
        Optional<LocationResponse> optionalLocation = body.stream()
                .filter(l -> l.getName().equals("findLocation_byAreaAdnType-3"))
                .filter(l -> l.getType().equals(premium.toString()))
                .filter(l -> l.getLatitude().equals(lat_3))
                .filter(l -> l.getLongitude().equals(lng_3))
                .findFirst();
        assertThat(optionalLocation.isPresent()).isTrue();
    }
}
