package com.example.mind_android.bookingapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.example.mind_android.bookingapp.activities.dashboard_part.ReportActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.beans.Sales;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}


