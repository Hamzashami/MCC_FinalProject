package com.hamzashami.coronaproject.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private FirebaseAuth auth;

    private Context context;
    private List<Message> messages;
    private String imageUrl;

    public static final int LEFT_MESSAGE = 0;
    public static final int RIGHT_MESSAGE = 1;

    public MessagesAdapter(Context context, List<Message> messages, String imageUrl) {
        this.context = context;
        this.messages = messages;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == LEFT_MESSAGE
                ? new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false))
                : new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.tv_message.setText(currentMessage.getMessageText());
        if (imageUrl == null || TextUtils.isEmpty(imageUrl) || imageUrl.equalsIgnoreCase(context.getString(R.string.no_image))) {
            holder.circleImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get().load(imageUrl).into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView tv_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        auth = FirebaseAuth.getInstance();
        String currentUser = auth.getCurrentUser().getUid();
        return messages.get(position).getSenderId().equalsIgnoreCase(currentUser) ? RIGHT_MESSAGE : LEFT_MESSAGE;
    }
}
