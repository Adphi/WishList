package fr.wcs.wishlist.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adphi on 30/10/17.
 */

public class User implements Parcelable{
    private String name;
    private ArrayList<Item> wishItems = new ArrayList<>();
    private ArrayList<Item> giftItems = new ArrayList<>();
    private ArrayList<Item> offeredItems = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getWishItems() {
        return wishItems;
    }

    public void setWishItems(ArrayList<Item> wishItems) {
        this.wishItems = wishItems;
    }

    public ArrayList<Item> getGiftItems() {
        return giftItems;
    }

    public void setGiftItems(ArrayList<Item> giftItems) {
        this.giftItems = giftItems;
    }

    public ArrayList<Item> getOfferedItems() {
        return offeredItems;
    }

    public void setOfferedItems(ArrayList<Item> offeredItems) {
        this.offeredItems = offeredItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeSerializable(wishItems);
        dest.writeSerializable(giftItems);
        dest.writeSerializable(offeredItems);
    }

    protected User(Parcel in) {
        name = in.readString();
        wishItems = in.createTypedArrayList(Item.CREATOR);
        giftItems = in.createTypedArrayList(Item.CREATOR);
        offeredItems = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
