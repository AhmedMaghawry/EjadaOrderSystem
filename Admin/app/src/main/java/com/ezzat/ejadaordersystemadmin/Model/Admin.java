package com.ezzat.ejadaordersystemadmin.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Admin implements Serializable {

    private boolean status;
    private String name, password;
    private ArrayList<Store> stores;
    private HashMap<String, Client> clients;

    public Admin(){}

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public HashMap<String, Client> getClients() {
        return clients;
    }

    public void setClients(HashMap<String, Client> clients) {
        this.clients = clients;
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public void reset(){
        status = false;
        stores = new ArrayList<>();
        clients = new HashMap<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("name", name);
        map.put("password", password);
        map.put("stores", stores);
        map.put("clients", clients);
        return map;
    }
}
