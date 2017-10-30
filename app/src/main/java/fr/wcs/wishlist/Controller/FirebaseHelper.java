package fr.wcs.wishlist.Controller;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by adphi on 30/10/17.
 */

public class FirebaseHelper {
    private static FirebaseDatabase mFirebaseDatabase = null;

    public static FirebaseDatabase getInstance() {
        if(mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase.setPersistenceEnabled(true);
        }
        return mFirebaseDatabase;
    }
}
