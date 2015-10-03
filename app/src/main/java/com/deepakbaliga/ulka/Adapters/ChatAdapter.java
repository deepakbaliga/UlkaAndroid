package com.deepakbaliga.ulka.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepakbaliga.ulka.App;
import com.deepakbaliga.ulka.Models.ChatModel;
import com.deepakbaliga.ulka.R;

import java.util.LinkedList;

/**
 * Created by deezdroid on 28/09/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {

    private LinkedList<ChatModel> chatModels;
    private int ME = 1;
    private int YOU = 2;

    public ChatAdapter(LinkedList<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }

    @Override
    public ChatVH onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ME){
            final  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_me_chat, parent, false);
            ChatVH chatVH = new ChatVH(view);
            return chatVH;
        }else{
            final  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_you_chat, parent, false);
            ChatVH chatVH = new ChatVH(view);
            return chatVH;
        }


    }

    @Override
    public void onBindViewHolder(ChatVH holder, int position) {

        ChatModel model = chatModels.get(position);

        holder.getChatMessageView().setText(model.getMessage());
        holder.getChatMessageView().setTypeface(App.montLight);
    }

    @Override
    public int getItemViewType(int position) {
        if(chatModels.get(position).isMe()) return ME;
        else return YOU;
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public class ChatVH extends RecyclerView.ViewHolder{

        private TextView chatMessageView;

        public ChatVH(View itemView) {
            super(itemView);

            chatMessageView = (TextView) itemView.findViewById(R.id.template_chat_text);
        }

        public TextView getChatMessageView() {
            return chatMessageView;
        }

        public void setChatMessageView(TextView chatMessageView) {
            this.chatMessageView = chatMessageView;
        }
    }
}
