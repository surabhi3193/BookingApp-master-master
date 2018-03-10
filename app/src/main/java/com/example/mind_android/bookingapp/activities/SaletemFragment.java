package com.example.mind_android.bookingapp.activities;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.FormActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.adapter.SalesAdapter;
import com.example.mind_android.bookingapp.beans.Sales;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;

import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class SaletemFragment extends android.app.DialogFragment {

    Button btn;
    ListView lv;
    SearchView sv;
    ArrayAdapter<String> adapter;
    static String[]stockArr = {};
    static String[]stockidArr = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView=inflater.inflate(R.layout.sale, null);

        //SET TITLE DIALOG TITLE
        getDialog().setTitle("Stock List");

        lv=(ListView) rootView.findViewById(R.id.listView1);
        sv=(SearchView) rootView.findViewById(R.id.searchView1);
        btn=(Button) rootView.findViewById(R.id.dismiss);


   if (isNetworkAvailable(getActivity().getApplicationContext()))
      getAllstocks(getActivity());
   else
   {
       getAllStocksFromLocal();
   }



        //SEARCH
        sv.setQueryHint("Search..");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                // TODO Auto-generated method stub

                if (adapter!=null)
                adapter.getFilter().filter(txt);
                return false;
            }
        });

        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("=========== item selected ===========");
                FormActivity.sale_stock=stockArr[position];
                FormActivity.sale_item_name.setText(stockArr[position]);
                FormActivity.sale_item_id=stockidArr[position];
                dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("=========== item selected ===========");
                FormActivity.sale_stock=stockArr[position];
                FormActivity.sale_item_name.setText(stockArr[position]);
                FormActivity.sale_item_id=stockidArr[position];
                dismiss();
            }
        });

        return rootView;
    }

    private void getAllStocksFromLocal() {
        final List<String> stockList = new ArrayList<String>();
        final List<String> idlist = new ArrayList<String>();

        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        JSONArray jArray = new JSONArray();

        Log.d("Reading: ", "Reading all Stocks..");
        List<Stock> stocks = db.getAllStocksExcept2();
        try {
            for (Stock cn : stocks) {
                JSONObject jobj = new JSONObject();

                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_qty() +
                        " ,unit price : " + cn.get_unit_per_price() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);


                jobj.put("stock_id", cn.get_id());
                jobj.put("stock_name", cn.get_name());
                jobj.put("stock_qty", cn.get_qty());
                jArray.put(jobj);

                if (!cn.get_qty().equals("0")) {
                    stockList.add(cn.get_name());
                    idlist.add(String.valueOf(cn.get_id()));
                }

            }
            stockArr = stockList.toArray(new String[stockList.size()]);
            stockidArr = idlist.toArray(new String[idlist.size()]);
            //CREATE AND SET ADAPTER TO LISTVIEW
            adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,stockArr);
            lv.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


    public  void getAllstocks(final Activity activity) {
        final List<String> stockList = new ArrayList<String>();
        final List<String> idlist = new ArrayList<String>();

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String bk_userid = getData(activity, "user_id", "");
        params.put("bk_userid", bk_userid);
        System.out.println(params);

        client.post(BASE_URL_NEW + "stock_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* all stock response ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {

                    }

                    else {

                        JSONArray jArray = response.getJSONArray("stocks");
                        if (jArray.length()>0)
                        {
                            for (int i =0;i<jArray.length();i++)
                            {
                                JSONObject obj = jArray.getJSONObject(i);

                                String id = obj.getString("stock_id");
                                String name = obj.getString("stock_name");
                                String qty = obj.getString("stock_qty");

                                if (!qty.equals("0")) {
                                    stockList.add(name);
                                    idlist.add(id);
                                }

                            }

                            stockArr = stockList.toArray(new String[stockList.size()]);
                            stockidArr = idlist.toArray(new String[idlist.size()]);
                            //CREATE AND SET ADAPTER TO LISTVIEW
                            adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,stockArr);
                            lv.setAdapter(adapter);


                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });

    }

}
