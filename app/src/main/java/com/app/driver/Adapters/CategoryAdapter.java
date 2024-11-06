package com.app.driver.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.Category;
import com.app.driver.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.CatViewHolder> {

    private OnItemClickListener listener;
    public Context context;
    int selected_position = 0;
    int active = 0;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CatViewHolder holder, int i, @NonNull Category categories) {
        if (categories.getCategory() != null){
            holder.catName.setText( " "+categories.getCategory()+" " );
        }

        if (active ==1 ) {
            holder.Vieew.setBackgroundResource(selected_position == i ?  R.drawable.bg_sec_category :  R.drawable.bg_grey_search);
            holder.catName.setTextColor( selected_position == i ? Color.parseColor("#06265B") : Color.parseColor("#06265B"));

        }
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        this.context = parent.getContext();
        return new CategoryAdapter.CatViewHolder(v);
    }


    class CatViewHolder extends RecyclerView.ViewHolder{
        private TextView catName;
        private RelativeLayout Vieew;
        private View view;

        public CatViewHolder(@NonNull View itemView) {
            super(itemView);

            catName = itemView.findViewById(R.id.cat_name);
            Vieew = itemView.findViewById(R.id.LayoutRow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    active = 1;
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                    YoYo.with(Techniques.Pulse)
                            .duration(200)
                            .repeat(1)
                            .playOn(catName);

                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                }
            });



        }
    }

    public interface  OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
