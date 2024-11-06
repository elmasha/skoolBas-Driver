package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Counties;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class CountyAdapter extends FirestoreRecyclerAdapter<Counties, CountyAdapter.CountyViewHolder> {

    private OnItemCickListener listener;
    public Context context;


    int selected_position = 0;
    int active = 0;



    public CountyAdapter(@NonNull FirestoreRecyclerOptions<Counties> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CountyViewHolder holder, int i, @NonNull Counties model) {
        holder.Name.setText(model.getCounty());
        holder.county.setText(model.getNo()+"");

        if (active ==1 ) {
            holder.r1.setBackgroundResource(selected_position == i ? R.drawable.bg_sec_category :  R.drawable.border_grey_county);

        }

    }

    @NonNull
    @Override
    public CountyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.county_row,parent,false);
        this.context = parent.getContext();
        return new CountyViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CountyViewHolder extends RecyclerView.ViewHolder{
        private TextView Name, county, ward,mobile,age;
        private RelativeLayout r1;
        private CircleImageView profile;
        private View view;

        public CountyViewHolder(@NonNull View itemView) {
            super(itemView);

            r1 = itemView.findViewById(R.id.rrr);
            Name = itemView.findViewById(R.id.county);
            county = itemView.findViewById(R.id.county_no);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    active = 1;
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }


                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
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
