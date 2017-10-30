package fr.wcs.wishlist.Helpers;

import android.util.Log;

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
    private static UserDataReadyListener mListener = null;

    private UserHelper(){}

    public static User init(String name) {
        Log.d(TAG, "init() called with: name = [" + name + " " + String.valueOf(name.hashCode()) + "]");
        mUser = new User(name);
        mFirebaseDatabase = FirebaseHelper.getInstance();
        mRef = mFirebaseDatabase.getReference("Users");
        mRef.keepSynced(true);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(String.valueOf(mUser.getName().hashCode()))){
                    mRef.child(String.valueOf(mUser.getName().hashCode())).setValue(mUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        return mUser;
    }

    public static User getInstance(){
        return mUser;
    }

    public static void update(){
        mRef.child(String.valueOf(mUser.getName().hashCode())).setValue(mUser);
    }

    public interface UserDataReadyListener {
        void onUserDataReady(User user);
    }

    public static void setOnUserDataReaderListener(UserDataReadyListener listener){
        mListener = listener;
    }
}
