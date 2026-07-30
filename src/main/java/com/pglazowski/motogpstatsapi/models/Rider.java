package com.pglazowski.motogpstatsapi.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "full_name", nullable = false, unique = true, length = 200)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "race_number", unique = true)
    private Integer raceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "fk_rider_team"))
    private Team team;

    @Column(nullable = false)
    private Integer wins = 0;

    @Column(nullable = false)
    private Integer podiums = 0;

    @Column(name = "pole_positions", nullable = false)
    private Integer polePositions = 0;

    @Column(name = "world_titles", nullable = false)
    private Integer worldTitles = 0;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Rider() {
    }

    public Rider(String firstName, String lastName, String fullName, String nationality,
                 Integer age, Integer raceNumber, Team team) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.nationality = nationality;
        this.age = age;
        this.raceNumber = raceNumber;
        this.team = team;
        this.wins = 0;
        this.podiums = 0;
        this.polePositions = 0;
        this.worldTitles = 0;
    }

    public Rider(String firstName, String lastName, String fullName, String nationality,
                 Integer age, Integer raceNumber, Team team, Integer wins, Integer podiums,
                 Integer polePositions, Integer worldTitles, String biography, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.nationality = nationality;
        this.age = age;
        this.raceNumber = raceNumber;
        this.team = team;
        this.wins = wins != null ? wins : 0;
        this.podiums = podiums != null ? podiums : 0;
        this.polePositions = polePositions != null ? polePositions : 0;
        this.worldTitles = worldTitles != null ? worldTitles : 0;
        this.biography = biography;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(Integer raceNumber) {
        this.raceNumber = raceNumber;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins != null ? wins : 0;
    }

    public Integer getPodiums() {
        return podiums;
    }

    public void setPodiums(Integer podiums) {
        this.podiums = podiums != null ? podiums : 0;
    }

    public Integer getPolePositions() {
        return polePositions;
    }

    public void setPolePositions(Integer polePositions) {
        this.polePositions = polePositions != null ? polePositions : 0;
    }

    public Integer getWorldTitles() {
        return worldTitles;
    }

    public void setWorldTitles(Integer worldTitles) {
        this.worldTitles = worldTitles != null ? worldTitles : 0;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", age=" + age +
                ", raceNumber=" + raceNumber +
                ", team=" + (team != null ? team.getName() : "null") +
                ", wins=" + wins +
                ", podiums=" + podiums +
                ", polePositions=" + polePositions +
                ", worldTitles=" + worldTitles +
                '}';
    }
}
