package fr.wcs.wishlist.Models;

/**
 * Created by adphi on 30/10/17.
 */

public class Item{

    private String description;
    private String imageUrl;
    private String itemUrl;
    private User user;

    public Item(String description, String imageUrl, String itemUrl, User user) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.itemUrl = itemUrl;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
