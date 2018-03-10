package com.example.mind_android.bookingapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static CircleImageView nav_img;
    public static String image = "";
    public Toolbar toolbar;
    public FrameLayout frameLayout;
    public NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.content_frame);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setHomeAsUpIndicator(R.drawable.menu);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        nav_img = hView.findViewById(R.id.nav_img);

        TextView signout_btn = findViewById(R.id.signout_btn);


        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        image = getData(BaseActivity.this, "user_image", "");
        if (image.length() > 5)
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        //to prevent current item select over and over
        if (item.isChecked()) {
            if (item.getItemId() != R.id.reset) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        }

        if (id == R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finishAffinity();
        } else if (id == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.sales) {
            startActivity(new Intent(getApplicationContext(), SalesActivity.class));
        } else if (id == R.id.inventory) {
            startActivity(new Intent(getApplicationContext(), StockActivity.class));
        } else if (id == R.id.reset) {
            resetAllWarning();
        } else if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), HTmlActivity.class)
                    .putExtra("name", "About Us"));
        } else if (id == R.id.terms) {
            startActivity(new Intent(getApplicationContext(), HTmlActivity.class)
                    .putExtra("name", "Terms & Conditions"));
        } else if (id == R.id.privacy) {
            startActivity(new Intent(getApplicationContext(), HTmlActivity.class)
                    .putExtra("name", "Privacy Policy"));
        } else if (id == R.id.chat) {
            startActivity(new Intent(getApplicationContext(), LiveChatActivity.class));
        } else if (id == R.id.sharing) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Bookkeeping App");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
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
                if (isNetworkAvailable(BaseActivity.this))
                    resetAll();
                else {
                    DatabaseHandler db = new DatabaseHandler(BaseActivity.this);

                    db.deleteAllStocks();
                    db.deleteAllSales();
                }

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

    private void resetAll() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BaseActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);
        String user_id = getData(BaseActivity.this, "user_id", "");
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


