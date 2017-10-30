package fr.wcs.wishlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import fr.wcs.wishlist.Models.Item;

public class AddActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseStorage mFirebaseStorage;
    private ProgressDialog mProgressDialog;

    private AlertDialog alert;

    private ImageButton mImageWish;
    private Button addButton;

    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mProgressDialog = new ProgressDialog(this);

        mFirebaseStorage = FirebaseStorage.getInstance();

        ImageButton imageWish =  findViewById(R.id.imageWish);
        imageWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

                builder.setView(view);
                alert = builder.create();
                alert.show();
                ImageButton takeImage = view.findViewById(R.id.takeImage);
                takeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dispatchTakePictureIntent();
                    }
                });
                ImageButton selectImage = view.findViewById(R.id.selectImage);
                selectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);

                    }
                });
            }
        });
        addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageWish = findViewById(R.id.imageWish);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            mProgressDialog.setMessage("Uploading");
            mProgressDialog.show();

            final StorageReference photopath = mFirebaseStorage.getReference("Photos").child(uri.getLastPathSegment());

            photopath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                    Glide.with(AddActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(photopath)
                            .into(mImageWish);

                    final EditText descriptionText = findViewById(R.id.descriptionEditText);
                    final EditText linkText = findViewById(R.id.linkEditText);
                    if (!(descriptionText.equals("") && photopath.toString().equals(""))){
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Item item = new Item(descriptionText.getText().toString(), photopath.toString(), linkText.getText().toString());
                                AddActivity.super.onBackPressed();
                            }
                        });
                    }
                    else {
                        Toast.makeText(AddActivity.this, "Il nous manque des informations !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}



