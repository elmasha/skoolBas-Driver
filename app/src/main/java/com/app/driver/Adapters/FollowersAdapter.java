package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Reviews;
import com.app.driver.Models.StopsModel;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersAdapter extends FirestoreRecyclerAdapter<StopsModel, FollowersAdapter.InboxViewHolder> {
    private OnItemCickListener listener;

    private OnLongClickListener LongClickListener;
    public List<Reviews> products;
    public Context context;


    public FollowersAdapter(@NonNull FirestoreRecyclerOptions<StopsModel> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull InboxViewHolder holder, int i, @NonNull StopsModel model) {

        if (model.getStop_name() != null) {
            String s=model.getStop_name().substring(0,2);
            holder.init.setText(s);
        }



    }


    ///Update item
    public void UpdateItem (int position, Map<String,Object> map) {
        getSnapshots().getSnapshot(position).getReference().update(map);
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_row,parent,false);
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

        private CircleImageView image;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            init = itemView.findViewById(R.id.inbox_name_following_II);
            relativeLayout = itemView.findViewById(R.id.LayoutNoImageFollowing);

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
