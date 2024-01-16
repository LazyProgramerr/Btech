package com.sai.btech.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.activities.WelcomeActivity;
import com.sai.btech.databinding.ActivityLoginBinding;
import com.sai.btech.dialogs.Loading;
import com.sai.btech.firebase.Token;
import com.sai.btech.managers.SharedPreferenceManager;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity{
    private ActivityLoginBinding layoutWidgets;
    private FirebaseAuth fAuth;
    private GoogleSignInClient signInClient;
    private Loading loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutWidgets = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());
//        initialize dialog
        loadingDialog = new Loading(this);
        loadingDialog.setCancelable(false);

//        initialize google Auth and google SignIn client
        fAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this,signInOptions);

        layoutWidgets.signInButton.setOnClickListener(v -> {
            loadingDialog.show();
            String eMail = layoutWidgets.mail.getText().toString().trim();
            String password = layoutWidgets.password.getText().toString().trim();
            if (eMail.isEmpty() ||  !Patterns.EMAIL_ADDRESS.matcher(eMail).matches()){
                loadingDialog.dismiss();
                layoutWidgets.mail.setError(eMail.isEmpty() ? "cannot be Empty!!" : "Invalid Email");
            } else if (password.isEmpty() || password.length() < 8) {
                loadingDialog.dismiss();
                layoutWidgets.password.setError(password.isEmpty() ? "password cannot be empty!!" : "password length must be greater or equal to 8");
            }else {
                layoutWidgets.mail.setError(null);
                layoutWidgets.password.setError(null);
                fAuth.signInWithEmailAndPassword(eMail,password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                SharedPreferenceManager.saveLoginStatus(this,true);
                                loadingDialog.dismiss();
                                Snackbar.make(layoutWidgets.signInButton,"success",Snackbar.LENGTH_SHORT).show();
                                checkUserDetailsStatus(0);
                            }else {
                                loadingDialog.dismiss();
                                Snackbar.make(layoutWidgets.signInButton,"sign in failed \ncheck your network and try again ",Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        layoutWidgets.gSignin.setOnClickListener(v -> {
            loadingDialog.show();
            activityResultLauncher.launch(new Intent(signInClient.getSignInIntent()));
        });
        layoutWidgets.signUpText.setOnClickListener(v -> {
            Intent SignupActivityIntent = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(SignupActivityIntent);
        });

    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK){
                    Intent resultData = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(resultData);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        SigninWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        loadingDialog.dismiss();
                        Snackbar.make(layoutWidgets.gSignin, Objects.requireNonNull(e.getMessage()),Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    loadingDialog.dismiss();
                }
            }
    );

    private void SigninWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        fAuth.signInWithCredential(credential).addOnCompleteListener(this,task -> {
            if (task.isSuccessful()){
                SharedPreferenceManager.saveLoginStatus(this,true);
                Snackbar.make(layoutWidgets.gSignin, "success",Snackbar.LENGTH_SHORT).show();
                checkUserDetailsStatus(1);
            }else {
                loadingDialog.dismiss();
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                assert errorMessage != null;
                Snackbar.make(layoutWidgets.gSignin, errorMessage, Snackbar.LENGTH_SHORT).show();}
        });
    }


    private void checkUserDetailsStatus(int i) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
        assert fUser != null;
        String uid = fUser.getUid();
        Query query = usersReference.orderByChild("uId").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),UserDetailsActivity.class);
                    intent.putExtra("loginType",i);
                    startActivity(intent);
                    finish();
                } else {
//                    generate token and add to database
                    Token.getToken(LoginActivity.this, token -> {
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users/"+uid);
                        dr.child("Token").setValue(token);
                    });
                    setDetailsInLocal(usersReference,uid);
                    SharedPreferenceManager.saveLoginStatus(LoginActivity.this,true);
                    loadingDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void setDetailsInLocal(DatabaseReference db,String uid) {
        Query query = db.orderByChild("uId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()) {
                    SharedPreferenceManager.saveUserData(LoginActivity.this,
                            String.valueOf(ds.child("Name").getValue()),
                            String.valueOf(ds.child("eMail").getValue()),
                            String.valueOf(ds.child("Phone").getValue()),
                            String.valueOf(ds.child("userImg").getValue()),
                            String.valueOf(ds.child("uId").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}