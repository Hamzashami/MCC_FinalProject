package com.hamzashami.coronaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzashami.coronaproject.adapters.MessagesAdapter;
import com.hamzashami.coronaproject.model.Message;
import com.hamzashami.coronaproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = "MessagesActivity";

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference messagesRef;
    private FirebaseUser firebaseUser;

    private RecyclerView rv_messages;
    private EditText et_newMessage;
    private ImageButton ib_sendMessage;

    private List<Message> messages;
    private List<String> messagesIds;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("uid")) {
            Toast.makeText(this, "There is no user have this id", Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_messages);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        messagesRef = database.getReference("messages");
        firebaseUser = auth.getCurrentUser();

        rv_messages = findViewById(R.id.rv_messages);
        et_newMessage = findViewById(R.id.et_newMessage);
        ib_sendMessage = findViewById(R.id.ib_sendMessage);

        messages = new ArrayList<>();
        messagesIds = new ArrayList<>();
        rv_messages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rv_messages.setLayoutManager(linearLayoutManager);

        String uid = intent.getStringExtra("uid");
        String imageUrl = intent.getStringExtra("imageUrl");

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                setTitle(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ib_sendMessage.setOnClickListener(v -> {
            sendNewMessage(firebaseUser.getUid(), uid);
        });

        getPreviousMessages(uid, imageUrl);
    }

    private void getPreviousMessages(String uid, String imageUrl) {

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                messagesIds.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    Log.d(TAG, "onDataChange: dataSnapshot1.getKey() " + dataSnapshot1.getKey());

                    if ((message.getSenderId().equalsIgnoreCase(firebaseUser.getUid()) && message.getReceiverId().equalsIgnoreCase(uid))
                            || (message.getSenderId().equalsIgnoreCase(uid) && message.getReceiverId().equalsIgnoreCase(firebaseUser.getUid()))) {
                        messagesIds.add(dataSnapshot1.getKey());
                        messages.add(message);
                    }
                }

                messagesAdapter = new MessagesAdapter(getApplicationContext(), messages, imageUrl);
                rv_messages.setAdapter(messagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: messagesIds.clear() Cleared");
        messagesIds.clear();
    }

    private void sendNewMessage(String sender, String receiver) {
        String newMessage = et_newMessage.getText().toString();
        if (TextUtils.isEmpty(newMessage)) {
            return;
        }
        Message message = new Message(sender, receiver, newMessage, System.currentTimeMillis());
        messagesRef.push().setValue(message);
        et_newMessage.getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mi_deleteChat) {
            deleteChat();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteChat() {
        displayDeleteChatDialog();
    }

    private void displayDeleteChatDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MessagesActivity.this);
        alert.setTitle("Delete Chat");
        alert.setMessage("Are you sure you need to delete chat, will be delete for all partners?");

        alert.setPositiveButton("Delete", (dialog, whichButton) -> {
            for (String msgId : messagesIds) {
                Log.d(TAG, "deleteChat: Delete Message " + msgId);
                messagesRef.child(msgId).removeValue();
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }
}
