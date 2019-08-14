package com.ezzat.ejadaordersystemadmin.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Client implements Serializable {

    private HashMap<String, String> orders;
    private String name;

    public Client(){}

    public Client(String name, HashMap<String, String> orders) {
        this.name = name;
        this.orders = orders;
    }

    public HashMap<String, String> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, String> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("orders", orders);
        return map;
    }
}
