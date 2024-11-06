package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Reviews;
import com.app.driver.R;
import com.app.driver.TimeAgo.TimeAgo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ReviewsFarmAdapter extends FirestoreRecyclerAdapter<Reviews, ReviewsFarmAdapter.ReviewsViewHolder> {
    private OnItemCickListener listener;
    public List<Reviews> products;
    public Context context;


    public ReviewsFarmAdapter(@NonNull FirestoreRecyclerOptions<Reviews> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewsViewHolder holder, int i, @NonNull Reviews model) {
        holder.FarmName.setText(model.getUser());
        holder.message.setText(""+model.getComment()+"");
        holder.rating.setRating(Float.parseFloat(model.getRating()+""));
        String s=model.getUser().substring(0,2);
        holder.init.setText(s);
        if (String.valueOf(model.getTimestamp().getTime())!=null){
            holder.RDate.setText(TimeAgo.getTimeAgo(model.getTimestamp().getTime()));
        }

    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent,false);
        this.context = parent.getContext();
        return new ReviewsViewHolder(v);
    }

    ///---delete item----//
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class ReviewsViewHolder extends RecyclerView.ViewHolder{
        private TextView init,UserName,FarmName, message,RDate;
        private RatingBar rating;

        private ImageView image;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            init = itemView.findViewById(R.id.review_name_II);
            FarmName = itemView.findViewById(R.id.review_user_row);
            message = itemView.findViewById(R.id.review_message_row);
            rating = itemView.findViewById(R.id.review_rate_row);
            RDate = itemView.findViewById(R.id.review_date_row);

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
