package com.wvs.shoppercrux.Product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.wvs.shoppercrux.R;
import com.wvs.shoppercrux.activities.Product;
import com.wvs.shoppercrux.activities.ProductList;
import com.wvs.shoppercrux.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUNED on 6/16/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<GetDataAdapter> getDataAdapter;
    ImageLoader imageLoader1;
    JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    String GET_CART_COUNT_URL="http://shoppercrux.com/shopper_android_api/addtocart.php";
    public String CART;
    public int count;
    public static final String MY_PREFS_NAME="CartCount";

    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context) {

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);

        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        viewHolder.networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        viewHolder.networkImageView.setImageUrl(getDataAdapter1.getImageServerUrl(), imageLoader1);

        viewHolder.ImageTitleNameView.setText(getDataAdapter1.getImageTitleName());
        viewHolder.sellerId.setText(getDataAdapter1.getSellerID());
        viewHolder.storename.setText(getDataAdapter1.getStoreName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);
                String sell = Product.sellerId;
                Log.d("sellnewid", sell);
                String product_id = getDataAdapter1.getSellerID();

                Intent i = new Intent(context, ProductList.class);
              //  i.putExtra("seller_id", sell);
                i.putExtra("product_id", product_id);
                context.startActivity(i);

//                Toast.makeText(context, "sdfsdfds", Toast.LENGTH_SHORT).show();
//                Log.d("productlist","dfskdkjdnf");
            }
        });

        viewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customer_id = SQLiteHandler.user_id;
                String product_id = getDataAdapter1.getSellerID();
                CART=GET_CART_COUNT_URL+"?cid="+customer_id+"&pid="+product_id;
                Log.d("Cart Url","Cart Url"+CART);
                addToCart();
            }
        });
    }

    public void addToCart() {

        jsonObjectRequest = new JsonObjectRequest(CART, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("sc","Response"+response);
              try {
                  count = Integer.parseInt(response.getString("count"));
                  Product.setBadgeCount(context,Product.icon, String.valueOf(count));

                  SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
                  SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
                  sharedEditor.putString("TotalCart", String.valueOf(count));
                  sharedEditor.commit();

                  Log.d("CartCount" ,"Cart count "+count);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

    }

    public void setFilter(List<GetDataAdapter> products) {
     getDataAdapter = new ArrayList<>();
     getDataAdapter.addAll(products);
     notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ImageTitleNameView,storename;
        public NetworkImageView networkImageView;
        public TextView sellerId;
        public ImageButton addToCart;

        public ViewHolder(View itemView) {

            super(itemView);
            ImageTitleNameView = (TextView) itemView.findViewById(R.id.textView_item);
            networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1);
            sellerId = (TextView) itemView.findViewById(R.id.tx_seller_id);
            storename = (TextView) itemView.findViewById(R.id.nickname);
            addToCart = (ImageButton) itemView.findViewById(R.id.addToCart);
        }
    }
}
