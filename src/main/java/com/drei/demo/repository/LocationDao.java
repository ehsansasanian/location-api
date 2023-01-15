package com.drei.demo.repository;

import com.drei.demo.api.response.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class LocationDao {
    private final Logger LOG = LoggerFactory.getLogger(LocationDao.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public LocationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.parameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<LocationResponse> findLocationsByType(String type) {
        final String sql = "SELECT name AS name, latitude AS latitude, longitude AS longitude, type AS type " +
                "FROM locations WHERE type = ?";
        List<LocationResponse> responses;
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("SQL Query = {}", sql);
            }
            responses = jdbcTemplate.query(sql, getLocationResponseDTORowMapper(), type);
        } catch (Exception e) {
            LOG.error("An error occurred while executing findLocationsByType method: {}", e.getMessage(), e);
            responses = new ArrayList<>();
        }
        return responses;
    }

    /**
     * Retrieves a list of locations within a given rectangular area and of a certain type.
     *
     * @param p1Lat  The latitude coordinate of the first point defining the rectangular area.
     * @param p1Long The longitude coordinate of the first point defining the rectangular area.
     * @param p2Lat  The latitude coordinate of the second point defining the rectangular area.
     * @param p2Long The longitude coordinate of the second point defining the rectangular area.
     * @param type   The type of location to retrieve. If not provided, would not be included in the query.
     * @param limit  The maximum number of locations to retrieve. If not provided, all locations will be retrieved.
     * @return A list of LocationResponse objects that match the specified criteria, and ordered by the type where the premium ones come first.
     */
    public List<LocationResponse> findLocationsByAreaAndType(Double p1Lat, Double p1Long, Double p2Lat,
                                                             Double p2Long, String type, Integer limit) {
        final StringBuilder sql = new StringBuilder("SELECT name AS name, latitude AS latitude, longitude AS longitude, type AS type " +
                "FROM locations WHERE ST_Within(geometry, ST_MakeEnvelope(:p1Lat, :p1Long, :p2Lat, :p2Long, 4326)) ");

        // define parameters for the query
        Map<String, Object> params = new HashMap<>();
        params.put("p1Lat", p1Lat);
        params.put("p1Long", p1Long);
        params.put("p2Lat", p2Lat);
        params.put("p2Long", p2Long);

        if (!type.isEmpty()) {
            sql.append("AND type = :type ");
            params.put("type", type);
        }
        sql.append("ORDER BY type ASC");
        if (limit != null && limit > 0) {
            sql.append(" LIMIT :limit");
            params.put("limit", limit);
        }

        List<LocationResponse> responses;

        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("SQL Query = {}", sql);
            }
            responses = parameterJdbcTemplate.query(sql.toString(), params, getLocationResponseDTORowMapper());
        } catch (Exception e) {
            LOG.error("An error occurred while executing findLocationsByAreaAndType method: {}", e.getMessage(), e);
            responses = new ArrayList<>();
        }
        return responses;
    }

    private static RowMapper<LocationResponse> getLocationResponseDTORowMapper() {
        return (rs, rowNum) -> new LocationResponse(
                null, rs.getString("name"), rs.getDouble("latitude"),
                rs.getDouble("longitude"), rs.getString("type")
        );
    }
}