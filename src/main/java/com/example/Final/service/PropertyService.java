package com.example.Final.service;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.PostInformation;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepo propertyRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private RentalHistoryService rentalHistoryService;
    @Autowired
    private SalesHistoryService salesHistoryService;


    public List<Properties> getAll() {
        return propertyRepo.findAll();
    }

    public List<Properties> getAllByUser(User user) {
        return propertyRepo.getPropertiesByUser(user);
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
        //propertyRepo.delete(propertyRepo.findById(id).orElseThrow(()-> new RuntimeException("Could not Found")));
        Properties properties = propertyRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found property"));
        properties.setAvailable(false);
        propertyRepo.save(properties);
    }

    public void delete(Properties properties) {
        propertyRepo.delete(properties);
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

    public void updateProperty(Properties properties) {
        Properties oldProperties = propertyRepo.findById(properties.getPropertyId()).orElseThrow(() -> new RuntimeException("Property not found"));

        oldProperties.setPropertyTypeTransaction(properties.getPropertyTypeTransaction());
        oldProperties.setPropertyTitle(properties.getPropertyTitle());
        oldProperties.setPropertyDescription(properties.getPropertyDescription());
        oldProperties.setPropertyStatus(properties.getPropertyStatus());
        oldProperties.setSquareMeters(properties.getSquareMeters());
        oldProperties.setPropertyFloor(properties.getPropertyFloor());
        oldProperties.setBedrooms(properties.getBedrooms());
        oldProperties.setBathrooms(properties.getBathrooms());
        PostInformation postInformation = oldProperties.getPostInformation();

        if (properties.getPostInformation() != null) {
            postInformation.setEmail(properties.getPostInformation().getEmail());
            postInformation.setFullName(properties.getPostInformation().getFullName());
            postInformation.setPhone(properties.getPostInformation().getPhone());
            contactRepo.save(postInformation);
        }

        if (properties.getPropertyPrice() != oldProperties.getPropertyPrice()) {
            if (properties.getPropertyTypeTransaction().equals(oldProperties.getPropertyTypeTransaction()) && oldProperties.getPropertyTypeTransaction().equals("rent")) {
                rentalHistoryService.createRentalHistory(properties);
                oldProperties.setPropertyPrice(properties.getPropertyPrice());
            }
            if (properties.getPropertyTypeTransaction().equals(oldProperties.getPropertyTypeTransaction()) && oldProperties.getPropertyTypeTransaction().equals("sell")) {
                salesHistoryService.createSalesHistory(properties);
                oldProperties.setPropertyPrice(properties.getPropertyPrice());
            }
        }
        oldProperties.setPostInformation(postInformation);
        propertyRepo.save(oldProperties);
    }

    public List<Properties> findPropertiesByKey(String key) {
        return propertyRepo.findPropertiesByKey(key);
    }

    public List<Properties> findPropertiesByForm(String optionType, String city,
                                                 String district, String ward,
                                                 String houseType, Double priceMin,
                                                 Double priceMax, Double sqmtMin,
                                                 Double sqmtMax) {
        return propertyRepo.findByForm(optionType, city, district, ward, houseType, priceMin, priceMax, sqmtMin, sqmtMax);
    }

    public List<Properties> findPropertiesByProvince(String province) {
        return propertyRepo.findByAddress_Province(province);
    }

    public List<Properties> findByCity(String city, String houseType, Double priceMin,
                                       Double priceMax, Double sqmtMin,
                                       Double sqmtMax, Integer bedroom) {
        return propertyRepo.findByCity(city, houseType, priceMin, priceMax, sqmtMin, sqmtMax, bedroom);
    }

}
