package com.drei.demo.api;

import com.drei.demo.api.request.CreateLocationRequest;
import com.drei.demo.api.response.LocationResponse;
import com.drei.demo.domain.Type;
import com.drei.demo.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponse> create(@RequestBody @Valid CreateLocationRequest request) {
        return new ResponseEntity<>(this.locationService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<LocationResponse>> findLocationsByType(@PathVariable Type type) {
        return ResponseEntity.ok(this.locationService.findLocationsByType(type));
    }

    @GetMapping("/area")
    public ResponseEntity<List<LocationResponse>> findLocationsByArea(
            @RequestParam Double p1Long, @RequestParam Double p1Lat,
            @RequestParam Double p2Long, @RequestParam Double p2Lat,
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(this.locationService.findLocationsByArea(p1Lat, p1Long, p2Lat, p2Long, limit));
    }

    @GetMapping(value = "/{type}/area")
    public ResponseEntity<List<LocationResponse>> findLocationsByAreaAndType(
            @RequestParam Double p1Lat, @RequestParam Double p1Long,
            @RequestParam Double p2Lat, @RequestParam Double p2Long,
            @PathVariable Type type,
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(
                this.locationService.findLocationsByAreaAndType(p1Lat, p1Long, p2Lat, p2Long, type, limit));
    }
}
