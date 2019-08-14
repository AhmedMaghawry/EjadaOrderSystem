package com.ezzat.ejadaordersystem.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ezzat.ejadaordersystem.Control.ExpandableListAdapter;
import com.ezzat.ejadaordersystem.Control.SharedValues;
import com.ezzat.ejadaordersystem.Model.Admin;
import com.ezzat.ejadaordersystem.Model.Client;
import com.ezzat.ejadaordersystem.Model.Store;
import com.ezzat.ejadaordersystem.Model.User;
import com.ezzat.ejadaordersystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private User user;
    private List<String> listDataHeader;
    private HashMap<String, List<Store>> listDataChild;
    private Button order;
    private HashMap<String, HashMap<String, List<String>>> lists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        user = (User) getIntent().getSerializableExtra("user");


        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        lists = new HashMap<>();
        order = findViewById(R.id.order);

        SharedValues.saveValue(MainActivity.this,"loginBA",true);
        SharedValues.saveValue(MainActivity.this,"name", user.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Hello " + user.getName());

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference("Admin");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDataChild.clear();
                listDataHeader.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Admin admin = messageSnapshot.getValue(Admin.class);
                    if (admin.getStatus()) {
                        listDataHeader.add(admin.getName());
                        listDataChild.put(admin.getName(), admin.getStores());
                    }
                }

                for (String x : listDataHeader) {
                    HashMap<String, List<String>> h = new HashMap<>();
                    if (listDataChild.get(x) != null) {
                        for (Store s : listDataChild.get(x)) {
                            h.put(s.getName(), new ArrayList<String>());
                        }
                    }
                    lists.put(x, h);
                }

                listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild, lists);

                // setting list adapter
                expListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Pair<String, String>> orders = formatOrders();
                Client client = new Client(user.getName(), orders);
                client.publish();
                Toast.makeText(MainActivity.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Pair<String, String>> formatOrders() {
        ArrayList<Pair<String, String>> res = new ArrayList();
        for (HashMap.Entry<String, HashMap<String, List<String>>> entry : lists.entrySet()) {
            HashMap<String, List<String>> f = entry.getValue();
            for (HashMap.Entry<String, List<String>> entry2 : f.entrySet()) {
                List<String> orderN = entry2.getValue();
                for (String ord : orderN) {
                    res.add(new Pair<String, String>(entry.getKey(),entry2.getKey() + ":" + ord));
                }
            }
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.exit:
                SharedValues.saveValue(MainActivity.this,"login",false);
                //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
