package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.AdsExplore;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdsAdapterExplore extends FirestoreRecyclerAdapter<AdsExplore, AdsAdapterExplore.FarmImageViewHolder> {
    private OnItemCickListener listener;
    public List<AdsExplore> products;
    public Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdsAdapterExplore(@NonNull FirestoreRecyclerOptions<AdsExplore> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FarmImageViewHolder holder, int i, @NonNull AdsExplore model) {
        Picasso.with(holder.image.getContext()).load(model.getAd_image()).into(holder.image);
        Picasso.with(holder.CircleImage.getContext()).load(model.getUser_image()).placeholder(R.drawable.user).into(holder.CircleImage);
        holder.price.setText("Ksh "+model.getAd_price());
        holder.Name.setText(model.getAd_title());
        holder.desc.setText(model.getAd_description());
        holder.userName.setText(model.getUser_name());

    }

    @NonNull
    @Override
    public FarmImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_explore_row,parent,false);
        this.context = parent.getContext();
        return new FarmImageViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class FarmImageViewHolder extends RecyclerView.ViewHolder{
        private TextView Name, desc, time,price,userName;

        private ImageView image;

        private CircleImageView CircleImage;

        public FarmImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.Ad_image);
            Name = itemView.findViewById(R.id.Ad_name);
            desc = itemView.findViewById(R.id.Ad_desc);
            price = itemView.findViewById(R.id.Ad_price);
            userName = itemView.findViewById(R.id.Ad_farmName);
            CircleImage = itemView.findViewById(R.id.user_image_ad_row);


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
