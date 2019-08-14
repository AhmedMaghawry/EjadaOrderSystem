package com.ezzat.ejadaordersystemadmin.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Store implements Serializable {

    private String name;
    private int id;

    public Store(){
        this.name = "Abo Rabi3";
        this.id = 0;
    }

    public Store(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", id);
        return map;
    }
}
