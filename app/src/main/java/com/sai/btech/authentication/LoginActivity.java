package com.sai.btech.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
import com.sai.btech.activities.UserStdDetailsActivity;
import com.sai.btech.firebaseUtil.Token;
import com.sai.btech.sharedPreference.SharedPreferenceManager;
import com.sai.btech.activities.WelcomeActivity;
import com.sai.btech.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize GoogleSignInClient and FirebaseAuth
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.signinButton.setOnClickListener(
                v -> {
                    String email = binding.mail.getText().toString().trim();
                    String password = binding.password.getText().toString().trim();
                    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        binding.mail.setError(
                                email.isEmpty() ? "Cannot be empty" : "Invalid Email");
                    } else if (password.isEmpty() || password.length() < 6) {
                        binding.password.setError(
                                password.isEmpty() ? "Cannot be empty" : "Too Short");
                    } else {
                        binding.mail.setError(null);
                        binding.password.setError(null);
//                        binding.progressBar.setVisibility(View.VISIBLE);
                        signInWithEmailAndPassword(email,password);
                    }
                });

        binding.gSignin.setOnClickListener(
                v -> {
//                    binding.progressBar.setVisibility(View.VISIBLE);
                    resultIntent.launch(new Intent(googleSignInClient.getSignInIntent()));
                });
    }
    private void signInWithEmailAndPassword(String email, String password) {
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
//                                binding.progressBar.setVisibility(View.GONE);
                                checkUser();

                            } else {
//                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();

                            }
                        });
    }

    ActivityResultLauncher<Intent> resultIntent =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent resultData = result.getData();
                                Task<GoogleSignInAccount> task =
                                        GoogleSignIn.getSignedInAccountFromIntent(resultData);
                                try {
                                    GoogleSignInAccount account =
                                            task.getResult(ApiException.class);
                                    assert account != null;
                                    authWithGoogle(account.getIdToken());
                                } catch (ApiException e) {
                                    // Sign-in failed, display an error message
//                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    private void authWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(
                        this,
                        task -> {
                            if (task.isSuccessful()) {
                                SharedPreferenceManager.saveLoginStatus(LoginActivity.this, true);
//                                binding.progressBar.setVisibility(View.GONE);
                                checkUser();
                            } else {
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }



    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = usersRef.orderByChild("uId").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    startActivity(new Intent(getApplicationContext(), UserStdDetailsActivity.class));
                    finish();

                } else {
//                    generate token and add to database
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Token.getToken(LoginActivity.this,token -> {
                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                        dr.child("token").setValue(token);
                    });
                    setDetailsInLocal();
                    SharedPreferenceManager.saveLoginStatus(LoginActivity.this,true);
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private void passwordRecover(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(
                        this,
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Forget password email link sent Successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(
                                                LoginActivity.this,
                                                Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
    }
    private void setDetailsInLocal() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        Query query = db.orderByChild("uId").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()) {
                    for (DataSnapshot db :snapshot.getChildren()) {
                        SharedPreferenceManager.saveUserData(LoginActivity.this,
                                ""+ds.child("name").getValue(),
                                ""+ds.child("email").getValue(),
                                ""+ds.child("phone").getValue(),
                                ""+ds.child("image").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToRegister(View v) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
}
