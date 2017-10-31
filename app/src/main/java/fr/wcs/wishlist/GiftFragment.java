package fr.wcs.wishlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Helpers.FirebaseHelper;
import fr.wcs.wishlist.Helpers.UserHelper;
import fr.wcs.wishlist.Models.Item;
import fr.wcs.wishlist.Models.User;

public class GiftFragment extends Fragment{

    private DatabaseReference mRef;
    private ArrayList<Item> mItems = new ArrayList<>();
    private User mUser;
    private ItemAdapter mItemAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.gift, container, false);
        mUser = UserHelper.getInstance();
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewGift);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), mItems, ItemAdapter.State.GIFT);
        recyclerView.setAdapter(mItemAdapter);

        final String userUid = String.valueOf(mUser.getName().hashCode());
        mRef = FirebaseHelper.getInstance().getReference("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mItems.clear();
                Log.d("HELPER", "GiftList Changed");
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if(!userUid.equals(data.getKey())) {
                        User user = data.getValue(User.class);
                        for(Item item : user.getWishItems()) {
                            mItems.add(item);

                        }
                    }
                }
                mItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mItemAdapter.setOnGiftListener(new ItemAdapter.ItemGiftListener() {
            @Override
            public void onItemGift(int index) {
                final Item item = mItems.get(index);
                final String userName = item.getUserName();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("HELPER", "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                        String userUid = String.valueOf(userName.hashCode());
                        DataSnapshot userData = dataSnapshot.child(userUid);
                        User user = userData.getValue(User.class);
                        Log.d("HELPER", "onDataChange: " + user.toString());
                        user.getOfferedItems().add(item);
                        user.getWishItems().remove(item);
                        mRef.child(userUid).setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return rootview;
    }
}
