package fr.wcs.wishlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Models.CDiscount.RequestBuilder;
import fr.wcs.wishlist.Models.Item;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchItemActivity extends AppCompatActivity {

    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(this, mItems, ItemAdapter.State.GIFT);
        recyclerView.setAdapter(mItemAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, new RequestBuilder("Tablette", RequestBuilder.Sort
                .RELEVANCE, 5, 0).build());
        Request request = new Request.Builder()
                .url("https://api.cdiscount.com/OpenApi/json/Search")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("HELPER", "onCreate: " + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
