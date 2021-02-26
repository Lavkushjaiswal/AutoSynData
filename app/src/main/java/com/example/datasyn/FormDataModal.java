package com.example.datasyn;

public class FormDataModal {
    int _id;
    String name;
    String email;
    String role;
    String country;
    String imageLink;

    public FormDataModal(){}

    public FormDataModal(String name, String email, String role, String country, String imageLink) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.country = country;
        this.imageLink = imageLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
