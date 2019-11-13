package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Post_content extends AppCompatActivity  {
    EditText title,desc;
    Button post,back;
  //  StorageReference storageReference;
     DatabaseReference databaseReference;
     FirebaseFirestore db;
     int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        title = findViewById(R.id.posttitle);
        desc = findViewById(R.id.notice);
        post = findViewById(R.id.post);
        back = findViewById(R.id.back);
    //    storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        db = FirebaseFirestore.getInstance();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new_post();
                posting();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Post_content.this,home.class));
                finish();
            }
        });
    }
    public  void new_post()
    {
        DocumentReference contact = db.collection("Notice").document("Count");
        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                  DocumentSnapshot doc =task.getResult();
                   c = Integer.parseInt(doc.getString("count"));
                }
            }
        });
        Map<String,Object> newContent = new HashMap<>();
        newContent.put("Title",title.getText().toString().trim());
        newContent.put("Desc",desc.getText().toString().trim());
        c = c+1;
        String count = ""+c;
        db.collection("Notice").document(count).set(newContent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });

        contact.update("count",count);
    }
    //old method of uploading using real time database

    public void posting() {
        String posttile = title.getText().toString().trim();
        String postdesc = desc.getText().toString().trim();
        if(!TextUtils.isEmpty(posttile) && !TextUtils.isEmpty(postdesc))
        {
            DatabaseReference newpost =databaseReference.push();
            newpost.child("Title").setValue(posttile);
            newpost.child("Desc").setValue(postdesc);
            //  newpost.child("uid").setValue(FirebaseAuth.get)
            Toast.makeText(getApplicationContext(),"posted",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
        }
        title.getText().clear();
        desc.getText().clear();
    }


}
