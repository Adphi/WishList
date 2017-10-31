
package fr.wcs.wishlist.Models.CDiscount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seller {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Seller() {
    }

    /**
     * 
     * @param id
     * @param name
     */
    public Seller(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
