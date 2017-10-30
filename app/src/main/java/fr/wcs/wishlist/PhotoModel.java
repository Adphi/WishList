package fr.wcs.wishlist;

public class PhotoModel {
    private String photoName;
    private String photoUri;

    public PhotoModel() {
    }

    public PhotoModel(String photoName, String photoUri) {
        this.photoName = photoName;
        this.photoUri = photoUri;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
