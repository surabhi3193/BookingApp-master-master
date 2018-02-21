package com.example.mind_android.bookingapp.Constant;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CheckInternetConnection  extends Activity
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    static boolean grantLOc;
    static String provider;
    private static LocationManager locationManager;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager

                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    }
