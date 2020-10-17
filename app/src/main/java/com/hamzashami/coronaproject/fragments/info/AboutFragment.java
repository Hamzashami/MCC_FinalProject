package com.hamzashami.coronaproject.fragments.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.adapters.LinkAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";


    private FirebaseDatabase database;
    private DatabaseReference aboutRef;
    private String aboutString, relatedLinkString;
    private String[] links;
    private TextView tv_about;
    private RecyclerView rv_linkList;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        tv_about = view.findViewById(R.id.tv_about);
        rv_linkList = view.findViewById(R.id.rv_linkList);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        aboutRef = database.getReference("info").child("about");
        rv_linkList.setHasFixedSize(true);
        rv_linkList.setLayoutManager(new LinearLayoutManager(getContext()));

        aboutRef.child("about_text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aboutString = dataSnapshot.getValue(String.class);
                tv_about.setText(aboutString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        aboutRef.child("related_links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                relatedLinkString = dataSnapshot.getValue(String.class);
                links = relatedLinkString.split(",,");
                Log.d(TAG, "onDataChange: Links " + links.length);
                rv_linkList.setAdapter(new LinkAdapter(getContext(), Arrays.asList(links)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
