package org.blep.poc.hcq;

import javax.xml.bind.annotation.XmlElement;

public class HotelDto {

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    private Hotel hotel;

    public HotelDto(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelDto() {
    }

    public String getId() {
        return "row_" + hotel.getId();
    }


    public String getName() {
        return hotel.getName();
    }

    public String getAdress() {
        return hotel.getAddress();
    }

    public String getCity() {
        return hotel.getCity();
    }

    public String getState() {
        return hotel.getState();
    }

    public String getPrice() {
        return hotel.getPrice().toString();
    }

}
