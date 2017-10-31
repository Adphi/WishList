
package fr.wcs.wishlist.Models.CDiscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceDetails {

    @SerializedName("Discount")
    @Expose
    private Object discount;
    @SerializedName("ReferencePrice")
    @Expose
    private String referencePrice;
    @SerializedName("Saving")
    @Expose
    private Saving saving;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PriceDetails() {
    }

    /**
     * 
     * @param referencePrice
     * @param saving
     * @param discount
     */
    public PriceDetails(Object discount, String referencePrice, Saving saving) {
        super();
        this.discount = discount;
        this.referencePrice = referencePrice;
        this.saving = saving;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Saving getSaving() {
        return saving;
    }

    public void setSaving(Saving saving) {
        this.saving = saving;
    }

}
