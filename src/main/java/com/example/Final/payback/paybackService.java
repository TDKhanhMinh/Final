package com.example.Final.payback;

import org.springframework.stereotype.Service;

@Service

public class paybackService {
    public void updateProperty(Properties properties, String type, String legal, String interior,
                               double squareMeters, double price,
                               int floatFloors, int bedrooms, int bathrooms){
        properties.setPropertyType(type);
        properties.setPropertyLegal(legal);
        properties.setPropertyInterior(interior);
        properties.setPropertyFloor(floatFloors);
        properties.setBedrooms(bedrooms);
        properties.setPropertyPrice(price);
        properties.setSquareMeters(squareMeters);
        properties.setBathrooms(bathrooms);
        properties.setPropertyPrice(price);
        properties.setSquareMeters(squareMeters);
    }
}
