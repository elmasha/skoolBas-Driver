package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.FarmerAround;
import com.app.driver.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainViewFarmImageAdapter extends FirestoreRecyclerAdapter<FarmerAround, MainViewFarmImageAdapter.FarmImageViewHolder> {
    private OnItemCickListener listener;
    public List<FarmerAround> products;
    public Context context;


    public MainViewFarmImageAdapter(@NonNull FirestoreRecyclerOptions<FarmerAround> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FarmImageViewHolder holder, int i, @NonNull FarmerAround model) {
        holder.Name.setText(model.getFarm_Name());
        holder.desc.setText(model.getFarm_category());
        holder.rating.setText(model.getFarm_rating()+"");
        holder.county.setText(model.getFarm_location());
        Picasso.with(holder.image.getContext()).load(model.getFarm_image()).fit().into(holder.image);
    }

    @NonNull
    @Override
    public FarmImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_view_row,parent,false);
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

            image = itemView.findViewById(R.id.farm_row_image_v);
            Name = itemView.findViewById(R.id.farm_row_name_v);
            desc = itemView.findViewById(R.id.farm_row_category_v);
            rating = itemView.findViewById(R.id.farm_row_rate_v);
            county = itemView.findViewById(R.id.farm_row_county_v);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Pulse)
                            .duration(200)
                            .repeat(1)
                            .playOn(itemView);
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
