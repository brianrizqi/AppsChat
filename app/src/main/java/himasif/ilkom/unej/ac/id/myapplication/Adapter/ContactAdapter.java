package himasif.ilkom.unej.ac.id.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import himasif.ilkom.unej.ac.id.myapplication.Message;
import himasif.ilkom.unej.ac.id.myapplication.Model.Chat;
import himasif.ilkom.unej.ac.id.myapplication.Model.User;
import himasif.ilkom.unej.ac.id.myapplication.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private boolean isChat;
    String lastMessage;

    public ContactAdapter(Context context, List<User> users, boolean isChat) {
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User m = users.get(position);

        holder.txtUsername.setText(m.getUsername());
        if (m.getImageURL().equals("default")) {
            holder.imgProfile.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(m.getImageURL()).into(holder.imgProfile);
        }

        if (isChat) {
            lastMessage(m.getId(), holder.txtLast);
        } else {
            holder.txtLast.setVisibility(View.GONE);
        }

        if (isChat) {
            if (m.getStatus().equals("online")) {
                holder.imgOn.setVisibility(View.VISIBLE);
                holder.imgOff.setVisibility(View.GONE);
            } else {
                holder.imgOn.setVisibility(View.GONE);
                holder.imgOff.setVisibility(View.VISIBLE);
            }
        } else {
            holder.imgOn.setVisibility(View.GONE);
            holder.imgOff.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Message.class);
                i.putExtra("userid", m.getId());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUsername;
        public CircleImageView imgProfile;
        private CircleImageView imgOn;
        private CircleImageView imgOff;
        private TextView txtLast;

        public ViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
            imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            imgOn = (CircleImageView) itemView.findViewById(R.id.imgOn);
            imgOff = (CircleImageView) itemView.findViewById(R.id.imgOff);
            txtLast = (TextView) itemView.findViewById(R.id.txtLast);
        }
    }

    private void lastMessage(final String userid, final TextView txtLast) {
        lastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                        lastMessage = chat.getMessage();
                    }
                }

                switch (lastMessage) {
                    case "default":
                        txtLast.setText("No Message");
                        break;
                    default:
                        txtLast.setText(lastMessage);
                        break;
                }

                lastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
