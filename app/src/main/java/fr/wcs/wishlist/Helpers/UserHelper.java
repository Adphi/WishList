package fr.wcs.wishlist.Helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wcs.wishlist.Models.User;

/**
 * Created by adphi on 30/10/17.
 */

public class UserHelper {
    private static final String TAG = "Helper";
    private static User mUser = null;
    private static FirebaseDatabase mFirebaseDatabase = null;
    private static DatabaseReference mRef = null;
    private UserDataReadyListener mListener = null;

    public UserHelper(){
        mFirebaseDatabase = FirebaseHelper.getInstance();
        mRef = mFirebaseDatabase.getReference("Users");
        mRef.keepSynced(true);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot userSnapShot = dataSnapshot.child(String.valueOf(mUser.getName().hashCode()));
                mUser = userSnapShot.getValue(User.class);
                if(mListener != null && mUser != null) {
                    mListener.onUserDataReady(mUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static User init(String name) {
        mUser = new User(name);
        return mUser;
    }

    public static User getUser(){
        return mUser;
    }

    public static void update(){
        mRef.child(String.valueOf(mUser.getName().hashCode())).setValue(mUser);
    }

    public interface UserDataReadyListener {
        void onUserDataReady(User user);
    }

    public void setOnUserDataReaderListener(UserDataReadyListener listener){
        mListener = listener;
    }
}
