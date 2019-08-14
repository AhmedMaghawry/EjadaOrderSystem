package com.ezzat.ejadaordersystemadmin.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezzat.ejadaordersystemadmin.Control.SharedValues;
import com.ezzat.ejadaordersystemadmin.Model.Admin;
import com.ezzat.ejadaordersystemadmin.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {


    private Button login;
    private EditText name, password;
    private Admin admin;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference("Admin");

//        ArrayList<String> x = new ArrayList<String>();
//        x.add("aaaa");
//        x.add("aaaa");
//        x.add("aaaa");
//        Client c = new Client("EEEE", x);
//
//        Admin m = new Admin("EEEff", "asdasda");
//        ArrayList<Client> cc = new ArrayList<>();
//        cc.add(c);
//        m.setClients(cc);
//
//        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference f = mDatabase.getReference().push();
//        f.setValue(m);

        boolean loged = SharedValues.getValueB(LoginActivity.this, "login");

        if (loged) {
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            final String adminName = SharedValues.getValueS(LoginActivity.this, "name");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        if (messageSnapshot.getKey().equals(adminName)) {
                            admin = messageSnapshot.getValue(Admin.class);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                            finish();
                            pDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean flag = false;
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            if (messageSnapshot.getKey().equals(name.getText().toString())) {
                                admin = messageSnapshot.getValue(Admin.class);
                                if (admin == null || ! admin.getPassword().equals(password.getText().toString())) {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                } else {
                                    flag = true;
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("admin", admin);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        if (!flag)
                            Toast.makeText(LoginActivity.this, "User not Found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
