package com.app.driver.MainActivity;

import static com.app.driver.Common.CATERef;
import static com.app.driver.Common.CountyDbRef;
import static com.app.driver.Common.DB;
import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.DriverLocationGeoFire;
import static com.app.driver.Common.FarmAvailable;
import static com.app.driver.Common.FarmGeoFire;
import static com.app.driver.Common.FeedBackRef;
import static com.app.driver.Common.RouteDbRef;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.Common.df;
import static com.app.driver.Common.mAuth;
import static com.app.driver.WalkThroughActivity.isConnectionAvailable;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.app.driver.Adapters.CategoryAdapter;
import com.app.driver.Adapters.CountyAdapter;
import com.app.driver.Adapters.CustomInfoWindowAdapter;
import com.app.driver.Adapters.FollowersAdapter;
import com.app.driver.Adapters.MainArticleAdapter;
import com.app.driver.Adapters.MainArticleAdapter2;
import com.app.driver.Adapters.MainViewFarmImageAdapter;
import com.app.driver.MainActivity.Account.LoginActivity;
import com.app.driver.MainActivity.Account.ProfileActivity;
import com.app.driver.MainActivity.Account.RegisterActivity;
import com.app.driver.MainActivity.Uploads.UploadFarmActivity;
import com.app.driver.MainFragments.NotificationFragment;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.AllFarmsSearch;
import com.app.driver.Models.Category;
import com.app.driver.Models.Counties;
import com.app.driver.Models.CountiesSearch;
import com.app.driver.Models.Routes;
import com.app.driver.Models.StopsModel;
import com.app.driver.Models.UserAccount;
import com.app.driver.R;
import com.app.driver.directionhelpers.FetchURL;
import com.app.driver.directionhelpers.TaskLoadedCallback;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, RoutingListener, TaskLoadedCallback {


    public static int UPDATE_INTERVAL = 5000;
    public static int FASTEST_INTERVAL = 5000;
    private String Doc_id_OrderRef;
    public static int DISPLACEMENT = 30;
    public static GoogleApiClient googleApiClient;
    public static Location lastLocation;
    public static LocationRequest locationRequest;
    private String TAG = "TAG";

    private GoogleMap mMap;
    private Geocoder geocoder;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    private ViewPager mSliderViewPager;
    private LinearLayout mDotsLayout;

    private TextView[] mDots;
    TextView privacy;
    private Button Finishbtn;

    private int mCurrentPage;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout, LayoutPrivacy;
    private TextView get_started, enableLocation, OpenDrawer, Notify, Clear_filter;

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

    public static FloatingActionButton SearchLayout;
    private ImageButton filterLayout;
    private FrameLayout frameLayout;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private EditText SearchTextInput;
    private CircleImageView userProfileImage;

    private int SearchState = 0;
    //---Bottom Navigation----
    private MeowBottomNavigation bottomNavigation;
    private int FARM = 1;
    private int EXPLORE = 2;
    private int INBOX = 3;

    private int ROUTES = 4;


    private RecyclerView mRecyclerView, mRecyclerViewCat, mRecyclerViewS;
    private CategoryAdapter adapter1;
    ;
    private MainViewFarmImageAdapter adapter;
    private String SearchCategory = "";
    private EditText SearchText, Search_county;

    private MainArticleAdapter mainArticleAdapter;
    private MainArticleAdapter2 mainArticleAdapter2;

    private CountyAdapter countyAdapter;
    private ArrayList<AllFarmsSearch> list;
    private ArrayList<CountiesSearch> list2;
    private FrameLayout SearchListLayout;

    private Spinner SpinnerFilter;
    private LinearLayout MySearch_algolia, RefreshList_my, RangeFarmLayout;

    PlacesClient placesClient;


    private int radius = 8;
    private int limit = 25;

    private Fragment Places;

    private String LocationSearch;

    FusedLocationProviderClient fusedLocationProviderClient;

    private RelativeLayout GooglePlaceSearch, FilterLayout;

    private Places places;

    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private String spinnerFilter = "";
    private LatLng SearchLatlng = null;
    private float v;
    private Handler handler;
    private int index, next;

    private double lat, lng;

    private LatLng destinationLatLng, pickupLoaction, currentPosition, startPosition, endpostion;

    private List<LatLng> polylineList;
    private Polyline secoPolyline, blackPolyline;
    private PolylineOptions polylineOptions, blackOptions;
    Location myUpdatedLocation = null;
    float Bearing = 0;
    boolean AnimationStatus = false;
    private Marker vendorMarker;

    static Marker carMarker;
    ArrayList markerPoints = new ArrayList();
    Bitmap BitMapMarker;



    private LinearLayout RouteSelectLayout;
    private ImageButton dropDownTotals,dropTopTotals;
    private TextView timeView,distanceView,textRoute_from,textRoute_to,textRoute_stops,textRoute_name,textDep_time,textArrival_time,route_busType;

    private CardView RouteCardView;


    private RecyclerView recyclerViewStops;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map23);
        mapFragment.getMapAsync(this);
        requestPermision();
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bus);
        Bitmap b = bitmapdraw.getBitmap();
        BitMapMarker = Bitmap.createScaledBitmap(b, 50, 150, false);
        if (!hasPermissions(getApplicationContext())) {
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        geocoder = new Geocoder(this);
        Check_Location_enable();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Clear_filter = findViewById(R.id.Clear_filter);
        SpinnerFilter = findViewById(R.id.SpinnerFilter);
        userProfileImage = findViewById(R.id.userProfileImage);
        mRecyclerView = findViewById(R.id.FetchFarmImagesRecyclerView_v);
        mRecyclerViewCat = findViewById(R.id.CategoryRecyclerView_v);
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        mRecyclerViewS = findViewById(R.id.SearchRecyclerView_v);
        SearchText = findViewById(R.id.Search_text);
        filterLayout = findViewById(R.id.tuneSearch);
        mRecyclerViewS.setHasFixedSize(false);
        mRecyclerViewS.setLayoutManager(new LinearLayoutManager(this));
        SearchListLayout = findViewById(R.id.SearchListLayout_maps);
        nv = (NavigationView) findViewById(R.id.navigation_menu2);
        MySearch_algolia = findViewById(R.id.MySearch_algolia);
        RefreshList_my = findViewById(R.id.RefreshList_my);
        RangeFarmLayout = findViewById(R.id.RangeFarmLayout);
        GooglePlaceSearch = findViewById(R.id.GooglePlaceSearch);
        Search_county = findViewById(R.id.Search_county);
        FilterLayout = findViewById(R.id.L877);
        RouteSelectLayout = findViewById(R.id.RouteSelectLayout);
        dropDownTotals = findViewById(R.id.dropDownTotals);
        dropTopTotals = findViewById(R.id.dropTopTotals);
        textRoute_name = findViewById(R.id.route_name);
        textRoute_from = findViewById(R.id.route_from12);
        textRoute_to = findViewById(R.id.route_to);
        textRoute_stops = findViewById(R.id.route_stops);
        textArrival_time = findViewById(R.id.route_arrival_time);
        textDep_time = findViewById(R.id.route_dep_time);
        RouteSelectLayout = findViewById(R.id.RouteSelectLayout);
        dropDownTotals = findViewById(R.id.dropDownTotals);
        dropTopTotals = findViewById(R.id.dropTopTotals);
        route_busType = findViewById(R.id.route_busType);
        recyclerViewStops = findViewById(R.id.recycler_stops);
        timeView = findViewById(R.id.timeView);

        dropDownTotals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropDownTotals.setVisibility(View.GONE);
                dropTopTotals.setVisibility(View.VISIBLE);
                RouteSelectLayout.setVisibility(View.VISIBLE);
            }
        });

        dropTopTotals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                startLatlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                new FetchURL(MapsActivity.this).execute(getUrl(startLatlng, endLoc, "driving"), "driving");

                dropDownTotals.setVisibility(View.VISIBLE);
                dropTopTotals.setVisibility(View.GONE);
                RouteSelectLayout.setVisibility(View.GONE);
            }
        });


        if (!places.isInitialized()) {
            places.initialize(getApplicationContext(), getString(R.string.API_KEY), Locale.US);
        }


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", ");
                LocationSearch = place.getName();
                SearchLatlng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);


                getFarmAround(new LatLng(SearchLatlng.latitude, SearchLatlng.longitude));
                //  getFarmOnMapsAround(new LatLng(SearchLatlng.latitude,SearchLatlng.longitude ));


