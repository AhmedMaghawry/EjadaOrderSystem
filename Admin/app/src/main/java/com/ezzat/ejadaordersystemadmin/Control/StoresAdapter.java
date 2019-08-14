package com.ezzat.ejadaordersystemadmin.Control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ezzat.ejadaordersystemadmin.R;
import com.ezzat.ejadaordersystemadmin.Model.Store;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StoresAdapter extends ArrayAdapter<Store> {

    private ArrayList<Store> stores;
    private LayoutInflater inflater;
    private Context context;
    private List<String> availables;

    public StoresAdapter(Context context, ArrayList<Store> stores, List<String> availables) {
        super(context, R.layout.lv_item, stores);
        this.context = context;
        this.stores = stores;
        this.availables = availables;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.lv_item, parent, false);
        FloatingActionButton delete = rowView.findViewById(R.id.tock);
        Spinner spinner = rowView.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_dropdown_item, availables);

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(stores.get(position).getId());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stores.get(position).setId(i);
                stores.get(position).setName(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stores.remove(position);
                rowView.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
        return rowView;
    }

}
