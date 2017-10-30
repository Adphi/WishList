package fr.wcs.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Helpers.UserHelper;
import fr.wcs.wishlist.Models.User;

public class WishFragment extends Fragment{

    private static final String TAG = "HELPER";

    public User mUser;
    ItemAdapter mItemAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.wish, container, false);


        mUser = UserHelper.getInstance();
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewWish);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), mUser.getWishItems(), ItemAdapter.State.WISH);
        recyclerView.setAdapter(mItemAdapter);

        FloatingActionButton fab = rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        UserHelper.setOnUserDataReaderListener(new UserHelper.UserDataReadyListener() {
            @Override
            public void onUserDataReader(User user) {
                Log.d(TAG, "onUserDataReader() called with: user = [" + user + "]");
                mUser = user;
                mItemAdapter.setItems(mUser.getWishItems());
            }
        });

        mItemAdapter.setOnItemDeletedListener(new ItemAdapter.ItemDeletedListener() {
            @Override
            public void onItemDeleted(int index) {
                mUser.getWishItems().remove(index);
                mItemAdapter.notifyDataSetChanged();
                UserHelper.update();
            }
        });

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mItemAdapter.notifyDataSetChanged();
    }
}

