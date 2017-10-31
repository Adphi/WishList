package fr.wcs.wishlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Helpers.UserHelper;
import fr.wcs.wishlist.Models.Item;
import fr.wcs.wishlist.Models.User;

public class OfferedFragment extends Fragment{

    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItems;
    private User mUser;
    private UserHelper mUserHelper;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.offered, container, false);

        mUser = UserHelper.getInstance();
        mUserHelper = new UserHelper();
        mItems = mUser.getOfferedItems();
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewOffered);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), mItems, ItemAdapter.State.OFFER);
        recyclerView.setAdapter(mItemAdapter);

        mUserHelper.setOnUserDataReaderListener(new UserHelper.UserDataReadyListener() {
            @Override
            public void onUserDataReady(User user) {
                Log.d("HELPER", "onUserDataReady: Items Offered Changed");
                mUser = user;
                mItems = mUser.getOfferedItems();
                Log.d("HELPER", "onGiftChanged: " + mItems);
                mItemAdapter.setItems(mItems);
                mItemAdapter.notifyDataSetChanged();
            }
        });

        return rootview;
    }
}
