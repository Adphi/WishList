package fr.wcs.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import fr.wcs.wishlist.Controller.ItemAdapter;
import fr.wcs.wishlist.Models.CDiscount.RequestBuilder;
import fr.wcs.wishlist.Models.Item;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchItemActivity extends AppCompatActivity {

    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItems = new ArrayList<>();
    private String mSearchText = "";
    private ProgressBar mProgressBar;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(this, mItems, ItemAdapter.State.CDISCOUNT);
        recyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setOnItemSelectedListener(new ItemAdapter.ItemItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Item item = mItems.get(index);
                Intent intent = new Intent(SearchItemActivity.this, AddActivity.class);
                intent.putExtra("SelectedItem", item);
                startActivity(intent);
            }
        });

        mItemAdapter.setOnLoadMoreListener(new ItemAdapter.LoadMoreListener() {
            @Override
            public void onLoadMoreListener() {
                page++;
                mProgressBar.setVisibility(View.VISIBLE);
                try {
                    new AsynchronousGet().run(page);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        query("Android");
    }

    private void query(String text) {
        page = 0;
        mSearchText = text;
        mItems.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        try {
            new AsynchronousGet().run(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final class AsynchronousGet {
        private final OkHttpClient client = new OkHttpClient();
        public void run(int page) throws Exception {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            String args = new RequestBuilder(mSearchText, RequestBuilder.Sort
                    .RELEVANCE, 10, page).build();
            Log.d("HELPER", "run: " + args);
            RequestBody body = RequestBody.create(mediaType, args);
            Request request = new Request.Builder()
                    .url("https://api.cdiscount.com/OpenApi/json/Search")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    String data = response.body().string();
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(data).getAsJsonObject();
                    JsonArray productsArray = rootObj.getAsJsonArray("Products");
                    for(JsonElement element : productsArray){
                        JsonObject jsonItem = element.getAsJsonObject();
                        // Name
                        String name = jsonItem.get("Name").getAsString();
                        // URL
                        String url = jsonItem.getAsJsonObject("BestOffer").get("ProductURL").getAsString();
                        // Image
                        String imageUrl = jsonItem.get("MainImageUrl").getAsString();
                        mItems.add(new Item(name, imageUrl, url, ""));
                    }
                    Log.d("HELPER", "onResponse: " + mItems.toString());
                    SearchItemActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mItemAdapter.setItems(mItems);
                            mItemAdapter.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }
}
