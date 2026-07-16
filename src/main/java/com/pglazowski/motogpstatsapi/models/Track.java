package com.pglazowski.motogpstatsapi.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String city;
    private Double lengthKm;
    private Integer numberOfTurns;
    private Integer raceLaps;
    private Double topSpeedKmh;
    private String lapRecordTime;
    private String lapRecordHolder;
    private Integer lapRecordYear;
    private Integer firstHeldYear;

    public Track(){}
    public Track(
            String name,
            String country,
            String city,
            Double lengthKm,
            Integer numberOfTurns,
            Integer raceLaps,
            Double topSpeedKmh,
            String lapRecordTime,
            String lapRecordHolder,
            Integer lapRecordYear,
            Integer firstHeldYear
    ){
        this.name = name;
        this.country = country;
        this.city = city;
        this.lengthKm = lengthKm;
        this.numberOfTurns = numberOfTurns;
        this.raceLaps = raceLaps;
        this.topSpeedKmh = topSpeedKmh;
        this.lapRecordTime = lapRecordTime;
        this.lapRecordHolder = lapRecordHolder;
        this.lapRecordYear = lapRecordYear;
        this.firstHeldYear = firstHeldYear;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setLengthKm(Double lengthKm) {
        this.lengthKm = lengthKm;
    }

    public Double getLengthKm() {
        return lengthKm;
    }

    public void setNumberOfTurns(Integer numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public Integer getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setRaceLaps(Integer raceLaps) {
        this.raceLaps = raceLaps;
    }

    public Integer getRaceLaps() {
        return raceLaps;
    }

    public void setTopSpeedKmh(Double topSpeedKmh) {
        this.topSpeedKmh = topSpeedKmh;
    }

    public Double getTopSpeedKmh() {
        return topSpeedKmh;
    }

    public void setLapRecordTime(String lapRecordTime) {
        this.lapRecordTime = lapRecordTime;
    }

    public String getLapRecordTime() {
        return lapRecordTime;
    }

    public void setLapRecordHolder(String lapRecordHolder) {
        this.lapRecordHolder = lapRecordHolder;
    }

    public String getLapRecordHolder() {
        return lapRecordHolder;
    }

    public void setLapRecordYear(Integer lapRecordYear) {
        this.lapRecordYear = lapRecordYear;
    }

    public Integer getLapRecordYear() {
        return lapRecordYear;
    }

    public void setFirstHeldYear(Integer firstHeldYear) {
        this.firstHeldYear = firstHeldYear;
    }

    public Integer getFirstHeldYear() {
        return firstHeldYear;
    }
}
