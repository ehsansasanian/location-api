package com.drei.demo.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LocationResponse {
    @JsonInclude(Include.NON_NULL)
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String type;
}
