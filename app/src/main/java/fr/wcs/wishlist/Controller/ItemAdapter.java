package fr.wcs.wishlist.Controller;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import fr.wcs.wishlist.Models.Item;
import fr.wcs.wishlist.R;

/**
 * Created by adphi on 30/10/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Item> mItems;
    private FirebaseStorage mFirebaseStorage;
    private ItemDeletedListener listener = null;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.mContext = context;
        this.mItems = items;
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    public void setItems(ArrayList<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String itemImageUrl = mItems.get(position).getImageUrl();
        StorageReference reference = mFirebaseStorage.getReferenceFromUrl(itemImageUrl);
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(reference)
                .into(holder.mImageViewItemPhoto);
        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(mItems.get(position));
                notifyDataSetChanged();
                if(listener != null) {
                    listener.onItemDeleted(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewItemPhoto;
        FloatingActionButton mButtonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewItemPhoto = itemView.findViewById(R.id.imageViewItemImage);
            mButtonDelete = itemView.findViewById(R.id.floatingActionButtonDelete);
        }

    }

    public void setOnItemDeletedListener(ItemDeletedListener listener) {
        this.listener = listener;
    }

    interface ItemDeletedListener {
        void onItemDeleted(int index);
    }
}
