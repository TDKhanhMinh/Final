package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.service.PropertyService;
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
        List<Properties> result = propertyService.getAll();
        Collections.shuffle(result);
        List<Properties> random = result.stream()
                .limit(8)
                .toList();
        model.addAttribute("randomProperty", random);
        return "home/homebody";
    }

    @GetMapping("/all-listings")
    public String getAllListings(Model model) {
        List<Properties> propertiesList = propertyService.getAll();
        model.addAttribute("properties", propertiesList);
        return "listing/all-listing";
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

    @GetMapping("/searchForm")
    public String getSearch(@Param("optionType") String optionType,
                            @Param("city") String city,
                            @Param("district") String district,
                            @Param("ward") String ward,
                            @Param("houseType") String houseType,
                            @Param("rangePrice") String rangePrice,
                            @Param("sqmtRange") String sqmtRange,
                            Model model
    ) {
        Double minPrice = null;
        Double maxPrice = null;
        Double minSqmt = null;
        Double maxSqmt = null;
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

        String replace = city.replace(",", "").replace("Tỉnh ", "").replace("Thành phố ", "");
        List<Properties> propertiesList = propertyService.findPropertiesByForm(optionType,
                replace,
                district.replace(",", "").replace("Huyện ", "").replace("Thị xã ", ""),
                ward.replace(",", "").replace("Xã ", ""), houseType, minPrice, maxPrice, minSqmt, maxSqmt);
        System.out.println(replace);
        if (city.isEmpty() && city.trim().isEmpty()) {
            model.addAttribute("city", "Toàn quốc");
            model.addAttribute("properties", propertyService.getAll());
        } else {
            model.addAttribute("city", replace);
            model.addAttribute("properties", propertiesList);
        }
        System.out.println(propertiesList);
        return "listing/all-listing";
    }

    @PostMapping("/searchByKey")
    public String getSearchProductPage(@RequestParam("searchKey") String searchKey, Model model) {
        List<Properties> propertiesList = propertyService.findPropertiesByKey(searchKey);
        model.addAttribute("properties", propertiesList);
        model.addAttribute("city", "Toàn quốc");
        return "listing/all-listing";
    }

    @GetMapping("/searchCity")
    public String getSearchCity(Model model, @RequestParam("city") String city) {
        List<Properties> propertiesList = propertyService.findPropertiesByProvince(city);
        model.addAttribute("properties", propertiesList);
        model.addAttribute("city", city);
        return "listing/all-listing";
    }
}
