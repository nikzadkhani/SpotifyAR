package com.example.spotifyar.models;

import androidx.annotation.NonNull;

public class User {
    private String country;
    private String display_name;
    private String email;
    private String id;

    /**
     * The object to serialize the json object from UserService call
     * @param country
     * @param display_name
     * @param email
     * @param id
     */
    public User(String country, String display_name, String email, String id, String explicit_content) {
        this.country = country;
        this.display_name = display_name;
        this.email = email;
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "country='" + country + '\'' +
                ", display_name='" + display_name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

// Json object serialized
//{"country":"US",
//        "display_name":"khaninikzad",
//        "email":"khaninikzad@gmail.com",
//        "explicit_content":{"filter_enabled":false,"filter_locked":false},
//        "external_urls":{"spotify":"https:\/\/open.spotify.com\/user\/khaninikzad"},
//        "followers":{"href":null,"total":15},
//        "href":"https:\/\/api.spotify.com\/v1\/users\/khaninikzad",
//        "id":"khaninikzad",
//        "images":[],
//        "product":"premium",
//        "type":"user",
//        "uri":"spotify:user:khaninikzad"
//}
