package com.hamzashami.coronaproject.fragments.mainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.adapters.UsersAdapter;
import com.hamzashami.coronaproject.model.Message;
import com.hamzashami.coronaproject.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    private RecyclerView rv_chats;
    private SwipeRefreshLayout srl_chat;
    private ConstraintLayout cl_chatLoadingOverlay;

    private List<User> users;
    private Set<String> usersIds;
    private UsersAdapter usersAdapter;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference messagesRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        rv_chats = view.findViewById(R.id.rv_chats);
        srl_chat = view.findViewById(R.id.srl_chat);
        cl_chatLoadingOverlay = view.findViewById(R.id.cl_chatLoadingOverlay);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        messagesRef = database.getReference("messages");

        rv_chats.setHasFixedSize(true);
        rv_chats.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        users = new ArrayList<>();
        usersIds = new HashSet<>();
        initUsers();

        srl_chat.setOnRefreshListener(this::initUsers);
    }

    private void initUsers() {
        srl_chat.setRefreshing(false);
        cl_chatLoadingOverlay.setVisibility(View.VISIBLE);
        users.clear();
        usersIds.clear();
        String currentUserId = auth.getCurrentUser().getUid();
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cl_chatLoadingOverlay.setVisibility(View.GONE);

                usersAdapter = new UsersAdapter(getContext(), users);
                rv_chats.setAdapter(usersAdapter);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    if (message.getSenderId().equalsIgnoreCase(currentUserId)) {
                        usersIds.add(message.getReceiverId());
                    } else if (message.getReceiverId().equalsIgnoreCase(currentUserId)) {
                        usersIds.add(message.getSenderId());
                    }
                }

                for (String id : usersIds) {
                    Log.d(TAG, "onDataChange: UserId " + id);
                    userRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Log.d(TAG, "onDataChange: User " + user.toString());
                            users.add(user);
                            usersAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Collections.sort(users, (s1, s2) -> s1.getUsername().compareToIgnoreCase(s2.getUsername()));
                usersAdapter.notifyDataSetChanged();

                Log.d(TAG, "onDataChange: Users : " + users.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cl_chatLoadingOverlay.setVisibility(View.GONE);
            }
        });
    }
}
