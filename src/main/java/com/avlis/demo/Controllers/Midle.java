package com.avlis.demo.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/onomatopeia")
public class Midle {
    @GetMapping
    public String getObjects(){
        return "ola mundo";
    }
}
