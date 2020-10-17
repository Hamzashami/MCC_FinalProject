package com.hamzashami.coronaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.model.MainItem;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private Context context;
    private List<MainItem> mainItems;

    public MainAdapter(Context context, List<MainItem> mainItems) {
        this.context = context;
        this.mainItems = mainItems;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainItem mainItem = mainItems.get(position);
        holder.tv_number.setText(mainItem.getNumber() + "");
        holder.tv_title.setText(mainItem.getTitle());
        holder.tv_number.setTextColor(context.getResources().getColor(mainItem.getColorResId()));
    }

    @Override
    public int getItemCount() {
        return mainItems.size();
    }


    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView tv_number, tv_title;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MainViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
