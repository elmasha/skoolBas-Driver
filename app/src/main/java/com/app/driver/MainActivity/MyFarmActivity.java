package com.app.driver.MainActivity;

import static com.app.driver.Common.AdsRef;
import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.Common.mAuth;
import static com.app.driver.MainActivity.MapsActivity.hasPermissions;

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
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.driver.Adapters.AdsAdapter;
import com.app.driver.Adapters.FarmImageAdapter;
import com.app.driver.Adapters.FollowingAdapter;
import com.app.driver.Adapters.ReviewsFarmAdapter;
import com.app.driver.Adapters.SliderAdapter;
import com.app.driver.ItemDecorator;
import com.app.driver.MainActivity.Uploads.EditFarmActivity;
import com.app.driver.Models.Ads;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.FarmImage;
import com.app.driver.Models.Followers;
import com.app.driver.Models.Reviews;
import com.app.driver.Models.UserAccount;
import com.app.driver.R;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class MyFarmActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

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

    private int mCurrentPage;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout, LayoutPrivacy;
    private TextView get_started, enableLocation, Notify,myFarmVisiblity;

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
    public static String FCM_KEY;

    public static FloatingActionButton OpenDrawer, SearchLayout;
    private FrameLayout frameLayout;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private TextView UploadImage;


    ///----Image storage and compression---////
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;
    private UploadTask uploadTask;

    private Uri imageUri,imageUri2;
    private Button Upload_FarmBtn;
    private RecyclerView mRecyclerView,mRecyclerViewAd, mRecyclerViewFollowers;
    private FarmImageAdapter adapter;
    private AdsAdapter adapterAds;
    private boolean edits = true;
    private String UID;


    private TextView Farm_name, Farm_category, Farm_offers, User_name, Farm_contact, Farm_rating, Farm_description,
            Farm_county, BackMaps,AdsCount,FollowersCount,ReviewCount;

    private LinearLayout OnMap, OnAds, OnReviews, LayoutMaps, LayoutAds, LayoutReviews, ExpandReView,WarningLayout,Add_Ad;
    private Switch ShowMyLocation;

    private LinearLayout showMore,EditFarm,MyFollwers;
    private ImageView showMore1,showMore2;

    private TextView  showMore0_my;


    private RecyclerView mRecyclerView_review;
    private ReviewsFarmAdapter adapter_review;

    private ImageView ErrorImageReview_v,ErrorImageAds_v;


    private SliderAdapter sliderAdapter;
    private ArrayList<FarmImage> sliderDataArrayList;
    private SliderView sliderView;
    private String Doc_Id,Doc_Id2;


    private int showMoreState= 1;


    private FollowingAdapter adapterFollowing;


    private RecyclerView mRecyclerViewFollowing;


    private RelativeLayout Followers,Following;

    private TextView BackToMainInbox;
    private ImageView ErrorImage,ErrorImage2;


    private LinearLayout FollowerList,FollowingList;
    private TextView followerText,followingText;

    private boolean mapLocationState;
    private TextView weather_location,weather_temperature,weather_temp_max,weather_temp_min,weather_humidity,
            weather_pressure,weather_wind,weather_desc,weather_refresh;

    private ImageView weather_iconView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map4);
        mapFragment.getMapAsync(this);
        Check_Location_enable();
        mRecyclerView = findViewById(R.id.FetchFarmImagesRecyclerView_my);
        mRecyclerView_review = findViewById(R.id.ReviewRecyclerView_my);
        mRecyclerViewFollowers = findViewById(R.id.FollowersFarmRecyclerview);
        WarningLayout = findViewById(R.id.WarningLayout);
        Farm_name = findViewById(R.id.farm_name_my);
        Farm_category = findViewById(R.id.farm_category_my);
        Farm_rating = findViewById(R.id.farm_rate_my);
        Farm_description = findViewById(R.id.farm_Description_my);
        Farm_contact = findViewById(R.id.farm_phone_my);
        Farm_county = findViewById(R.id.farm_county_my);
        OnAds = findViewById(R.id.OnAds_my);
        OnMap = findViewById(R.id.OnMaps_my);
        OnReviews = findViewById(R.id.OnReviews_my);
        BackMaps = findViewById(R.id.BackMaps_my);
        User_name = findViewById(R.id.user_name_my);
        ShowMyLocation = findViewById(R.id.ShowMyLocation);
        ExpandReView = findViewById(R.id.ExpandReView_my);
        showMore = findViewById(R.id.showMore);
        showMore1 = findViewById(R.id.showMore1);
        showMore2 = findViewById(R.id.showMore2);
        Add_Ad = findViewById(R.id.Add_ads);
        showMore0_my = findViewById(R.id.showMore0_my);
        ErrorImageReview_v = findViewById(R.id.ErrorImageReview_my);
        ErrorImageAds_v = findViewById(R.id.ErrorImageAds_my);

        mRecyclerViewFollowing = findViewById(R.id.FollowingFarmRecyclerview);
        Followers = findViewById(R.id.FollowersRecy);
        Following = findViewById(R.id.FollowingsRecy);
        FollowerList = findViewById(R.id.FollowerList);
        FollowingList = findViewById(R.id.FollowingList);
        followerText = findViewById(R.id.followerText);
        followingText = findViewById(R.id.followingText);


        myFarmVisiblity = findViewById(R.id.myFarmVisiblity1);

        FollowersCount = findViewById(R.id.farm_followersCount_my);
        AdsCount = findViewById(R.id.farm_adsCount_my);
        UploadImage = findViewById(R.id.Add_imageFarm);
        ReviewCount = findViewById(R.id.farm_reviewsCount_my);
        EditFarm = findViewById(R.id.EditFarm);
        MyFollwers = findViewById(R.id.my_profile_followers);




        weather_location = findViewById(R.id.weather_locationMy);
        weather_temperature = findViewById(R.id.weather_temperatureMy);
        weather_temp_max = findViewById(R.id.weather_temp_maxMy);
        weather_temp_min = findViewById(R.id.weather_temp_minMy);
        weather_iconView= findViewById(R.id.weather_iconViewMy);
        weather_humidity = findViewById(R.id.weather_humidityMy);
        weather_pressure = findViewById(R.id.weather_pressureMy);
        weather_wind = findViewById(R.id.weather_windMy);
        weather_desc = findViewById(R.id.weather_descMy);
        weather_refresh = findViewById(R.id.weather_refreshMy);



        weather_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastBack("Refreshing weather.");
                if (mAuth.getCurrentUser() != null){
                    LoadFarmDetails(mAuth.getCurrentUser().getUid());
                }
            }
        });


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


        MyFollwers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FollowListActivity.class));
            }
        });

        EditFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditFarmActivity.class));
            }
        });






        sliderView = findViewById(R.id.slider2);

        sliderDataArrayList = new ArrayList<>();

        Add_Ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAdsDialog();
            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showMoreState == 0){
                    ExpandReView.setVisibility(View.GONE);
                    showMore2.setVisibility(View.GONE);
                    showMore1.setVisibility(View.VISIBLE);
                    showMore0_my.setText("Show More");
                    showMoreState = 1;
                }else if (showMoreState ==1){
                    ExpandReView.setVisibility(View.VISIBLE);
                    showMore1.setVisibility(View.GONE);
                    showMore2.setVisibility(View.VISIBLE);
                    showMore0_my.setText("Show less");
                    showMoreState = 0;
                }

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ShowMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShowMyLocation.isChecked()) {
                    String msg = "Are you sure you want to make your farm VISIBLE";
                    Location_Alert(mapLocationState,msg);

                }else if (!ShowMyLocation.isChecked()) {
                    String msg = "Are you sure you want to make your farm INVISIBLE";
                    Location_Alert(mapLocationState,msg);
                }
            }
        });
        ShowMyLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mapLocationState = b;
            }
        });


        BackMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });

        LayoutAds = findViewById(R.id.LayoutAds_my);
        LayoutMaps = findViewById(R.id.LayoutMaps_my);
        LayoutReviews = findViewById(R.id.LayoutReview_my);
        mRecyclerViewAd = findViewById(R.id.AdRecyclerView);


        OnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ExpandReView.setVisibility(View.GONE);
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
                //ExpandReView.setVisibility(View.GONE);
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

        if (mAuth.getCurrentUser() != null) {
            LoadFarmDetails(mAuth.getCurrentUser().getUid());
           //LoadFarmDetails2(mAuth.getCurrentUser().getUid());
            FetchUploadedImages(mAuth.getCurrentUser().getUid());
            FetchFollowers(mAuth.getCurrentUser().getUid());
            FetchReviews(mAuth.getCurrentUser().getUid());
            FetchAds(mAuth.getCurrentUser().getUid());
            FetchReviews(mAuth.getCurrentUser().getUid());
            loadImages(mAuth.getCurrentUser().getUid());
            FetchFollowing(mAuth.getCurrentUser().getUid());
            LoadMainUserDetails(mAuth.getCurrentUser().getUid());
        }

        newtime();
        FetchReviewCount();
        FetchAdsCount();
        FetchFollowersCount();


        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getApplicationContext())) {
                }
                UploadState =0;
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(MyFarmActivity.this);
            }
        });

    }



    private void LoadWeather(String farmCounty){
        String CITY = farmCounty;
        String ApiKey = "9876ceafcaf56f270df75c649edce67f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + ApiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONObject sys = response.getJSONObject("sys");
                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    String cast = weather.getString("description");
                    String icon = weather.getString("icon");
                    String name = response.getString("name");
                    String country = sys.getString("country");
                    JSONObject wind = response.getJSONObject("wind");
                    String speed = wind.getString("speed");



                    String icon_ulr = "http://openweathermap.org/img/wn/"+icon+"@2x.png";
                    Picasso.with(weather_iconView.getContext()).load(icon_ulr).into(weather_iconView);


                    String  temperature = jsonObject.getString("temp");
                    String  tem_max = jsonObject.getString("temp_max");
                    String  tem_min = jsonObject.getString("temp_min");
                    String  pressure = jsonObject.getString("pressure");
                    String  humidity = jsonObject.getString("humidity");

                    Double temp = Double.parseDouble(temperature);
                    Double tempMax = Double.parseDouble(tem_max);
                    Double tempMin = Double.parseDouble(tem_min);

                    weather_location.setText(name+","+country);
                    weather_temperature.setText(Math.round(temp)+"°C");
                    weather_temp_max.setText(Math.round(tempMax)+"°C");
                    weather_temp_min.setText(Math.round(tempMin)+"°C");
                    weather_humidity.setText(humidity+"%");
                    weather_pressure.setText(pressure +" hPa");
                    weather_wind.setText(speed +"m/s");
                    weather_desc.setText(cast);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }




    private void FetchFollowing(String UID){
        Query query = DriverDbRef.document(UID).collection("Following")
                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Followers> options = new FirestoreRecyclerOptions.Builder<Followers>()
                .setQuery(query, Followers.class)
                .setLifecycleOwner(this)
                .setLifecycleOwner(this)
                .build();
        adapterFollowing = new FollowingAdapter(options);
        mRecyclerViewFollowing.setHasFixedSize(false);
        mRecyclerViewFollowing.addItemDecoration(new ItemDecorator(-24));
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


    private void FetchFollowers(String UID){
        Query query = DriverDbRef.document(UID).collection("Followers")
                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Followers> options = new FirestoreRecyclerOptions.Builder<Followers>()
                .setQuery(query, Followers.class)
                .setLifecycleOwner(this)
                .setLifecycleOwner(this)
                .build();
        adapterFollowing = new FollowingAdapter(options);
        mRecyclerViewFollowers.setHasFixedSize(false);
        mRecyclerViewFollowers.addItemDecoration(new ItemDecorator(-24));
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


    private AlertDialog dialog22;

    public void Location_Alert(boolean b,String msg) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog22 = builder.create();
        dialog22.show();
        builder.setTitle(msg);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UploadDetailsActive(b);
                        UploadDetails();
                        if (b==true){
                            farmMarker();
                        }
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShowMyLocation.toggle();
                dialog22.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }




    private void RefreshPage(){
        if (mAuth.getCurrentUser() != null) {
            LoadFarmDetails(mAuth.getCurrentUser().getUid());
            FetchUploadedImages(mAuth.getCurrentUser().getUid());
            FetchAds(mAuth.getCurrentUser().getUid());
            FetchReviews(mAuth.getCurrentUser().getUid());
            loadImages(mAuth.getCurrentUser().getUid());
        }

        newtime();
        FetchReviewCount();
        FetchAdsCount();
    }

    //----Image Slider ----//
    private long likess,commentss;
    private void loadImages(String UID) {
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



    private void FetchReviews(String UID){
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




    private void newtime() {
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                YoYo.with(Techniques.FadeOut)
                        .duration(200)
                        .repeat(1)
                        .playOn(WarningLayout);
                WarningLayout.setVisibility(View.GONE);
            }
        }.start();
    }


    private int deletePositionAds;
    private String adImageEdit,adTitleEdit,adTypeEdit,adCatEdit,adDescEdit,adPriceEdit;
    private void FetchAds(String UID) {

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



        adapterAds.setOnItemClickListener(new AdsAdapter.OnItemCickListenerDelete() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Ads ads = documentSnapshot.toObject(Ads.class);
                DeleteAds_Alert(position,documentSnapshot.getId());
            }
        });

        adapterAds.setOnItemClickListener(new AdsAdapter.OnItemCickListenerEdit() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Ads ads = documentSnapshot.toObject(Ads.class);
                adImageEdit = ads.getAd_image();
                adTitleEdit = ads.getAd_title();
                adDescEdit = ads.getAd_description();
                adPriceEdit = ads.getAd_price();
                adCatEdit = ads.getAd_category();
                adTypeEdit = ads.getAd_type();
                EditAdsDialog();
            }
        });

    }


    private AlertDialog dialogDeleteAds;

    public void DeleteAds_Alert(int deletePositionAds,String doc_Id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogDeleteAds = builder.create();
        dialogDeleteAds.show();
        builder.setTitle("Are you sure you want to delete this Ad");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AdsRef.document(doc_Id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    adapterAds.deleteItem(deletePositionAds);
                                    if (dialogDeleteAds != null)dialogDeleteAds.dismiss();
                                }else {
                                    ToastBack(task.getException().toString());
                                }
                            }
                        });

                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogDeleteAds.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }



    private AlertDialog dialogAdsEdit;
    private ImageView imageViewAdEdit;
    private String ImageAdEdit,adCategoryEdit;
    private TextView CloseDialogEdit,AdNoImageEdit;
    private FloatingActionButton AddImageEdit;
    private Button Btn_PostEdit;
    private EditText Ad_titleEdit, Ad_descEdit, Ad_priceEdit,Ad_catagoryEdit;

    @SuppressLint("MissingInflatedId")
    private void EditAdsDialog() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_edit_ads, null);
        mbuilder.setView(mView);
        imageViewAdEdit = mView.findViewById(R.id.AdImageViewE);
        AddImageEdit = mView.findViewById(R.id.UploadImageAdE);
        Ad_titleEdit = mView.findViewById(R.id.Ad_titleE);
        Ad_descEdit = mView.findViewById(R.id.Ad_descriptionE);
        Ad_priceEdit = mView.findViewById(R.id.Ad_priceE);
        Ad_catagoryEdit = mView.findViewById(R.id.Ad_categoryE);
        CloseDialogEdit = mView.findViewById(R.id.CloseAdImageViewE);
        Btn_PostEdit = mView.findViewById(R.id.Btn_PostAdE);


        Ad_catagoryEdit.setText(adCatEdit);
        Ad_titleEdit.setText(adTitleEdit);
        Ad_descEdit.setText(adDescEdit);
        Ad_priceEdit.setText(adPriceEdit);

        Btn_PostEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation2()){

                }else {
                    UploadState =1;
                    UpdateAdsDetails();
                }
            }
        });


        AddImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadState =1;
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(MyFarmActivity.this);
            }
        });


        CloseDialogEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogAdsEdit != null) dialogAdsEdit.dismiss();
            }
        });

        Picasso.with(imageViewAdEdit.getContext()).load(adImageEdit).fit().into(imageViewAdEdit);


        dialogAdsEdit = mbuilder.create();
        dialogAdsEdit.show();




    }





    private AlertDialog dialogAds;
    private ImageView imageViewAd;
    private String ImageAd,adCategory;
    private TextView CloseAd,AdNoImage;
    private FloatingActionButton AddImage;
    private Button Btn_Post;
    private EditText Ad_title, Ad_desc, Ad_price,Ad_catagory;

    @SuppressLint("MissingInflatedId")
    private void UploadAdsDialog() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_post_ads, null);
        mbuilder.setView(mView);
        imageView = mView.findViewById(R.id.AdImageView);
        AddImage = mView.findViewById(R.id.UploadImageAd);
        Ad_title = mView.findViewById(R.id.Ad_title);
        Ad_desc = mView.findViewById(R.id.Ad_description);
        Ad_price = mView.findViewById(R.id.Ad_price);
        Ad_catagory = mView.findViewById(R.id.Ad_category);
        CloseDialog = mView.findViewById(R.id.CloseAdImageView);
        Btn_Post = mView.findViewById(R.id.Btn_PostAd);
        AdNoImage = mView.findViewById(R.id.AdNoImage);


        Ad_catagory.setText(farmCategory);
        Btn_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()){

                }else {
                    UploadState =1;
                    UploadAdsDetails();
                }
            }
        });


        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadState =1;
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(MyFarmActivity.this);
            }
        });


        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogAds != null) dialogAds.dismiss();
            }
        });

        Picasso.with(imageView.getContext()).load(ImageMain).fit().into(imageView);


        dialogAds = mbuilder.create();
        dialogAds.show();




    }



    private boolean validation2() {
        adTitleEdit = Ad_titleEdit.getText().toString();
        adDescEdit = Ad_descEdit.getText().toString();
        adPriceEdit = Ad_priceEdit.getText().toString();
        adCategoryEdit = Ad_catagoryEdit.getText().toString();

        if (adTitleEdit.isEmpty()) {
            Ad_titleEdit.setError("Provide Ad title");
            return false;
        }else if (adCategoryEdit.isEmpty()) {
            Ad_catagoryEdit.setError("Provide Ad category");
            return false;
        } else if (adDescEdit.isEmpty()) {
            Ad_descEdit.setError("Provide Ad description");
            return false;
        } else if (adPriceEdit.isEmpty()) {
            Ad_priceEdit.setError("Provide Ad price");
            return false;
        }
        else {

            return true;
        }

    }



    private boolean validation() {
        adName = Ad_title.getText().toString();
        adDesc = Ad_desc.getText().toString();
        adPrice = Ad_price.getText().toString();
        adCategory = Ad_catagory.getText().toString();

        if (adName.isEmpty()) {
            Ad_title.setError("Provide Ad title");
            return false;
        }else if (adCategory.isEmpty()) {
            Ad_catagory.setError("Provide Ad category");
            return false;
        } else if (adDesc.isEmpty()) {
            Ad_desc.setError("Provide Ad description");
            return false;
        } else if (adPrice.isEmpty()) {
            Ad_price.setError("Provide Ad price");
            return false;
        }
        else {

            return true;
        }

    }


    int UploadState =0;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK)
