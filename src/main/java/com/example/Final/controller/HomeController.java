package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.service.PropertyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final PropertyService propertyService;

    @GetMapping("/home")
    public String getHome(Model model) {

//        for (Properties property : result) {
//            if (property.getPropertyType() == null ||
//                    property.getPropertyDescription() == null ||
//                    property.getListImages() == null) {
//                propertyService.delete(property);
//            }
//        }
        List<Properties> result = propertyService.getAll();
        result.removeIf(properties -> !properties.isAvailable());
        Collections.shuffle(result);
        List<Properties> random = result.stream()
                .limit(8)
                .toList();
        model.addAttribute("randomProperty", random);
        return "home/homebody";
    }

    @GetMapping("/all-listings")
    public String getAllListings(Model model, HttpSession session) {
        if (session.getAttribute("USERNAME") == null) {
            model.addAttribute("error", "Hãy đăng nhập để xem tin");
            return "user/login";
        } else {
            List<Properties> propertiesList = propertyService.getAll();
            propertiesList.removeIf(properties -> !properties.isAvailable());
            model.addAttribute("properties", propertiesList);
            model.addAttribute("city", "Toàn quốc");
            return "listing/all-listing";
        }
    }


    @GetMapping("/post-info")
    public String getPostInfo() {
        return "listing/post-information";
    }

    @GetMapping("/post-contact")
    public String getPostContact() {
        return "listing/post-description-contact";
    }

    @GetMapping("/post-image")
    public String getPostImage() {
        return "listing/post-image";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "user/login";
    }

    @GetMapping("/login-page")
    public String getLogin() {
        return "user/login";
    }

    @GetMapping("/searchForm")
    public String getSearch(@Param("optionType") String optionType,
                            @Param("city") String city,
                            @Param("district") String district,
                            @Param("ward") String ward,
                            @Param("houseType") Integer houseType,
                            @Param("rangePrice") Integer rangePrice,
                            @Param("sqmtRange") Integer sqmtRange,
                            Model model
    ) {
        if (city.isEmpty()) {
            city = null;

        } else {
            city = city.replace(",", "").replace("Tỉnh ", "").replace("Thành phố ", "");
        }
        if (district.isEmpty()) {
            district = null;
        } else {
            district = district.replace(",", "").replace("Huyện ", "").replace("Thị xã ", "");
        }
        if (ward.isEmpty()) {
            ward = null;
        } else {
            ward = ward.replace(",", "").replace("Xã ", "");
        }

        List<Properties> propertiesList = propertyService.findPropertiesByForm(optionType, city, district, ward, houseType, rangePrice, sqmtRange);
        propertiesList.removeIf(properties -> !properties.isAvailable());
        model.addAttribute("city", city);
        model.addAttribute("properties", propertiesList);
        return "listing/all-listing";
    }

    @PostMapping("/searchByKey")
    public String getSearchProductPage(@RequestParam("searchKey") String searchKey, Model model) {

        List<Properties> propertiesList = propertyService.findPropertiesByKey(searchKey);
        propertiesList.removeIf(properties -> !properties.isAvailable());
        model.addAttribute("properties", propertiesList);
        model.addAttribute("city", "Toàn quốc");
        return "listing/all-listing";
    }

    @GetMapping("/searchCity")
    public String getSearchCity(Model model, @RequestParam("city") String city,
                                HttpSession session) {

        if (session.getAttribute("USERNAME") == null) {
            model.addAttribute("error", "Hãy đăng nhập để đăng tin");
            return "user/login";
        } else {
            List<Properties> propertiesList = propertyService.findPropertiesByProvince(city);
            propertiesList.removeIf(properties -> !properties.isAvailable());
            model.addAttribute("properties", propertiesList);
            model.addAttribute("city", city);
            return "listing/all-listing";
        }

    }

    @GetMapping("/sortByCity")
    public String getSortByCity(Model model, @RequestParam("city") String city,
                                @RequestParam("sortOption") String option, HttpSession session) {
        System.out.println(city);

        if (!city.equals("Toàn quốc")) {
            switch (option) {
                case "priceDesc" -> {
                    List<Properties> propertiesList = propertyService.sortPriceByCityDESC(city);
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";

                }
                case "priceAsc" -> {
                    List<Properties> propertiesList = propertyService.sortPriceByCityASC(city);
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
                case "sqftDesc" -> {
                    List<Properties> propertiesList = propertyService.sortSqftByCityDESC(city);
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
                default -> {
                    List<Properties> propertiesList = propertyService.sortSqftByCityASC(city);
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
            }
        } else {
            switch (option) {

                case "priceDesc" -> {
                    List<Properties> propertiesList = propertyService.sortPriceByAllDESC();
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";

                }
                case "priceAsc" -> {
                    List<Properties> propertiesList = propertyService.sortPriceByAllASC();
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
                case "sqftDesc" -> {
                    List<Properties> propertiesList = propertyService.sortSqftByAllDESC();
                    propertiesList.removeIf(properties -> !properties.isAvailable());

                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
                default -> {
                    List<Properties> propertiesList = propertyService.sortSqftByAllASC();
                    propertiesList.removeIf(properties -> !properties.isAvailable());
                    model.addAttribute("properties", propertiesList);
                    model.addAttribute("city", city);
                    return "listing/all-listing";
                }
            }
        }

    }

    @GetMapping("/searchByCity")
    public String getSearchByCity(Model model, @RequestParam("city") String city,
                                  @Param("houseType") String houseType,
                                  @Param("rangePrice") String rangePrice,
                                  @Param("sqmtRange") String sqmtRange,
                                  @Param("bedroom") String bedroom) {


        Double minPrice = null;
        Double maxPrice = null;
        Double minSqmt = null;
        Double maxSqmt = null;
        Integer bed = null;
        if (bedroom != null && !bedroom.isEmpty()) {
            bed = Integer.valueOf(bedroom);
        }
        if (rangePrice != null && !rangePrice.isEmpty()) {
            String[] rangeParts = rangePrice.split("&");
            List<Double> allPrices = new ArrayList<>();

            for (String part : rangeParts) {
                if (part.contains(",")) {
                    String[] prices = part.split(",");
                    for (String price : prices) {
                        allPrices.add(Double.valueOf(price));
                    }
                } else {
                    allPrices.add(Double.valueOf(part));
                }
            }
            if (!allPrices.isEmpty()) {
                minPrice = Collections.min(allPrices);
                maxPrice = Collections.max(allPrices);
            }
        }
        if (sqmtRange != null && !sqmtRange.isEmpty()) {
            String[] rangeParts = sqmtRange.split("&");
            List<Double> allSqmt = new ArrayList<>();

            for (String part : rangeParts) {
                if (part.contains(",")) {
                    String[] prices = part.split(",");
                    for (String price : prices) {
                        allSqmt.add(Double.valueOf(price));
                    }
                } else {
                    allSqmt.add(Double.valueOf(part));
                }
            }
            if (!allSqmt.isEmpty()) {
                minSqmt = Collections.min(allSqmt);
                maxSqmt = Collections.max(allSqmt);
            }

        }
        if (city.equals("Toàn quốc")) {
            city = null;
            List<Properties> propertiesList = propertyService.findByCity(city, houseType, minPrice, maxPrice, minSqmt, maxSqmt, bed);
            propertiesList.removeIf(properties -> !properties.isAvailable());
            model.addAttribute("properties", propertiesList);
            model.addAttribute("city","Toàn quốc");
        } else {
            List<Properties> propertiesList = propertyService.findByCity(city, houseType, minPrice, maxPrice, minSqmt, maxSqmt, bed);
            propertiesList.removeIf(properties -> !properties.isAvailable());
            model.addAttribute("properties", propertiesList);
            model.addAttribute("city", city);
        }
        return "listing/all-listing";
    }


}
