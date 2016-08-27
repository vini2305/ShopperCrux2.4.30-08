package com.wvs.shoppercrux.Cart;

import android.util.Log;

/**
 * Created by root on 25/8/16.
 */
public class CartList {

    public String productImage,productName,productQuantity,productId,productPrice;

    public void setProductImage(String productImage) {
        productImage = productImage.replaceAll(" ", "%20");
        Log.d("Image URls","Cart image replace URL:"+productImage);
        this.productImage = "http://shoppercrux.com/image/"+productImage;
    }

    public String getProductImage() { return productImage; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getProductName() { return productName; }

    public void setProductQuantity(String productQuantity) { this.productQuantity = productQuantity; }

    public String getProductQuantity() { return productQuantity; }

    public void setProductId(String productId) { this.productId = productId; }

    public String getProductId() { return productId; }

    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }

    public String getProductPrice() { return productPrice; }

}
