package com.sai.btech.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.sai.btech.R;
import com.sai.btech.databinding.ActivitySignupBinding;
import com.sai.btech.dialogs.Loading;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding layoutWidgets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutWidgets = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());

        Loading loadingDialog = new Loading(this);
        loadingDialog.setCancelable(false);

        layoutWidgets.signUpContinueBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String eMail = layoutWidgets.inputMail.getText().toString().trim();
            String password = layoutWidgets.inputPassword.getText().toString().trim();
            if (eMail.isEmpty() ||  !Patterns.EMAIL_ADDRESS.matcher(eMail).matches()){
                loadingDialog.dismiss();
                layoutWidgets.inputMail.setError(eMail.isEmpty() ? "cannot be Empty!!" : "Invalid Email");
            } else if (password.isEmpty() || password.length() < 8) {
                loadingDialog.dismiss();
                layoutWidgets.inputPassword.setError(password.isEmpty() ? "password cannot be empty!!" : "password length must be greater or equal to 8");
            }else {
                createAccount(eMail,password);
            }
        });
    }

    private void createAccount(String eMail, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(eMail,password).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Intent i = new Intent(this, UserDetailsActivity.class);
               i.putExtra("mail",eMail);
               i.putExtra("password",password);
               i.putExtra("loginType",2);
               startActivity(i);
           }
        });
    }
}