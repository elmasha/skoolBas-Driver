package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.AllFarms;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExploreFarmAdapter extends FirestoreRecyclerAdapter<AllFarms, ExploreFarmAdapter.FarmImageViewHolder> {
    private OnItemCickListener listener;
    public List<AllFarms> products;
    public Context context;


    public ExploreFarmAdapter(@NonNull FirestoreRecyclerOptions<AllFarms> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FarmImageViewHolder holder, int i, @NonNull AllFarms model) {
        holder.Name.setText(model.getFarmName());
        holder.desc.setText(model.getFarmCategory());
        holder.rating.setText(model.getRating()+"");
        holder.county.setText(model.getCounty());
        Picasso.with(holder.image.getContext()).load(model.getImage()).fit().into(holder.image);
    }

    @NonNull
    @Override
    public FarmImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_explore_row,parent,false);
        this.context = parent.getContext();
        return new FarmImageViewHolder(v);
    }

    ///---delete item----//
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class FarmImageViewHolder extends RecyclerView.ViewHolder{
        private TextView Name, desc, rating,county;

        private ImageView image;

        public FarmImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.farm_row_image_e);
            Name = itemView.findViewById(R.id.farm_row_name_e);
            desc = itemView.findViewById(R.id.farm_row_category_e);
            rating = itemView.findViewById(R.id.farm_row_rate_e);
            county = itemView.findViewById(R.id.farm_row_county_e);

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
