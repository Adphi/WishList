
package fr.wcs.wishlist.Models.CDiscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saving {

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Value")
    @Expose
    private String value;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Saving() {
    }

    /**
     * 
     * @param value
     * @param type
     */
    public Saving(String type, String value) {
        super();
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
