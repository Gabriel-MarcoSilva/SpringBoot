package com.avlis.demo.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avlis.demo.entites.Departaments;
import com.avlis.demo.entites.Products;

@RestController
@RequestMapping(value = "/products")
public class Controllers {
    //methods:

    @GetMapping
    public List<Products> getObjects(){
        Departaments d1 = new Departaments(2L, "tech");
        Departaments d2 = new Departaments(1L, "mebate");

        Products p1 = new Products(1L, "MacBook", 20000.00, d2);
        Products p2 = new Products(1L, "MacBook", 20000.00, d1);

        List<Products> list = Arrays.asList(p1,p2);

        return list;
        
    }
}
