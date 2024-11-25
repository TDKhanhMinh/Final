package com.example.Final.service;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.repository.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepo propertyRepo;

    public List<Properties> getAll() {
        return propertyRepo.findAll();
    }

    public List<Properties> getByHistoryListing(HistoryListing historyListing) {
        return propertyRepo.getPropertiesByHistoryListing(historyListing);
    }

    public Properties create(Properties properties) {
        return propertyRepo.save(properties);
    }

    public void save(Properties properties) {
        propertyRepo.save(properties);
    }

    public void deleteById(int id) {
        propertyRepo.deleteById(id);
    }

    public Properties getById(int id) {
        return propertyRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found property"));
    }

    public void updateInfo(int id, String type, String legal, String interior, double square, double price, int floor, int bed, int bath) {

        Properties properties = propertyRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found property"));
        properties.setPropertyType(type);
        properties.setPropertyLegal(legal);
        properties.setPropertyInterior(interior);
        properties.setBathrooms(bath);
        properties.setBedrooms(bed);
        properties.setPropertyFloor(floor);
        properties.setPropertyPrice(price);
        properties.setSquareMeters(square);
        propertyRepo.save(properties);
    }

    public void updateImages(Properties properties) {
        Properties oldProperties = propertyRepo.findById(properties.getPropertyId()).orElseThrow();
        oldProperties.setListImages(properties.getListImages());
        propertyRepo.save(oldProperties);
    }

}
