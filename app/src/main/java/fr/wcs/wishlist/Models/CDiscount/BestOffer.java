
package fr.wcs.wishlist.Models.CDiscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BestOffer {

    @SerializedName("Condition")
    @Expose
    private String condition;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("IsAvailable")
    @Expose
    private Boolean isAvailable;
    @SerializedName("PriceDetails")
    @Expose
    private PriceDetails priceDetails;
    @SerializedName("ProductURL")
    @Expose
    private String productURL;
    @SerializedName("SalePrice")
    @Expose
    private String salePrice;
    @SerializedName("Seller")
    @Expose
    private Seller seller;
    @SerializedName("Shippings")
    @Expose
    private Object shippings;
    @SerializedName("Sizes")
    @Expose
    private Object sizes;

    /**
     * No args constructor for use in serialization
     * 
     */
    public BestOffer() {
    }

    /**
     * 
     * @param sizes
     * @param id
     * @param priceDetails
     * @param condition
     * @param shippings
     * @param salePrice
     * @param seller
     * @param isAvailable
     * @param productURL
     */
    public BestOffer(String condition, String id, Boolean isAvailable, PriceDetails priceDetails, String productURL, String salePrice, Seller seller, Object shippings, Object sizes) {
        super();
        this.condition = condition;
        this.id = id;
        this.isAvailable = isAvailable;
        this.priceDetails = priceDetails;
        this.productURL = productURL;
        this.salePrice = salePrice;
        this.seller = seller;
        this.shippings = shippings;
        this.sizes = sizes;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Object getShippings() {
        return shippings;
    }

    public void setShippings(Object shippings) {
        this.shippings = shippings;
    }

    public Object getSizes() {
        return sizes;
    }

    public void setSizes(Object sizes) {
        this.sizes = sizes;
    }

}
