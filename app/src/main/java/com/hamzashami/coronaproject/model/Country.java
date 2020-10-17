package com.hamzashami.coronaproject.model;

public class Country {

    private int id;
    private String countryName;
    private String countryCode;
    private long countryPopulation;
    private int confirmed;
    private int deaths;
    private int recovered;
    private double lat, lng;
    private String lastUpdated;


    public Country(int id, String countryName, String countryCode, long countryPopulation, int confirmed, int deaths, int recovered, String lastUpdated) {
        this.id = id;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryPopulation = countryPopulation;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.lastUpdated = lastUpdated;
        this.lat = 0;
        this.lng = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public long getCountryPopulation() {
        return countryPopulation;
    }

    public void setCountryPopulation(long countryPopulation) {
        this.countryPopulation = countryPopulation;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
