package com.cosek.edms.soccer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamResponse {

    @JsonProperty("team")
    private Team team;

    @JsonProperty("venue")
    private Venue venue;

    // Getters and setters

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        private int id;
        private String name;
        private String code;
        private String country;
        private Integer founded;
        private boolean national;
        private String logo;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public Integer getFounded() { return founded; }
        public void setFounded(Integer founded) { this.founded = founded; }

        public boolean isNational() { return national; }
        public void setNational(boolean national) { this.national = national; }

        public String getLogo() { return logo; }
        public void setLogo(String logo) { this.logo = logo; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Venue {
        private Integer id;
        private String name;
        private String address;
        private String city;
        private Integer capacity;
        private String surface;
        private String image;

        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public String getSurface() { return surface; }
        public void setSurface(String surface) { this.surface = surface; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }
}
