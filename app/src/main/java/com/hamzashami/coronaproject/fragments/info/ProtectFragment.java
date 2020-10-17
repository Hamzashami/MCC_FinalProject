package com.hamzashami.coronaproject.fragments.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.adapters.LinkAdapter;
import com.hamzashami.coronaproject.adapters.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.draw.data.RtlMode;
import com.smarteist.autoimageslider.SliderView;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProtectFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference protectRef;
    private SliderView imageSlider1, imageSlider2, imageSlider3;
    private String[] links;

    public ProtectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_protect, container, false);
        imageSlider1 = view.findViewById(R.id.imageSlider1);
        imageSlider2 = view.findViewById(R.id.imageSlider2);
        imageSlider3 = view.findViewById(R.id.imageSlider3);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageSlider1.setIndicatorRtlMode(RtlMode.On);
        imageSlider2.setIndicatorRtlMode(RtlMode.On);
        imageSlider3.setIndicatorRtlMode(RtlMode.On);

        database = FirebaseDatabase.getInstance();
        protectRef = database.getReference("info").child("protext");

        protectRef.child("mask").child("links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relatedLinkString = dataSnapshot.getValue(String.class);
                links = relatedLinkString.split(",,");
                SliderAdapter sliderAdapter = new SliderAdapter(Arrays.asList(links));
                imageSlider1.setSliderAdapter(sliderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        protectRef.child("mask").child("links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relatedLinkString = dataSnapshot.getValue(String.class);
                links = relatedLinkString.split(",,");
                SliderAdapter sliderAdapter = new SliderAdapter(Arrays.asList(links));
                imageSlider1.setSliderAdapter(sliderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        protectRef.child("home_care").child("links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relatedLinkString = dataSnapshot.getValue(String.class);
                links = relatedLinkString.split(",,");
                SliderAdapter sliderAdapter = new SliderAdapter(Arrays.asList(links));
                imageSlider2.setSliderAdapter(sliderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        protectRef.child("general").child("links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relatedLinkString = dataSnapshot.getValue(String.class);
                links = relatedLinkString.split(",,");
                SliderAdapter sliderAdapter = new SliderAdapter(Arrays.asList(links));
                imageSlider3.setSliderAdapter(sliderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
