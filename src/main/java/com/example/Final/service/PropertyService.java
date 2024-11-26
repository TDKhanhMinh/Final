package com.example.Final.service;

import com.example.Final.entity.listingservice.Contact;
import com.example.Final.entity.listingservice.HistoryListing;
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
        Contact contact = oldProperties.getContact();

        if (properties.getContact() != null) {
            contact.setEmail(properties.getContact().getEmail());
            contact.setFullName(properties.getContact().getFullName());
            contact.setPhone(properties.getContact().getPhone());
            contactRepo.save(contact);
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
        oldProperties.setContact(contact);
        propertyRepo.save(oldProperties);
    }

}
