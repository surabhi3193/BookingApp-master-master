package com.example.mind_android.bookingapp.storage;

import android.content.Context;
import android.content.SharedPreferences;


public class MySharedPref
{

       static SharedPreferences sp;

        public static void saveData(Context context, String key, String value)
        {
            sp = context.getSharedPreferences("xchangemall",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key,value);
            editor.commit();
        }
        public static String getData(Context context, String key, String value)
        {
            sp = context.getSharedPreferences("xchangemall",context.MODE_PRIVATE);
            return sp.getString(key,value);
        }
        public static void DeleteData(Context context)
        {
            sp = context.getSharedPreferences("xchangemall",context.MODE_PRIVATE);
            sp.edit().clear().commit();
        }

        public static void NullData(Context context , String key)
        {
            sp = context.getSharedPreferences("xchangemall",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key,null);
            editor.commit();
        }


}
