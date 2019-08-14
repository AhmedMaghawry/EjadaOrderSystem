package com.ezzat.ejadaordersystem.Model;

import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client implements Serializable {

    private ArrayList<Pair<String, String>> orders;
    private String name;

    public Client(){}

    public Client(String name, ArrayList<Pair<String, String>> orders) {
        this.name = name;
        this.orders = orders;
    }

    public ArrayList<Pair<String, String>> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Pair<String, String>> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void publish(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference r = mDatabase.getReference("Admin");

        for (int i = 0; i < orders.size(); i++) {
            Pair<String, String> p = orders.get(i);
            DatabaseReference x = r.child(p.first).child("clients");
            DatabaseReference z = x.child(name);
            z.child("name").setValue(name);
            DatabaseReference y = z.child("orders").push();
            y.setValue(p.second);
        }
    }
}
