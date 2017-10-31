package fr.wcs.wishlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Models.Item;

public class SearchItemActivity extends AppCompatActivity {

    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewGift);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(this, mItems, ItemAdapter.State.GIFT);
        recyclerView.setAdapter(mItemAdapter);

        AsyncHttpClient client = new AsyncHttpClient();

    }
}
