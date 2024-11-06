package com.app.driver.MainActivity;

import static com.app.driver.Common.DB;
import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.Common.df;
import static com.app.driver.Common.mAuth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.driver.Adapters.AdsAdapter;
import com.app.driver.Adapters.FarmImageAdapter2;
import com.app.driver.Adapters.FollowingAdapter;
import com.app.driver.Adapters.ReviewsFarmAdapter;
import com.app.driver.Adapters.SliderAdapter;
import com.app.driver.ItemDecorator;
import com.app.driver.Models.Ads;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.FarmImage;
import com.app.driver.Models.Followers;
import com.app.driver.Models.Messages;
import com.app.driver.Models.Reviews;
import com.app.driver.Models.UserAccount;
import com.app.driver.Models.Users;
import com.app.driver.Notify.FcmNotificationsSender;
import com.app.driver.R;
import com.app.driver.utils.FirebaseUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hsalf.smileyrating.SmileyRating;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ViewFarmActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private AppBarConfiguration appBarConfiguration;
    public static GoogleApiClient googleApiClient;
    public static Location lastLocation;
    public static LocationRequest locationRequest;
    private String TAG = "TAG";

    private GoogleMap mMap;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    private ViewPager mSliderViewPager;
    private LinearLayout mDotsLayout;

    private TextView[] mDots;
    TextView privacy;
    private Button Finishbtn;
    private RecyclerView mRecyclerViewFollowers;

    private  int mCurrentPage;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout,LayoutPrivacy;
    private TextView get_started,enableLocation,Notify;

    private CheckBox checkBox;

    private String current_user;

    double radiusInMeters = 100.0;
    int strokeColor = 0xffff0000; //Color Code you want
    int shadeColor = 0x44ff0000; //opaque red fill
    private Circle mCircle;

    public static SettingsClient mSettingsClient;
    public static LocationSettingsRequest mLocationSettingsRequest;
    public static final int REQUEST_CHECK_SETTINGS = 214;
    public static final int REQUEST_ENABLE_GPS = 516;
    public static  String FCM_KEY;

    public static FloatingActionButton OpenDrawer,SearchLayout;
    private FrameLayout frameLayout;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private FloatingActionButton UploadImage;


    ///----Image storage and compression---////
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;
    private UploadTask uploadTask;

    private Uri imageUri ;
    private Button Upload_FarmBtn;
    private RecyclerView mRecyclerView,mRecyclerView_review;
    private FarmImageAdapter2 adapter;
    private ReviewsFarmAdapter adapter_review;
    private boolean edits = true;
    private String UID,chatRoomId;


    private TextView Farm_name ,Farm_category,Farm_offers,User_name,Farm_contact,Farm_rating,Farm_description,Farm_county,BackMaps,CloseReview;
    private ImageView Farm_image;

    private LinearLayout OnMap,OnAds,OnReviews,LayoutMaps,LayoutAds,LayoutReviews,ExpandReView,Add_a_review,AddMyReviews_v;
    private SmileyRating ratingBar;
    private EditText InputReview;

    private int ReviewState = 0;
    private String reviewText;
    private FloatingActionButton Btn_submit_review;
    private int RatingReview;

    private LinearLayout showMore;

    private Messages messagesList;

    private TextView showMore0,ChatRoom,CLoseChatLayout;
    private ImageView showMore1,showMore2;
    private int showMoreState= 1;

    private RecyclerView mRecyclerViewAd;
    private AdsAdapter adapterAds;
    LinearLayout ChatLayout;
    EditText InputMessage;
    FloatingActionButton BtnSendMessage;

    String Message="";
    private int ChatState = 0;


    private Users userProfile;

    private ImageView ErrorImageReview_v,ErrorImageAds_v;


    private SliderAdapter sliderAdapter;
    private ArrayList<FarmImage> sliderDataArrayList;
    private SliderView sliderView;
    private String Doc_Id,Doc_Id2;

    private TextView followButton;

    private ImageButton Add_f_icon;



    private FollowingAdapter adapterFollowing;


    private RecyclerView mRecyclerViewFollowing;


    private RelativeLayout Followers,Following;

    private TextView BackToMainInbox;
    private ImageView ErrorImage,ErrorImage2;


    private LinearLayout FollowerList,FollowingList;
    private TextView followerText,followingText;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_farm);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);
        if (getIntent()!=null){
            //ToastBack(getIntent().getStringExtra("ID"));
            UID = getIntent().getStringExtra("ID").toString();
        }
        chatRoomId = FirebaseUtils.getChatRoomId(mAuth.getUid().toString(),UID);
        Check_Location_enable();
        mRecyclerViewFollowers = findViewById(R.id.FollowersFarmRecyclerviewView);
        mRecyclerView = findViewById(R.id.FetchFarmImagesRecyclerView_view);
        Farm_name = findViewById(R.id.farm_name_view);
        Farm_category = findViewById(R.id.farm_category_view);
        Farm_rating = findViewById(R.id.farm_rate_view);
        Farm_image = findViewById(R.id.farm_image_view);
        Farm_description = findViewById(R.id.farm_Description_view);
        Farm_contact = findViewById(R.id.farm_phone_view);
        Farm_county = findViewById(R.id.farm_county_view);
        OnAds = findViewById(R.id.OnAds);
        OnMap = findViewById(R.id.OnMaps);
        OnReviews = findViewById(R.id.OnReviews);
        BackMaps = findViewById(R.id.BackMaps);
        User_name =findViewById(R.id.user_name_view);
        ExpandReView = findViewById(R.id.ExpandReView_v);
        ratingBar = findViewById(R.id.RatingBar);
        Add_a_review = findViewById(R.id.Add_a_review);
        AddMyReviews_v= findViewById(R.id.AddMyReviews_v);
        CloseReview = findViewById(R.id.CloseReview);
        mRecyclerView_review = findViewById(R.id.RecyclerViewReviews);
        mRecyclerViewAd = findViewById(R.id.AdRecyclerView_v);
        InputReview = findViewById(R.id.reviewText);
        Btn_submit_review = findViewById(R.id.Btn_submit_review);
        showMore = findViewById(R.id.showMore_v);
        showMore1 = findViewById(R.id.showMore1_v);
        showMore2 = findViewById(R.id.showMore2_v);
        showMore0 = findViewById(R.id.showMore0_v);
        ChatRoom = findViewById(R.id.farm_chat_view);
        ChatLayout = findViewById(R.id.ChatLayout);
        InputMessage = findViewById(R.id.InputMessage2);
        BtnSendMessage = findViewById(R.id.BtnSendMessage2);
        CLoseChatLayout = findViewById(R.id.CloseMessageView);
        ErrorImageReview_v = findViewById(R.id.ErrorImageReview_v);
        sliderView = findViewById(R.id.slider);
        Add_f_icon = findViewById(R.id.Add_f_icon);

        ErrorImageAds_v = findViewById(R.id.ErrorImageAds_v);
        sliderDataArrayList = new ArrayList<>();



        mRecyclerViewFollowing = findViewById(R.id.FollowingFarmRecyclerviewView);
        Followers = findViewById(R.id.FollowersRecyView);
        Following = findViewById(R.id.FollowingsRecyView);
        FollowerList = findViewById(R.id.FollowerListView);
        FollowingList = findViewById(R.id.FollowingListView);
        followerText = findViewById(R.id.followerTextView);
        followingText = findViewById(R.id.followingTextView);


        FollowerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(FollowerList);
                RefreshPage();
                FollowingList.setBackgroundResource(R.color.transparent);
                followerText.setTextColor(Color.parseColor("#ffffff"));
                followingText.setTextColor(Color.parseColor("#06265B"));

                FollowerList.setBackgroundResource(R.drawable.bg_blue_category);
                Followers.setVisibility(View.VISIBLE);
                Following.setVisibility(View.GONE);
            }
        });

        FollowingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(FollowingList);
                RefreshPage();


                FollowerList.setBackgroundResource(R.color.transparent);


                FollowingList.setBackgroundResource(R.drawable.bg_blue_category);


                followerText.setTextColor(Color.parseColor("#06265B"));
                followingText.setTextColor(Color.parseColor("#ffffff"));



                Followers.setVisibility(View.GONE);
                Following.setVisibility(View.VISIBLE);
            }
        });




         followButton = findViewById(R.id.Following);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    toggleFollow(UID);


            }
        });
        CLoseChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatState = 0;
                ChatLayout.setVisibility(View.GONE);
            }
        });



        BtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message = InputMessage.getText().toString().trim();
                if (Message.equals("")) {
                    ToastBack("write a message");
                } else {

                    BtnSendMessage.setEnabled(false);
                    SendMessage();

                }
            }
        });



        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showMoreState == 0){

                    ExpandReView.setVisibility(View.GONE);
                    showMore2.setVisibility(View.GONE);
                    showMore1.setVisibility(View.VISIBLE);
                    showMore0.setText("Show More");
                    showMoreState = 1;
                }else if (showMoreState ==1){
                    ExpandReView.setVisibility(View.VISIBLE);
                    showMore1.setVisibility(View.GONE);
                    showMore2.setVisibility(View.VISIBLE);
                    showMore0.setText("Show less");
                    showMoreState = 0;
                }

            }
        });





        Btn_submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewText = InputReview.getText().toString();
                if (reviewText.isEmpty()){
                    ToastBack("Write something...");
                }else {
                    UploadReview();
                }
            }
        });



        CloseReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_a_review.setVisibility(View.GONE);
                ReviewState = 0;
            }
        });


        AddMyReviews_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(AddMyReviews_v);
                ReviewDialog();
            }
        });




        if (mAuth.getCurrentUser() !=null ){
            updateUI(UID,mAuth.getCurrentUser().getUid());
        }else {

        }






        BackMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });

        LayoutAds = findViewById(R.id.LayoutAds);
        LayoutMaps = findViewById(R.id.LayoutMaps);
        LayoutReviews = findViewById(R.id.LayoutReview);


        OnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandReView.setVisibility(View.GONE);
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(OnMap);
               LayoutAds.setVisibility(View.GONE);
               LayoutMaps.setVisibility(View.VISIBLE);
               LayoutReviews.setVisibility(View.GONE);
            }
        });

        OnAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandReView.setVisibility(View.GONE);
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(OnAds);
                LayoutAds.setVisibility(View.VISIBLE);
                LayoutMaps.setVisibility(View.GONE);
                LayoutReviews.setVisibility(View.GONE);
            }
        });

        OnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandReView.setVisibility(View.GONE);
                YoYo.with(Techniques.Pulse)
                        .duration(200)
                        .repeat(1)
                        .playOn(OnReviews);
                LayoutAds.setVisibility(View.GONE);
                LayoutMaps.setVisibility(View.GONE);
                LayoutReviews.setVisibility(View.VISIBLE);
            }
        });




        if (mAuth.getCurrentUser() != null){
            LoadMainUserDetails(mAuth.getCurrentUser().getUid());
            updateUI(UID,mAuth.getCurrentUser().getUid());

        }

        FetchFollowing();
        FetchFollowers();
        FetchUploadedImages();

        LoadFarmDetails();
        FetchReviews();
        LoadDeviceToken();
        FetchAds();
        FetchReviewCount();
        FetchAdsCount();

        loadImages();




        ChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ChatState == 0){
                    ChatLayout.setVisibility(View.VISIBLE);
                    ChatState = 1;
                } else if (ChatState == 1) {
                    ChatLayout.setVisibility(View.GONE);
                    ChatState = 0;
                }

            }
        });



    }

    private void RefreshPage(){
        if (mAuth.getCurrentUser() != null) {
           FetchFollowers();
           FetchFollowing();
        }

        FetchReviewCount();
        FetchAdsCount();
    }



    private void FetchFollowing(){
        Query query = DriverDbRef.document(UID).collection("Following")
                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<com.app.driver.Models.Followers> options = new FirestoreRecyclerOptions.Builder<Followers>()
                .setQuery(query, Followers.class)
                .setLifecycleOwner(this)
                .setLifecycleOwner(this)
                .build();
        adapterFollowing = new FollowingAdapter(options);
        mRecyclerViewFollowing.setHasFixedSize(false);
        mRecyclerViewFollowing.addItemDecoration(new ItemDecorator(-20));
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFollowing.setLayoutManager(horizontalLayoutManager);
        mRecyclerViewFollowing.setNestedScrollingEnabled(false);
        mRecyclerViewFollowing.setAdapter(adapterFollowing);




        adapterFollowing.setOnItemClickListener(new FollowingAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                roomID = documentSnapshot.getId();

                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                logout.putExtra("ID",documentSnapshot.getId());
                startActivity(logout);

            }
        });

    }


    private void FetchFollowers(){
        Query query = DriverDbRef.document(UID).collection("Followers")
                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Followers> options = new FirestoreRecyclerOptions.Builder<Followers>()
                .setQuery(query, Followers.class)
                .setLifecycleOwner(this)
                .setLifecycleOwner(this)
                .build();
        adapterFollowing = new FollowingAdapter(options);
        mRecyclerViewFollowers.setHasFixedSize(false);
        mRecyclerViewFollowers.addItemDecoration(new ItemDecorator(-20));
        mRecyclerViewFollowers.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFollowers.setLayoutManager(horizontalLayoutManager);
        mRecyclerViewFollowers.setAdapter(adapterFollowing);
        mRecyclerViewFollowers.setNestedScrollingEnabled(false);
        mRecyclerViewFollowers.setAdapter(adapterFollowing);




        adapterFollowing.setOnItemClickListener(new FollowingAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                roomID = documentSnapshot.getId();

                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                logout.putExtra("ID",documentSnapshot.getId());
                startActivity(logout);

            }
        });

    }


    private void QueryFollower(String tel) {

        DriverDbRef.document(UID).collection("Followers").whereEqualTo("uid", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {

                        } else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//

    }

    private void toggleFollow(String UID) {


        if (followButton.getText().equals("Follow")){

            DocumentReference sdRef1 =  DriverDbRef.document(UID).collection("Followers").document(mAuth.getCurrentUser().getUid());
            HashMap<String, Object> store = new HashMap<>();
            store.put("user", Name2);
            store.put("uid", UID);
            store.put("image", farmImage2);
            store.put("created_at", FieldValue.serverTimestamp());



            DocumentReference sdRef2 =  DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Following").document(UID);
            HashMap<String, Object> stores = new HashMap<>();
            stores.put("user", farmName);
            stores.put("uid", UID);
            stores.put("image", farmImage);
            stores.put("created_at", FieldValue.serverTimestamp());



            DB.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                    // Note: this could be done without a transaction
                    //       by updating the population using FieldValue.increment()
                    transaction.set(sdRef1, store);
                    transaction.set(sdRef2, stores);
                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "Transaction success!");
                    updateUI(UID,mAuth.getCurrentUser().getUid());
                    ToastBack("Followed "+farmName);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Transaction failure.", e);

                }
            });


        }else if (followButton.getText().equals("Following")){
           Unfollow_Alert();
        }



    }



    ///-----LogOut-----//////
    private AlertDialog dialogF;

    public void Unfollow_Alert() {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogF = builder.create();
        dialogF.show();
        builder.setMessage("Are you sure to unfollow..\n");
        builder.setPositiveButton("Yes,Unfollow",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> store = new HashMap<>();
                        store.put("user", null);
                        store.put("uid", null);
                        store.put("image", null);
                        store.put("created_at", FieldValue.serverTimestamp());

                        // Update the user's data in Firestore
                        DriverDbRef.document(UID).collection("Followers").document(mAuth.getCurrentUser().getUid()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (mAuth.getCurrentUser() != null)
                                        {
                                            updateUI(UID,mAuth.getCurrentUser().getUid());
                                            ToastBack("Unfollowed "+farmName);
                                        }
                                    }
                                });

                        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Following").document(UID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (mAuth.getCurrentUser() != null)
                                        {
                                            updateUI(UID,mAuth.getCurrentUser().getUid());
                                            ToastBack("Unfollowed "+farmName);
                                        }
                                    }
                                });

                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogF.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void updateUI(String UID,String uid) {
//        TextView followersCount = findViewById(R.id.followersCount);
//        followersCount.setText(String.valueOf(userProfile.getFollowers().size()));

        DriverDbRef.document(UID).collection("Followers")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            followButton.setText("Follow");
                            Add_f_icon.setVisibility(View.VISIBLE);
                            followButton.setTextColor(Color.parseColor("#06265B"));
                            followButton.setBackgroundResource(R.color.transparent);
                        } else {
                            followButton.setText("Following");
                            Add_f_icon.setVisibility(View.GONE);
                            followButton.setTextColor(Color.parseColor("#ffffff"));
                            followButton.setBackgroundResource(R.drawable.bg_main_category);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//


    }


    //----Image Slider ----//
    private long likess,commentss;
    private void loadImages() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        DriverDbRef.document(UID).collection("Farm_Images").orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // inside the on success method we are running a for loop
                        // and we are getting the data from Firebase Firestore
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            // after we get the data we are passing inside our object class.
                            FarmImage sliderData = documentSnapshot.toObject(FarmImage.class);
                            FarmImage model = new FarmImage();

                            // below line is use for setting our
                            // image url for our modal class.
                            model.setImage(sliderData.getImage());


                            // after that we are adding that
                            // data inside our array list.
                            sliderDataArrayList.add(model);

                            // after adding data to our array list we are passing
                            // that array list inside our adapter class.
                            sliderAdapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);


                            // belows line is for setting adapter
                            // to our slider view
                            sliderView.setSliderAdapter(sliderAdapter);

                            // below line is for setting animation to our slider.
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                            // below line is for setting auto cycle duration.
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                            // below line is for setting
                            // scroll time animation
                            sliderView.setScrollTimeInSec(4);

                            // below line is for setting auto
                            // cycle animation to our slider
                            sliderView.setAutoCycle(true);


                            // below line is use to start
                            // the animation of our slider view.
                            sliderView.startAutoCycle();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we get any error from Firebase we are
                        // displaying a toast message for failure
                        Toast.makeText(getApplicationContext(), "Fail to load slider data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    ///_____ImageSlider end






    private void SendMessage() {
        if (chatRoomId != null) {
            HashMap<String, Object> store = new HashMap<>();
            store.put("content", Message);
            store.put("uid", mAuth.getCurrentUser().getUid());
            store.put("type", "text");
            store.put("created_at", FieldValue.serverTimestamp());


            HashMap<String, Object> stores = new HashMap<>();
            stores.put("from_name", Name2);
            stores.put("from_uid", mAuth.getCurrentUser().getUid());
            stores.put("to_uid", UID);
            stores.put("last_msg", Message);
            stores.put("last_uid", mAuth.getCurrentUser().getUid());
            stores.put("to_name", from_inbox_name);
            stores.put("msg_count", 0);
            stores.put("status", true);
            stores.put("chatroomId", chatRoomId);
            stores.put("userId", Arrays.asList(mAuth.getCurrentUser().getUid(),UID));
            stores.put("created_at", FieldValue.serverTimestamp());


            DocumentReference sdRef2 = FirebaseUtils.getChatRoomRef(chatRoomId).collection("chats").document();
            DocumentReference sdRef1 = FirebaseUtils.getChatRoomRef(chatRoomId);


            DB.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                    // Note: this could be done without a transaction
                    //       by updating the population using FieldValue.increment()
                    transaction.set(sdRef1, stores);
                    transaction.set(sdRef2, store);
                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "Transaction success!");
                    BtnSendMessage.setEnabled(true);

                    String Title = from_inbox_name;
                    String Msg = Message;
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender( deviceToken, Title, Msg,
                            getApplicationContext(),ViewFarmActivity.this);
                    notificationsSender.SendNotifications();
                    InputMessage.setText("");
                    startActivity(new Intent(getApplicationContext(),InboxActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Transaction failure.", e);
                    ToastBack("Error." + e.getMessage());
                    BtnSendMessage.setEnabled(true);
                }
            });


        }


    }



    private void CreateInbox(){
        FirebaseUtils.getChatRoomRef(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    messagesList = task.getResult().toObject(Messages.class);

                    if (messagesList == null){
                        UploadInbox();
                    }else {
                        startActivity(new Intent(getApplicationContext(),InboxActivity.class));
                    }
                }
            }
        });
    }

    private void UploadInbox() {
        HashMap<String, Object> store = new HashMap<>();
        store.put("from_name", Name2);
        store.put("from_uid", mAuth.getCurrentUser().getUid());
        store.put("to_uid", UID);
        store.put("last_msg", "");
        store.put("to_name", from_inbox_name);
        store.put("msg_count", 0);
        store.put("status", false);
        store.put("chatroomId", chatRoomId);
        store.put("userId", Arrays.asList(mAuth.getCurrentUser().getUid(),UID));
        store.put("created_at", FieldValue.serverTimestamp());

        FirebaseUtils.getChatRoomRef(chatRoomId).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),InboxActivity.class));
                }
            }
        });


    }


    private void FetchAds() {

        Query query = DriverDbRef.document(UID).collection("Ads")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Ads> options = new FirestoreRecyclerOptions.Builder<Ads>()
                .setQuery(query, Ads.class)
                .setLifecycleOwner(this)
                .build();
        adapterAds = new AdsAdapter(options);

        mRecyclerViewAd.setHasFixedSize(true);
        mRecyclerViewAd.setNestedScrollingEnabled(false);
        mRecyclerViewAd.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        mRecyclerViewAd.setAdapter(adapterAds);

        adapterAds.setOnItemClickListener(new AdsAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
//                ImageMain = farmImage1.getImage();
//                ImageView(ImageMain);
            }
        });

    }


    private void FetchReviews(){
        Query query = DriverDbRef.document(UID).collection("Reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Reviews> options = new FirestoreRecyclerOptions.Builder<Reviews>()
                .setQuery(query, Reviews.class)
                .setLifecycleOwner(this)
                .build();
        adapter_review = new ReviewsFarmAdapter(options);

        mRecyclerView_review.setHasFixedSize(true);
        mRecyclerView_review.setNestedScrollingEnabled(false);
        mRecyclerView_review.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView_review.setAdapter(adapter_review);

        adapter_review.setOnItemClickListener(new ReviewsFarmAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
                ImageMain = farmImage1.getImage();
               // ImageView(ImageMain);
            }
        });

    }


    //------Count Category---//
    ArrayList<Object> uniqueDatesAds3 = new ArrayList<Object>();
    int sumAds3 = 0;

    private void FetchAdsCount() {

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Ads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            uniqueDatesAds3.clear();
                            sumAds3 = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uniqueDatesAds3.add(document.getData());
                                for (sumAds3 = 0; sumAds3 < uniqueDatesAds3.size(); sumAds3++) {

                                }
                            }
                            if (sumAds3 > 0) {

                                ErrorImageAds_v.setVisibility(View.GONE);
                            } else {

                                ErrorImageAds_v.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                });

    }
    //==end





    //------Count Category---//
    ArrayList<Object> uniqueDatesReview3 = new ArrayList<Object>();
    int sumReview3 = 0;

    private void FetchReviewCount() {

        DriverDbRef.document(UID).collection("Reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            uniqueDatesReview3.clear();
                            sumReview3 = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uniqueDatesReview3.add(document.getData());
                                for (sumReview3 = 0; sumReview3 < uniqueDatesReview3.size(); sumReview3++) {

                                }
                            }
                            if (sumReview3 > 0) {

                                ErrorImageReview_v.setVisibility(View.GONE);
                            } else {

                                ErrorImageReview_v.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                });

    }
    //==end



    ///-----LogOut-----//////
    private AlertDialog dialogReview;

    public void Review_Update_Alert() {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogReview = builder.create();
        dialogReview.show();
        builder.setMessage("You already submitted a review.\n Do you want to update your review..");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UploadReview2();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dialogReview1 != null)dialogReview1.dismiss();
                if (dialogReview != null)dialogReview.dismiss();
                InputReview.setText("");
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    //===end


    private String adName,adDesc,adPrice,adType;
    private float totalRating = 0;
    private int numberOfRatings = 0;
    private void UploadReview() {

        HashMap<String, Object> store = new HashMap<>();
        store.put("user", Name2);
        store.put("rating", RatingReview);
        store.put("farmName", farm_Name2);
        store.put("comment", reviewText);
        store.put("reviewId", mAuth.getCurrentUser().getUid()+UID+farm_Name2);
        store.put("likes", 0);
        store.put("timestamp", FieldValue.serverTimestamp());

        DocumentReference sdRef1 = DriverDbRef.document(UID).collection("Reviews").document(mAuth.getCurrentUser().getUid()+UID+farm_Name2);


        DB.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                transaction.set(sdRef1, store);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Transaction success!");
                UploadRating();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Transaction failure.", e);
                ToastBack("Error." + e.getMessage());

            }
        });


    }



    private void UploadReview2() {

        HashMap<String, Object> store = new HashMap<>();
        store.put("user", Name2);
        store.put("rating", RatingReview);
        store.put("farmName", farm_Name2);
        store.put("comment", reviewText);
        store.put("reviewId", mAuth.getCurrentUser().getUid()+UID+farm_Name2);
        store.put("likes", 0);
        DocumentReference sdRef1 = DriverDbRef.document(UID).collection("Reviews").document(mAuth.getCurrentUser().getUid()+UID+farm_Name2);


        DB.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                transaction.update(sdRef1, store);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Transaction success!");
                UploadRating();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Transaction failure.", e);
                ToastBack("Error." + e.getMessage());

            }
        });


    }


    private void UploadRating() {

        HashMap<String, Object> store = new HashMap<>();
        store.put("rating", Integer.valueOf(df.format(averageRating)));
        DriverDbRef.document(UID).update(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            InputReview.setText("");
                            String Title = RatingReview+" Star Rating & review";
                            String Msg = "You've been awarded a "+df.format(averageRating)+" Star Rating & review";
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender( deviceToken, Title, Msg,
                                    getApplicationContext(),ViewFarmActivity.this);
                            notificationsSender.SendNotifications();
                            Toast.makeText(getApplicationContext(), " Your Review was submitted..", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    private AlertDialog dialogReview1;
    private TextView CloseDialogReview;


    private float averageRating;

    @SuppressLint("MissingInflatedId")
    private void ReviewDialog()
    {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        mbuilder.setView(mView);



        ratingBar = mView.findViewById(R.id.RatingBar);
        CloseReview = mView.findViewById(R.id.CloseReview);
        mRecyclerView_review = mView.findViewById(R.id.RecyclerViewReviews);
        InputReview = mView.findViewById(R.id.reviewText);
        Btn_submit_review = mView.findViewById(R.id.Btn_submit_review);

        ratingBar.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {

                RatingReview = type.getRating();
                float userRating = type.getRating();
                float  total = TotalUserRating + userRating;

                if (sumReview3 <= 0){
                    averageRating =  total / 1;

                    // Display the average rating

                }else {
                    averageRating =  total / sumReview3;

                    // Display the average rating
                }
                // Calculate the average rating



            }
        });

        Btn_submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewText = InputReview.getText().toString();
                if (reviewText.isEmpty()){
                    ToastBack("Write something...");
                }else {
                   QueryReview(mAuth.getCurrentUser().getUid()+UID+farm_Name2);
                }
            }
        });


        CloseReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogReview1!= null)dialogReview1.dismiss();
            }
        });



        dialogReview1 = mbuilder.create();
        dialogReview1.show();


    }


    private void QueryReview(String tel) {

        DriverDbRef.document(UID).collection("Reviews").whereEqualTo("reviewId", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            UploadReview();
                        } else {
                            Review_Update_Alert();
                            ToastBack("You already submitted a review.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//

    }


    private void QueryForMyShop(String tel) {

        DriverDbRef.whereEqualTo("farmContacts", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {

                        } else {

                            if (mAuth.getCurrentUser() != null){
                                LoadUserDetails(mAuth.getCurrentUser().getUid());

                            }else {

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//

    }


    private AlertDialog dialogQty;
    private ImageView imageView;
    private String ImageMain;
    private TextView CloseDialog;
    private void ImageView(String ImageMain)
    {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_imageview, null);
        mbuilder.setView(mView);

        imageView = mView.findViewById(R.id.ImageMainView);
        CloseDialog = mView.findViewById(R.id.CloseMainImageView);


        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogQty!= null)dialogQty.dismiss();
            }
        });

        Picasso.with(imageView.getContext()).load(ImageMain).fit().into(imageView);





        dialogQty = mbuilder.create();
        dialogQty.show();


    }



    private String farmName,farmCategory,farmContact,farmOffers,farmDesc,farmImage,farmUser,to_uid,to_name;

    private float TotalUserRating;
    private double latf,lngf;
    private void LoadFarmDetails(){
            DriverDbRef.document(UID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e!=null){
                                return;
                            }
                            if (documentSnapshot.exists()) {
                                AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
                                userProfile = documentSnapshot.toObject(Users.class);
                                farmName = scm_user.getFarmName();
                                farmUser = scm_user.getUser();
                                farmCategory = scm_user.getFarmCategory();
                                farmDesc = scm_user.getFarmDescription();
                                String s = scm_user.getFarmName().substring(0, 2);
                                farmImage = scm_user.getImage();
                                TotalUserRating = (float) scm_user.getRating();
                                latf = scm_user.getLat();
                                lngf = scm_user.getLng();



                                //QueryForMyShop(scm_user.getFarmContacts());
                                Farm_name.setText(farmName);
                                Farm_category.setText(farmCategory);
                                Farm_rating.setText(scm_user.getRating()+"");
                                Farm_description.setText(scm_user.getFarmDescription());
                                Farm_contact.setText(scm_user.getFarmContacts());
                                User_name.setText(scm_user.getUser());
                                Farm_county.setText(""+scm_user.getCounty());

                                Picasso.with(Farm_image.getContext()).load(farmImage).fit().into(Farm_image);

                                //CheckPaymentStatus(UID);


                                if (scm_user.isActive() == true){

                                    if (ClentMarker != null) ClentMarker.remove();
                                    farmMarker(latf,lngf,farmName,farmUser);
                                }else {
                                    if (ClentMarker != null) ClentMarker.remove();
                                }

                            }else{
                                ToastBack("EMPTY DOC"+e.getMessage().toString());
                            }
                        }
                    });


    }


    private void farmMarker(double pickupLat, double pickupLng,String farmName,String farmUser) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickupLat, pickupLng), 15.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);
        ClentMarker = mMap.addMarker(new MarkerOptions()
                .title(farmName)
                .snippet(farmUser)
                .position(new LatLng(pickupLat, pickupLng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.garden)));
    }
    private double lat,lng;


    private String farmName2,farmCategory2,farmContact2,farmOffers2,farmDesc2,farmUser2,farmImage2,from_uid,from_name;

    private void LoadUserDetails(String UID){
        DriverDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
                            farmName2 = scm_user.getFarmName();
                            farmCategory2 = scm_user.getFarmCategory();
                            farmDesc2 = scm_user.getFarmDescription();
                            farmImage2 = scm_user.getImage();
                            String s = scm_user.getFarmName().substring(0, 2);

                        }else{
                            ToastBack("EMPTY DOC"+e.getMessage().toString());
                        }
                    }
                });


    }


    private String Name2,farm_Name2,farm_Contact2,userImage2;

    private void LoadMainUserDetails(String UID){
        UserDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);

                            Name2 = scm_user.getUserName();
                            farm_Contact2 = scm_user.getPhone_NO();
                            farm_Name2 = scm_user.getFarm_name();
                            userImage2 = scm_user.getFarm_image();

                            String s = scm_user.getFarm_name().substring(0, 2);

                        }else{
                            ToastBack("EMPTY DOC"+e.getMessage().toString());
                        }
                    }
                });

    }


    private String deviceToken,from_inbox_uid,from_inbox_name;
    private void LoadDeviceToken(){
        UserDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                            from_uid = scm_user.getUser_ID();
                            from_name = scm_user.getUserName();
                            from_inbox_name = scm_user.getUserName();
                            from_inbox_uid = scm_user.getUser_ID();
                            deviceToken = scm_user.getDevice_token();

                        }else{
                            ToastBack("EMPTY DOC"+e.getMessage().toString());
                        }
                    }
                });


    }

    private void FetchUploadedImages(){

        Query query = DriverDbRef.document(UID).collection("Farm_Images")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FarmImage> options = new FirestoreRecyclerOptions.Builder<FarmImage>()
                .setQuery(query, FarmImage.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new FarmImageAdapter2(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FarmImageAdapter2.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
                ImageMain = farmImage1.getImage();
                ImageView(ImageMain);
            }
        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiCLient();
                mMap.setMyLocationEnabled(false);
                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.mapstyle));
                // Add a marker in Sydney and move the camera
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiCLient();
            mMap.setMyLocationEnabled(false);
            // Add a marker in Sydney and move the camera
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }



    }


    //---Build Api Client---
    protected synchronized void buildGoogleApiCLient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    ////------My Location Function-----///
    private void ClientLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {

            final double lat = lastLocation.getLatitude();
            final double lng = lastLocation.getLongitude();

            final LatLng clientPosition = new LatLng(lastLocation.getLongitude(), lastLocation.getLatitude());



        }


    }


    //.....end My location..
    private Marker ClentMarker;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        if (lastLocation != null) {

           // ClientLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
//        if (ClentMarker != null)ClentMarker.remove();
//        if (ClentMarker != null) {
//            ClentMarker.remove();
//        }
//
//        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        ClentMarker = mMap.addMarker(markerOptions);
//
//        CircleOptions addCircle = new CircleOptions().center(latLng).radius(3).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
//        mCircle = mMap.addCircle(addCircle);

        //move map camera
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }   }

    //---Location Update ---//
    private void LocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        if (locationRequest == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


        } else {
            //your location code here...
        }

    }
    //...end Location update

    //---Check location Update ---
    public void Check_Location_enable() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(ViewFarmActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here

                        if (locationSettingsResponse.getLocationSettingsStates().isGpsPresent()) {


                        } else {

                            Check_Location_enable();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(ViewFarmActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS", "Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS", "checkLocationSettings -> onCanceled");
                    }
                });


    }
    //...end check location..

    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ViewFarmActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildGoogleApiCLient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    private void BackPress(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        BackPress();
    }
}