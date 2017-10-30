package fr.wcs.wishlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * A login screen that offers login via email/password.
 */
public class AddActivity extends AppCompatActivity {

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

            }
        });
    }
    /*
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                mCurrentPhotoUri = FileProvider.getUriForFile(getActivity(),
                        "fr.wcs.viaferrata.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, TAKE_IMAGE_REQUEST);
            }
        }
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;

            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(),matrix, true);
        return rotatedBitmap;
    }

    *//*private Bitmap setReducedImageSize(){
        int targetImageViewWidht = mImageView.getWidth();
        int targetImageViewHeight = mImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);

        int cameraImageWidth =bmOptions.outWidth;
        int cameraImageHeight =bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewHeight, cameraImageHeight/targetImageViewHeight);
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
    }*//*

    public void checkPermission() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    PERMISSIONS, PERMISSION_WRITE_EXTERNAL_STORAGE);
//
//        }
        //si la personne arrive ici elle a les droits

        uploadFromPath(mCurrentPhotoUri);
//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mCurrentPhotoUri);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        bitmap = rotateImage(bitmap);
//      //  mImageView.setImageBitmap(bitmap);
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
                } else {

                    //La personne a refusé les permissions, on re-demande en boucle
                    //TODO: Afficher toast à la place pour expliquer pourquoi ca ne marchera pas

                }
            }
        }
    }

    public void uploadFromPath(final Uri path) {
        if (path != null) {

            *//*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mCurrentPhotoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();*//*

            final StorageReference viaRef = mStorageReference.child("image/" + mViaName.replace(" ", "_") + "/" + path.getLastPathSegment());
            viaRef.putFile(path)
                    //viaRef.putBytes(data)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.GONE);
                            mUploadInfo.setVisibility(View.GONE);
                            mTakeImage.setVisibility(View.GONE);
                            mSelectImage.setVisibility(View.GONE);
                            mInfoDialog.setVisibility(View.GONE);

                            Toast.makeText(getActivity().getApplicationContext(), "Image envoyée ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            DatabaseReference imageRef = mDatabase.getReference("photos");
                            PhotoModel newPhoto = new PhotoModel(mViaName,viaRef.toString());
                            imageRef.push().setValue(newPhoto);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

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
    }*/
}



