package com.wvs.shoppercrux.Cart;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wvs.shoppercrux.R;

import java.util.List;

/**
 * Created by root on 25/8/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartList> cartList;
    ImageLoader Imageloader1;

    public CartAdapter(List<CartList> cartList, Context context){
        super();
        this.cartList = cartList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, int position) {

        final CartList cartPojo = cartList.get(position);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Imageloader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        if(cartPojo.getImageServerUrl() != null && position !=0) {
            Imageloader1.get(cartPojo.getImageServerUrl(),
                    ImageLoader.getImageListener(
                            holder.prodImage,//Server Image
                            R.mipmap.ic_launcher,//Before loading server image the default showing image.
                            android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                    )
            );

            holder.prodImage.setImageUrl(cartPojo.getImageServerUrl(),Imageloader1);
            holder.prodName.setText(cartPojo.getProductName());
            holder.quantity.setText(cartPojo.getProductQuantity());
            holder.price.setText(cartPojo.getTotalPrice());
            holder.increase.setImageResource(R.drawable.plus);
            holder.decrease.setImageResource(R.drawable.minus);
        } else if(cartPojo.getImageServerUrl() == null) {
            holder.cardView.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.GONE);
            holder.increase.setVisibility(View.GONE);
            holder.decrease.setVisibility(View.GONE);
            holder.prodImage.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            holder.prodName.setVisibility(View.GONE);
            holder.quantity.setVisibility(View.GONE);
            holder.linearLayout.setLayoutParams(params);
        }

        Log.d("Image URl","Bindview:"+cartPojo.getImageServerUrl()+","+position);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView prodImage;
        public TextView prodName,quantity,price;
        public ImageView increase,decrease;
        public CardView cardView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.cartItems);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
            prodImage = (NetworkImageView) itemView.findViewById(R.id.cartProdImage);
            prodName = (TextView) itemView.findViewById(R.id.productName);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            price = (TextView) itemView.findViewById(R.id.price);
            increase = (ImageView) itemView.findViewById(R.id.increase);
            decrease = (ImageView) itemView.findViewById(R.id.decrease);
        }
    }
}























