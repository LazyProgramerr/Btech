package com.sai.btech.AppFeatures;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.btech.databinding.ActivityAboutAppBinding;

import com.sai.btech.R;

import java.io.File;

public class AppUpdate extends AppCompatActivity {
    private static final int PICK_APK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickApkFile();
    }

    private void pickApkFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.android.package-archive");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select APK File"),
                    PICK_APK_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(
                            this,
                            "Please install a File Manager.",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_APK_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri apkUri = data.getData();

            // Now you have the Uri of the selected APK file.
            // You can use this Uri to upload the file to Firebase Storage.

            // Example:
            uploadApkToStorage(apkUri);
        } else {
            // User canceled the file picker.
            // Handle accordingly.
        }
    }

    private void uploadApkToStorage(Uri apkUri) {
        // Add your Firebase Storage upload logic here.
        // Use the provided Uri to upload the selected APK file to Firebase Storage.

        // Get a reference to the storage location
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("apks");

        // Get a reference to the APK file
        StorageReference apkRef = storageRef.child("btech.apk");

        // Upload the file
        apkRef.putFile(apkUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Handle successful upload
                    // You can get the download URL for the uploaded file if needed
                    apkRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        String downloadUrlStr = downloadUrl.toString();
                        // Do something with the download URL

                        // Add APK details to Realtime Database
                        saveApkDetailsToDatabase(downloadUrlStr);
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful upload
                    Toast.makeText(AppUpdate.this, "Failed to upload APK:" +exception, Toast.LENGTH_SHORT).show();
                });
    }

    private void saveApkDetailsToDatabase(String downloadUrl) {
        // Get a reference to the Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("");
        // Save the APK details to the Realtime Database
        databaseRef.child("apk").setValue(downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful database update
                    Toast.makeText(AppUpdate.this, "APK details added to database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful database update
                    Toast.makeText(AppUpdate.this, "Failed to add APK details to database", Toast.LENGTH_SHORT).show();
                });
    }


}