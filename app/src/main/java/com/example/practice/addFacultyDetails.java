package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class addFacultyDetails extends AppCompatActivity {
    Button upload, back, chooseFile;
    EditText fileName;
    ImageView imageView;
    ProgressDialog progressDialog;
    Uri uri;
    String perm="",downLoadUrl;
    private static final int PICK_IMAGE_REQUEST =1;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty_details);
        Intent i = getIntent();
        perm = i.getStringExtra("perm");
        upload = findViewById(R.id.upload);
        back = findViewById(R.id.back);
        chooseFile = findViewById(R.id.choosefile);
        imageView = findViewById(R.id.imageView);
        fileName = findViewById(R.id.filename);
        progressDialog = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addFacultyDetails.this, facultyDetails.class);
                intent.putExtra("perm",perm);
                startActivity(intent);
                finish();
            }
        });
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfile();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                   Toast.makeText(getApplicationContext(), "Uploading In Progress..... Please wait",Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });
    }
    private void openfile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uri = data.getData();
            Picasso.with(this).load(uri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadFile()
    {
        if(uri!=null)
        {
            try {
                progressDialog.setMessage("Uploading...");
                progressDialog.show();
                final StorageReference fileRefernce = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                mUploadTask=fileRefernce.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRefernce.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downLoadUrl =uri.toString();
                                        Toast.makeText(getApplicationContext(),downLoadUrl,Toast.LENGTH_SHORT).show();
                                        store(downLoadUrl);
                                    }
                                });
                                //  Upload u = new Upload(fileName.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                               // Upload u = new Upload(fileName.getText().toString().trim(), downLoadUrl);

                               // String uploadId = databaseReference.push().getKey();
                                 // databaseReference.child(uploadId).setValue(u);
                                 // progressDialog.dismiss();
                                //  Toast.makeText(addFacultyDetails.this, "Successfully Uploaded"+uploadId, Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addFacultyDetails.this, ""+e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
    public void store(String link)
    {
         Upload u = new Upload(fileName.getText().toString().trim(), link);
         String uploadId = databaseReference.push().getKey();
         databaseReference.child(uploadId).setValue(u);
         progressDialog.dismiss();
         Toast.makeText(addFacultyDetails.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
    }
}
