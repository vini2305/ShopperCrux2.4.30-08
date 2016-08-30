package com.wvs.shoppercrux.Wishlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wvs.shoppercrux.Product.ServerImageParseAdapter;
import com.wvs.shoppercrux.R;

import java.util.List;

/**
 * Created by root on 30/8/16.
 */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    Context context;
    List<WishListPojo> wishListPojo;
    ImageLoader imageLoader;

    public WishListAdapter(List<WishListPojo> wishListPojo,Context context){
        super();
        this.context=context;
        this.wishListPojo=wishListPojo;
    }

    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WishListAdapter.ViewHolder holder, int position) {
        final WishListPojo pojo= wishListPojo.get(position);
        imageLoader = ServerImageParseAdapter.getInstance(context).getImageLoader();
        imageLoader.get(pojo.getProdImage(),
                ImageLoader.getImageListener(
                        holder.image,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );
        holder.image.setImageUrl(pojo.getProdImage(),imageLoader);
        holder.name.setText(pojo.getProdName());
    }

    @Override
    public int getItemCount() {
        return wishListPojo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView image;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.prodImage);
            name = (TextView) itemView.findViewById(R.id.prodName);
        }
    }
}
