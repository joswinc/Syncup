package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView postl;
    public String permval= "";
    int flag;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth us;
    String uid;
    private ProgressBar mProgressCircle;
   // SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        postl = findViewById(R.id.postlist);
        mProgressCircle = findViewById(R.id.progress_circle);
        //swipeRefreshLayout = findViewById(R.id.swipe);
        postl.setHasFixedSize(true);
        postl.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        databaseReference.keepSynced(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
       // us = FirebaseAuth.getInstance();
      //  FirebaseUser u = us.getCurrentUser();
        //uid = u.getUid();
       // flag=0;

        final List<postdisplay> postlist = new ArrayList<>();
//        postlist.add(new postdisplay("Exam 1","Exam on 28/02/2019"));
//        postlist.add(new postdisplay("Exam 2","Exam on 28/03/2019"));
//        postlist.add(new postdisplay("Exam 3","Exam on 28/04/2019"));
//        postlist.add(new postdisplay("Exam 4","Exam on 28/05/2019"));
//        Adapter adapter=new Adapter(postlist);
//        postl.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                startActivity(new Intent(home.this,home.class));
//                finish();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },4000);
//            }
//        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                   // Toast.makeText(getApplicationContext(),""+postlist,Toast.LENGTH_SHORT).show();
                    Notify teacher = dataSnapshot.getValue(Notify.class);
                    String postTitle = teacher.getTitle();// String.valueOf(teacher.getTitle());
                    String postDesc = teacher.getDesc();//String.valueOf(teacher.getDesc());
                    //String mmmmm =  String.valueOf(teacher);
                    postlist.add(new postdisplay(postDesc,postTitle));
                    Adapter adapter=new Adapter(postlist);
                    postl.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                    //  arrayAdapter.add(postTitle);
                    // arrayAdapter.add(postDesc);
                   // postlist.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            //    startActivity(new Intent(home.this,home.class));
            //    finish();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void checkperm(String perm){
      //  Toast.makeText(getApplicationContext(),"func"+perm,Toast.LENGTH_SHORT).show();
        permval=perm;
    }
    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private List<postdisplay> postlist;
        public Adapter(List<postdisplay> postlist){
            this.postlist=postlist;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String title = postlist.get(position).getTitle();
            String desc = postlist.get(position).getNotice();
            holder.setData(title,desc);
        }

        @Override
        public int getItemCount() {
            return postlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView desc;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                desc = itemView.findViewById(R.id.description);
            }
            private void setData(String titleText,String descText){
                title.setText(titleText);
                desc.setText(descText);
            }
        }


    }
//    @Override
   // protected void onStart() {
 //       super.onStart();
       // FirebaseRecyclerOptions<postdisplay,postViewHolder> firebaseRecyclerOptions
   //     FirebaseRecyclerAdapter<postdisplay,postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<postdisplay, postViewHolder>(
       //           postdisplay.class,
//                R.layout.post_list,
//                postViewHolder.class,
//                databaseReference
//        ) {
//            @Override
//            protected void onBindViewHolder(@NonNull postViewHolder postViewHolder, int i, @NonNull postdisplay postdisplay) {
//
//            }
//
//            @NonNull
//            @Override
//            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
      //  FirebaseRecyclerOptions<> options = new FirebaseRecyclerOptions.Bluider<>
        //FirebaseRecyclerAdapter<postdisplay,postViewHolder> firebaseRecycleAdapter = new FirebaseRecyclerAdapter<postdisplay,postViewHolder>(
//                postdisplay.class,
//                R.layout.post_list,
//                postViewHolder.class,
//                databaseReference
//        )
//        {
//            @Override
//            protected void pop
//        }
   // }
//    public  class MyAdapter extends RecyclerView.Adapter<postViewHolder>
//    {
//        Context c;
//        ArrayList<postdisplay> models;
//        @NonNull
//        @Override
//        public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }
//    public static class postViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView post_title, post_desc;
//       // View mView;
//        public postViewHolder(@NonNull View itemView) {
//            super(itemView);
//          //  mView = itemView;
//            this.post_title = itemView.findViewById(R.id.title);
//            this.post_desc = itemView.findViewById(R.id.description);

      //  }
//        public void setTile(String title)
//        {
//            TextView post_title = (TextView) mView.findViewById(R.id.title);
//            post_title.setText(title);
//        }
//        public void setDesc(String desc)
//        {
//            TextView post_desc = (TextView) mView.findViewById(R.id.description);
//            post_desc.setText(desc);
//        }
  //  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.m, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       // Toast.makeText(getApplicationContext(),wp,Toast.LENGTH_SHORT).show();
        try {
            us = FirebaseAuth.getInstance();
            FirebaseUser u = us.getCurrentUser();
            uid = u.getUid();
            DocumentReference contact = firebaseFirestore.collection("users").document(uid);
            contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot doc =task.getResult();
                        String WP = doc.getString("WritePer");
                        checkperm(WP);
                       // Toast.makeText(getApplicationContext(),"in"+WP,Toast.LENGTH_SHORT).show();

                }
            }});
        //  Toast.makeText(getApplicationContext(),"out"+permval,Toast.LENGTH_SHORT).show();

                if (!permval.equals("Yes")) {
                    menu.findItem(R.id.f2).setEnabled(false);
                    menu.findItem(R.id.f3).setEnabled(false);

                }
                else{
                    menu.findItem(R.id.f2).setEnabled(true);
                    menu.findItem(R.id.f3).setEnabled(true);
                }

//           }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
        return true;
        //return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.f1:
                startActivity(new Intent(home.this, MainActivity.class));
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.f2:
                startActivity(new Intent(home.this,Post_content.class));
                return true;
            case R.id.f3:
                startActivity(new Intent(home.this, signUpPage.class));
                finish();
                return true;
            case R.id.f4:
                Intent intent = new Intent(home.this, facultyDetails.class);
                intent.putExtra("perm",permval);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
