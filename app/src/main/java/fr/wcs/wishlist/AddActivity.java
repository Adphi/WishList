package fr.wcs.wishlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import fr.wcs.wishlist.Helpers.UserHelper;
import fr.wcs.wishlist.Models.Item;
import fr.wcs.wishlist.Models.User;


public class AddActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseStorage mFirebaseStorage;
    private ProgressDialog mProgressDialog;

    private AlertDialog alert;

    private ImageButton mImageWish;
    private Button addButton;

    private User mUser;
    private Uri mUri = null;
    private Bitmap mBitmap = null;

    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mProgressDialog = new ProgressDialog(this);

        mUser = UserHelper.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance();



        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        ImageButton imageWish =  findViewById(R.id.imageWish);
        imageWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

                builder.setView(view);
                alert = builder.create();
                alert.show();
                ImageButton takeImage = view.findViewById(R.id.takeImage);
                takeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                        alert.cancel();
                    }
                });
                ImageButton selectImage = view.findViewById(R.id.selectImage);
                selectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);
                        alert.cancel();
                    }
                });
            }
        });
        addButton = findViewById(R.id.addButton);



        final EditText descriptionText = findViewById(R.id.descriptionEditText);
        final EditText linkText = findViewById(R.id.linkEditText);
        if (!(descriptionText.equals(""))){
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgressDialog.setMessage("Uploading");
                    mProgressDialog.show();
                    if(mUri != null) {
                        final StorageReference photopath = mFirebaseStorage.getReference("Photos").child(mUri.getLastPathSegment());
                        photopath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(AddActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                                mProgressDialog.cancel();
                                Item item = new Item(descriptionText.getText().toString(), photopath.toString(), linkText.getText().toString(), mUser.getName());
                                mUser.getWishItems().add(item);
                                UserHelper.update();
                                AddActivity.super.onBackPressed();
                            }
                        });
                    }
                    else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String hash = String.valueOf(mBitmap.hashCode());
                        UploadTask uploadTask = mFirebaseStorage.getReference("Photos").child(hash).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Toast.makeText(AddActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                                mProgressDialog.cancel();
                                Item item = new Item(descriptionText.getText().toString(), downloadUrl.toString(), linkText.getText().toString(), mUser.getName());
                                mUser.getWishItems().add(item);
                                UserHelper.update();
                                AddActivity.super.onBackPressed();
                            }
                        });
                    }
                }
            });
        }
        else {
            Toast.makeText(AddActivity.this, "Il nous manque des informations !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageWish = findViewById(R.id.imageWish);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mUri = data.getData();
            mImageWish.setImageURI(mUri);


        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            mBitmap = (Bitmap) data.getExtras().get("data");
            mImageWish.setImageBitmap(mBitmap);
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}



