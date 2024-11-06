package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.FarmImage;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class FarmImageAdapter extends FirestoreRecyclerAdapter<FarmImage,FarmImageAdapter.FarmImageViewHolder> {
    private OnItemClickListener listener;
    private OnItemClickListenerDelete listenerDelete;
    public List<FarmImage> products;
    public Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FarmImageAdapter(@NonNull FirestoreRecyclerOptions<FarmImage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FarmImageViewHolder holder, int i, @NonNull FarmImage model) {
        Picasso.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
    }

    @NonNull
    @Override
    public FarmImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_image_row,parent,false);
        this.context = parent.getContext();
        return new FarmImageViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    ///Update item
    public void UpdateItem (int position, Map<String,Object> map) {
        getSnapshots().getSnapshot(position).getReference().update(map);
    }


    class FarmImageViewHolder extends RecyclerView.ViewHolder{
        private TextView Name, desc, time;

        private ImageView image,delete;

        public FarmImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_row_farm);
            delete = itemView.findViewById(R.id.delete_farm_row);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listenerDelete != null){
                        listenerDelete.onItemClickDelete(getSnapshots().getSnapshot(position),position);

                    }
                }
            });


        }
    }


    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnItemClickListenerDelete {
        void onItemClickDelete(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;

    }


    public void setOnItemClickListener(OnItemClickListenerDelete listener){
        this.listenerDelete = listener;

    }
}
