package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.driver.Models.FarmImage;
import com.app.driver.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<FarmImage> mSliderItems = new ArrayList<>();
    private String Doc_Id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    // constructor for our adapter class.
    public SliderAdapter(Context context, List<FarmImage> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_latest, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        final FarmImage sliderItem = mSliderItems.get(position);
        Picasso.with(viewHolder.imageView.getContext()).load(sliderItem.getImage()).fit().into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doc_Id = sliderItem.getImage();
//                if (Doc_Id != null) {
//                    Intent toVendorPref = new Intent(context, ViewNewsActivity.class);
//                    toVendorPref.putExtra("doc_ID", Doc_Id);
//                    context.startActivity(toVendorPref);
//                    viewsCount(Doc_Id);
//                }
            }
        });
    }



    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    // view holder class for initializing our view holder.
    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        // variables for our view and image view.
        View itemView;
        ImageView imageView;
        TextView headline, likes, comment, category, viewsCount, stories, date;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            // initializing our views.
            imageView = itemView.findViewById(R.id.image_slider);

            this.itemView = itemView;
        }
    }



}