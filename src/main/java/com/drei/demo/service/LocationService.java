package com.drei.demo.service;

import com.drei.demo.api.request.CreateLocationRequest;
import com.drei.demo.api.response.LocationResponse;
import com.drei.demo.domain.Type;

import java.util.List;

/**
 * The LocationService interface defines a set of methods for managing locations in the system.
 *
 * @author Ehsan Sasanianno
 */
public interface LocationService {

    /**
     * Saves a new location to the system.
     *
     * @param request an object containing the information needed to create a new location
     * @return a LocationResponse object representing the newly created location
     */
    LocationResponse save(CreateLocationRequest request);

    /**
     * Finds all locations of a specific type.
     *
     * @param type the type of location to search for
     * @return a list of LocationResponse objects representing the locations that were found
     */
    List<LocationResponse> findLocationsByType(Type type);

    /**
     * Finds all locations within a specific area.
     *
     * @param p1Lat  the latitude of the first point defining the area
     * @param p1Long the longitude of the first point defining the area
     * @param p2Lat  the latitude of the second point defining the area
     * @param p2Long the longitude of the second point defining the area
     * @param limit  the maximum number of locations to return
     * @return a list of LocationResponse objects representing the locations that were found
     */
    List<LocationResponse> findLocationsByArea(Double p1Lat, Double p1Long, Double p2Lat, Double p2Long, Integer limit);

    /**
     * Finds all locations within a specific area and of a specific type.
     *
     * @param p1Lat  the latitude of the first point defining the area
     * @param p1Long the longitude of the first point defining the area
     * @param p2Lat  the latitude of the second point defining the area
     * @param p2Long the longitude of the second point defining the area
     * @param type   the type of location to search for
     * @param limit  the maximum number of locations to return
     * @return a list of LocationResponse objects representing the locations that were found
     */
    List<LocationResponse> findLocationsByAreaAndType(Double p1Lat, Double p1Long, Double p2Lat, Double p2Long, Type type, Integer limit);
}