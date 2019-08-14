package com.ezzat.ejadaordersystem.Control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utils {

    public final static String TAG = "Utils";
    public static SweetAlertDialog pDialog;

    public static boolean isInternetConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager connectivityMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityMgr != null) {
                NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void launchActivity(Context context, Class classToGo, Bundle bundle) {
        Intent intent = new Intent(context, classToGo);
        intent.putExtra("send", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showDialog(Context context,String title, String message) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("OK");
        pDialog.show();
    }

    public static void showError(Context context,String title, String message) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
        pDialog.show();
    }

    public static void showLoading(Activity c) {
        hideDialog();
        pDialog = new SweetAlertDialog(c, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0EAE95"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {
                pDialog = null;
                Log.e(TAG, "Utils:: hideProgressDialog: ", e);
            }
        }
    }
}