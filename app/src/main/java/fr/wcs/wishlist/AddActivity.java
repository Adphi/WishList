package fr.wcs.wishlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AddActivity extends AppCompatActivity {

    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 99;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int TAKE_IMAGE_REQUEST = 4;
    private ImageView mImageView;
    private Uri mFilePath;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public static String mViaName = "";
    private ImageButton mFloatingActionButton;
    private AlertDialog dialog;
    private ProgressBar mProgressBar;
    private ImageView mBeMyFirst;
    private TextView mUploadInfo;
    private TextView mInfoDialog;
    private StorageReference mStorageReference;
    public FirebaseStorage mStorage;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ImageButton imageWish = (ImageButton) findViewById(R.id.imageWish);
        imageWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_photo, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();
                ImageButton takeImage = view.findViewById(R.id.takeImage);
                takeImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                });
                ImageButton selectImage = view.findViewById(R.id.selectImage);
                selectImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selectionner une image"), PICK_IMAGE_REQUEST);

                    }
                });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            mFilePath = data.getData();
            uploadFromPath(mFilePath);
        } else if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            checkPermission();
        }
    }


    String mCurrentPhotoPath;
    Uri mCurrentPhotoUri;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = AddActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(AddActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoUri = FileProvider.getUriForFile(AddActivity.this,
                        "fr.wcs.wishlist.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, TAKE_IMAGE_REQUEST);
            }
        }
    }

    public void checkPermission() {

        uploadFromPath(mCurrentPhotoUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //La personne a accepté les permissions
                    checkPermission();
                }
            }
        }
    }

    public void uploadFromPath(final Uri path) {
        if (path != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(AddActivity.this.getContentResolver(),mCurrentPhotoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            StorageReference photoRef = mStorageReference.child("image/" + mViaName.replace(" ", "_") + "/" + path.getLastPathSegment());
            photoRef.putFile(path)
                    //viaRef.putBytes(data)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.GONE);
                            mUploadInfo.setVisibility(View.GONE);
                            mTakeImage.setVisibility(View.GONE);
                            mSelectImage.setVisibility(View.GONE);
                            mInfoDialog.setVisibility(View.GONE);

                            Toast.makeText(AddActivity.this, "Image envoyée ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            DatabaseReference imageRef = mDatabase.getReference("photos");
                            PhotoModel newPhoto = new PhotoModel("example",photoRef.toString());
                            imageRef.push().setValue(newPhoto);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(AddActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            float progress = 100f * ((float)taskSnapshot.getBytesTransferred() / (float)taskSnapshot.getTotalByteCount());
                            System.out.println("Upload is " + progress + "% done");
                            int currentProgress = (int) progress;
                            mProgressBar.setProgress(currentProgress);
                            mProgressBar.setVisibility(View.VISIBLE);
                            mUploadInfo.setText("Envoi en cours :" +" " + String.valueOf(currentProgress) +"%");
                            mUploadInfo.setVisibility(View.VISIBLE);
                            mTakeImage.setVisibility(View.GONE);
                            mSelectImage.setVisibility(View.GONE);
                            mInfoDialog.setVisibility(View.GONE);
                            mImageView.setVisibility(View.GONE);

                        }
                    });
        }
    }
}



