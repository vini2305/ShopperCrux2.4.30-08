package com.wvs.shoppercrux.StoreProfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wvs.shoppercrux.R;
import com.wvs.shoppercrux.Stores.ServerImageParseAdapter;

import java.util.List;

/**
 * Created by root on 23/8/16.
 */
public class StoreProfileRecycle extends RecyclerView.Adapter<StoreProfileRecycle.ViewHolder> {

    Context context;
    List<StoreProfileAdapter> storeProfile;
    ImageLoader imageLoader;

    public StoreProfileRecycle(List<StoreProfileAdapter> storeProfile,Context context){
        super();
        this.storeProfile = storeProfile;
        this.context = context;
    }

    @Override
    public StoreProfileRecycle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_profile_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StoreProfileRecycle.ViewHolder holder, int position) {
        StoreProfileAdapter storePro = storeProfile.get(position);
        imageLoader = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader.get(storePro.getImageServerUrl(),
                ImageLoader.getImageListener(
                        holder.storeBanner,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );
        holder.storeBanner.setImageUrl(storePro.getImageServerUrl(), imageLoader);
        holder.storeName.setText(storePro.getStoreName());
        holder.storeDescription.setText(stripHtml(storePro.getStoreDescription()));
        holder.storeAddress.setText(storePro.getStoreAddress());
        holder.storeContact.setText(storePro.getStoreContact());
    }

    @Override
    public int getItemCount() {
        return storeProfile.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName,storeAddress,storeContact,storeDescription;
        private NetworkImageView storeBanner;
        public ViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.store_name);
            storeAddress = (TextView) itemView.findViewById(R.id.seller_address);
            storeContact = (TextView) itemView.findViewById(R.id.seller_contact);
            storeDescription = (TextView) itemView.findViewById(R.id.seller_desc);
            storeBanner = (NetworkImageView) itemView.findViewById(R.id.store_banner);
        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }
}
