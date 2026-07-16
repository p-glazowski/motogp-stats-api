package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }


    public TrackResponse toDto(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getGpName(),
                track.getCircuitName(),
                track.getCountry(),
                track.getCity(),
                track.getLengthKm(),
                track.getNumberOfTurns(),
                track.getRaceLaps(),
                track.getLapRecordTime(),
                track.getLapRecordHolder(),
                track.getLapRecordYear(),
                track.getFirstHeldYear()
        );
    }

    public List<TrackResponse> getAllTracks() {
        return trackRepository.findAll().stream().map(this::toDto).toList();
    }
}
