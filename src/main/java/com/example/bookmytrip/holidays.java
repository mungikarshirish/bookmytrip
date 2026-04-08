package com.example.bookmytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class holidays {
    @GetMapping("/holidays")
    public String getData() {return  "Please Book your holidays holidays from book mytrip at 50% discont" ; }
}