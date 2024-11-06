package com.app.driver.MainActivity;

import static com.app.driver.Common.AdsRef;
import static com.app.driver.Common.CATERef;
import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.mAuth;
import static com.app.driver.WalkThroughActivity.isConnectionAvailable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.driver.Adapters.AdsAdapterExplore;
import com.app.driver.Adapters.CategoryAdapter;
import com.app.driver.Adapters.CountyAdapter;
import com.app.driver.Adapters.ExploreFarmAdapter;
import com.app.driver.Adapters.MainArticleAdapter;
import com.app.driver.Adapters.MainArticleAdapter2;
import com.app.driver.Models.AdsExplore;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.AllFarmsSearch;
import com.app.driver.Models.Category;
import com.app.driver.Models.CountiesSearch;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ExploreActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView,mRecyclerViewCat,mRecyclerViewS;
    private ExploreFarmAdapter adapter;
    private CategoryAdapter adapter1;

    private String SearchCategory = "";
    private EditText SearchText;

    private MainArticleAdapter mainArticleAdapter;

    private FrameLayout SearchListLayout;

    Client client;
    Index index;
    private ArrayList<AllFarmsSearch> list;


    private TextView BackExplore_my,Clear_filter;



    private FloatingActionButton OpenSearchLayout;

    private RecyclerView AdsrecyclerView;


    private CountyAdapter countyAdapter;

    private AdsAdapterExplore adsAdapterExplore;


    private EditText Search_county;

    private MainArticleAdapter2 mainArticleAdapter2;

    private ArrayList<CountiesSearch> list2;

    private Spinner SpinnerFilter;
    private LinearLayout MySearch_algolia, RefreshList_my, RangeFarmLayout;

    PlacesClient placesClient;

    private ImageButton filterLayout;



    private int radius = 20;
    private int limit = 25;

    private Fragment Places;

    private String LocationSearch;


    private RelativeLayout GooglePlaceSearch,FilterLayout;

    private com.google.android.libraries.places.api.Places places;


    private TextView weather_location,weather_temperature,weather_temp_max,weather_temp_min,weather_humidity,
            weather_pressure,weather_wind,weather_desc,weather_refresh;

    private ImageView weather_iconView;


    private int SearchState= 0;
    private String spinnerFilter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Clear_filter = findViewById(R.id.Clear_filter_e);
        mRecyclerView = findViewById(R.id.FetchFarmImagesRecyclerView_e);
        mRecyclerViewCat = findViewById(R.id.CategoryRecyclerView_e);
        SearchText = findViewById(R.id.Search_text_e);
        SearchListLayout = findViewById(R.id.SearchListLayout_explore);
        BackExplore_my = findViewById(R.id.BackExplore_my);
        OpenSearchLayout = findViewById(R.id.OpenSearchLayout);
        AdsrecyclerView = findViewById(R.id.AdsRecyclerView_e);
        filterLayout = findViewById(R.id.tuneSearch_e);
        SpinnerFilter = findViewById(R.id.SpinnerFilter_e);
        GooglePlaceSearch = findViewById(R.id.GooglePlaceSearch_e);
        MySearch_algolia = findViewById(R.id.MySearch_algolia_e);

        weather_location = findViewById(R.id.weather_location);
        weather_temperature = findViewById(R.id.weather_temperature);
        weather_temp_max = findViewById(R.id.weather_temp_max);
        weather_temp_min = findViewById(R.id.weather_temp_min);
        weather_iconView= findViewById(R.id.weather_iconView);
        weather_humidity = findViewById(R.id.weather_humidity);
        weather_pressure = findViewById(R.id.weather_pressure);
        weather_wind = findViewById(R.id.weather_wind);
        weather_desc = findViewById(R.id.weather_desc);
        weather_refresh = findViewById(R.id.weather_refresh);


        Search_county = findViewById(R.id.Search_county_e);
        FilterLayout = findViewById(R.id.L877_e);

        if (!places.isInitialized()) {
            places.initialize(getApplicationContext(), getString(R.string.API_KEY), Locale.US);
        }



        weather_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastBack("Refreshing weather.");
                if (mAuth.getCurrentUser() != null){
                    LoadFarmDetails(mAuth.getCurrentUser().getUid());
                }
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_e);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", ");
                LocationSearch = place.getName();

