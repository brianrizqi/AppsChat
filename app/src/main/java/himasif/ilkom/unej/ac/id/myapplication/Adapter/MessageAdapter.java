package himasif.ilkom.unej.ac.id.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import himasif.ilkom.unej.ac.id.myapplication.Message;
import himasif.ilkom.unej.ac.id.myapplication.Model.Chat;
import himasif.ilkom.unej.ac.id.myapplication.Model.User;
import himasif.ilkom.unej.ac.id.myapplication.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    private Context context;
    private List<Chat> chats;
    private String imageurl;

    FirebaseUser firebaseUser;


    public MessageAdapter(Context context, List<Chat> chats, String imageurl) {
        this.context = context;
        this.chats = chats;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat m = chats.get(position);

        holder.showChat.setText(m.getMessage());

        if (imageurl.equals("default")) {
            holder.imgChat.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageurl).into(holder.imgChat);
        }

        if (position == chats.size() - 1) {
            if (m.isIsseen()) {
                holder.txtSeen.setText("Seen");
            } else {
                holder.txtSeen.setText("Delivered");
            }
        } else {
            holder.txtSeen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView showChat;
        CircleImageView imgChat;
        TextView txtSeen;

        public ViewHolder(View itemView) {
            super(itemView);
            showChat = (TextView) itemView.findViewById(R.id.showChat);
            imgChat = (CircleImageView) itemView.findViewById(R.id.imgChat);
            txtSeen = (TextView) itemView.findViewById(R.id.txtSeen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
