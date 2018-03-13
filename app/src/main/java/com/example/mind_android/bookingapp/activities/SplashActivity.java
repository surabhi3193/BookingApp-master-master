package com.example.mind_android.bookingapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;

import static com.example.mind_android.bookingapp.activities.Utility.checkSMSPermission;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String login = getData(getApplicationContext(), "login", "");

        if (login.equals("1")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else
            {
                boolean per = checkSMSPermission(SplashActivity.this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        System.out.println("requestCode " +requestCode );
        System.out.println("permissions " +permissions.toString() );
        System.out.println("grantResults " + grantResults.length + " ///// "+grantResults.toString() );

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_SMS:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                        }
                    }, SPLASH_TIME_OUT);

                }
                else
                {
                    Toast.makeText(SplashActivity.this," Allow Permission To Continue",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
