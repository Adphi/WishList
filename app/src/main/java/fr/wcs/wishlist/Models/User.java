package fr.wcs.wishlist.Models;

import java.util.ArrayList;

/**
 * Created by adphi on 30/10/17.
 */

public class User{
    private String name;
    private ArrayList<Item> wishItems = new ArrayList<>();
    private ArrayList<Item> giftItems = new ArrayList<>();
    private ArrayList<Item> offeredItems = new ArrayList<>();
    private String message = "";

    public User(String name) {
        this.name = name;
    }

    public User(){}

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
