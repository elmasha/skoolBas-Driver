package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Ads;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsAdapter extends FirestoreRecyclerAdapter<Ads, AdsAdapter.FarmImageViewHolder> {
    private OnItemCickListener listener;
    private OnItemCickListenerEdit listenerEdit;

    private OnItemCickListenerDelete listenerDelete;

    public List<Ads> products;
    public Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdsAdapter(@NonNull FirestoreRecyclerOptions<Ads> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FarmImageViewHolder holder, int i, @NonNull Ads model) {
        Picasso.with(holder.image.getContext()).load(model.getAd_image()).into(holder.image);
        holder.price.setText(model.getAd_price());
        holder.Name.setText(model.getAd_title());
        holder.desc.setText(model.getAd_type());
    }

    @NonNull
    @Override
    public FarmImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row,parent,false);
        this.context = parent.getContext();
        return new FarmImageViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class FarmImageViewHolder extends RecyclerView.ViewHolder{
        private TextView Name, desc, time,price,edit,delete;

        private ImageView image;

        public FarmImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.Ad_image);
            Name = itemView.findViewById(R.id.Ad_name);
            desc = itemView.findViewById(R.id.Ad_category);
            price = itemView.findViewById(R.id.Ad_price);
            edit = itemView.findViewById(R.id.Edit_ads);
            delete = itemView.findViewById(R.id.Delete_ads);



            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listenerDelete.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listenerEdit.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });


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


    public interface  OnItemCickListenerDelete{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }


    public void setOnItemClickListener(OnItemCickListenerDelete listener){
        this.listenerDelete = listener;

    }


    public interface  OnItemCickListenerEdit{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }


    public void setOnItemClickListener(OnItemCickListenerEdit listener){
        this.listenerEdit = listener;

    }

    public interface  OnItemCickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemCickListener listener){

        this.listener = listener;

    }
}
