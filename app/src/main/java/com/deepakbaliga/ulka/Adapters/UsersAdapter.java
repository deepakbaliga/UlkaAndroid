package com.deepakbaliga.ulka.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepakbaliga.ulka.App;
import com.deepakbaliga.ulka.Listeners.UsersTouchListener;
import com.deepakbaliga.ulka.Models.User;
import com.deepakbaliga.ulka.R;

import java.util.LinkedList;

/**
 * Created by deezdroid on 27/09/15.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private LinkedList<User> users;
    private UsersTouchListener usersTouchListener;

    public UsersAdapter(LinkedList<User> users, UsersTouchListener usersTouchListener) {
        this.users = users;
        this.usersTouchListener = usersTouchListener;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_users, parent,false);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        User user = users.get(position);

        holder.getName().setText(user.getName());
        holder.getUsername().setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{

        private ImageView userImage;
        private TextView name;
        private TextView username;

        public UsersViewHolder(View itemView) {
            super(itemView);

            userImage = (ImageView) itemView.findViewById(R.id.template_users_image);
            name = (TextView) itemView.findViewById(R.id.template_users_name);
            username = (TextView) itemView.findViewById(R.id.template_users_username);

            name.setTypeface(App.montLight);
            username.setTypeface(App.montUltraLight);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usersTouchListener.touched(getPosition());
                }
            });
        }

        public ImageView getUserImage() {
            return userImage;
        }

        public void setUserImage(ImageView userImage) {
            this.userImage = userImage;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }
    }
}
