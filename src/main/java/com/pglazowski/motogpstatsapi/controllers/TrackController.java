package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.services.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name="Tracks",
        description = "MotoGP circuit management API"
)
@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @Operation(
            summary = "Get all MotoGP tracks",
            description = "Returns every circuit stored in the database"
    )
    @GetMapping
    public Page<TrackResponse> getAllTracks(Pageable pageable) {
        return trackService.getAllTracks(pageable);
    }

    @Operation(
            summary = "Get track by ID",
            description = "Returns a single track for the given database ID."
    )
    @GetMapping("/{id}")
    public TrackResponse getTrackById(@PathVariable Long id) {
        return trackService.getTrackById(id);
    }

    @Operation(
            summary = "Find track by circuit name",
            description = "Returns a track matching the specified circuit name."
    )
    @GetMapping("/circuit")
    public TrackResponse getTrackByCircuitName(@RequestParam String circuitName) {
        return trackService.getTrackByCircuitName(circuitName);
    }

    @Operation(
            summary = "Get tracks by country",
            description = "Returns all MotoGP tracks located in the specified country."
    )
    @GetMapping("/country")
    public List<TrackResponse> getTracksByCountry(@RequestParam String country) {
        return trackService.getTracksByCountry(country);
    }

    @Operation(
            summary = "Get tracks sorted by length",
            description = "Returns all tracks sorted by circuit length in descending order."
    )
    @GetMapping("/sorted/length-desc")
    public List<TrackResponse> getTracksSortedByLengthDesc() {
        return trackService.getTracksSortedByLengthDesc();
    }

    @Operation(
            summary = "Create a new track",
            description = "Creates a new MotoGP track and returns the created resource."
    )
    @PostMapping
    public TrackResponse createTrack(@RequestBody @Valid TrackRequest request) {
        return trackService.createTrack(request);
    }

    @Operation(
            summary = "Update an existing track",
            description = "Updates all information for the specified track."
    )
    @PutMapping("/{id}")
    public TrackResponse updateTrack(@PathVariable Long id, @RequestBody @Valid TrackRequest request) {
        return trackService.updateTrack(id, request);
    }

    @Operation(
            summary = "Delete a track",
            description = "Deletes the track with the specified ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrack(id);

        return ResponseEntity.noContent().build();
    }
}
