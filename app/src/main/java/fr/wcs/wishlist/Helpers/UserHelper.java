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
        mUser = new User(name);
        mFirebaseDatabase = FirebaseHelper.getInstance();
        int uid = mUser.getName().hashCode();
        mRef = mFirebaseDatabase.getReference(String.valueOf(uid));
        mRef.keepSynced(true);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                if(mListener != null) {
                    mListener.onUserDataReader(mUser);
                    Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
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
        mRef.setValue(mUser);
    }

    public interface UserDataReadyListener {
        void onUserDataReader(User user);
    }

    public static void setOnUserDataReaderListener(UserDataReadyListener listener){
        mListener = listener;
    }
}
