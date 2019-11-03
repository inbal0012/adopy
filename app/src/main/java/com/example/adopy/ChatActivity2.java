package com.example.adopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adopy.Utilities.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatActivity2 extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        //put user details in Header of Navigation Drawer START ------------------------
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseUser.getUid());
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//
//                //Header of Navigation Drawer
//                View headerView = navigationView.getHeaderView(0);
//
//                TextView userNameTv = headerView.findViewById(R.id.nav_header_user_name);
//                TextView userEmailTv = headerView.findViewById(R.id.nav_header_user_email);
//                CircleImageView profile_image = findViewById(R.id.nav_header_circleImageView);
//
//                //user name
//                String userName = user.getUsername();
//                userNameTv.setText(userName);
//
//                //email
//                String userEmail = mFirebaseUser.getEmail();
//                userEmailTv.setText(userEmail);
//
//                //image
//                if (user.getImageURL().equals("default")) {
//                    profile_image.setImageResource(R.drawable.user_male);
//                    if (user.getGender().equals("Female")) {
//                        profile_image.setImageResource(R.drawable.user_female);
//                    }
//                } else {
//                    Glide.with(ChatActivity2.this).load(user.getImageURL()).into(profile_image);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        //put user details in Header of Navigation Drawer END ------------------------

        //put chatted user in toolbar START ---------------------------------
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userid");

        final CircleImageView profile_image = findViewById(R.id.chat_profile_image);
        final TextView username = findViewById(R.id.chat_username);

        mReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                //user name
                String userName = user.getUsername();
                username.setText(userName);

                //image
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.user_male);
                    if (user.getGender().equals("Female")) {
                        profile_image.setImageResource(R.drawable.user_female);
                    }
                } else {
                    Glide.with(ChatActivity2.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //put chatted user in toolbar END ---------------------------------

        ImageButton sendBtn = findViewById(R.id.btn_send);
        final EditText text_send = findViewById(R.id.text_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(mFirebaseUser.getUid(), userId, msg);
                } else {
                    Toast.makeText(ChatActivity2.this, getString(R.string.empty_msg), Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }

    private void sendMessage(String i_Sender, String i_Receiver, String i_Message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", i_Sender);
        hashMap.put("receiver", i_Receiver);
        hashMap.put("message", i_Message);

        reference.child("Chats").push().setValue(hashMap);

    }
}