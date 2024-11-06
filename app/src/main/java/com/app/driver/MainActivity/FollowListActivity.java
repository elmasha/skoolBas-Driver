package com.app.driver.MainActivity;

import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.mAuth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Adapters.FollowersAdapter;
import com.app.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView,mRecyclerView2;


    private RelativeLayout Followers,Following;

    private TextView BackToMainInbox;
    private ImageView ErrorImage,ErrorImage2;

    private FollowersAdapter adapter;

    private LinearLayout FollowerList,FollowingList;
    private TextView followerText,followingText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        mRecyclerView = findViewById(R.id.followers_recycler_view);
        mRecyclerView2 = findViewById(R.id.following_recycler_view);
        Followers = findViewById(R.id.FollowersRecy);
        Following = findViewById(R.id.FollowingsRecy);
        FollowerList = findViewById(R.id.FollowerList);
        FollowingList = findViewById(R.id.FollowingList);
        followerText = findViewById(R.id.followerText);
        followingText = findViewById(R.id.followingText);

        ErrorImage = findViewById(R.id.ErrorImage2);
        ErrorImage2 = findViewById(R.id.ErrorImage22);


        FollowerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshPage();
                FollowingList.setBackgroundResource(R.color.transparent);

                FollowerList.setBackgroundResource(R.drawable.bg_main_category);
                Followers.setVisibility(View.VISIBLE);
                Following.setVisibility(View.GONE);
            }
        });

        FollowingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshPage();
                FollowerList.setBackgroundResource(R.color.transparent);


                FollowingList.setBackgroundResource(R.drawable.bg_main_category);


                Followers.setVisibility(View.GONE);
                Following.setVisibility(View.VISIBLE);
            }
        });







        BackToMainInbox = findViewById(R.id.BackToMainInbox);

        BackToMainInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });


        if (mAuth.getCurrentUser() != null){
            FetchFollowersCount(mAuth.getCurrentUser().getUid());
            FetchFollowingsCount(mAuth.getCurrentUser().getUid());
        }


        FollowerList.setBackgroundResource(R.drawable.bg_main_category);

    }


    private void RefreshPage(){
        if (mAuth.getCurrentUser() != null){
            FetchFollowersCount(mAuth.getCurrentUser().getUid());
            FetchFollowingsCount(mAuth.getCurrentUser().getUid());
        }
    }

    //------Count Category---//
    ArrayList<Object> uniqueDatesFo3 = new ArrayList<Object>();
    int sumFo3 = 0;

    private void FetchFollowersCount(String UID) {

        DriverDbRef.document(UID).collection("Followers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            uniqueDatesFo3.clear();
                            sumFo3 = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uniqueDatesFo3.add(document.getData());
                                for (sumFo3 = 0; sumFo3 < uniqueDatesFo3.size(); sumFo3++) {

                                }
                            }

                            if (sumFo3 > 0) {
                                ErrorImage.setVisibility(View.GONE);
                            } else {
                                ErrorImage.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                });

    }
    //==end


    //------Count Category---//
    ArrayList<Object> uniqueDatesFow3 = new ArrayList<Object>();
    int sumFow3 = 0;

    private void FetchFollowingsCount(String UID) {

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            uniqueDatesFow3.clear();
                            sumFow3 = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uniqueDatesFow3.add(document.getData());
                                for (sumFow3 = 0; sumFow3 < uniqueDatesFow3.size(); sumFow3++) {

                                }
                            }

                            if (sumFow3 > 0) {
                                ErrorImage2.setVisibility(View.GONE);
                            } else {
                                ErrorImage2.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                });

    }
    //==end




    public static String roomID,to_user_uid;
//    private void FetchFollowers(String UID){
//        Query query = FarmDbRef.document(UID).collection("Followers")
//                .orderBy("created_at", Query.Direction.DESCENDING);
//        FirestoreRecyclerOptions<Followers> options = new FirestoreRecyclerOptions.Builder<Followers>()
//                .setQuery(query, Followers.class)
//                .setLifecycleOwner(this)
//                .setLifecycleOwner(this)
//                .build();
//        adapter = new FollowersAdapter(options);
//
//        mRecyclerView.setHasFixedSize(false);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        mRecyclerView.setAdapter(adapter);
//
//
//
//
//        adapter.setOnItemClickListener(new FollowersAdapter.OnItemCickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                roomID = documentSnapshot.getId();
//
//                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
//                logout.putExtra("ID",documentSnapshot.getId());
//                startActivity(logout);
//
//            }
//        });
//
//    }






    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }



    private void BackPress(){
        super.onBackPressed();
    }



    @Override
    public void onBackPressed() {
        BackPress();
    }

}