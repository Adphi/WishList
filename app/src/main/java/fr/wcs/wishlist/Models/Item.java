package fr.wcs.wishlist.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adphi on 30/10/17.
 */

public class Item implements Parcelable{

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

    @Override
    public String toString() {
        return "Item{" +
                "description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(itemUrl);
        dest.writeString(userName);
    }

    protected Item(Parcel in) {
        description = in.readString();
        imageUrl = in.readString();
        itemUrl = in.readString();
        userName = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
