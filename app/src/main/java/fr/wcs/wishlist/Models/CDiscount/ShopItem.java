
package fr.wcs.wishlist.Models.CDiscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopItem {

    @SerializedName("AssociatedProducts")
    @Expose
    private Object associatedProducts;
    @SerializedName("BestOffer")
    @Expose
    private BestOffer bestOffer;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Ean")
    @Expose
    private Object ean;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Images")
    @Expose
    private Object images;
    @SerializedName("MainImageUrl")
    @Expose
    private String mainImageUrl;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Offers")
    @Expose
    private Object offers;
    @SerializedName("OffersCount")
    @Expose
    private String offersCount;
    @SerializedName("Rating")
    @Expose
    private String rating;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ShopItem() {
    }

    /**
     * 
     * @param id
     * @param ean
     * @param bestOffer
     * @param description
     * @param offers
     * @param name
     * @param associatedProducts
     * @param images
     * @param brand
     * @param offersCount
     * @param rating
     * @param mainImageUrl
     */
    public ShopItem(Object associatedProducts, BestOffer bestOffer, String brand, String description, Object ean, String id, Object images, String mainImageUrl, String name, Object offers, String offersCount, String rating) {
        super();
        this.associatedProducts = associatedProducts;
        this.bestOffer = bestOffer;
        this.brand = brand;
        this.description = description;
        this.ean = ean;
        this.id = id;
        this.images = images;
        this.mainImageUrl = mainImageUrl;
        this.name = name;
        this.offers = offers;
        this.offersCount = offersCount;
        this.rating = rating;
    }

    public Object getAssociatedProducts() {
        return associatedProducts;
    }

    public void setAssociatedProducts(Object associatedProducts) {
        this.associatedProducts = associatedProducts;
    }

    public BestOffer getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(BestOffer bestOffer) {
        this.bestOffer = bestOffer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getEan() {
        return ean;
    }

    public void setEan(Object ean) {
        this.ean = ean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getImages() {
        return images;
    }

    public void setImages(Object images) {
        this.images = images;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOffers() {
        return offers;
    }

    public void setOffers(Object offers) {
        this.offers = offers;
    }

    public String getOffersCount() {
        return offersCount;
    }

    public void setOffersCount(String offersCount) {
        this.offersCount = offersCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
