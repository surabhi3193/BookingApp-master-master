package com.example.mind_android.bookingapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.ExpenceActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.app.Config;
import com.example.mind_android.bookingapp.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;
//#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end #parse("File Header.java") public class ${NAME} { }

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "bOOKkEEPING";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView stockLay = findViewById(R.id.stockLay);
        ImageView salesLay = findViewById(R.id.salesLay);
        ImageView expenseLay = findViewById(R.id.expenseLay);
        ImageView reportLay = findViewById(R.id.reportLay);
        ImageView loanLay = findViewById(R.id.loanLay);
        ImageView bankLay = findViewById(R.id.bankLay);


       stockLay.setOnClickListener(this);

        salesLay.setOnClickListener(this);
        expenseLay.setOnClickListener(this);
        reportLay.setOnClickListener(this);
        loanLay.setOnClickListener(this);
        bankLay.setOnClickListener(this);
        TextView signout_btn = findViewById(R.id.signout_btn);

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_SHORT).show();

//                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.stockLay:
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                break;

                case R.id.salesLay:
                startActivity(new Intent(getApplicationContext(), SalesActivity.class));
                break;

                case R.id.expenseLay:
                startActivity(new Intent(getApplicationContext(), ExpenceActivity.class));
                break;
        }

    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)){}
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else{}
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
