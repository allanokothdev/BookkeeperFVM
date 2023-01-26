package com.bookkeeperfvm.android.models;

import java.io.Serializable;

public class Location implements Serializable {

    private String id;
    private String buildingID;
    private String address;
    private String village;     //landmark
    private String cell;        //estate
    private String sector;      //ward
    private String district;    //sub county
    private String province;    //county
    private String country;

    public Location() {
    }

    public Location(String id, String buildingID, String address, String village, String cell, String sector, String district, String province, String country) {
        this.id = id;
        this.buildingID = buildingID;
        this.address = address;
        this.village = village;
        this.cell = cell;
        this.sector = sector;
        this.district = district;
        this.province = province;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Location location = (Location) obj;
        return id.matches(location.getId());
    }
}
