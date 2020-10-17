package com.hamzashami.coronaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamzashami.coronaproject.CountryDetailsActivity;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.model.Country;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.MainViewHolder> {
    private static final String TAG = "CountryAdapter";

    private Context context;
    private List<String> countryList;

    public LinkAdapter(Context context, List<String> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String link = countryList.get(position);

        holder.tv_link.setText(link);
        holder.tv_link.setMovementMethod(LinkMovementMethod.getInstance());
        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }


    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tv_link;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_link = itemView.findViewById(R.id.tv_link);
        }
    }
}
