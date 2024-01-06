package com.sai.btech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.R;
import com.sai.btech.firebaseUtil.Token;
import com.sai.btech.managers.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserStdDetailsActivity extends AppCompatActivity {
    private String college,regulation,course;
    private Spinner regulationDropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_std_details);
        Spinner collegeDropDown = findViewById(R.id.spinner_college);
        regulationDropDown = findViewById(R.id.spinner_regulation);
        regulationDropDown.setEnabled(false);
        Spinner courseDropDown = findViewById(R.id.spinner_course);
        Button btn = findViewById(R.id.proceed);


//        sample data
        List<String> colleges = new ArrayList<>();
        colleges.add("choose your college");
        colleges.add("VAAGDEVI ENGINEERING COLLEGE(JNTUH)");
        colleges.add("VAAGDEVI ENGINEERING COLLEGE");
        colleges.add("VAAGDEVI COLLEGE OF ENGINEERING");

        List<String> Regulations = new ArrayList<>();
        Regulations.add("choose your Regulation");
        Regulations.add("R18");
        Regulations.add("R20");
        Regulations.add("R22");

        List<String> Courses = new ArrayList<>();
        Courses.add("choose your course");
        Courses.add("ECE");
        Courses.add("CSE");
        Courses.add("CSD");
        Courses.add("CSM");
        Courses.add("EEE");



//        adapter for spinner
        ArrayAdapter<String> collegeDataAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,colleges);
        ArrayAdapter<String> regulationDataAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,Regulations);
        ArrayAdapter<String> courseDataAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,Courses);

        collegeDataAdapter.setDropDownViewResource(me.ibrahimsn.lib.R.layout.support_simple_spinner_dropdown_item);
        regulationDataAdapter.setDropDownViewResource(me.ibrahimsn.lib.R.layout.support_simple_spinner_dropdown_item);
        courseDataAdapter.setDropDownViewResource(me.ibrahimsn.lib.R.layout.support_simple_spinner_dropdown_item);

        collegeDropDown.setAdapter(collegeDataAdapter);
        collegeDropDown.setSelection(0);
        regulationDropDown.setAdapter(regulationDataAdapter);
        regulationDropDown.setSelection(0);
        courseDropDown.setAdapter(courseDataAdapter);
        courseDropDown.setSelection(0);
        collegeDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                college = (String) parent.getItemAtPosition(position);
                if (college.equals("VAAGDEVI ENGINEERING COLLEGE(JNTUH)")){
                    regulationDropDown.setEnabled(true);
                }else {
                    regulationDropDown.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        courseDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        regulationDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regulation = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btn.setOnClickListener(v -> {
            if (college.equals("choose your college")) {
                Toast.makeText(UserStdDetailsActivity.this, "Select your college", Toast.LENGTH_SHORT).show();
            } else if (regulationDropDown.isEnabled() && regulation.equals("choose your Regulation")) {
                Toast.makeText(UserStdDetailsActivity.this, "Select your regulation", Toast.LENGTH_SHORT).show();
            }else if (course.equals("choose your course")) {
                Toast.makeText(UserStdDetailsActivity.this, "Select your course", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserStdDetailsActivity.this, "College: " + college + ", Regulation: " + regulation, Toast.LENGTH_SHORT).show();
                Token.getToken(this, token -> {
                    Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
                    saveData(college,regulation,course,token);
                });
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        });

    }
    private void saveData(String college,String Regulation,String course,String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            try {
                HashMap<String, String> data = new HashMap<>();

                data.put("email", user.getEmail());
                data.put("uId", user.getUid());
                data.put("name", user.getDisplayName());
                data.put("phoneN", user.getPhoneNumber());
                data.put("image", user.getPhotoUrl() != null ? user.getPhotoUrl().toString()+ "?sz=1024": "");
                data.put("college",college);
                if (regulationDropDown.isEnabled()){
                    data.put("regulation",Regulation);
                }else {
                    data.put("regulation","");
                }
                data.put("course",course);
                data.put("token",token);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseRefer = database.getReference("Users");
                databaseRefer.child(user.getUid()).setValue(data);

                SharedPreferenceManager.saveUserData(this, user.getDisplayName(),user.getEmail(), user.getPhoneNumber(), String.valueOf(user.getPhotoUrl()),user.getUid());
                SharedPreferenceManager.saveLoginStatus(this,true);
            }catch (Exception e){
                SharedPreferenceManager.saveLoginStatus(this,false);
                Toast.makeText(this, "Please check your internet and try again", Toast.LENGTH_SHORT).show();
            }

        }
    }
}