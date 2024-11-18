package com.example.Final.controller;

import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.PropertyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {



    @GetMapping
    public String getHome() {
        return "home/homebody";
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
}
