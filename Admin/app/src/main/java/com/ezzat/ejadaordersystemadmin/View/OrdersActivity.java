package com.ezzat.ejadaordersystemadmin.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.ezzat.ejadaordersystemadmin.Control.ExpandableListAdapter;
import com.ezzat.ejadaordersystemadmin.Model.Admin;
import com.ezzat.ejadaordersystemadmin.Model.Client;
import com.ezzat.ejadaordersystemadmin.Model.Order;
import com.ezzat.ejadaordersystemadmin.Model.Store;
import com.ezzat.ejadaordersystemadmin.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Admin admin;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        Intent intent = getIntent();
        admin = (Admin) intent.getSerializableExtra("admin");

        if (admin.getClients() != null) {
            listDataHeader = getClientsNames(admin.getClients());
            listDataChild = getChildsClients(admin.getClients());
        } else {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Clients");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // preparing list data
        //prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private HashMap<String, List<String>> getChildsClients(HashMap<String, Client> clients) {
        HashMap<String, List<String>> res = new HashMap<>();
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            List<String> f = new ArrayList<>();
            HashMap<String, String> tt = entry.getValue().getOrders();
            for (Map.Entry<String, String> e : tt.entrySet())
                f.add(e.getValue());
            res.put(entry.getValue().getName(),f);
        }
        return res;
    }

    private List<String> getClientsNames(HashMap<String, Client> clients) {
        ArrayList<String> res = new ArrayList<>();
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            res.add(entry.getValue().getName());
        }
        return res;
    }

//    private void prepareListData() {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, HashMap<String, String>>();
//
//        // Adding child data
//        listDataHeader.add("Top 250");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");
//
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//
//        for (int i = 0; i < 6; i++) {
//            String o = new String("The Godfather1 " + i);
//            top250.add(o);
//        }
//
//        List<String> nowShowing = new ArrayList<String>();
//
//        for (int i = 0; i < 6; i++) {
//            String o = new String("The Godfather2 " + i);
//            nowShowing.add(o);
//        }
//
//        List<String> comingSoon = new ArrayList<String>();
//        for (int i = 0; i < 6; i++) {
//            String o = new String("The Godfather3 " + i);
//            comingSoon.add(o);
//        }
//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
        intent.putExtra("admin", admin);
        startActivity(intent);
        finish();
    }
}
