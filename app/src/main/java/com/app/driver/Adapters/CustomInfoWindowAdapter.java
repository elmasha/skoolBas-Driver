package com.app.driver.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.driver.MainActivity.ViewFarmActivity;
import com.app.driver.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter{

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }



    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    TextView tvTitle,tvSubTitle,tvRate;
    ImageView farmImage;
    @SuppressLint("MissingInflatedId")
    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.customwindow, null);

        tvTitle =  view.findViewById(R.id.tv_title);
         tvSubTitle =  view.findViewById(R.id.tv_subtitle);
         farmImage =  view.findViewById(R.id.tv_image);
         tvRate = view.findViewById(R.id.tv_rate);

        tvRate.setText(marker.getTitle());
        tvTitle.setText(marker.getTag().toString());

        Picasso.with(context).load(marker.getSnippet()).placeholder(R.drawable.loading).into(farmImage);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(context, ViewFarmActivity.class);
                logout.putExtra("ID", marker.getTag().toString());
                context.startActivity(logout);
            }
        });

        return view;
    }
}