//                RemoveCLoserVendor();
//                getVendorAround(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
//                getSearchVendor(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
                Toast.makeText(MapsActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RefreshList_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    LoadMainUserDetails(mAuth.getCurrentUser().getUid());
                    FetchInboxCount();
                    //LoadFarmDetails(mAuth.getCurrentUser().getUid());
                }

                RefreshList_my.setVisibility(View.GONE);
                RangeFarmLayout.setVisibility(View.VISIBLE);
                SearchCounty = "";
                SearchCategory = "";
                FetchCategory();


            }
        });


        bottomNavigation.add(new MeowBottomNavigation.Model(FARM, R.drawable.person_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(EXPLORE, R.drawable.route));
        bottomNavigation.add(new MeowBottomNavigation.Model(INBOX, R.drawable.notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(ROUTES, R.drawable.menu));

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        if (mAuth.getCurrentUser() != null) {
                            QueryForMyShop(farm_Name2, farm_UID);
                        } else {
                            Account_Alert();
                        }
                        break;
                    case 2:
                        if (mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                        } else {
                            Account_Alert();
                        }

                        break;
                    case 3:
                        if (mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                        } else {
                            Account_Alert();
                        }

                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                                new NotificationFragment()).commit();
                        break;
                    default:
                        ;
                }
            }
        });


        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        if (mAuth.getCurrentUser() != null) {
                            QueryForMyShop(farm_Name2, farm_UID);
                        } else {
                            Account_Alert();
                        }
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                        break;
                    case 3:
                        if (mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                        } else {
                            Account_Alert();
                        }

                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                                new NotificationFragment()).commit();
                        break;
                    default:
                        ;
                }
            }
        });


        bottomNavigation.show(EXPLORE, true);

        OpenDrawer = findViewById(R.id.OpenDrawer);
        dl = (DrawerLayout) findViewById(R.id.drawer);
        nv = (NavigationView) findViewById(R.id.navigation_menu2);
        Notify = findViewById(R.id.Notify);


        SpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerFilter = SpinnerFilter.getSelectedItem().toString().toString();
                ToastBack(i + "");
                if (spinnerFilter == "") {

                }
                if (i == 0) {
                    GooglePlaceSearch.setVisibility(View.GONE);
                    SearchText.setVisibility(View.GONE);
                    Search_county.setVisibility(View.VISIBLE);
                    if (SearchState == 0) {
                        Clear_filter.setVisibility(View.VISIBLE);
                        SpinnerFilter.setVisibility(View.VISIBLE);
                        SearchListLayout.setVisibility(View.GONE);
                        SearchState = 1;
                    }
                } else if (i == 1) {
                    GooglePlaceSearch.setVisibility(View.GONE);
                    SearchText.setVisibility(View.VISIBLE);
                    Search_county.setVisibility(View.GONE);
                } else if (i == 2) {
                    GooglePlaceSearch.setVisibility(View.VISIBLE);
                    SearchText.setVisibility(View.GONE);
                    Search_county.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SearchState == 0) {
                    Clear_filter.setVisibility(View.VISIBLE);
                    SpinnerFilter.setVisibility(View.VISIBLE);
                    SearchListLayout.setVisibility(View.GONE);
                    FilterLayout.setVisibility(View.VISIBLE);
                    SearchState = 1;
                } else if (SearchState == 1) {
                    Clear_filter.setVisibility(View.GONE);
                    FilterLayout.setVisibility(View.GONE);
                    SpinnerFilter.setVisibility(View.GONE);
                    SearchListLayout.setVisibility(View.GONE);
                    filterLayout.setBackgroundResource(R.drawable.ic_tunes_24);
                    SearchState = 0;
                }

            }
        });

        Clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpinnerFilter.setSelection(0);
                Search_county.setText("");
                SearchText.setText("");
                if (SearchState == 1) {
                    FilterLayout.setVisibility(View.GONE);
                    Clear_filter.setVisibility(View.GONE);
                    SpinnerFilter.setVisibility(View.GONE);
                    SearchListLayout.setVisibility(View.GONE);
                    filterLayout.setBackgroundResource(R.drawable.ic_tunes_24);
                    SearchState = 0;
                }
                if (mAuth.getCurrentUser() != null) {
                    LoadMainUserDetails(mAuth.getCurrentUser().getUid());
                    FetchInboxCount();
                    //LoadFarmDetails(mAuth.getCurrentUser().getUid());
                } else {

                }
            }
        });
        Notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        OpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    FetchInboxCount();
                }
                if (!dl.isDrawerVisible(GravityCompat.START)) {
                    dl.openDrawer(GravityCompat.START);
                } else if (dl.isDrawerVisible(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                }
            }
        });


        //FetchCartWH();
