package fr.wcs.wishlist;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private Item mSelectedItem = null;
    private AlertDialog dialog;

    @Override
    public View onCreateView (LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        final View rootview = inflater.inflate(R.layout.gift, container, false);
        mUser = UserHelper.getUser();
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewGift);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), mItems, ItemAdapter.State.GIFT);
        recyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemSelectedListener(new ItemAdapter.ItemItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelectedItem = mItems.get(index);

                View view = getLayoutInflater().inflate(R.layout.dialog_card, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                ImageView image = view.findViewById(R.id.imagePlace);
                TextView textDescr = view.findViewById(R.id.descrText);

                textDescr.setText(mSelectedItem.getDescription());
                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(mSelectedItem.getImageUrl());
                Glide.with(getActivity())
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .into(image);
                final ImageButton linkBtn = view.findViewById(R.id.linkButton);
                String link = mSelectedItem.getItemUrl();
                if (link.isEmpty()){
                    linkBtn.setVisibility(View.GONE);
                }
                else {
                    linkBtn.setVisibility(View.VISIBLE);
                }
                linkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mSelectedItem.getItemUrl();
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                        catch(ActivityNotFoundException e) {
                            // Chrome is not installed
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(i);
                        }
                    }
                });
            }
        });

        SearchView searchView = rootview.findViewById(R.id.searchView);
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
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
