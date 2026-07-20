package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
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

    public Track toEntity(TrackRequest request) {
        return new Track(
                request.gpName(),
                request.circuitName(),
                request.country(),
                request.city(),
                request.lengthKm(),
                request.numberOfTurns(),
                request.raceLaps(),
                request.lapRecordTime(),
                request.lapRecordHolder(),
                request.lapRecordYear(),
                request.firstHeldYear()
        );
    }

    public List<TrackResponse> getAllTracks() {
        return trackRepository.findAll().stream().map(this::toDto).toList();
    }

    public TrackResponse getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Track id " + id + " not found"));

        return toDto(track);
    }

    public TrackResponse getTrackByCircuitName(String circuitName) {
        Track track = trackRepository.findByCircuitName(circuitName)
                .orElseThrow(() -> new NotFoundException("Track circuit " + circuitName + " not found"));
        return toDto(track);
    }

    public List<TrackResponse> getTracksByCountry(String country) {
        return trackRepository.findByCountry(country).stream().map(this::toDto).toList();
    }

    public boolean trackExistsByCircuitName(String circuitName) {
        return trackRepository.existsByCircuitName(circuitName);
    }

    public List<TrackResponse> getTracksSortedByLengthDesc() {
        return trackRepository.findAllByOrderByLengthKmDesc().stream().map(this::toDto).toList();
    }

    public TrackResponse createTrack(TrackRequest request) {
        Track track = toEntity(request);
        return toDto(trackRepository.save(track));
    }

    public TrackResponse updateTrack(Long id, TrackRequest request) {
        Track existing = trackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Track id " + id + " not found"));

        existing.setGpName(request.gpName());
        existing.setCircuitName(request.circuitName());
        existing.setCountry(request.country());
        existing.setCity(request.city());
        existing.setLengthKm(request.lengthKm());
        existing.setNumberOfTurns(request.numberOfTurns());
        existing.setRaceLaps(request.raceLaps());
        existing.setLapRecordTime(request.lapRecordTime());
        existing.setLapRecordHolder(request.lapRecordHolder());
        existing.setLapRecordYear(request.lapRecordYear());
        existing.setFirstHeldYear(request.firstHeldYear());

        return toDto(trackRepository.save(existing));
    }

    public void deleteTrack(Long id) {
        if (!trackRepository.existsById(id)) {
            throw new NotFoundException("Track id " + id + " not found");
        }
        trackRepository.deleteById(id);
    }

}