//        FetchCategory();
//        CheckUserAuthentication();
//
//        if (mAuth.getCurrentUser() != null) {
//            FetchFarmsAround(mAuth.getCurrentUser().getUid());
//            LoadMainUserDetails(mAuth.getCurrentUser().getUid());
//            FetchInboxCount();
//            //LoadFarmDetails(mAuth.getCurrentUser().getUid());
//        }else {
//            FecthAllFarm();
//        }


        // FetchFarmsForSearch();

        clientS = new Client("202QHE77ZG", "8eb0d05e4130d35b4c61ed544c1ca1c4");

        indexS = clientS.getIndex("i1-2Farm_Farms");

        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (i2 == 0) {
                    SearchListLayout.setVisibility(View.GONE);
                    MySearch_algolia.setVisibility(View.GONE);
//                    closeSearch.setVisibility(View.GONE);
                } else {
                    SearchListLayout.setVisibility(View.VISIBLE);
                    MySearch_algolia.setVisibility(View.VISIBLE);
//                    closeSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isConnectionAvailable(getApplicationContext())) {
                    ToastBack("No internet connection");
                } else {

                    if (editable.equals("")) {
                        SearchListLayout.setVisibility(View.GONE);
                        MySearch_algolia.setVisibility(View.GONE);
                    } else {
                        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(editable.toString())
                                .setAttributesToRetrieve("Farm_name")
                                .setHitsPerPage(10);
                        indexS.searchAsync(query, new CompletionHandler() {
                            @Override
                            public void requestCompleted(JSONObject content, AlgoliaException error) {
                                try {
                                    list.clear();
                                    JSONArray hits = content.getJSONArray("hits");
                                    for (int i = 0; i < hits.length(); i++) {
                                        JSONObject jsonObject = hits.getJSONObject(i);
                                        String productName = jsonObject.getString("Farm_name");
                                        list.add(new AllFarmsSearch("id", productName));

                                    }
                                    mainArticleAdapter = new MainArticleAdapter(getApplicationContext(), list);
                                    mainArticleAdapter.notifyDataSetChanged();
                                    mainArticleAdapter.OnRecyclerViewItemClickListener(new MainArticleAdapter.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onRecyclerViewItemClicked(int position, View view) {
                                            switch (view.getId()) {
                                                case R.id.searchLayout:
                                                    AllFarmsSearch article = (AllFarmsSearch) view.getTag();
                                                    if (!TextUtils.isEmpty(article.getId())) {
                                                        LoadShopData(article.getFarm_name());
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                    mRecyclerViewS.setAdapter(mainArticleAdapter);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ToastBack("1" + e.getMessage().toString());
                                }
                            }
                        });
                    }
                }


            }
        });


        Client client2 = new Client("EYQIHTGXGI", "6cb845f25faf9981533f975281bbfd62");

        Index index2 = client2.getIndex("counties");

        Search_county.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (i2 == 0) {
                    SearchListLayout.setVisibility(View.GONE);
                    MySearch_algolia.setVisibility(View.GONE);
//                    closeSearch.setVisibility(View.GONE);
                } else {
                    SearchListLayout.setVisibility(View.VISIBLE);
                    MySearch_algolia.setVisibility(View.VISIBLE);
//                    closeSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isConnectionAvailable(getApplicationContext())) {
                    ToastBack("No internet connection");
                } else {

                    if (editable.equals("")) {
                        SearchListLayout.setVisibility(View.GONE);
                        MySearch_algolia.setVisibility(View.GONE);
                    } else {
                        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(editable.toString())
                                .setAttributesToRetrieve("category")
                                .setHitsPerPage(10);
                        index2.searchAsync(query, new CompletionHandler() {
                            @Override
                            public void requestCompleted(JSONObject content, AlgoliaException error) {
                                try {
                                    list2.clear();
                                    JSONArray hits = content.getJSONArray("hits");
                                    for (int i = 0; i < hits.length(); i++) {
                                        JSONObject jsonObject = hits.getJSONObject(i);
                                        String productName = jsonObject.getString("category");
                                        list2.add(new CountiesSearch(productName));

                                    }
                                    mainArticleAdapter2 = new MainArticleAdapter2(getApplicationContext(), list2);
                                    mainArticleAdapter2.notifyDataSetChanged();
                                    mainArticleAdapter2.OnRecyclerViewItemClickListener(new MainArticleAdapter2.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onRecyclerViewItemClicked(int position, View view) {

                                            switch (view.getId()) {
                                                case R.id.searchLayout:
                                                    CountiesSearch article = (CountiesSearch) view.getTag();
                                                    if (!TextUtils.isEmpty(article.getCategory())) {
                                                        SearchCounty = article.getCategory();
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                    mRecyclerViewS.setAdapter(mainArticleAdapter2);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ToastBack("1" + e.getMessage().toString());
                                }
                            }
                        });
                    }
                }


            }
        });


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.searchBar_nav:
                        if (dl.isDrawerOpen(GravityCompat.START)) {
                            dl.closeDrawer(GravityCompat.START);
                        }

                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        break;
                    case R.id.myFarm_nav:
                        if (dl.isDrawerOpen(GravityCompat.START)) {
                            dl.closeDrawer(GravityCompat.START);
                        }
                        if (mAuth.getCurrentUser() != null) {
                            QueryForMyShop(farm_Name2, farm_UID);
                        } else {
                            Account_Alert();
                        }

                        break;
                    case R.id.feedback_nav:
                        if (dl.isDrawerOpen(GravityCompat.START)) {
                            dl.closeDrawer(GravityCompat.START);
                        }
                        if (mAuth.getCurrentUser() != null) {
                            Feedback_Alert();
                        } else {
                            Account_Alert();
                        }

                        break;

                    default:
                }

                return true;

            }
        });


        // FetchCartWH();
        LocationstatusCheck();
        LoadRoute();
        FetchFollowers();
        FetchStopsMarker();


    }


    private FollowersAdapter adapterA;
    private String roomID;

    private void FetchFollowers(){
        Query query = RouteDbRef.document("Route (A) Kabiria")
                .collection("Stops");
        FirestoreRecyclerOptions<StopsModel> options = new FirestoreRecyclerOptions.Builder<StopsModel>()
                .setQuery(query, StopsModel.class)
                .setLifecycleOwner(this)
                .build();
        adapterA = new FollowersAdapter(options);

        recyclerViewStops.setHasFixedSize(false);
        recyclerViewStops.setNestedScrollingEnabled(false);
        recyclerViewStops.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false));
        recyclerViewStops.setAdapter(adapterA);

        adapterA.setOnItemClickListener(new FollowersAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                roomID = documentSnapshot.getId();
                StopsModel stopsModel = documentSnapshot.toObject(StopsModel.class);
                dropDownTotals.setVisibility(View.VISIBLE);
                dropTopTotals.setVisibility(View.GONE);
                RouteSelectLayout.setVisibility(View.GONE);
                LatLng stopLt = new LatLng(stopsModel.getStart_location_lat(),stopsModel.getStart_location_lng());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(stopLt.latitude, stopLt.longitude), 14.0f));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f), 300, null);
                ArroundShop = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(stopLt.latitude, stopLt.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_b)));

            }
        });

    }



    private double distanceL;
    private void getTime() {
        distanceL = distance(StartRoutesLatLng.latitude,StartRoutesLatLng.longitude,EndRoutesLatLng.latitude,EndRoutesLatLng.longitude,'K');
//        int hr = time /60;
        double speed = 80;

        double speedPerMinute = speed / 60.0;


        int totalMinutes = (int) (distanceL / speedPerMinute);
        // Calculate time taken (time = distance / speed)

        // Calculate hours and remaining minutes
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;


        if (hours >0){
            timeView.setText(df.format(hours)+"hr"+ minutes+" mins ");
        }else {
            timeView.setText(df.format(minutes)+" mins ");
        }



    }


    private LatLng StartRoutesLatLng;
    private LatLng EndRoutesLatLng;
    private void LoadRoute() {

        //ToastBack(shop);
        RouteDbRef.document("Route (A) Kabiria")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Routes routes = documentSnapshot.toObject(Routes.class);
//                            ToastBack(routes.getRoute_name());
                            StartRoutesLatLng = new LatLng(documentSnapshot.getDouble("start_location_lat"),documentSnapshot.getDouble("start_location_lng"));
                            EndRoutesLatLng =  new LatLng(documentSnapshot.getDouble("end_location_lat"),documentSnapshot.getDouble("end_location_lng"));
                            //distanceAndTime(EndRoutesLatLng);
                            //Findroutes(StartRoutesLatLng,EndRoutesLatLng);
//                            Directions directions = new Directions(getApplicationContext(),MapsActivity.this,StartRoutesLatLng,EndRoutesLatLng,mMap);
//                            directions.Findroutes(StartRoutesLatLng,EndRoutesLatLng);
                            textRoute_name.setText(routes.getRoute_name());
                            textRoute_from.setText("My location");
                            textRoute_to.setText("End of route");
                            textArrival_time.setText(routes.getEstimated_arrival_time());
                            textDep_time.setText(routes.getEstimated_departure_time());
                            route_busType.setText(documentSnapshot.getString("bus_RegNo") +" "+documentSnapshot.getString("bus_type"));
                            //getTime();

                        }else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastBack(e.getMessage().toString());
                    }
                });


    }






    private LatLng StartStopLatLng;
    private LatLng EndStopLatLng;

    private void LoadStops() {

        //ToastBack(shop);
        RouteDbRef.document("Route (A) Kabiria")
                .collection("Stops").document("James")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            StartStopLatLng = new LatLng(documentSnapshot.getDouble("start_location_lat"),documentSnapshot.getDouble("start_location_lng"));
                            EndStopLatLng =  new LatLng(documentSnapshot.getDouble("end_location_lat"),documentSnapshot.getDouble("end_location_lng"));
                          //  Findroutes(StartRoutesLatLng,EndRoutesLatLng);
//                            Stops stops = new Stops(getApplicationContext()
//                                    ,MapsActivity.this,StartStopLatLng,
//                                    new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),mMap,polylinesStop);
//                            stops.Findroutes(StartStopLatLng,new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
                        }else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastBack(e.getMessage().toString());
                    }
                });


    }









    //------Count Category---//
    private Marker ArroundShop;
    private Marker ArroundShop2;
    private LatLng farmLatLng;

    boolean getDriversAroundStarted = false;
    private ArrayList<Marker> Arroundmarkerss = new ArrayList<Marker>();

    private String stopID;
    private void FetchStopsMarker() {
        RouteDbRef.document("Route (A) Kabiria").collection("Stops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Arroundmarkerss.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                stopID = document.getId();
                                StopsModel stopsModel = document.toObject(StopsModel.class);
                                for (Marker markerIt : Arroundmarkerss) {
                                    if (markerIt.getTag().equals(stopID))
                                        return;
                                }
                                ArroundShop = mMap.addMarker(new MarkerOptions()
                                        .title(shop)
                                        .position(new LatLng(stopsModel.getStart_location_lat(),stopsModel.getStart_location_lng()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
                            }
                            ArroundShop.setTag(stopID);



                        } else {

                        }
                    }
                });

    }
    //==end









