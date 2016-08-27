package com.wvs.shoppercrux.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wvs.shoppercrux.Product.GetDataAdapter;
import com.wvs.shoppercrux.Product.RecyclerViewAdapter;
import com.wvs.shoppercrux.R;
import com.wvs.shoppercrux.classes.BadgeDrawable;
import com.wvs.shoppercrux.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<GetDataAdapter> getDataAdapters;
    private RecyclerView recyclerView;
    private GridLayoutManager recyclerViewlayoutManager;
    private TextView title;
    private RecyclerViewAdapter recyclerViewadapter;
    private Toolbar toolbar;
    private String GET_JSON_DATA_HTTP_URL = "http://shoppercrux.com/shopper_android_api/product.php?id=";
    private String JSON_IMAGE_TITLE_NAME = "name";
    private String JSON_IMAGE_URL = "image";
    private String PRODUCT_ID = "product_id";
    private String STORE_NAME ="nickname";
    private String SELLER_ID="seller_id";
    private String STORE_URL;
    private SearchManager searchManager;
    private SearchView searchView;
    private JsonArrayRequest jsonArrayRequest;
    public static String product_new_data;
    private RequestQueue requestQueue;
    public static String sellerId;
    public static MenuItem mSearchItem,mCart;
    public Menu mMenu;
    JsonObjectRequest jsonObjectRequest;
    String GET_CART_COUNT_URL="http://shoppercrux.com/shopper_android_api/cartcount.php";
    String cartCount;
    public static LayerDrawable icon;
    public static final String MY_PREFS_NAME="CartCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.storeName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        sellerId = intent.getStringExtra("seller_id");
//        textView = (TextView) findViewById(R.sellerId.display_id);
//        textView.setText(sellerId);
         Log.d("seller_id ",sellerId);
        STORE_URL = GET_JSON_DATA_HTTP_URL + sellerId;
        Log.d("location sellerId", STORE_URL);
        getDataAdapters = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);


//        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerViewlayoutManager= new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        JSON_DATA_WEB_CALL();

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Product.this,StoreDescription.class);
                intent.putExtra("seller_id",sellerId);
                startActivity(intent);
            }
        });

    }



    public void JSON_DATA_WEB_CALL() {

        jsonArrayRequest = new JsonArrayRequest(STORE_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            GetDataAdapter getDataAdapter = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                getDataAdapter.setImageTitleNamee(json.getString(JSON_IMAGE_TITLE_NAME));

                getDataAdapter.setImageServerUrl(json.getString(JSON_IMAGE_URL));
                getDataAdapter.setSellerID(json.getString(PRODUCT_ID));
                getDataAdapter.setStoreName(json.getString(STORE_NAME));

                title.setText(getDataAdapter.getStoreName());

                product_new_data = getDataAdapter.setSellerID(json.getString(PRODUCT_ID));

                Log.d("product_new_data", product_new_data);


            } catch (JSONException e) {

                e.printStackTrace();
            }

            getDataAdapters.add(getDataAdapter);
        }

        recyclerViewadapter = new RecyclerViewAdapter(getDataAdapters, this);

        recyclerView.setAdapter(recyclerViewadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mCart = menu.findItem(R.id.cart);
        icon = (LayerDrawable) mCart.getIcon();

        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String sharedText = preferences.getString("TotalCart",null);
        if(sharedText != null) {
            setBadgeCount(this,icon,sharedText);
        } else {
            setBadgeCount(this,icon,"0");
        }

        mSearchItem = menu.findItem(R.id.search);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),StoreActivity.class)));
            searchView.setSubmitButtonEnabled(true);
            searchView.setQueryRefinementEnabled(true);
            searchView.setOnQueryTextListener(this);
        }

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener(){

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                recyclerViewadapter.setFilter(getDataAdapters);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.cart:
                Intent intent = new Intent(Product.this,CartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        final List<GetDataAdapter> products = filter(getDataAdapters,query);
        recyclerViewadapter.setFilter(products);
        return false;
    }

    private List<GetDataAdapter> filter(List<GetDataAdapter> products,String query) {
        query = query.toLowerCase();

        final List<GetDataAdapter> prod = new ArrayList<>();
        for(GetDataAdapter product : products) {
        final String productName = product.getImageTitleName().toLowerCase();
            if(productName.contains(query)) {
                prod.add(product);
            }
        }
        return prod;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


        public String count(final VolleyCallback callback) {

            String customer_id = SQLiteHandler.user_id;
            String CART=  GET_CART_COUNT_URL+"?cid="+customer_id;
            Log.d("CART", "count url :"+CART);

            jsonObjectRequest = new JsonObjectRequest(CART, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("sc","Response"+response);
                    try {
                        cartCount = response.getString("count");
                        callback.onSuccess(cartCount);
                        Log.d("CartCount" ,"Cart count "+cartCount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
            return cartCount;
        }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

}




















