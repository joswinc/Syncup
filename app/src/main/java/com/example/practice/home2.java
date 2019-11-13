package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class home2 extends AppCompatActivity {
    ListView listView;
    int c;
    public String wp = "";
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth us;
    private ArrayList<String> noticelist = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        firebaseFirestore = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listview);
        us = FirebaseAuth.getInstance();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noticelist);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.add("WELCOME");
        FirebaseUser u = us.getCurrentUser();
        String uid = u.getUid();
        DocumentReference contact = firebaseFirestore.collection("users").document(uid);
        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc =task.getResult();
                    wp = doc.getString("WritePer");
                    // Toast.makeText(getApplicationContext(),wp,Toast.LENGTH_SHORT).show();
                }
            }
        });
//        DocumentReference Count = firebaseFirestore.collection("Notice").document("Count");
//        Count.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    DocumentSnapshot doc =task.getResult();
//                    c = Integer.parseInt(doc.getString("count"));
//                }
//            }
//        });

        //old method which uses real time database.

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    Notify teacher = dataSnapshot.getValue(Notify.class);
                   // String postTitle = teacher.getTitle();// String.valueOf(teacher.getTitle());
                  //  String postDesc = teacher.getDesc();//String.valueOf(teacher.getDesc());
                    String mmmmm =  String.valueOf(teacher);
                    arrayAdapter.add(mmmmm);
                  //  arrayAdapter.add(postTitle);
                   // arrayAdapter.add(postDesc);
                    arrayAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            startActivity(new Intent(home2.this,home2.class));
            finish();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.m, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Toast.makeText(getApplicationContext(),wp,Toast.LENGTH_SHORT).show();
      //  String y = "Yes";
        try {
            if (wp.equals("Yes")) {
                menu.findItem(R.id.f2).setEnabled(false);
                menu.findItem(R.id.f3).setEnabled(false);

            // menu.getItem(1).setEnabled(false);
                // menu.getItem(2).setEnabled(false);
          }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
        return true;
                   //return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.f1:
                startActivity(new Intent(home2.this, MainActivity.class));
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.f2:
                startActivity(new Intent(home2.this, Post_content.class));
                finish();
                return true;
            case R.id.f3:
                startActivity(new Intent(home2.this, signUpPage.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