//    public void Findroutes(LatLng Start, LatLng End)
//    {
//        if(Start==null || End==null) {
//            Toast.makeText(MapsActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//
//            Routing routing = new Routing.Builder()
//                    .travelMode(AbstractRouting.TravelMode.DRIVING)
//                    .withListener(MapsActivity.this)
//                    .alternativeRoutes(true)
//                    .waypoints(Start, End)
//                    .key("AIzaSyA6IhPzFMBBM2eqG9QyjiW85hQvcSub4dM")  //also define your api key here.
//                    .build();
//            routing.execute();
//        }
//    }


    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                LocationstatusCheck();
            }
            locationPermission = true;
            //init google map fragment to show map.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map23);
            mapFragment.getMapAsync(this);

        }
    }
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {



                    // setUserLocationMarker(locationResult.getLastLocation());


            }
        }
    };


    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }



    private String user_name, shop_name, deviceToken, mR;

    private void LoadShopData(String shop) {

        //ToastBack(shop);
        DriverDbRef.whereEqualTo("farmName", shop)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
                                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                                logout.putExtra("ID", documentSnapshot.getId());
                                startActivity(logout);

                            } else {

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastBack(e.getMessage().toString());
                    }
                });


    }


    private String farmName, farmCategory, farmContact, farmOffers, farmDesc, farmImage;

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
                            farmImage = scm_user.getImage();
                            farmContact = scm_user.getFarmContacts();


                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }


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

                            if (userImage != null) {
                                Picasso.with(userProfileImage.getContext()).load(userImage).into(userProfileImage);
                            } else {
                            }

                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }

    private String SearchCounty = "";


    private void FetchCategory() {

        Query query = CATERef.orderBy("category", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .setLifecycleOwner(this)
                .build();
        adapter1 = new CategoryAdapter(options);

        mRecyclerViewCat.setHasFixedSize(false);
        mRecyclerViewCat.setNestedScrollingEnabled(false);
        mRecyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewCat.setAdapter(adapter1);

        adapter1.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Category category = documentSnapshot.toObject(Category.class);
                SearchCategory = category.getCategory();

                if (SearchCategory.equals("All")) {
                    SearchCategory = "";
                    RefreshList_my.setVisibility(View.GONE);
                    RangeFarmLayout.setVisibility(View.VISIBLE);
                    SearchCounty = "";
                    FetchCategory();
                    if (mAuth.getCurrentUser() != null) {
                        LoadMainUserDetails(mAuth.getCurrentUser().getUid());
                        FetchInboxCount();
                        //LoadFarmDetails(mAuth.getCurrentUser().getUid());
                    } else {

                    }
                } else {
                    RefreshList_my.setVisibility(View.VISIBLE);
                    if (mAuth.getCurrentUser() != null) {
                    } else {

                    }
                }

            }
        });

    }

    private void CheckUserAuthentication() {
        if (mAuth.getCurrentUser() != null) {

        } else {
            Account_Alert();
        }

    }


    private void FetchCartWH() {
        CountyDbRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Counties cartList = document.toObject(Counties.class);

                                if (document.getId() != null) {

                                    UploadIndex(cartList.getCounty(), document.getId());

                                } else {

                                }

                            }


                        } else {

                        }
                    }
                });

    }


    private void SaveDisListDetails(String catID, String category) {

        HashMap<String, Object> format = new HashMap<>();
        format.put("category", category);
        format.put("cat_id", catID);

        CATERef.document(category).set(format)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //ToastBack("Category Updated");
                        } else {
                            ToastBack(task.getException().getMessage());

                        }
                    }
                });


    }


    private Client clientS;
    private Index indexS;

    private void UploadIndex(String Name, String UID) {
        clientS = new Client("EYQIHTGXGI", "6cb845f25faf9981533f975281bbfd62");

        indexS = clientS.getIndex("counties");

        Map<String, Object> mapOne = new HashMap<>();
        mapOne.put("category", Name);
        mapOne.put("cat_id", UID);

        List<JSONObject> productList = new ArrayList<>();
        productList.add(new JSONObject(mapOne));

        indexS.addObjectsAsync(new JSONArray(productList), new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                if (e != null) {
                    ToastBack("Error" + e.getMessage().toString());

                } else {
                    ToastBack("Stored");
                }
            }
        });


    }




    private void QueryForMyShop1(String tel) {

        DriverDbRef.whereEqualTo("user_id", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {

                        } else {

                            if (mAuth.getCurrentUser() != null) {
                                LoadFarmDetails(mAuth.getCurrentUser().getUid());
                            } else {

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

    ////Query if Shop exists
    private void QueryForMyShop(String tel, String tel2) {

        DriverDbRef.whereEqualTo("farmName", tel)
                .whereEqualTo("user_id", tel2)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Intent toDis = new Intent(getApplicationContext(), UploadFarmActivity.class);
                            startActivity(toDis);
                        } else {
                            Intent toDis = new Intent(getApplicationContext(), MyFarmActivity.class);
                            startActivity(toDis);
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


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    //    private void erasePolylines(){
//        for(Polyline line : polylines){
//            line.remove();
//        }
//        polylines.clear();
//    }
    private Location myLocation;
    private Marker BusMarker;
    private LatLng endLoc = new LatLng(-1.288672310184358,36.722252429012975);



    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*::  This function converts decimal degrees to radians             :*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*::  This function converts radians to decimal degrees             :*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private String routeDistance;
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation = location;

                getFarmOnMapsAround(new LatLng(location.getLatitude(),location.getLongitude()));
                if (AnimationStatus) {
                    myUpdatedLocation = location;
                } else {
                    myLocation = location;
                    myUpdatedLocation = location;


//                    HashMap<String,Object> track = new HashMap<>();
//                    track.put("lat",myUpdatedLocation.getLatitude());
//                    track.put("lon",myUpdatedLocation.getLongitude());
//                    TrackLocationRef.child("Driver").updateChildren(track)
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    // Data is successfully written
//                                     ToastBack(routeDistance);
//                                } else {
//                                    // Write failed
//                                }
//                            });

//                    routeDistance = String.valueOf(df.format(distance(myLocation.getLatitude(),myLocation.getLongitude(),endLoc.latitude,endLoc.longitude,'K')));
//                    OpenDrawer.setText(routeDistance+"");
                    // ToastBack(routeDistance);

//                    if (Integer.parseInt(routeDistance) == 0){
//                        Toast.makeText(MapsActivity.this, "Bus has arrived", Toast.LENGTH_SHORT).show();
//                    }else if (Integer.parseInt(routeDistance) == 2){
//                        Toast.makeText(MapsActivity.this, "Bus is around the corner", Toast.LENGTH_SHORT).show();
//
//                    }

                 /*  if (Integer.parseInt(String.valueOf(df.format(distance(myLocation.getLatitude(),myLocation.getLongitude(),endLoc.latitude,endLoc.longitude,'K')))) < 2){
                       bottomNavigation.setVisibility(View.GONE);
                   }*/

                    if (carMarker !=null)carMarker.remove();
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    carMarker = mMap.addMarker(new MarkerOptions()
                            .rotation(location.getBearing())
                            .anchor((float)0.5,(float)0.5)
                            .position(latlng)
                            .icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            latlng, 17f);
                    carMarker.setAnchor(0.5F,0.5F);
                    carMarker.setRotation(location.getBearing());
                    mMap.animateCamera(cameraUpdate);

                }
                Bearing = location.getBearing();
                LatLng updatedLatLng = new LatLng(myUpdatedLocation.getLatitude(), myUpdatedLocation.getLongitude());
                changePositionSmoothly(carMarker, updatedLatLng, Bearing);

            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void LocationstatusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final Float bearing) {
        final LatLng startPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        final LatLng finalPosition = newLatLng;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                myMarker.setRotation(bearing);
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                myMarker.setPosition(currentPosition);

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
                myLocation.setLatitude(newLatLng.latitude);
                myLocation.setLongitude(newLatLng.longitude);
            }
        });
    }

    private void rotateMarker(Marker vendorMarker, float i, GoogleMap mMap) {

        Handler handler = new Handler();
        long start = SystemClock.uptimeMillis();
        float startRotation = vendorMarker.getRotation();
        long duration = 1500;

        LinearInterpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                float rot = t * i + (1 - t) * startRotation;
                vendorMarker.setRotation(-rot > 180 ? rot / 2 : rot);

                if (t < 1.0) {

                    handler.postDelayed(this, 16);
                }
            }
        });


    }


    LatLng startLatlng,endLatlng;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiCLient();
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
               /// Driver_Location();

            }
        } else {
            buildGoogleApiCLient();
            mMap.setMyLocationEnabled(true);
            // Add a marker in Sydney and move the camera
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                startLatlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                endLatlng = new LatLng(latLng.latitude,latLng.longitude);
//                Findroutes(startLatlng,endLoc);
               // Driver_Location();
//                ToastBack(mAuth.getCurrentUser().getUid());
                new FetchURL(MapsActivity.this).execute(getUrl(startLatlng, endLatlng, "driving"), "driving");


            }
        });


    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);
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

    //....end Build API.
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }

            }
        }
        return true;
    }


    private AlertDialog dialog212;

    public void Terms_Alert2() {
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog212 = builder.create();
        dialog212.show();
        builder.setIcon(R.drawable.ic_policy_24);
        builder.setTitle("You are currently not logged in,");
        builder.setPositiveButton("Create account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });
        builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });
        builder.show();
    }

    private AlertDialog dialog21;
    private TextView CloseLoginDialog;
    private Button RegisterBtn, LoginBtn;

    @SuppressLint("MissingInflatedId")
    public void Account_Alert() {

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
        mbuilder.setView(mView);
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        CloseLoginDialog = mView.findViewById(R.id.CloseMainLogin);
        RegisterBtn = mView.findViewById(R.id.Btn_to_register);
        LoginBtn = mView.findViewById(R.id.Btn_to_login);


        CloseLoginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog21 != null) dialog21.dismiss();
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        dialog21 = mbuilder.create();
        dialog21.show();

    }

    private AlertDialog dialogViewMap;
    private TextView CloseLoginViewFarmMap;
    private TextView Name, desc, rating, toFarm;
    private String farmViewName, farmViewDesc, farmViewRating, farmViewImage;

    private ImageView image;

    @SuppressLint("MissingInflatedId")
    public void Account_ViewFarm() {

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_farm_map, null);
        mbuilder.setView(mView);
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        CloseLoginViewFarmMap = mView.findViewById(R.id.CloseFarmView);
        RegisterBtn = mView.findViewById(R.id.Btn_to_register);
        LoginBtn = mView.findViewById(R.id.Btn_to_login);
        image = mView.findViewById(R.id.farm_map_image_v);
        Name = mView.findViewById(R.id.farm_map_name_v);
        desc = mView.findViewById(R.id.farm_map_category_v);
        rating = mView.findViewById(R.id.farm_map_rate_v);
        toFarm = mView.findViewById(R.id.ViewFarm);


        Name.setText(farmViewName);
        desc.setText(farmViewDesc);
        rating.setText(farmViewRating);
        Picasso.with(image.getContext()).load(farmViewImage).fit().into(image);
        CloseLoginViewFarmMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogViewMap != null) dialogViewMap.dismiss();
            }
        });

        toFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                logout.putExtra("ID", FarmAround);
                startActivity(logout);

            }
        });


        dialogViewMap = mbuilder.create();
        dialogViewMap.show();

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


    ///----Measure Distance -----////
    ///---Distance----

    private int distMeters, distances;

    private void distanceAndTime() {

        if (lastLocation != null) {


            Location startPoint = new Location("locationA");
            startPoint.setLatitude(lastLocation.getLatitude());
            startPoint.setLongitude(lastLocation.getLongitude());

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(farmLatLng.latitude);
            endPoint.setLongitude(farmLatLng.longitude);

            double distance = startPoint.distanceTo(endPoint);

            int speed = 60;

            double iKm = (distance / 1000);


            DecimalFormat df = new DecimalFormat("#.##");
            iKm = Double.valueOf(df.format(iKm));

            ////---distance --//
            distMeters = (int) (iKm * 1000);
            if (distMeters > 1000) {

                distMeters = (int) (iKm / 1000);
                //distances.setText(distMeters + " km away");

            } else if (distMeters < 1000) {
                distMeters = (int) (iKm * 1000);
                ///distances.setText(distMeters + " meters away");

            }

            //--time--//
            int time = (int) (distance / speed);
            if (time <= 1) {
//                textTime.setText(" less than a min");
            } else if (time >= 60) {
//                textTime.setText(time+" hrs");
            } else if (time > 1) {
//                textTime.setText(time+" mins");
            }


//            clientMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(getlat,getlng)).title("Customer drop point")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationhome)));


        }

    }

    //.....end My location..
    private Marker ClentMarker;


    private void FetchFarmsForSearch() {
        DriverDbRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                AllFarms cartList = document.toObject(AllFarms.class);

                                if (document.getId() != null) {


                                    HashMap<String, Object> registerB = new HashMap<>();
                                    registerB.put("Farm_name", cartList.getFarmName());
                                    registerB.put("farm_category", cartList.getFarmCategory());
                                    registerB.put("farm_owner", cartList.getUser());
                                    registerB.put("image", cartList.getImage());
                                    registerB.put("id", document.getId());

                                    SaveWhListDetails(registerB);


                                } else {

                                }

                            }


                        } else {

                        }
                    }
                });

    }

    private void SaveWhListDetails(HashMap<String, Object> mapOne) {

        clientS = new Client("202QHE77ZG", "8eb0d05e4130d35b4c61ed544c1ca1c4");

        indexS = clientS.getIndex("i1-2Farm_Farms");


        List<JSONObject> productList = new ArrayList<>();
        productList.add(new JSONObject(mapOne));

        indexS.addObjectsAsync(new JSONArray(productList), new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                if (e != null) {
                    ToastBack("Error" + e.getMessage().toString());
                    Log.i("elmasha", e.getMessage().toString());
                } else {
                    ToastBack("Stored");
                }
            }
        });


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (googleApiClient != null){
           /// LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }



        if (lastLocation != null) {
            getFarmOnMapsAround(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
           // RemoveCLoserVendor();
            getFarmAround(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
            getVendorAround(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));

        }


//        LocationUpdates();
       /// getMyLocation();
        Farm_Location();
        ///RemoveCLoserVendor();


    }


    ///-----Get shops around-----//////
    private String wholeName;
    String shop;

    private String WholeSalerAroundID = "";


    private void getFarmAround(LatLng latLng) {
        WholeSalerAroundID = "";

        if (latLng != null) {
            GeoQuery geoQuery = FarmGeoFire.queryAtLocation(new
                    GeoPoint(latLng.latitude, latLng.longitude), radius);
            geoQuery.removeAllListeners();

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String s, GeoPoint geoPoint) {

                    WholeSalerAroundID = s;
                    if (WholeSalerAroundID.equals("")) {
                        ToastBack("No Available Farm on  a max of " + limit + "Km radius...Try again later.");
                    } else {
                        GetVendorDetails(WholeSalerAroundID, geoPoint);
                    }


                }

                @Override
                public void onKeyExited(String key) {


                }

                @Override
                public void onKeyMoved(String key, GeoPoint geoPoint) {


                }

                @Override
                public void onGeoQueryReady() {
                    if (radius <= limit) {

                        radius++;
                        getFarmAround(new LatLng(latLng.latitude, latLng.longitude));

                    } else {
                        if (!WholeSalerAroundID.equals("")) {


                        } else {

                            if (radius >= limit) {
                                ToastBack("No Available Farm on  a max 8" + limit + "Km radius...Try again later.");
                            }

                        }


                    }


                }

                @Override
                public void onGeoQueryError(Exception e) {

                    if (radius <= limit) {

                        radius++;

                    } else {


                    }

                    if (WholeSalerAroundID != null) {


                    } else {

                        if (radius >= limit) {
                            ToastBack("Oops! No Available WholeSeller on " + radius + "Km radius...Try again later.");
                        }

                    }
                }
            });
        }


    }
    //...end farm around.


    ///-----VENDORS ARROUND-----//////
    private String shopNo, shopName, VendorSearchID;

    private String FarmAround = "";
    private String FarmAround2 = "";



    private ArrayList<Marker> Arroundmarkers = new ArrayList<Marker>();

    private int clickState = 0;

    private void getFarmOnMapsAround(LatLng latLng) {
        FirebaseFirestore DB = FirebaseFirestore.getInstance();
        CollectionReference FarmAvailable = DB.collection("skoolBas_parents_location");
        GeoFirestore FarmGeoFire =  new GeoFirestore(FarmAvailable);
       final GeoQuery geoQuery = FarmGeoFire.queryAtLocation(new GeoPoint(latLng.latitude, latLng.longitude), 2);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String s, GeoPoint geoPoint) {
                FarmAround = s;

                if (FarmAround != null) {

                    if (mAuth.getCurrentUser() != null) {

                            if (Arroundmarkers != null) Arroundmarkers.clear();
                            for (Marker markerIt : Arroundmarkers) {
                                if (markerIt.getTag().equals(s))
                                    return;
                            }

                            farmLatLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());


                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), 14.0f));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);

                                ArroundShop = mMap.addMarker(new MarkerOptions()
                                        .title(shop)
                                        .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));

                            ArroundShop.setTag(s);

