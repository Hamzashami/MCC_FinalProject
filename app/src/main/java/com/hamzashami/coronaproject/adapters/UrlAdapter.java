package com.hamzashami.coronaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamzashami.coronaproject.R;

import java.util.List;

public class UrlAdapter extends RecyclerView.Adapter<UrlAdapter.MainViewHolder> {
    private static final String TAG = "CountryAdapter";

    private Context context;
    private List<String> countryList;

    public UrlAdapter(Context context, List<String> countryList) {
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
