package com.drei.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private final String name;
    @Enumerated(value = EnumType.STRING)
    private Type type;

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geometry;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Location(String name, Type type, Double latitude, Double longitude, Point point) {
        this.name = name;
        this.type = type;
        this.geometry = point;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
    }
}