//                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                            @Override
//                            public boolean onMarkerClick(Marker marker) {
//                                FarmAround = marker.getTag().toString();
//
//                                if (FarmAround != null) {
//
//                                    if (lastLocation != null) {
//                                        distanceAndTime();
//                                    }
//                                    if (mAuth.getCurrentUser() != null) {
//                                        if (FarmAround.equals(mAuth.getCurrentUser().getUid())) {
//
//                                        } else {
//
//                                            DriverDbRef.document(FarmAround).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                    if (documentSnapshot.exists()) {
//                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), 14.0f));
//                                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f), 300, null);
//                                                        ArroundShop = mMap.addMarker(new MarkerOptions()
//                                                                .position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
//                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
//
//                                                        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
//                                                        mMap.setInfoWindowAdapter(adapter);
//                                                        AllFarms allFarms = documentSnapshot.toObject(AllFarms.class);
//                                                        farmViewName = allFarms.getFarmName();
//                                                        farmViewDesc = allFarms.getFarmCategory();
//                                                        farmViewRating = String.valueOf(allFarms.getRating());
//                                                        farmViewImage = allFarms.getImage();
//                                                        ArroundShop = mMap.addMarker(new MarkerOptions()
//                                                                .title(farmViewRating + "")
//                                                                .snippet(farmViewImage)
//                                                                .position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
//                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
//                                                        ArroundShop.setTag(farmViewName + "\n" + farmViewDesc);
//                                                        ArroundShop.showInfoWindow();
//                                                        new CountDownTimer(1000, 1000) {
//                                                            public void onTick(long millisUntilFinished) {
//                                                            }
//
//                                                            public void onFinish() {
//                                                                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
//                                                                mMap.setInfoWindowAdapter(adapter);
//                                                                AllFarms allFarms = documentSnapshot.toObject(AllFarms.class);
//                                                                farmViewName = allFarms.getFarmName();
//                                                                farmViewDesc = allFarms.getFarmCategory();
//                                                                farmViewRating = String.valueOf(allFarms.getRating());
//                                                                farmViewImage = allFarms.getImage();
//                                                                ArroundShop = mMap.addMarker(new MarkerOptions()
//                                                                        .title(farmViewRating + "")
//                                                                        .snippet(farmViewImage)
//                                                                        .position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
//                                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
//                                                                ArroundShop.setTag(farmViewName + "\n" + farmViewDesc);
//                                                                ArroundShop.showInfoWindow();
//                                                            }
//                                                        }.start();
//
////                                                        if (ArroundShop.getTag().equals(farmViewName + "\n" +farmViewDesc)){
////                                                            ToastBack("Match");
////                                                        }
//                                                        RangeFarmLayout.setVisibility(View.GONE);
//                                                        RefreshList_my.setVisibility(View.VISIBLE);
//
//                                                    } else {
//
//                                                    }
//                                                }
//                                            });
//
//
////                                        if (farmViewName != null) {
////                                            Account_ViewFarm();
////                                        }
//
//                                        }
//                                    }
//
//                                } else {
//
//                                }
//
//
//                                return false;
//                            }
//                        });
                    }

                }

            }

            @Override
            public void onKeyExited(String key) {

                if (Arroundmarkers != null) Arroundmarkers.clear();
                for (Marker markerIt : Arroundmarkers) {
                    if (markerIt.getTag().equals(key)) {
                        markerIt.remove();
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoPoint geoPoint) {

                if (Arroundmarkers != null) Arroundmarkers.clear();
                for (Marker markerIt : Arroundmarkers) {
                    if (markerIt.getTag().equals(key)) {
                        markerIt.setPosition(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                        //
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {


            }

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });

    }
//    ...end Vendor around.


    private String VendorAroundID;
    private LatLng driverLocation;

    private void getVendorAround(LatLng latLng) {

        VendorAroundID = "";
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        if (latLng != null) {


            CollectionReference FarmAvailable = DB.collection("Farm_location");
            GeoFirestore FarmGeoFireLocation = new GeoFirestore(FarmAvailable);


            GeoQuery geoQuery = FarmGeoFireLocation.queryAtLocation(new
                    GeoPoint(latLng.latitude, latLng.longitude), 6);


            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onKeyEntered(String s, GeoPoint geoPoint) {

                    VendorAroundID = s;


                    driverLocation = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), 14.0f));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);

                    ArroundShop = mMap.addMarker(new MarkerOptions()
                            .title(shop)
                            .position(driverLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
                    ArroundShop.setTag(VendorAroundID);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            VendorAroundID = marker.getTag().toString();

                            if (VendorAroundID != null) {

//                                if (preVendorLocation != null) {
//                                    distanceAndTime();
//                                }
                                if (VendorAroundID.equals(mAuth.getCurrentUser().getUid())) {

                                } else {

                                    Intent tiview = new Intent(getApplicationContext(), ViewFarmActivity.class);
                                    tiview.putExtra("VendorID", VendorAroundID);
                                    startActivity(tiview);

                                }
                            }


                            return false;
                        }
                    });


                }

                @Override
                public void onKeyExited(String key) {


                }

                @Override
                public void onKeyMoved(String key, GeoPoint geoPoint) {


                }

                @Override
                public void onGeoQueryReady() {


                }

                @Override
                public void onGeoQueryError(Exception e) {

                }
            });

        } else if (SearchLatlng != null) {

            CollectionReference FarmAvailable = DB.collection("Farm_location");
            GeoFirestore FarmGeoFireLocation = new GeoFirestore(FarmAvailable);

            GeoQuery geoQuery = FarmGeoFireLocation.queryAtLocation(new
                    GeoPoint(SearchLatlng.latitude, SearchLatlng.longitude), 6);
            geoQuery.removeAllListeners();


            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String s, GeoPoint geoPoint) {

                    VendorAroundID = s;
                    VendorSearchID = s;


                    driverLocation = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                    ToastBack(VendorSearchID);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), 14.0f));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);

                    ArroundShop = mMap.addMarker(new MarkerOptions()
                            .title(shop)
                            .position(driverLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.farm_pin)));
                    ArroundShop.setTag(VendorAroundID);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            VendorAroundID = marker.getTag().toString();

                            if (VendorAroundID != null) {

//                                if (preVendorLocation != null) {
//                                    distanceAndTime();
//                                }
                                if (VendorAroundID.equals(mAuth.getCurrentUser().getUid())) {

                                } else {


                                }
                            }


                            return false;
                        }
                    });

                }

                @Override
                public void onKeyExited(String key) {


                }

                @Override
                public void onKeyMoved(String key, GeoPoint geoPoint) {


                }

                @Override
                public void onGeoQueryReady() {


                }

                @Override
                public void onGeoQueryError(Exception e) {
                    ToastBack(e.getMessage());
                }
            });

        }


    }
    //...end Vendor around.


    //------Count Category---//
    ArrayList<Object> uniqueDatesCat3 = new ArrayList<Object>();
    int sumCat3 = 0;

    private void FetchInboxCount() {

        DB.collection("Inbox")
                .whereEqualTo("last_uid", mAuth.getCurrentUser().getUid())
                .whereArrayContains("userId", mAuth.getCurrentUser().getUid())
                .whereEqualTo("status", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            return;
                        }

                        uniqueDatesCat3.clear();
                        sumCat3 = 0;

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            uniqueDatesCat3.add(documentSnapshot.getData());
                            for (sumCat3 = 0; sumCat3 < uniqueDatesCat3.size(); sumCat3++) {

                            }

                            if (sumCat3 > 0) {

                            } else {
                                bottomNavigation.setCount(INBOX, String.valueOf(sumCat3));
                            }

                        }


                    }
                });

