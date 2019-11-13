package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUpPage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button createuser, backto;
    EditText name, usn, contact, email, branch;
    String emailID, passwd, Name, Contact, Branch;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView importExcel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        createuser = findViewById(R.id.createusers);
        backto = findViewById(R.id.backto);
        name =findViewById(R.id.name);
        usn =findViewById(R.id.usn);
        email =findViewById(R.id.email);
        contact =findViewById(R.id.contactno);
        branch =findViewById(R.id.branch);
        radioGroup = findViewById(R.id.rg);
        importExcel = findViewById(R.id.importExcel);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUpPage.this,home.class));
                finish();
            }
        });
        importExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUpPage.this,ExcelSignUp.class));
                finish();
            }
        });
        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
    }
    public void create() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        if (selectedId == -1) {
            Toast.makeText(signUpPage.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {

            emailID = email.getText().toString().trim();
            passwd = usn.getText().toString().trim();
            Name = name.getText().toString().trim();
            Contact = contact.getText().toString().trim();
            Branch = branch.getText().toString().trim();
            if (TextUtils.isEmpty(emailID) || TextUtils.isEmpty(passwd) || TextUtils.isEmpty(Contact) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Branch)) {
                Toast.makeText(signUpPage.this, "All fields are not filled", Toast.LENGTH_SHORT).show();

            } else {
                firebaseAuth.createUserWithEmailAndPassword(emailID, passwd).addOnCompleteListener(signUpPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "user is added to Auth", Toast.LENGTH_SHORT).show();
                            Map<String, Object> newUser = new HashMap<>();
                            newUser.put("Name", Name);
                            newUser.put("USN", passwd);
                            newUser.put("Email", emailID);
                            newUser.put("Contact", Contact);
                            newUser.put("Branch", Branch);
                            newUser.put("WritePer", radioButton.getText().toString());
                            firebaseFirestore.collection("users").document(currentUser.getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "user is added to database", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "user not added to database", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "user not added to Auth", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
