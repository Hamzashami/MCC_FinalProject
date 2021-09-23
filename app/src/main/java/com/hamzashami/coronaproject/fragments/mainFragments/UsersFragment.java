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
import com.hamzashami.coronaproject.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {
    private static final String TAG = "UsersFragment";

    private RecyclerView rv_users;

    private SwipeRefreshLayout srl_users;
    private ConstraintLayout cl_usersLoadingOverlay;

    private List<User> users;
    private UsersAdapter usersAdapter;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        rv_users = view.findViewById(R.id.rv_users);
        srl_users = view.findViewById(R.id.srl_users);
        cl_usersLoadingOverlay = view.findViewById(R.id.cl_usersLoadingOverlay);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        Log.d(TAG, "onViewCreated: userRef " + userRef);

        rv_users.setHasFixedSize(true);
        rv_users.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        users = new ArrayList<>();
        initUsers();
        srl_users.setOnRefreshListener(this::initUsers);
    }

    private void initUsers() {
        srl_users.setRefreshing(false);
        cl_usersLoadingOverlay.setVisibility(View.VISIBLE);
        users.clear();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                cl_usersLoadingOverlay.setVisibility(View.GONE);

                usersAdapter = new UsersAdapter(getContext().getApplicationContext(), users);
                rv_users.setAdapter(usersAdapter);

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    String currentUserId = auth.getCurrentUser().getUid();
                    //To Remove Self User from list
                    if (user.getId().equalsIgnoreCase(currentUserId)) {
                        continue;
                    }
                    users.add(user);
                    usersAdapter.notifyDataSetChanged();
                }
                Collections.sort(users, (s1, s2) -> s1.getUsername().compareToIgnoreCase(s2.getUsername()));
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cl_usersLoadingOverlay.setVisibility(View.GONE);
            }
        });
    }
}
