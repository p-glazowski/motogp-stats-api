package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.services.TrackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    public List<TrackResponse> getAllTracks() {
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public TrackResponse getTrackById(@PathVariable Long id) {
        return trackService.getTrackById(id);
    }

    @GetMapping("/circuit")
    public TrackResponse getTrackByCircuitName(@RequestParam String circuitName) {
        return trackService.getTrackByCircuitName(circuitName);
    }

    @GetMapping("/country")
    public List<TrackResponse> getTracksByCountry(@RequestParam String country) {
        return trackService.getTracksByCountry(country);
    }

    @GetMapping("/sorted/length-desc")
    public List<TrackResponse> getTracksSortedByLengthDesc() {
        return trackService.getTracksSortedByLengthDesc();
    }

    @PostMapping
    public TrackResponse createTrack(@RequestBody @Valid TrackRequest request) {
        return trackService.createTrack(request);
    }

    @PutMapping("/{id}")
    public TrackResponse updateTrack(@PathVariable Long id, @RequestBody @Valid TrackRequest request) {
        return trackService.updateTrack(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrack(id);

        return ResponseEntity.noContent().build();
    }
}
