package com.app.driver.Adapters;

import static com.app.driver.Common.mAuth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.MsgBody;
import com.app.driver.R;
import com.app.driver.TimeAgo.ChatsTimeAgo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ChatsAdapter extends FirestoreRecyclerAdapter<MsgBody, ChatsAdapter.ChatsViewHolder> {
    private OnItemCickListener listener;
    public List<MsgBody> products;
    public Context context;


    public ChatsAdapter(@NonNull FirestoreRecyclerOptions<MsgBody> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int i, @NonNull MsgBody model) {
        if (model.uid.equals(mAuth.getCurrentUser().getUid())){
            holder.LeftLayout.setVisibility(View.GONE);
            holder.RightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(model.getContent());
            if (String.valueOf(model.getCreated_at().getTime()) != null){
                holder.rightDate.setText(ChatsTimeAgo.getTimeAgo(model.getCreated_at().getTime()));

            }
        }else {

            holder.LeftLayout.setVisibility(View.VISIBLE);
            holder.RightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(model.getContent());
            if (String.valueOf(model.getCreated_at().getTime()) != null){
                holder.leftDate.setText(ChatsTimeAgo.getTimeAgo(model.getCreated_at().getTime()));
            }
        }
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recyler_row,parent,false);
        this.context = parent.getContext();
        return new ChatsViewHolder(v);
    }

    ///---delete item----//
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class ChatsViewHolder extends RecyclerView.ViewHolder{
        private TextView init,leftMsg,rightMsg, leftDate,rightDate,initText;


        private LinearLayout LeftLayout,RightLayout;



        private ImageView image;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            LeftLayout = itemView.findViewById(R.id.LeftChatLayout);
            RightLayout = itemView.findViewById(R.id.RightChatLayout);

            rightMsg = itemView.findViewById(R.id.chat_message_row_right);
            rightDate = itemView.findViewById(R.id.chat_date_row_right);

            leftMsg = itemView.findViewById(R.id.chat_message_row_left);
            leftDate = itemView.findViewById(R.id.chat_date_row_left);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });



        }
    }

    public interface  OnItemCickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemCickListener listener){

        this.listener = listener;

    }
}
