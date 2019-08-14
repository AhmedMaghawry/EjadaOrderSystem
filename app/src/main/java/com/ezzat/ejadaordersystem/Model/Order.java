package com.ezzat.ejadaordersystem.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {

    private String order;
    private Store store;

    public Order(){}

    public Order(String order, Store store) {
        this.order = order;
        this.store = store;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("store", store);
        return map;
    }
}
