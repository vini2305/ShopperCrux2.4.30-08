package com.wvs.shoppercrux.Cart;

/**
 * Created by root on 25/8/16.
 */
public class CartList {

    private String productImage,productName,productQuantity,productId,productPrice;

    public String getProductImage() { return productImage; }

    public void setProductImage(String productImage) {
        productImage = productImage.replaceAll(" ", "%20");
        this.productImage = "http://shoppercrux.com/image/"+productImage;
    }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getProductQuantity() { return productQuantity; }

    public void setProductQuantity(String productQuantity) { this.productQuantity = productQuantity; }

    public String getProductId() { return productId; }

    public void setProductId(String productId) { this.productId = productId; }

    public String getProductPrice() { return productPrice; }

    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }
}
