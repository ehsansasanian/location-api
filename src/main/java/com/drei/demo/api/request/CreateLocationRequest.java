package com.drei.demo.api.request;

import com.drei.demo.domain.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLocationRequest(@NotBlank String name, @NotNull Double latitude,
                                    @NotNull Double longitude, @NotNull Type type) {
    public CreateLocationRequest(String name, Double latitude, Double longitude, Type type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Double latitude() {
        return latitude;
    }

    public Double longitude() {
        return longitude;
    }

    public Type type() {
        return type;
    }
}
