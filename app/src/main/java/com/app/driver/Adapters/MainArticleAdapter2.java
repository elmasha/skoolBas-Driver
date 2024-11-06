package com.app.driver.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Models.CountiesSearch;
import com.app.driver.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class MainArticleAdapter2 extends RecyclerView.Adapter<MainArticleAdapter2.ViewHolder> {


    private ArrayList<CountiesSearch> articleArrayList;
    private Context mContext;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public MainArticleAdapter2(Context context, ArrayList<CountiesSearch> articleArrayList) {
        this.mContext = context;
        this.articleArrayList = articleArrayList;
    }


    @NonNull
    @Override
    public MainArticleAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_row, viewGroup, false);
        return new MainArticleAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainArticleAdapter2.ViewHolder viewHolder, int position) {
        final CountiesSearch articleModel = articleArrayList.get(position);
        if (!TextUtils.isEmpty(articleModel.getCategory())) {
            viewHolder.titleText.setText(articleModel.getCategory());
        }

        viewHolder.artilceAdapterParentLinear.setTag(articleModel);


    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, date;
        private LinearLayout artilceAdapterParentLinear;

        public ViewHolder(@NonNull View view) {
            super(view);
            titleText = view.findViewById(R.id.search_name);
            artilceAdapterParentLinear = view.findViewById(R.id.searchLayout);
            artilceAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YoYo.with(Techniques.Pulse)
                            .duration(200)
                            .repeat(1)
                            .playOn(artilceAdapterParentLinear);
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onRecyclerViewItemClicked(getAdapterPosition(), view);
                    }
                }
            });

        }
    }


    public void OnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface  OnRecyclerViewItemClickListener {
        public void onRecyclerViewItemClicked(int position, View id);
    }
}

