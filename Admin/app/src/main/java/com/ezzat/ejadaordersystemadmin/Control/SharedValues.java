package com.ezzat.ejadaordersystemadmin.Control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ezzat.ejadaordersystemadmin.Model.Store;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by pkharche on 10/04/18.
 */
public class SharedValues {

    private static final String SHARED_PREFS = "shared_values";

    public static String getValueS(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);
        return value;
    }

    public static boolean getValueB(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, true);
        return value;
    }

    public static int getValueI(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }

    public static void saveValue(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value.getClass().equals(String.class)) {
            editor.putString(key, (String) value);
        } else if (value.getClass().equals(Integer.class)) {
            editor.putInt(key, (Integer) value);
        } else if (value.getClass().equals(Boolean.class)) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    public static void resetTripValues(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        /*editor.putString(Constants.TRIP_ID, null);
        editor.putString(Constants.TRIP_IS_ACCEPTED, null);
        editor.putString(Constants.PICKUP_PLACE, null);
        editor.putString(Constants.DROPOFF_PLACE, null);*/

        editor.apply();
    }

    public static void resetAllValues(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        /*editor.putString(Constants.USER_ID, null);
        editor.putString(Constants.TRIP_ID, null);
        editor.putString(Constants.TRIP_IS_ACCEPTED, null);
        editor.putString(Constants.PICKUP_PLACE, null);
        editor.putString(Constants.DROPOFF_PLACE, null);*/

        editor.apply();
    }

    public static void saveArrayList(ArrayList<Store> list, String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static ArrayList<Store> getArrayList(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        if (json == null)
            return null;
        Type type = new TypeToken<ArrayList<Store>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
