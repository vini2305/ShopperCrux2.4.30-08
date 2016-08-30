package com.wvs.shoppercrux.Gson;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wvs.shoppercrux.R;
import com.wvs.shoppercrux.fragments.StoreFragment;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    public static String locationData;
    private List<ItemObject> itemList;
    private Context context;
    public RecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        holder.songTitle.setText("" + itemList.get(position).getSongTitle());
        holder.songYear.setText("" + itemList.get(position).getSongYear());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              int pos = getItemCount();
//                Intent i = new Intent(context, StoreActivity.class);
////                i.putExtra("location_id",itemList.get(position).getSongYear());
//                context.startActivity(i);

//                String data =itemList.get(position).getSongYear();
//                Intent i = new Intent(context, StoreMapView.class);
//                i.putExtra("location_id",data);
//                context.startActivity(i);

//                String data =itemList.get(position).getSongYear();
//                Intent i = new Intent(context, StoreActivity.class);
//                i.putExtra("location_id",data);
//                context.startActivity(i);

//
                locationData = itemList.get(position).getSongYear();
                StoreFragment fragment;
                fragment = new StoreFragment();
                Bundle bundle = new Bundle();
                bundle.putString("location_id", locationData);
                fragment.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_frame, fragment)
                        .commit();
                // Toast.makeText(context, itemList.get(position).getSongYear(), Toast.LENGTH_SHORT).show();

            }
        });
        //holder.songAuthor.setText("Song Author: " + itemList.get(position).getSongAuthor());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
