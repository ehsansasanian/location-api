package com.drei.demo;

import com.drei.demo.api.request.CreateLocationRequest;
import com.drei.demo.domain.Type;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class FailureIntegrationTest extends InitHelper{

    private final static String URL = "/location";

    @Test
    public void createLocation_missingTypeOrName_returnsBadRequest() {
        ResponseEntity<Void> missingTypeResponse = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(
                new CreateLocationRequest("test", 20D, 20D, null)), Void.class
        );
        assertThat(missingTypeResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ResponseEntity<Void> missingNameResponse = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(
                new CreateLocationRequest("test", 20D, 20D, null)), Void.class
        );
        assertThat(missingNameResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createLocation_missingLatOrLong_returnsBadRequest() {
        ResponseEntity<Void> missingLatResponse = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(
                new CreateLocationRequest("test", null, 20D, Type.premium)), Void.class
        );
        assertThat(missingLatResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ResponseEntity<Void> missingLongResponse = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(
                new CreateLocationRequest("test", 20D, null, Type.premium)), Void.class
        );
        assertThat(missingLongResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findLocationsByType_wrongType_returnsBadRequest() {
        ResponseEntity<Void> response = restTemplate.getForEntity(URL + "/PREMIUM", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findLocationsByArea_incompleteCoordinate_returnsBadRequest() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                URL + "/area" + "?p1Long=11.11&p2Long=12.12&p1Lat=13.13", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findLocationsByTypeAndArea_incompleteCoordinate_returnsBadRequest() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                URL + "/standard/area" + "?p1Long=11.11&p2Long=12.12&p1Lat=13.13", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
