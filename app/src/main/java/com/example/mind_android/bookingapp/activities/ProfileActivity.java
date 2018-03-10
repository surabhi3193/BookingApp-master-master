package com.example.mind_android.bookingapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.activities.BaseActivity.nav_img;
import static com.example.mind_android.bookingapp.activities.MainActivity.image;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class ProfileActivity extends AppCompatActivity {
    private static String name;
    private static String email;
    private static String bus_name;
    private static String designation;
    CircleImageView user_pic;
    int count = 0;
    private EditText nameTV, cityTv, bus_nameTV, countryTV, emailTV;
    private TextView designationTV, phoneTV;
    private String phone;
    private String city;
    private String country;
    private ImageView edit_btn, edit_image;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask = "";

    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView back_btn = findViewById(R.id.back_btn);

        nameTV = findViewById(R.id.name);
        designationTV = findViewById(R.id.designation);
        cityTv = findViewById(R.id.location);
        countryTV = findViewById(R.id.country);
        bus_nameTV = findViewById(R.id.bus_name);
        phoneTV = findViewById(R.id.mobileNumber);
        emailTV = findViewById(R.id.email);
        user_pic = findViewById(R.id.profile);
        edit_btn = findViewById(R.id.edit);
        edit_image = findViewById(R.id.edit_image);

        getUserProfile(ProfileActivity.this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Utility.checkPermission(ProfileActivity.this);
                selectImage();

            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0) {
                    edit_btn.setImageResource(R.drawable.save);
                    count = 1;
                    enableDisabledEditTExt();
                } else {
                    name = nameTV.getText().toString();
                    designation = designationTV.getText().toString();
                    city = cityTv.getText().toString();
                    country = countryTV.getText().toString();
                    bus_name = bus_nameTV.getText().toString();
                    email = emailTV.getText().toString();
                    phone = phoneTV.getText().toString();


                    updateProfile(ProfileActivity.this, email, name, bus_name,
                            country, city, null);

                }
            }
        });

    }

    private void updateProfile(final Activity activity, final String email,
                               final String name, final String bus_name, final String country,
                               final String city, final File image_user) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(activity, "Please wait",
                "Updating Profile..", true);
        ringProgressDialog.setCancelable(false);

        String user_id = getData(activity, "user_id", "");

        params.put("bk_userid", user_id);

        params.put("bk_useremail", email);
        params.put("bk_fullname", name);
        params.put("bk_businessname", bus_name);
        params.put("bk_usercountry", country);
        params.put("bk_useraddress", city);

        try {
            params.put("bk_userpic", image_user);
        } catch (FileNotFoundException e) {
            params.put("bk_userpic", "");
            ringProgressDialog.dismiss();
            e.printStackTrace();
//            Toast.makeText(activity,"Image Error , Please select another image ",Toast.LENGTH_SHORT).show();

        }

        System.out.println(params);

        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* update profile  response ***");
                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    if (response.getString("status").equals("0")) {

                        Toast.makeText(activity,
                                response.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String name = response.getString("bk_fullname");
                        String bus_name = response.getString("business_name");
                        String email = response.getString("bk_useremail");
                        String phone = response.getString("bk_userphone");
                        String country = response.getString("bk_usercountry");
                        String city = response.getString("bk_useraddress");
                        image = response.getString("bk_userpic");
                        saveData(ProfileActivity.this, "user_image", image);
                        nameTV.setText(name);
                        designationTV.setText(designation);
                        cityTv.setText(city);
                        countryTV.setText(country);
                        bus_nameTV.setText(bus_name);
                        emailTV.setText(email);
                        phoneTV.setText(phone);
                        if (image.length() > 3)
                            Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(user_pic);
                        else
                            user_pic.setImageResource(R.drawable.profile);


                        enableDisabledEditTExt();
                        edit_btn.setImageResource(R.drawable.edit_pen);
                        count = 0;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void enableDisabledEditTExt() {
        if (nameTV.isEnabled()) {
            nameTV.setEnabled(false);
        } else nameTV.setEnabled(true);

        if (cityTv.isEnabled()) {
            cityTv.setEnabled(false);
        } else cityTv.setEnabled(true);

        if (bus_nameTV.isEnabled()) {
            bus_nameTV.setEnabled(false);
        } else bus_nameTV.setEnabled(true);

        if (emailTV.isEnabled()) {
            emailTV.setEnabled(false);
        } else emailTV.setEnabled(true);

        if (countryTV.isEnabled()) {
            countryTV.setEnabled(false);
        } else countryTV.setEnabled(true);

    }

    public void getUserProfile(final Activity activity) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(activity, "Please wait",
                "Loading Profile..", true);
        ringProgressDialog.setCancelable(false);
        String user_id = getData(activity, "user_id", "");

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "get_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* get profile response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {

                        Toast.makeText(activity,
                                response.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        name = response.getString("bk_fullname");
                        bus_name = response.getString("bk_businessname");
                        designation = response.getString("businesstype_name");
                        email = response.getString("bk_useremail");
                        phone = response.getString("bk_userphone");
                        country = response.getString("bk_usercountry");
                        city = response.getString("bk_useraddress");
                        image = response.getString("bk_userpic");
                        saveData(ProfileActivity.this, "user_image", image);
                        setData(name, bus_name, designation, email, phone, country, city, image);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void setData(String name, String bus_name, String designation,
                         String email, String phone, String country,
                         String city, String image_user) {

        nameTV.setText(name);
        designationTV.setText(designation);
        cityTv.setText(city);
        countryTV.setText(country);
        bus_nameTV.setText(bus_name);
        emailTV.setText(email);
        phoneTV.setText(phone);
        if (image_user.length() > 3) {
            Picasso.with(ProfileActivity.this).load(image_user).placeholder(R.drawable.profile).into(user_pic);
            Picasso.with(ProfileActivity.this).load(image_user).placeholder(R.drawable.profile).into(nav_img);

        } else
            user_pic.setImageResource(R.drawable.profile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = getImageUri(ProfileActivity.this, thumbnail);
        String path = getRealPathFromURI(uri, ProfileActivity.this);
        System.out.println("========== image path from gallery =======");
        System.out.println(uri);
        System.out.println(path);
        System.out.println(destination);

        updateProfile(ProfileActivity.this, email, name, bus_name, country, city, destination);
//        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm ;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().
                        getContentResolver(), data.getData());

                Uri uri = getImageUri(ProfileActivity.this, bm);

                String path = getRealPathFromURI(uri, ProfileActivity.this);

                File file = new File(path);
                System.out.println("========== image path from gallery =======");
                System.out.println(uri);
                System.out.println(file);
                System.out.println(path);
                updateProfile(ProfileActivity.this, email, name, bus_name, country,
                        city, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

