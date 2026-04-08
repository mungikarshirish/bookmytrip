package com.example.bookmytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class summerholidays {
    @GetMapping("/summerholidays")
    public String getData() {return  "Please Book your flight tickets from book mytrip at 50% discont" ; }
}