//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                        } else {
//
//                        }
//                    }
//                });

    }
    //==end


    ///-----Get shops details-----//////
    private String WholeSaleFoundId, FingLocation, FarmName, FarmRate, FarmImage, FarmID, FarmCategory;
    private double ratings;

    private void GetVendorDetails(String vendorFoundId, final GeoPoint geoPoint) {

        DriverDbRef.document(vendorFoundId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                if (documentSnapshot.exists()) {

                    if (vendorFoundId != null) {


                        AllFarms vendorUser = documentSnapshot.toObject(AllFarms.class);
                        FarmID = documentSnapshot.getId();
                        FarmName = vendorUser.getFarmName();
                        FarmCategory = vendorUser.getFarmCategory();
                        FarmImage = vendorUser.getImage();
                        ratings = vendorUser.getRating();


                        if (mAuth.getCurrentUser() != null) {
                            if (vendorFoundId.equals(mAuth.getCurrentUser().getUid())) {

                            } else {
                                Map<String, Object> aroundVendor = new HashMap<>();
                                aroundVendor.put("farm_Name", FarmName);
                                aroundVendor.put("farm_id", FarmID);
                                aroundVendor.put("farm_category", FarmCategory);
                                aroundVendor.put("farm_image", FarmImage);
                                aroundVendor.put("farm_rating", ratings);
                                aroundVendor.put("farm_location", vendorUser.getCounty());
                                aroundVendor.put("County", vendorUser.getCounty());
                                aroundVendor.put("timestamp", FieldValue.serverTimestamp());
                                SaveAroundVendorDetails(mAuth.getCurrentUser().getUid(), FarmID, aroundVendor);
                            }
                        } else {


                        }

                    } else {

                        // RemoveVendorDetails(vendorFoundId);
                        Toast.makeText(MapsActivity.this, "Sorry no Vendor available try Again later", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    //   ToastBack("Oops! No Available WholeSeller on "+_distance+"Km radius...Try again later.");

                    //Toast.makeText(MapsActivity.this, "No preferred vendor", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SaveAroundVendorDetails(String uid, String farmID, Map<String, Object> aroundVendor) {

        UserDbRef.document(uid).collection("FarmersAround").document(farmID).set(aroundVendor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(MapsActivity.this, "Searching for nearby farms..", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MapsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //===end


    ///------Remove Close Shops-----////
    private void RemoveCLoserVendor() {

        GeoQuery geoQuery = FarmGeoFire.queryAtLocation(new
                GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()), 30);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String s, final GeoPoint geoPoint) {

                WholeSalerAroundID = s;

                if (WholeSalerAroundID != null) {
                    RemoveVendorDetails(WholeSalerAroundID);
                } else if (WholeSalerAroundID == null) {

                    // Toast.makeText(MapsActivity.this, "Vendor not found Try again later.", Toast.LENGTH_LONG).show();
                    ToastBack("Vendor not found Try again later.");

                }


            }

            @Override
            public void onKeyExited(String s) {

            }

            @Override
            public void onKeyMoved(String s, GeoPoint geoPoint) {

            }

            @Override
            public void onGeoQueryReady() {

                if (radius <= limit) {
                    radius++;
//                    if (lastLocation != null){
//                        RemoveCLoserVendor(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
//
//                    }


                } else {


                }

                if (WholeSalerAroundID != null) {


                } else {

                    if (radius >= limit) {
                        ToastBack("No Vendor around your radius...Try again later.");

                    }

                }
            }

            @Override
            public void onGeoQueryError(Exception e) {


            }
        });

    }


    private void RemoveVendorDetails(String vendorFoundId) {


        DriverDbRef.document(vendorFoundId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                if (documentSnapshot.exists()) {


                    AllFarms allFarms = documentSnapshot.toObject(AllFarms.class);
                    String UID = allFarms.getUser_id();


                    if (mAuth.getCurrentUser() != null) {
                        DeleteClientDetails(mAuth.getCurrentUser().getUid(), allFarms.getUser_id());
                    }


                } else {

                }


            }
        });

    }

    private void DeleteClientDetails(final String VeFoundId, String UID) {


        UserDbRef.document(UID)
                .collection("FarmersAround").document(VeFoundId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                        } else {

                            ToastBack(task.getException().getMessage());
                        }
                    }
                });


    }
    ///====end


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
        if (ClentMarker != null) ClentMarker.remove();
        if (ClentMarker != null) {
            ClentMarker.remove();
        }

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

