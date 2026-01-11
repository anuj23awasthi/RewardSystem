package com.example.sharprewards.models;

public class RegisterRequest {
    public String name;
    public String email;
    public String password;
    public String area;
    public String city;
    public String country;

    public RegisterRequest(String name, String email, String password,
                           String area, String city, String country) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.area = area;
        this.city = city;
        this.country = country;
    }
}
