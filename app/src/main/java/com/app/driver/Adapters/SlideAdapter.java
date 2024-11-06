package com.app.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.driver.R;

public class SlideAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        mContext = context;
    }



    public String[] slideHeadings = {
            "",
            ""


    };

    public String[] slideDescription = {
            "",
            "",


    };



    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        TextView slideHeading = (TextView) view.findViewById(R.id.Slide_Heading);
        TextView slideDesc = (TextView) view.findViewById(R.id.Slide_Desc);

        slideHeading.setText(slideHeadings[position]);
        slideDesc.setText(slideDescription[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}