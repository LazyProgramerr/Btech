package com.sai.btech.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.activities.HomeActivity;
import com.sai.btech.databinding.ActivityUserDetailsBinding;
import com.sai.btech.firebase.Token;
import com.sai.btech.firebase.UploadToStorage;
import com.sai.btech.managers.SharedPreferenceManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {
    private ActivityUserDetailsBinding layoutWidgets;
    FirebaseUser firebaseUser;
    Bitmap userPhotoBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        layoutWidgets = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());
        layoutWidgets.defaultUserDetailsActivityLayout.setVisibility(View.VISIBLE);
        layoutWidgets.getDetailsLayout.setVisibility(View.GONE);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

        switch(intent.getIntExtra("loginType",0)){
            case 0 -> new Handler().postDelayed(()->{

            },2000);
            case 1-> new Handler().postDelayed(()-> addFromGoogleToDatabase(userRef),2000);
            case 2->{
                String mail = intent.getStringExtra("mail");
                String password = intent.getStringExtra("password");
                getDetailsAndAdd(userRef,mail,password);
            }
        }
    }

    private void getDetailsAndAdd(DatabaseReference userRef,String mail,String password) {
        layoutWidgets.defaultUserDetailsActivityLayout.setVisibility(View.GONE);
        layoutWidgets.getDetailsLayout.setVisibility(View.VISIBLE);

        layoutWidgets.userImage.setOnClickListener(v -> pickImage());
        layoutWidgets.saveDetails.setOnClickListener(v -> {
            layoutWidgets.defaultUserDetailsActivityLayout.setVisibility(View.VISIBLE);
            layoutWidgets.getDetailsLayout.setVisibility(View.INVISIBLE);
            String uid = firebaseUser.getUid();
            String name = layoutWidgets.userName.getText().toString();
            String phone = layoutWidgets.userPhone.getText().toString();
            UploadToStorage.uploadImage(this, userPhotoBitmap, uid, new UploadToStorage.UploadImageCallback() {
                @Override
                public void onImageUpload(String imageUrl) {
                    Token.getToken(UserDetailsActivity.this,token -> {
                        HashMap<String,String> uData = new HashMap<>();
                        uData.put("uId",uid);
                        uData.put("Name",name);
                        uData.put("Phone",phone);
                        uData.put("userState","online");
                        uData.put("eMail",mail);
                        uData.put("password",password);
                        uData.put("userImg",imageUrl);
                        uData.put("Token",token);

                        userRef.child(firebaseUser.getUid()).setValue(uData).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                SharedPreferenceManager.saveUserData(UserDetailsActivity.this,name,mail,phone,imageUrl,uid);
                                SharedPreferenceManager.saveLoginStatus(UserDetailsActivity.this,true);
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }
                        });
                    });
                }

                @Override
                public void onImageUploadFailed(Exception e) {
                    layoutWidgets.defaultUserDetailsActivityLayout.setVisibility(View.GONE);
                    layoutWidgets.getDetailsLayout.setVisibility(View.VISIBLE);
                    Snackbar.make(layoutWidgets.saveDetails.getRootView(),"image upload failed\ne:- "+e.getMessage() +"\nplease check your internet and try again",Snackbar.LENGTH_LONG).show();
                }
            });

        });

    }
    private void addFromGoogleToDatabase(DatabaseReference userRef) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String phone = firebaseUser.getPhoneNumber();
        String mail = firebaseUser.getEmail();
        String img = String.valueOf(firebaseUser.getPhotoUrl());
        Token.getToken(this,token -> {
            HashMap<String,String> uData = new HashMap<>();
            uData.put("uId",uid);
            uData.put("Name",name);
            uData.put("Phone",phone);
            uData.put("userState","online");
            uData.put("eMail",mail);
            uData.put("userImg",img);
            uData.put("Token",token);

            userRef.child(firebaseUser.getUid()).setValue(uData).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    SharedPreferenceManager.saveUserData(this,name,mail,phone,img,uid);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            });
        });
    }

    private void pickImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
        }
        openGalleryLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> openGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    Uri imageUri = result.getData().getData();
                    assert imageUri != null;
                    UCrop.of(imageUri, Uri.fromFile(new File(this.getCacheDir(), "cropped_image.jpg")))
                            .withAspectRatio(1, 1)
                            .start(this);
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            assert data != null;
            Uri croppedImageUri = UCrop.getOutput(data);
            userPhotoBitmap = uriToBitmap(croppedImageUri);
            layoutWidgets.userImage.setImageBitmap(userPhotoBitmap);
        }
    }

    private Bitmap uriToBitmap(Uri croppedImageUri) {
        try {
            InputStream imageStream = this.getContentResolver().openInputStream(croppedImageUri);
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


}