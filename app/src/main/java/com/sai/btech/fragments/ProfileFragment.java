package com.sai.btech.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sai.btech.R;
import com.sai.btech.dialogs.ProfileDialog;
import com.sai.btech.models.UserData;
import com.sai.btech.databinding.FragmentProfileBinding;
import com.sai.btech.sharedPreference.SharedPreferenceManager;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Context context;
    private TextView userName, userMail, userPhone;
    private ImageView profilePic;
    private FirebaseUser user;
    public ProfileFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userName = binding.userNameView;
        userMail = binding.userMailView;
        userPhone = binding.userPhoneView;
        profilePic = binding.profilePic;

        setDetails();

        profilePic.setOnClickListener(v -> {
            UserData ud = SharedPreferenceManager.getUserData(context);
            Dialog dialog = new ProfileDialog(context, ud.getImg());
            dialog.setCancelable(true);
            dialog.show();
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference dataRefer = FirebaseDatabase.getInstance().getReference("Users");
        dataRefer.keepSynced(true);


        binding.editButton.setOnClickListener(this::showMenu);

        Query query = dataRefer.orderByChild("uId").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String phoneNumber = "" + ds.child("phoneN").getValue();
                    String email = "" + ds.child("email").getValue();
                    String img = "" + ds.child("image").getValue();
                    SharedPreferenceManager.saveUserData(context, name, email, phoneNumber, img);
                    setDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError arg0) {
            }
        });
    }

    private void setDetails() {
        UserData ud = SharedPreferenceManager.getUserData(context);
        userName.setText(ud.getuName());
        userMail.setText(ud.geteMail());
        userPhone.setText(ud.getPhoneNumber());

        try {
            Glide.with(context)
                    .load(ud.getImg())
                    .placeholder(R.drawable.default_user_icon)
                    .error(R.drawable.default_user_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePic);
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.editPhoto) {
                openGallery();
                return true;
            } else if (itemId == R.id.editDetails) {
//                Dialog d = new EditDetailsDialog(context);
//                d.show();
                Toast.makeText(context, "clicked on details", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void openGallery() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
        }
        openGalleryLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> openGalleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            assert result.getData() != null;
                            Uri imageUri = result.getData().getData();
                            assert imageUri != null;
                            UCrop.of(imageUri, Uri.fromFile(new File(context.getCacheDir(), "cropped_image.jpg")))
                                    .withAspectRatio(1, 1)
                                    .start(requireActivity(), this);
                        } else {
                            Toast.makeText(context, "Please select an image", Toast.LENGTH_LONG).show();
                        }
                    });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            assert data != null;
            Uri croppedImageUri = UCrop.getOutput(data);
            Bitmap userPhotoBitmap = uriToBitmap(croppedImageUri);
            uploadToFirebase(userPhotoBitmap);
        }
    }


    private Bitmap uriToBitmap(Uri uri) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            if (imageStream != null) {
                imageStream.close();
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadToFirebase(Bitmap userPhotoBitmap) {
        // Get the FirebaseStorage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference
        StorageReference storageRef = storage.getReference();
        // Create a reference to the location where you want to store the user's photo
        // For example, "profile_photos" could be a folder in your storage
        StorageReference photoRef = storageRef.child("profile_photos/" +user.getUid()+ ".jpg");
        // Convert the photo (Bitmap, URI, or byte array) to an InputStream
        // In this example, you have a Bitmap named 'userPhotoBitmap'
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        InputStream photoStream = new ByteArrayInputStream(data);

        // Upload the photo to Firebase Storage
        UploadTask uploadTask = photoRef.putStream(photoStream);

        // Register observers to listen for when the upload is done or if it fails
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Photo uploaded successfully, get the download URL
            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String photoUrl = uri.toString();
                // Now, you can save the 'photoUrl' to the Realtime Database
                savePhotoUrlToDatabase(photoUrl);
            });
        }).addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Toast.makeText(context, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private void savePhotoUrlToDatabase(String photoUrl) {
        // Save the photo URL to the Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
        databaseReference.child("image").setValue(photoUrl)
                .addOnSuccessListener(aVoid -> {
                    // Photo URL successfully saved to the database
                    Toast.makeText(context, "Photo saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save photo URL to the database
                    Toast.makeText(context, "Failed to save Photo", Toast.LENGTH_SHORT).show();
                });
    }
}
