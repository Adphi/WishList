package fr.wcs.wishlist.Models;

/**
 * Created by adphi on 30/10/17.
 */

public class Item{

    private String description;
    private String imageUrl;
    private String itemUrl;
    private String userName;

    public Item(String description, String imageUrl, String itemUrl, String userName) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.itemUrl = itemUrl;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    @Override
    public boolean equals(Object obj) {
        Item other = (Item)obj;
        return this.userName.equals(other.userName)
                && this.description.equals(other.description)
                && this.imageUrl.equals(other.imageUrl)
                && this.itemUrl.equals(other.itemUrl);
    }
}
