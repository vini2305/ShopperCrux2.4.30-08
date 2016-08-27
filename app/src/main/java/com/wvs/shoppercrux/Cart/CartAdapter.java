package com.wvs.shoppercrux.Cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wvs.shoppercrux.R;

import java.util.List;

/**
 * Created by root on 25/8/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartList> list;
    private ImageLoader imageLoader;

    public CartAdapter(List<CartList> cartList, Context context){
        super();
        this.list = cartList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CartList cartList = list.get(position);

        imageLoader = ServerImageParseAdapter.getInstance(context).getImageLoader();


//        imageLoader.get(cartList.getProductImage(),
//                ImageLoader.getImageListener(
//                        holder.prodImage,//Server Image
//                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
//                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
//                )
//        );
//
//
//        holder.prodImage.setImageUrl(cartList.getProductImage(), imageLoader);

        Log.d("Image URl","Bindview:"+cartList.getProductImage());
        holder.prodName.setText(cartList.getProductName());
        holder.quantity.setText(cartList.getProductQuantity());
        holder.price.setText(cartList.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView prodImage;
        public TextView prodName,quantity,price;

        public ViewHolder(View itemView) {
            super(itemView);
            prodImage = (NetworkImageView) itemView.findViewById(R.id.productImage);
            prodName = (TextView) itemView.findViewById(R.id.productName);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}























