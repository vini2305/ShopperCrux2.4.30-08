package com.wvs.shoppercrux.fragments;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wvs.shoppercrux.R;
import com.wvs.shoppercrux.Stores.GetDataAdapter;
import com.wvs.shoppercrux.Stores.RecyclerViewAdapter;
import com.wvs.shoppercrux.activities.LocationActivity;
import com.wvs.shoppercrux.activities.StoreActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements SearchView.OnQueryTextListener {
    List<GetDataAdapter> getDataAdapters;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewadapter;
    Toolbar toolbar;
    String GET_JSON_DATA_HTTP_URL = "http://shoppercrux.com/shopper_android_api/seller.php?id=";
    String JSON_IMAGE_TITLE_NAME = "nickname";
    String JSON_IMAGE_URL = "banner";
    String SELLER_ID = "seller_id";
    String SELLER_ADDRESS = "seller_address";
    String STORE_URL;
    JsonArrayRequest jsonArrayRequest;
    Context context;
    RequestQueue requestQueue;
    View view;
    String id;
    private SearchManager searchManager;
    private SearchView searchView;
    private MenuItem mSearchItem;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton setLocation, mapView;
    private FloatingActionMenu menu;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);
        // Inflate the layout for this fragment
//        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Stores");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Intent intent = getIntent();
//        String id = intent.getStringExtra("location_id");
////        textView = (TextView) findViewById(R.id.display_id);
////        textView.setText(id);
//        Log.d("location id",id);
//        STORE_URL = GET_JSON_DATA_HTTP_URL+id;
//        Log.d("location id",STORE_URL);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("location_id");
        }

        //  Log.d("Id",id);

        STORE_URL = GET_JSON_DATA_HTTP_URL + id;
        Log.d("location id", STORE_URL);
        getDataAdapters = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_store_frag);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        JSON_DATA_WEB_CALL();
        Log.d("sellerid", SELLER_ID);
        setLocation = (FloatingActionButton) view.findViewById(R.id.locationBtn);
        mapView = (FloatingActionButton) view.findViewById(R.id.listBtn);
        menu = (FloatingActionMenu) view.findViewById(R.id.menu);

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                LocationActivity fragment2 = new LocationActivity();
//                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(StoreFragment.this);
                fragmentTransaction2.add(R.id.content_frame, fragment2);
                fragmentTransaction2.commit();

            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                HomeFragment fragment2 = new HomeFragment();
//                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(StoreFragment.this);
                fragmentTransaction2.add(R.id.content_frame, fragment2);
                fragmentTransaction2.commit();

            }
        });
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        final List<GetDataAdapter> stores = filter(getDataAdapters, query);
        recyclerViewadapter.setFilter(stores);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        mSearchItem = menu.findItem(R.id.search);
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getContext(), StoreActivity.class)));
            searchView.setSubmitButtonEnabled(true);
            searchView.setQueryRefinementEnabled(true);
            searchView.setOnQueryTextListener(this);
        }

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {

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

        requestQueue = Volley.newRequestQueue(getContext());

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
                getDataAdapter.setSellerID(json.getString(SELLER_ID));
                getDataAdapter.setSellerAddress(json.getString(SELLER_ADDRESS).replaceAll("\t", ""));
                // String seller_data =  getDataAdapter.setSellerID(json.getString(SELLER_ID));
//Log.d("sellerid", getDataAdapter.setSellerID(json.getString(SELLER_ID))

            } catch (JSONException e) {

                e.printStackTrace();
            }

            getDataAdapters.add(getDataAdapter);
        }

        recyclerViewadapter = new RecyclerViewAdapter(getDataAdapters, getContext());

        recyclerView.setAdapter(recyclerViewadapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private List<GetDataAdapter> filter(List<GetDataAdapter> stores, String query) {
        query = query.toLowerCase();

        final List<GetDataAdapter> storeList = new ArrayList<>();
        for (GetDataAdapter store : stores) {
            final String storeName = store.getImageTitleName().toLowerCase();
            if (storeName.contains(query)) {
                storeList.add(store);
            }
        }
        return storeList;
    }

}
