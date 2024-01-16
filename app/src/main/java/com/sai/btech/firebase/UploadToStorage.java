package com.sai.btech.firebase;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UploadToStorage {
    public interface UploadImageCallback {
        void onImageUpload(String imageUrl);
        void onImageUploadFailed(Exception e);
    }

    public static void uploadImage(Context context, Bitmap bitmap, String uid, UploadImageCallback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference photoRef = storageRef.child("profile_photos/" + uid + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        InputStream photoStream = new ByteArrayInputStream(data);

        // Upload the photo to Firebase Storage
        UploadTask uploadTask = photoRef.putStream(photoStream);

        // Register observers to listen for when the upload is done or if it fails
        // Call the callback with the failure exception
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Photo uploaded successfully, get the download URL
            // Call the callback with the failure exception
            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Call the callback with the image URL
                callback.onImageUpload(uri.toString());
            }).addOnFailureListener(callback::onImageUploadFailed);
        }).addOnFailureListener(callback::onImageUploadFailed);
    }
}
