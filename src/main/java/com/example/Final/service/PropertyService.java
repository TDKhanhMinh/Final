package com.example.Final.service;

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

    public void updateInfo(Properties oldProperties, Properties properties) {
        oldProperties.setPropertyPrice(properties.getPropertyPrice());
        oldProperties.setPropertyType(properties.getPropertyType());
        oldProperties.setPropertyTypeTransaction(oldProperties.getPropertyTypeTransaction());
        oldProperties.setAvailable(true);
        oldProperties.setPropertyInterior(properties.getPropertyInterior());
        oldProperties.setPropertyFloor(properties.getPropertyFloor());
        oldProperties.setBedrooms(properties.getBedrooms());
        oldProperties.setBathrooms(properties.getBathrooms());
        oldProperties.setSquareMeters(properties.getSquareMeters());
        oldProperties.setAddress(oldProperties.getAddress());
        propertyRepo.save(oldProperties);
    }

    public void updateImages(Properties properties) {
        Properties oldProperties = propertyRepo.findById(properties.getPropertyId()).orElseThrow();
        oldProperties.setListImages(properties.getListImages());
        propertyRepo.save(oldProperties);
    }

}
