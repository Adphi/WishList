package fr.wcs.wishlist.Models;

/**
 * Created by adphi on 30/10/17.
 */

public class Item {
    private String description;
    private String imageUrl;
    private String itemUrl;

    public Item(String description, String imageUrl, String itemUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.itemUrl = itemUrl;
    }

    public Item() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
