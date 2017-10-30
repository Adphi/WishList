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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(imageUrl);
    }

    protected Item(Parcel in) {
        description = in.readString();
        imageUrl = in.readString();
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
