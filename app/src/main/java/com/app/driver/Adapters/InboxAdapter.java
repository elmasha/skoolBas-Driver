package com.app.driver.Adapters;

import static com.app.driver.Common.mAuth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Messages;
import com.app.driver.Models.Reviews;
import com.app.driver.R;
import com.app.driver.TimeAgo.ChatsTimeAgo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

public class InboxAdapter extends FirestoreRecyclerAdapter<Messages, InboxAdapter.InboxViewHolder> {
    private OnItemCickListener listener;

    private OnLongClickListener LongClickListener;
    public List<Reviews> products;
    public Context context;


    public InboxAdapter(@NonNull FirestoreRecyclerOptions<Messages> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull InboxViewHolder holder, int i, @NonNull Messages model) {
        if (mAuth.getCurrentUser().getUid().equals(model.from_uid)){
            holder.FarmName.setText(model.getTo_name());
            String s=model.getTo_name().substring(0,2);
            holder.init.setText(s);
        }else {
            holder.FarmName.setText(model.getFrom_name());
            String s=model.getFrom_name().substring(0,2);
            holder.init.setText(s);
        }

        if (mAuth.getCurrentUser().getUid().equals(model.getLast_uid())){
            holder.message.setText("You: "+model.getLast_msg());
        }else {
            holder.message.setText(""+model.getLast_msg());
            if (model.isStatus() == false){
                holder.relativeLayout.setVisibility(View.VISIBLE);
            }else {
                holder.relativeLayout.setVisibility(View.GONE);
            }
        }


        if (String.valueOf(model.getCreated_at().getTime()) != null){
            holder.RDate.setText(ChatsTimeAgo.getTimeAgo(model.getCreated_at().getTime()));
        }

    }


    ///Update item
    public void UpdateItem (int position, Map<String,Object> map) {
        getSnapshots().getSnapshot(position).getReference().update(map);
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_row,parent,false);
        this.context = parent.getContext();
        return new InboxViewHolder(v);
    }

    ///---delete item----//
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class InboxViewHolder extends RecyclerView.ViewHolder{
        private TextView init,UserName,FarmName, message,RDate;
        private RelativeLayout relativeLayout;
        private RatingBar rating;

        private ImageView image;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            init = itemView.findViewById(R.id.inbox_name_II);
            FarmName = itemView.findViewById(R.id.inbox_user_row);
            message = itemView.findViewById(R.id.inbox_message_row);
            RDate = itemView.findViewById(R.id.inbox_date_row);
            relativeLayout = itemView.findViewById(R.id.LayoutRowInbox);

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

    public interface  OnLongClickListener{
        void longClick(DocumentSnapshot documentSnapshot, int position);
    }


    public void setOnLongItemClickListener(OnLongClickListener listenerLong){
        this.LongClickListener = listenerLong;

    }
    public void setOnItemClickListener(OnItemCickListener listener){

        this.listener = listener;

    }
}
