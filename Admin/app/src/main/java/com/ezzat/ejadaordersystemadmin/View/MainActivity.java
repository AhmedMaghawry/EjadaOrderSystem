package com.ezzat.ejadaordersystemadmin.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezzat.ejadaordersystemadmin.Control.MySingleton;
import com.ezzat.ejadaordersystemadmin.Control.SharedValues;
import com.ezzat.ejadaordersystemadmin.Control.StoresAdapter;
import com.ezzat.ejadaordersystemadmin.Model.Admin;
import com.ezzat.ejadaordersystemadmin.Model.Store;
import com.ezzat.ejadaordersystemadmin.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private StoresAdapter customeAdapter;
    private ArrayList<Store> stores;
    private Button orders, add, publish;
    private Switch status;
    private Admin admin;
    private FirebaseAnalytics mFirebaseAnalytics;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyDTtiEawJaAroiR_vCY1jlA0xU6je-WCCg";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    List<String> storesAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        admin = (Admin) getIntent().getSerializableExtra("admin");
        storesAv = new ArrayList<>();

        SharedValues.saveValue(MainActivity.this,"login",true);
        SharedValues.saveValue(MainActivity.this,"name",admin.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Hello " + admin.getName());

        lv = findViewById(R.id.lv);
        orders = findViewById(R.id.orders);
        add = findViewById(R.id.add);
        publish = findViewById(R.id.publish);
        status = findViewById(R.id.status);
        if (admin.getStores() == null) {
            stores = new ArrayList<>();
        } else
            stores = admin.getStores();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference d = database.getReference().child("Stores");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    storesAv.add((String) s.getValue());
                }
                customeAdapter = new StoresAdapter(MainActivity.this, stores, storesAv);
                lv.setAdapter(customeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        status.setChecked(admin.getStatus());

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }
        });

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                admin.setStatus(b);
                if (!b) {
                    admin.reset();
                    publish();
                } else {
                    TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                    NOTIFICATION_TITLE = "Provider Available";
                    NOTIFICATION_MESSAGE = admin.getName() + "is now Available you can order from him now";

                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    sendNotification(notification);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stores.add(new Store());
                customeAdapter.notifyDataSetChanged();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.setStores(stores);
                publish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        admin.reset();
//        publish();
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }



    public void publish(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = admin.toMap();
        childUpdates.put("/" + admin.getName(), userValues);
        mDatabase.getReference("Admin").updateChildren(childUpdates);

    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
