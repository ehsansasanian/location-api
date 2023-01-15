package com.drei.demo.service.impl;

import com.drei.demo.api.request.CreateLocationRequest;
import com.drei.demo.api.response.LocationResponse;
import com.drei.demo.domain.Location;
import com.drei.demo.domain.Type;
import com.drei.demo.repository.LocationDao;
import com.drei.demo.repository.LocationRepository;
import com.drei.demo.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final LocationRepository repo;
    private final LocationDao dao;

    @Override
    public LocationResponse save(CreateLocationRequest request) {
        // create a point object from the latitude and longitude in the request object
        var point = geometryFactory.createPoint(new Coordinate(request.latitude(), request.longitude()));
        Location saved = this.repo.save(
                new Location(request.name(), request.type(), request.latitude(), request.longitude(), point)
        );
        return new LocationResponse(saved.getId(), saved.getName(),
                saved.getLatitude(), saved.getLongitude(), saved.getType().toString());
    }

    @Override
    public List<LocationResponse> findLocationsByType(Type type) {
        return this.dao.findLocationsByType(type.toString());
    }

    @Override
    public List<LocationResponse> findLocationsByArea(Double p1Lat, Double p1Long, Double p2Lat, Double p2Long,
                                                      Integer limit) {
        return this.dao.findLocationsByAreaAndType(p1Lat, p1Long, p2Lat, p2Long, "", limit);
    }

    @Override
    public List<LocationResponse> findLocationsByAreaAndType(Double p1Lat, Double p1Long, Double p2Lat,
                                                             Double p2Long, Type type, Integer limit) {
        return this.dao.findLocationsByAreaAndType(p1Lat, p1Long, p2Lat, p2Long, type.toString(), limit);
    }
}
