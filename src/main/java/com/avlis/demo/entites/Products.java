package com.avlis.demo.entites;

public class Products {

    private Long id;
    private String name;
    private Double price;
    private Departaments departaments;

    public Products(){

    }

    public Products (Long id, String name, Double price, Departaments departament) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.departaments = departament;

    }

    public Departaments getDepartaments() {
        return departaments;
    }
    public void setDepartaments(Departaments departaments) {
        this.departaments = departaments;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