//                RemoveCLoserVendor();
//                getVendorAround(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
//                getSearchVendor(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
                Toast.makeText(ExploreActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        SpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerFilter = SpinnerFilter.getSelectedItem().toString().toString();
                ToastBack(i+"");
                if (spinnerFilter == ""){


                }

                if ( i == 0){
                    GooglePlaceSearch.setVisibility(View.GONE);
                    SearchText.setVisibility(View.GONE);
                    Search_county.setVisibility(View.VISIBLE);
                    if (SearchState == 0) {
                        Clear_filter.setVisibility(View.VISIBLE);
                        SpinnerFilter.setVisibility(View.VISIBLE);
                        SearchListLayout.setVisibility(View.GONE);
                        SearchState = 1;
                    }
                }else if (i == 1){
                    GooglePlaceSearch.setVisibility(View.GONE);
                    SearchText.setVisibility(View.VISIBLE);
                    Search_county.setVisibility(View.GONE);
                }else if (i == 2){
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
            }
        });




        BackExplore_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BackPress();
            }
        });

        mRecyclerViewS = findViewById(R.id.SearchRecyclerView_explore);
        mRecyclerViewS.setHasFixedSize(false);
        mRecyclerViewS.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();
        list2 = new ArrayList<>();


        // FetchFarmsForSearch();

        client = new Client("202QHE77ZG", "8eb0d05e4130d35b4c61ed544c1ca1c4");

        index = client.getIndex("i1-2Farm_Farms");

        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (i2 == 0){
                    SearchListLayout.setVisibility(View.GONE);
                    Clear_filter.setVisibility(View.GONE);
                }else {
                    SearchListLayout.setVisibility(View.VISIBLE);
                    Clear_filter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isConnectionAvailable(getApplicationContext())){
                    ToastBack("No internet connection");
                }else {

                    if (editable.equals("")){
                        SearchListLayout.setVisibility(View.GONE);
                    }else {
                        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(editable.toString())
                                .setAttributesToRetrieve("Farm_name")
                                .setHitsPerPage(10);
                        index.searchAsync(query, new CompletionHandler() {
                            @Override
                            public void requestCompleted(JSONObject content, AlgoliaException error) {
                                try {
                                    list.clear();
                                    JSONArray hits = content.getJSONArray("hits");
                                    for (int i = 0; i < hits.length(); i++) {
                                        JSONObject jsonObject = hits.getJSONObject(i);
                                        String productName = jsonObject.getString("Farm_name");
                                        list.add(new AllFarmsSearch("id",productName));

                                    }
                                    mainArticleAdapter = new MainArticleAdapter(getApplicationContext(),list);
                                    mainArticleAdapter.notifyDataSetChanged();
                                    mainArticleAdapter.OnRecyclerViewItemClickListener(new MainArticleAdapter.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onRecyclerViewItemClicked(int position, View view) {
                                            switch (view.getId()) {
                                                case R.id.searchLayout:
                                                    AllFarmsSearch article = (AllFarmsSearch) view.getTag();
                                                    if(!TextUtils.isEmpty(article.getId())) {
                                                        LoadShopData(article.getFarm_name());
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                    mRecyclerViewS.setAdapter(mainArticleAdapter);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ToastBack("1"+e.getMessage().toString());
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
                    Clear_filter.setVisibility(View.GONE);
                } else {
                    SearchListLayout.setVisibility(View.VISIBLE);
                    MySearch_algolia.setVisibility(View.VISIBLE);
                    Clear_filter.setVisibility(View.VISIBLE);
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
                                                        FetchAllFarms2(SearchCounty);
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

        FetchAllFarms();

        FetchCategory();



        if (mAuth.getCurrentUser() != null){
            LoadFarmDetails(mAuth.getCurrentUser().getUid());
        }



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

    private String farmName, farmCategory, farmContact, farmOffers, farmDesc, farmImage,farmCounty;

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
                            farmCounty = scm_user.getCounty();
                            FetchAdsCategoryAds();
                            LoadWeather(farmCounty);
                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }



    private void FetchAdsCategoryAds() {

        if (!SearchCategory.equals("")){
            Query query = AdsRef
                    .whereEqualTo("Ad_category",SearchCategory)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AdsExplore> options = new FirestoreRecyclerOptions.Builder<AdsExplore>()
                    .setQuery(query, AdsExplore.class)
                    .setLifecycleOwner(this)
                    .build();
            adsAdapterExplore = new AdsAdapterExplore(options);

            AdsrecyclerView.setHasFixedSize(false);
            AdsrecyclerView.setNestedScrollingEnabled(false);
            AdsrecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            AdsrecyclerView.setAdapter(adsAdapterExplore);

            adsAdapterExplore.setOnItemClickListener(new AdsAdapterExplore.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
//                ImageMain = farmImage1.getImage();
//                ImageView(ImageMain);


                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getString("user_id"));
                    startActivity(logout);

                }
            });
        }else {
            Query query = AdsRef
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AdsExplore> options = new FirestoreRecyclerOptions.Builder<AdsExplore>()
                    .setQuery(query, AdsExplore.class)
                    .setLifecycleOwner(this)
                    .build();
            adsAdapterExplore = new AdsAdapterExplore(options);

            AdsrecyclerView.setHasFixedSize(false);
            AdsrecyclerView.setNestedScrollingEnabled(false);
            AdsrecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            AdsrecyclerView.setAdapter(adsAdapterExplore);

            adsAdapterExplore.setOnItemClickListener(new AdsAdapterExplore.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);
//                ImageMain = farmImage1.getImage();
//                ImageView(ImageMain);


                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getString("user_id"));
                    startActivity(logout);

                }
            });
        }



    }



    private String SearchCounty="";



    private void LoadShopData(String shop){

        ToastBack(shop);
        DriverDbRef.whereEqualTo("farmName",shop)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (documentSnapshot.exists()){
                                AllFarms scm_user = documentSnapshot.toObject(AllFarms.class);
                                Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                                logout.putExtra("ID", documentSnapshot.getId());
                                startActivity(logout);


                            }else{
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



    private void FetchAllFarms(){
        if (SearchCategory.equals("")){
            Query query = DriverDbRef
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AllFarms> options = new FirestoreRecyclerOptions.Builder<AllFarms>()
                    .setQuery(query, AllFarms.class)
                    .setLifecycleOwner(this)
                    .build();
            adapter = new ExploreFarmAdapter(options);

            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            mRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ExploreFarmAdapter.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getId());
                    startActivity(logout);
                }
            });
        }else {
            Query query = DriverDbRef
                    .whereEqualTo("farmCategory",SearchCategory)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AllFarms> options = new FirestoreRecyclerOptions.Builder<AllFarms>()
                    .setQuery(query, AllFarms.class)
                    .setLifecycleOwner(this)
                    .build();
            adapter = new ExploreFarmAdapter(options);

            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            mRecyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(new ExploreFarmAdapter.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getId());
                    startActivity(logout);
                }
            });

        }



    }


    private void FetchAllFarms22(String searchCategory){

            Query query = DriverDbRef
                    .whereEqualTo("farmCategory",searchCategory)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AllFarms> options = new FirestoreRecyclerOptions.Builder<AllFarms>()
                    .setQuery(query, AllFarms.class)
                    .setLifecycleOwner(this)
                    .build();
            adapter = new ExploreFarmAdapter(options);

            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            mRecyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(new ExploreFarmAdapter.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getId());
                    startActivity(logout);
                }
            });





    }

    private void FetchAllFarms2(String searchCounty){
            Query query = DriverDbRef
                    .whereEqualTo("County",searchCounty)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<AllFarms> options = new FirestoreRecyclerOptions.Builder<AllFarms>()
                    .setQuery(query, AllFarms.class)
                    .setLifecycleOwner(this)
                    .build();
            adapter = new ExploreFarmAdapter(options);

            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            mRecyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(new ExploreFarmAdapter.OnItemCickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Intent logout = new Intent(getApplicationContext(), ViewFarmActivity.class);
                    logout.putExtra("ID",documentSnapshot.getId());
                    startActivity(logout);
                }
            });


    }


    private void FetchCategory(){

        Query query = CATERef.orderBy("category", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .setLifecycleOwner(this)
                .build();
        adapter1 = new CategoryAdapter(options);

        mRecyclerViewCat.setHasFixedSize(false);
        mRecyclerViewCat.setNestedScrollingEnabled(true);
        mRecyclerViewCat.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerViewCat.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Category category = documentSnapshot.toObject(Category.class);
                SearchCategory = category.getCategory();
                farmCategory = category.getCategory();
                if (SearchCategory.equals("All")){
                    if (mAuth.getCurrentUser() != null) {
                       SearchCategory = "";
                        FetchAllFarms();
                        FetchAdsCategoryAds();
                        //LoadFarmDetails(mAuth.getCurrentUser().getUid());
                    }else {

                    }
                }else {
                    FetchAdsCategoryAds();
                    FetchAllFarms22(SearchCategory);
                }
            }
        });

    }

    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }



    private void BackPress() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {
        BackPress();
    }
}