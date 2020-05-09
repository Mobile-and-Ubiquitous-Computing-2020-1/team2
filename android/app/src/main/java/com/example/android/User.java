package com.example.android;

import com.google.firebase.database.PropertyName;

import java.util.LinkedList;
import java.util.List;

public class User {
    @PropertyName("id")
    public String id = null;
    @PropertyName("friends_id")
    public List<String> friends_id = new LinkedList<>();
    @PropertyName("scores")
    public List<Integer> scores = new LinkedList<>();
    // scores of 7 days

    User() {}

    User(String id) {
        this.id = id;
    }
}