//        ClentMarker = mMap.addMarker(new MarkerOptions()
//                .title("My farm")
//                .snippet(farmName)
//                .position(new LatLng(location.getLatitude(), location.getLongitude()))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_f)));


        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));




        if (location != null) {
            getFarmOnMapsAround(new LatLng(location.getLatitude(),location.getLongitude()));
            // RemoveCLoserVendor();
            getFarmAround(new LatLng(location.getLatitude(),location.getLongitude()));
            getVendorAround(new LatLng(location.getLatitude(), location.getLongitude()));
           /// Driver_Location();

        }


        //stop location updates

    }


    private void Driver_Location() {

        if (lastLocation != null) {



                //update to firestore
                DriverLocationGeoFire.setLocation(mAuth.getCurrentUser().getUid(), new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()),
                        new GeoFirestore.CompletionListener() {
                            @Override
                            public void onComplete(Exception e) {

                                String uid = mAuth.getCurrentUser().getUid();
                                HashMap<String, Object> updateLocation = new HashMap<>();
                                updateLocation.put("lat", 0);
                                updateLocation.put("lng", 0);

                                UserDbRef.document(mAuth.getCurrentUser().getUid()).update(updateLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                             ToastBack("Driver location was updated..");
                                            //FarmGeoFire.setLocation(mAuth.getCurrentUser().getUid(), new GeoPoint(lat, lng));

                                        } else {

                                        }

                                    }
                                });


                            }
                        });




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

        mSettingsClient = LocationServices.getSettingsClient(MapsActivity.this);

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
                                    rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
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
                                ActivityCompat.requestPermissions(MapsActivity.this,
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
    boolean locationPermission = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            LocationstatusCheck();
                        }
                        //if permission granted.
                        locationPermission = true;
                        //init google map fragment to show map.
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map23);
                        mapFragment.getMapAsync(this);
                        if (googleApiClient == null) {
                            buildGoogleApiCLient();
                        }
                        //mMap.setMyLocationEnabled(true);
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





    //----Farm Location----///
    private void Farm_Location() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        if (lastLocation != null) {

            final double lat = lastLocation.getLatitude();
            final double lng = lastLocation.getLongitude();


            if (mAuth.getCurrentUser() != null) {

                //update to firestore
                DriverLocationGeoFire.setLocation(mAuth.getCurrentUser().getUid(), new GeoPoint(lat, lng),
                        new GeoFirestore.CompletionListener() {
                            @Override
                            public void onComplete(Exception e) {

                                String uid = mAuth.getCurrentUser().getUid();
                                HashMap<String, Object> updateLocation = new HashMap<>();
                                updateLocation.put("lat", lat);
                                updateLocation.put("lng", lng);

                                UserDbRef.document(mAuth.getCurrentUser().getUid()).update(updateLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            //ToastBack("Current Shop location was updated..");
                                            //FarmGeoFire.setLocation(mAuth.getCurrentUser().getUid(), new GeoPoint(lat, lng));

                                        } else {

                                        }

                                    }
                                });


                            }
                        });

            }


        }


    }


    private TextView CloseDialog;
    private EditText InputFeedback;


    private FloatingActionButton btn_Send;
    private AlertDialog dialogFeedback;

    @SuppressLint("MissingInflatedId")
    public void Feedback_Alert() {

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        mbuilder.setView(mView);
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        CloseLoginDialog = mView.findViewById(R.id.CloseReview);
        btn_Send = mView.findViewById(R.id.Btn_submit_review);
        InputFeedback = mView.findViewById(R.id.reviewInputText);


        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = InputFeedback.getText().toString().trim();

                if (!feedback.isEmpty()) {
                    HashMap<String, Object> registerB = new HashMap<>();
                    registerB.put("userName", Name2);
                    registerB.put("farmName", farm_Name2);
                    registerB.put("user_UID", farm_UID);
                    registerB.put("timestamp", FieldValue.serverTimestamp());

                    FeedBackRef.document(farm_UID).set(registerB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ToastBack("Feedback sent!.");
                                if (dialogFeedback != null) dialogFeedback.dismiss();
                            } else {
                                ToastBack(task.getException().getMessage().toString());
                            }
                        }
                    });

                } else {

                }
            }
        });


        CloseLoginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFeedback != null) dialogFeedback.dismiss();
            }
        });


        dialogFeedback = mbuilder.create();
        dialogFeedback.show();

    }





    ///-----LogOut-----//////
    private AlertDialog dialog2;

    public void Logout_Alert(String UID) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog2 = builder.create();
        dialog2.show();
        builder.setMessage("Are you sure to Log out..\n");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Log_out(UID);


                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog2.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    void Log_out(String User_ID) {


        HashMap<String, Object> store = new HashMap<>();
        store.put("device_token", FieldValue.delete());

        UserDbRef.document(User_ID).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {

                    dialog2.dismiss();
                    mAuth.signOut();
                    Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout);


                } else {

                    ToastBack(task.getException().getMessage());

                }

            }
        });

    }
    //===end


    ///.....end vendor Location
    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }

    private long backPressedTime;

    @Override
    public void onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();
            finish();
            return;
        } else {
            ToastBack("Tap again to exit");

        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    private void erasePolylines(){
        for(Polyline line : polylines){
            if (line != null) line.remove();
        }

    }


    @Override
    public void onRoutingStart() {
        if (polylines != null){
            polylines.clear();
        }
        startLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        ToastBack(String.valueOf(startLocation));

    }

    private LatLng startLocation;
    private List<Polyline> polylines;
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(startLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.primary));
                polyOptions.width(8);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }



        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        endMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.home));
        mMap.addMarker(endMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(polylineStartLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void onRoutingCancelled() {

    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyA6IhPzFMBBM2eqG9QyjiW85hQvcSub4dM";
        return url;
    }

    Button getDirection;
    private Polyline currentPolyline;

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


}