//            switch (requestCode) {
//                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//                    //data.getData returns the content URI for the selected Image
//
//                    imageUri = result.getUri();
//                    imageUri2 = result.getUri();
//
//                    if (imageUri != null) {
//
//                        if (UploadState == 1){
//                            AdNoImage.setVisibility(View.GONE);
//                            imageView.setImageURI(imageUri);
//                            UploadImage();
//                        }else {
//
//                        }
//
//
//                    } else {
//
//                    }
//
//                    if (imageUri2 != null) {
//                        if (UploadState == 0){
//                            UploadImage2(imageUri2);
//
//                        }else {
//
//                        }
//
//                    } else {
//
//                    }
//
//                    break;
//            }
//





    }


    private AlertDialog dialogQty;
    private ImageView imageView;
    private String ImageMain;
    private TextView CloseDialog;

    private void ImageView(String ImageMain) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_imageview, null);
        mbuilder.setView(mView);

        imageView = mView.findViewById(R.id.ImageMainView);
        CloseDialog = mView.findViewById(R.id.CloseMainImageView);


        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogQty != null) dialogQty.dismiss();
            }
        });

        Picasso.with(imageView.getContext()).load(ImageMain).fit().into(imageView);


        dialogQty = mbuilder.create();
        dialogQty.show();


    }


    //------Count Category---//
    ArrayList<Object> uniqueDatesFo3 = new ArrayList<Object>();
    int sumFo3 = 0;

    private void FetchFollowersCount() {

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Followers")
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
                            FollowersCount.setText(sumFo3+"");
                            if (sumFo3 > 0) {
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

                            AdsCount.setText(sumAds3+"");
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

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Reviews")
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
                            ReviewCount.setText(sumReview3+"");
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


    private String farmName, farmCategory, farmContact, farmOffers, farmDesc, farmImage;
    private double load_lat, load_lng;

    private boolean active;

    private void LoadFarmDetails(String UID) {
        DriverDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
                            farmName = scm_user.getFarmName();
                            farmCategory = scm_user.getFarmCategory();
                            farmDesc = scm_user.getFarmDescription();
                            load_lng = scm_user.getLng();
                            load_lat = scm_user.getLat();
                            String s = scm_user.getFarmName().substring(0, 2);
                            farmImage = scm_user.getImage();

                            LoadWeather(scm_user.getCounty());

                            Farm_name.setText(farmName);
                            Farm_category.setText(farmCategory);
                            Farm_rating.setText(scm_user.getRating() + "");
                            Farm_description.setText(scm_user.getFarmDescription());
                            Farm_contact.setText(scm_user.getFarmContacts());
                            User_name.setText(scm_user.getUser());
                            Farm_county.setText(scm_user.getCounty());
                            //Place current location marker
                            //CheckPaymentStatus(UID);


                            if (scm_user.isActive() == true){
                                if (!ShowMyLocation.isChecked()){
                                    ShowMyLocation.toggle();
                                    myFarmVisiblity.setText("Hide your farm location ");
                                }else {

                                }
                                if (ClentMarker != null) ClentMarker.remove();
                                farmMarker();
                            }else {
                                myFarmVisiblity.setText("Make your farm visible ");
                                if (ClentMarker != null) ClentMarker.remove();
                            }

                            if (ShowMyLocation.isChecked() == true){

                            }else {

                            }
                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }

//    private void LoadFarmDetails2(String UID) {
//        FarmDbRef.document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()){
//                            if (documentSnapshot.exists()) {
//                                AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
//                                active = scm_user.isActive();
//
//                                if (scm_user.isActive() == true){
//                                    ShowMyLocation.toggle();
//                                    myFarmVisiblity.setText("Hide your farm location ");
//                                    if (ShowMyLocation.isChecked() == true){
//                                        if (ClentMarker != null) ClentMarker.remove();
//                                        farmMarker();
//                                    }else {
//
//                                    }
//                                }
//                                }else {
//                                    myFarmVisiblity.setText("Make your farm visible ");
//                                    if (ClentMarker != null) ClentMarker.remove();
//                                }
//
//
//
//
//                                //Place current location marker
//                                //CheckPaymentStatus(UID);
//
//                            } else {
//                            }
//
//                    }
//                });
//
//
//
//    }


    private String Name2, farm_Name2, farm_Contact2, farm_UID, userImage;

    private void LoadMainUserDetails(String UID) {
        UserDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                            Name2 = scm_user.getUserName();
                            userImage = scm_user.getFarm_image();
                            farm_Contact2 = scm_user.getPhone_NO();
                            farm_Name2 = scm_user.getFarm_name();
                            farm_UID = scm_user.getUser_ID();
                            String s = scm_user.getFarm_name().substring(0, 2);


                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }


    private void farmMarker() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(load_lat, load_lng), 15.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);
        ClentMarker = mMap.addMarker(new MarkerOptions()
                .title("My Farm")
                .snippet("")
                .position(new LatLng(load_lat, load_lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.garden)));
    }


    private String ImageUrl;

    private void UploadImage() {
        File newimage = new File(imageUri.getPath());

        try {
            Compressor compressor = new Compressor(this);
            compressor.setMaxHeight(200);
            compressor.setMaxWidth(200);
            compressor.setQuality(10);
            compressedImageBitmap = compressor.compressToBitmap(newimage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        final StorageReference ref = storageReference.child("images/thumbs" + UUID.randomUUID().toString());
        uploadTask = ref.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    ImageUrl = downloadUri.toString();

                } else {
                    Toast.makeText(MyFarmActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String ImageUrl2;

    private void UploadImage2(Uri imageUri2) {
        ToastBack("Uploading image");
        File newimage = new File(imageUri2.getPath());

        try {
            Compressor compressor = new Compressor(this);
            compressor.setMaxHeight(200);
            compressor.setMaxWidth(200);
            compressor.setQuality(10);
            compressedImageBitmap = compressor.compressToBitmap(newimage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        final StorageReference ref = storageReference.child("images/thumbs" + UUID.randomUUID().toString());
        uploadTask = ref.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    ImageUrl2 = downloadUri.toString();

                    AddImage(ImageUrl2);


                } else {
                    Toast.makeText(MyFarmActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String ImageUID;
    private void AddImage(String imageUrl){
        HashMap<String, Object> store = new HashMap<>();
        store.put("image_id", imageUrl);
        store.put("image_Uid", mAuth.getCurrentUser().getUid());
        store.put("image", imageUrl);
        store.put("lat",lat);
        store.put("lng",lng);
        store.put("timestamp", FieldValue.serverTimestamp());

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Farm_Images").document(
                ).set(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            RefreshPage();
                            Toast.makeText(getApplicationContext(), "Stored Successfully..", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private String adName,adDesc,adPrice,adType;

    private void UploadAdsDetails() {

        String ID = AdsRef.document().getId();

        HashMap<String, Object> store = new HashMap<>();
        store.put("Ad_title", adName);
        store.put("Ad_description", adDesc);
        store.put("Ad_price", adPrice);
        store.put("Ad_category", adCategory);
        store.put("Ad_type", adType);
        store.put("Ad_id", ID);
        store.put("Ad_image", ImageUrl);
        store.put("user_image", userImage);
        store.put("user_id", mAuth.getCurrentUser().getUid());
        store.put("user_name", Name2);
        store.put("timestamp", FieldValue.serverTimestamp());


        AdsRef.document(ID).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Ads").document(adName+adPrice).set(store)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (dialogAds!=null)dialogAds.dismiss();
                                        Toast.makeText(getApplicationContext(), "Ad uploaded..", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else {
                    ToastBack(task.getException().getMessage().toString());
                }
            }
        });



    }

    private void UpdateAdsDetails() {

        if (ImageUrl == null){
            ToastBack("Uploading current image");
            HashMap<String, Object> store = new HashMap<>();
            store.put("Ad_title", adTitleEdit);
            store.put("Ad_description", adDescEdit);
            store.put("Ad_price", adPriceEdit);
            store.put("Ad_category", adCatEdit);
            store.put("Ad_type", adTypeEdit);
            store.put("Ad_image", adImageEdit);
            store.put("user_image", userImage);
            store.put("user_id", mAuth.getCurrentUser().getUid());
            store.put("user_name", Name2);
            store.put("timestamp", FieldValue.serverTimestamp());

            AdsRef.document(adTitleEdit+adPriceEdit).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Ads").document(adName+adPrice).set(store)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            if (dialogAds!=null)dialogAds.dismiss();
                                            Toast.makeText(getApplicationContext(), "Changes saved..", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }else {
                        ToastBack(task.getException().getMessage().toString());
                    }
                }
            });

        }else{
            HashMap<String, Object> store = new HashMap<>();
            store.put("Ad_title", adTitleEdit);
            store.put("Ad_description", adDescEdit);
            store.put("Ad_price", adPriceEdit);
            store.put("Ad_category", adCatEdit);
            store.put("Ad_type", adTypeEdit);
            store.put("Ad_image", ImageUrl);
            store.put("user_image", userImage);
            store.put("user_id", mAuth.getCurrentUser().getUid());
            store.put("user_name", Name2);
            store.put("timestamp", FieldValue.serverTimestamp());

            AdsRef.document(adTitleEdit+adPriceEdit).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Ads").document(adName+adPrice).set(store)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            if (dialogAds!=null)dialogAds.dismiss();
                                            Toast.makeText(getApplicationContext(), "Ad uploaded..", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }else {
                        ToastBack(task.getException().getMessage().toString());
                    }
                }
            });

        }


    }

    private void UploadDetailsActive(boolean checked) {

                HashMap<String, Object> store = new HashMap<>();
                store.put("active", checked);
                DriverDbRef.document(mAuth.getCurrentUser().getUid()).update(store)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    if (ShowMyLocation.isChecked() == true){
                                        myFarmVisiblity.setText("Make your farm invincible ");
                                    }else {
                                        myFarmVisiblity.setText("Make your farm visible ");
                                        if (ClentMarker != null) ClentMarker.remove();
                                    }
                                   // Toast.makeText(getApplicationContext(), "Location Updated..", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



    }

    private void UploadDetails() {
        if (lastLocation != null) {

                HashMap<String, Object> store = new HashMap<>();
                store.put("lat", lastLocation.getLatitude());
                store.put("lng", lastLocation.getLongitude());
                DriverDbRef.document(mAuth.getCurrentUser().getUid()).update(store)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Location Updated..", Toast.LENGTH_SHORT).show();
                                    if (dialog22!= null)dialog22.dismiss();
                                } else {

                                    Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

        }

    }


    private int deletePosition;
    private void FetchUploadedImages(String UID) {

        Query query = DriverDbRef.document(UID).collection("Farm_Images")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FarmImage> options = new FirestoreRecyclerOptions.Builder<FarmImage>()
                .setQuery(query, FarmImage.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new FarmImageAdapter(options);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);




        adapter.setOnItemClickListener(new FarmImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
                ImageMain = farmImage1.getImage();
                ImageView(ImageMain);
            }
        });



        adapter.setOnItemClickListener(new FarmImageAdapter.OnItemClickListenerDelete() {
            @Override
            public void onItemClickDelete(DocumentSnapshot documentSnapshot, int position) {
                Delete_Alert(position);
            }
        });




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiCLient();
                mMap.setMyLocationEnabled(true);
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
        } else {
            buildGoogleApiCLient();
            mMap.setMyLocationEnabled(true);
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


    private AlertDialog dialogDeleteImage;

    public void Delete_Alert(int position) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogDeleteImage = builder.create();
        dialogDeleteImage.show();
        builder.setTitle("Are you sure to delete this image..");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteItem(position);
                        dialogDeleteImage.dismiss();
                        RefreshPage();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogDeleteImage.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    //.....end My location..
    private Marker ClentMarker;

    private double lat, lng;

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


            //ClientLocation();
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
//        if (ClentMarker != null) ClentMarker.remove();
//        if (ClentMarker != null) {
//            ClentMarker.remove();
//        }

        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        ClentMarker = mMap.addMarker(markerOptions);
//
//        CircleOptions addCircle = new CircleOptions().center(latLng).radius(3).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
//        mCircle = mMap.addCircle(addCircle);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

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

        mSettingsClient = LocationServices.getSettingsClient(MyFarmActivity.this);

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
                                    rae.startResolutionForResult(MyFarmActivity.this, REQUEST_CHECK_SETTINGS);
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
                                ActivityCompat.requestPermissions(MyFarmActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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


    private void BackPress() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        BackPress();
    }
}