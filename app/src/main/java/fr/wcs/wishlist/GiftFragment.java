package fr.wcs.wishlist;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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

    private final String TAG = "HELPER";

    private DatabaseReference mRef;
    private ArrayList<Item> mItems = new ArrayList<>();
    private ArrayList<Item> mFiltedItems = new ArrayList<>();
    private User mUser;
    private ItemAdapter mItemAdapter;
    private ArrayList<String> mUsersNames = new ArrayList<>();
    private String mSearchText = "";

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.gift, container, false);
        mUser = UserHelper.getUser();
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewGift);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), mItems, ItemAdapter.State.GIFT);
        recyclerView.setAdapter(mItemAdapter);

        SearchView searchView = (SearchView) rootview.findViewById(R.id.searchView);
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mUsersNames);
        searchAutoComplete.setAdapter(adapter);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                mSearchText=(String)parent.getItemAtPosition(position);
                searchAutoComplete.setText(""+mSearchText);
                mItems.addAll(mFiltedItems);
                mFiltedItems.clear();
                for(Item item : mItems) {
                    if(!item.getUserName().toLowerCase().contains(mSearchText.toLowerCase())){
                        mFiltedItems.add(item);
                    }
                }
                mItems.removeAll(mFiltedItems);
                mItemAdapter.setItems(mItems);
                mItemAdapter.notifyDataSetChanged();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchText = newText;
                mItems.addAll(mFiltedItems);
                mFiltedItems.clear();
                for(Item item : mItems) {
                    if(!item.getUserName().toLowerCase().contains(mSearchText.toLowerCase())){
                        mFiltedItems.add(item);
                    }
                }
                mItems.removeAll(mFiltedItems);
                mItemAdapter.setItems(mItems);
                mItemAdapter.notifyDataSetChanged();
                return false;
            }
        });

        final String userUid = String.valueOf(mUser.getName().hashCode());
        mRef = FirebaseHelper.getInstance().getReference("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mItems.clear();
                mFiltedItems.clear();
                mUsersNames.clear();
                Log.d("HELPER", "GiftList Changed");
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if(!userUid.equals(data.getKey())) {
                        mUsersNames.add(user.getName());
                        for(Item item : user.getWishItems()) {
                            if(user.getName().toLowerCase().contains(mSearchText.toLowerCase())){
                                mItems.add(item);
                            }
                            else {
                                mFiltedItems.add(item);
                            }
                        }
                    }
                }
                mItemAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
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
                        String userUid = String.valueOf(userName.hashCode());
                        DataSnapshot userData = dataSnapshot.child(userUid);
                        User user = userData.getValue(User.class);
                        Log.d("HELPER", "onDataChange: " + user.toString());
                        user.setMessage(String.format("%s t'as offert %s", mUser.getName(), item.getDescription()));
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
