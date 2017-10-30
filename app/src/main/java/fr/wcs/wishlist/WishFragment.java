package fr.wcs.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Models.Item;

public class WishFragment extends Fragment{
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.wish, container, false);

        FloatingActionButton fab = rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("object1", "desciption1", "gs://wishlist-f39ed.appspot.com/arcade_navale.png"));
        items.add(new Item("object2", "description2", "gs://wishlist-f39ed.appspot.com/nikola_tesla_2037575.jpg"));
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerViewWish);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(getActivity(), items);
        recyclerView.setAdapter(itemAdapter);


        return rootview;
    }
}

