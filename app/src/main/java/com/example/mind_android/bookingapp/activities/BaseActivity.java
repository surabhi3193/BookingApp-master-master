package com.example.mind_android.bookingapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.ExpenceActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.ReportActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.beans.Sales;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
   public Toolbar toolbar;
  public FrameLayout frameLayout;
   public NavigationView navigationView;
    public static CircleImageView nav_img;
    public static String image = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);;

         toolbar = (Toolbar) findViewById(R.id.toolbar);

         setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setHomeAsUpIndicator(R.drawable.menu);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
         nav_img = (CircleImageView)hView.findViewById(R.id.nav_img);

        TextView signout_btn = findViewById(R.id.signout_btn);


        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        if (image.length()>5)
            Picasso.with(BaseActivity.this).load(image).placeholder(R.drawable.profile).into(nav_img);
        else
            nav_img.setImageResource(R.drawable.profile);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finishAffinity();
        }

        else if (id == R.id.profile)
        {
         startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }


        else if (id == R.id.sales) {
         startActivity(new Intent(getApplicationContext(), SalesActivity.class));
        }

        else if (id == R.id.inventory) {
         startActivity(new Intent(getApplicationContext(), StockActivity.class));
        }
          else if (id == R.id.reset) {
      resetAllWarning();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void resetAllWarning() {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (BaseActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle("Reset All").setIcon(R.drawable.reset);
        ab.setMessage("Are you sure ? ");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              resetAll();

//                Toast.makeText(BaseActivity.this,"Under Development",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();

    }

    private void resetAll()
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BaseActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);
        String user_id=getData(BaseActivity.this,"user_id","");
        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "all_clear", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* clear all  response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
              ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

}


