package com.app.driver.MainActivity.Uploads;

import static com.app.driver.Common.CATERef;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.app.driver.Adapters.CategoryAdapter;
import com.app.driver.Adapters.FarmImageAdapter;
import com.app.driver.MainActivity.MyFarmActivity;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.Category;
import com.app.driver.Models.FarmImage;
import com.app.driver.Models.UserAccount;
import com.app.driver.R;
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
import com.google.android.gms.maps.model.CircleOptions;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class EditFarmActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    public static GoogleApiClient googleApiClient;
    public static Location lastLocation;
    public static LocationRequest locationRequest;
    private String TAG = "TAG";

    private GoogleMap mMap;

    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 20003;
    private Bitmap compressedImageBitmap;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    private ViewPager mSliderViewPager;
    private LinearLayout mDotsLayout;

    private TextView[] mDots;
    TextView privacy;
    private Button Finishbtn;

    private  int mCurrentPage;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout,LayoutPrivacy;
    private TextView get_started,enableLocation,Notify,BackMaps_main;

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
    private EditText InputFarm_name ,InputFarm_category,InputFarm_offers,InputFarm_country,InputFarm_county,InputFarm_contact,InputFarm_County,InputFarm_Country,InputFarm_description;


    ///----Image storage and compression---////
    FirebaseStorage storage;
    StorageReference storageReference;
    private UploadTask uploadTask;

    private Uri imageUri ;
    private String farmName,farmCategory,farmContact,farmOffers,farmDesc,country,county;
    private Button Upload_FarmBtn;
    private RecyclerView mRecyclerView;
    private FarmImageAdapter adapter;
    private boolean edits = true;

    private RecyclerView mRecyclerViewCat;
    private CategoryAdapter adapter1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farm);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2_edit);
        mapFragment.getMapAsync(this);
        if (!hasPermissions(getApplicationContext())) {
        }

        Check_Location_enable();
        InputFarm_name = findViewById(R.id.InputFarm_name_edit);
        InputFarm_category = findViewById(R.id.InputFarm_category_edit);
        InputFarm_offers = findViewById(R.id.InputFarm_offers_edit);
        InputFarm_contact = findViewById(R.id.InputFarm_contact_edit);
        InputFarm_description = findViewById(R.id.InputFarm_description_edit);
        UploadImage = findViewById(R.id.UploadImage_edit);
        Upload_FarmBtn = findViewById(R.id.UploadFarmBtn_edit);
        mRecyclerView = findViewById(R.id.FetchFarmImagesRecyclerView_edit);
        BackMaps_main = findViewById(R.id.BackMaps_main_edit);
        mRecyclerViewCat = findViewById(R.id.CategoryRecyclerView_upload_edit);
        InputFarm_country = findViewById(R.id.InputFarm_country_edit);
        InputFarm_county = findViewById(R.id.InputFarm_county_edit);


        BackMaps_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });



        Upload_FarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()){

                }else {
                    UploadDetails();
                }
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getApplicationContext())) {
                }
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(EditFarmActivity.this);
            }
        });


        FetchCategory();
        FetchUploadedImages();
        if (mAuth.getCurrentUser() != null){
            LoadUserDetails(mAuth.getCurrentUser().getUid());
            LoadFarmDetails(mAuth.getCurrentUser().getUid());
        }


    }




    private void FetchCategory() {

        Query query = CATERef;
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
                farmCategory = category.getCategory();
                InputFarm_category.setText(category.getCategory());

            }
        });

    }


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
//
//                    if (imageUri != null){
//                        UploadImage();
//                    }else {
//
//                    }
//
//                    break;
//            }

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
                        FetchUploadedImages();
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





    private void FetchUploadedImages(){

        Query query = DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Farm_Images")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FarmImage> options = new FirestoreRecyclerOptions.Builder<FarmImage>()
                .setQuery(query, FarmImage.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new FarmImageAdapter(options);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new FarmImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                FarmImage farmImage1 = documentSnapshot.toObject(FarmImage.class);

            }
        });



        adapter.setOnItemClickListener(new FarmImageAdapter.OnItemClickListenerDelete() {
            @Override
            public void onItemClickDelete(DocumentSnapshot documentSnapshot, int position) {
                Delete_Alert(position);
            }
        });


    }


    private void QueryForMyShop(String tel) {

        DriverDbRef.whereEqualTo("user_id", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {

                            NewFarm();
                            ToastBack("Farm does not exist");
                        } else {
                            ToastBack("We found a farm that matches your search");
                            ExistingFarm();
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


    private double lat,lng;

    private void LoadUserDetails(String UID){
        UserDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                            lat = scm_user.getLat();
                            lng = scm_user.getLng();

                            InputFarm_name.setText(scm_user.getFarm_name());


                            //CheckPaymentStatus(UID);

                        }else{
                            ToastBack("EMPTY DOC"+e.getMessage().toString());
                        }
                    }
                });


    }


    private String ImageUID,ImageUrl;
    private void UploadImage(){
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
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
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

                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();

                    ImageUrl = downloadUri.toString();


                    if (mAuth.getCurrentUser() != null){
                        QueryForMyShop(mAuth.getCurrentUser().getUid());
                    }else {

                    }


                }else {
                    Toast.makeText(EditFarmActivity.this,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String farmNameE, farmCategoryE, farmContactE,farmCountryE,farmCountyE, farmOffersE, farmDescE, farmImageE;
    private double load_latE, load_lngE;

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
                            farmNameE = scm_user.getFarmName();
                            farmCategoryE = scm_user.getFarmCategory();
                            farmDescE = scm_user.getFarmDescription();
                            load_lngE = scm_user.getLng();
                            load_latE = scm_user.getLat();
                            farmCountryE = scm_user.getCountry();
                            farmCountyE = scm_user.getCounty();
                            String s = scm_user.getFarmName().substring(0, 2);
                            farmImageE = scm_user.getImage();



                            InputFarm_name.setText(farmNameE);
                            InputFarm_category.setText(farmCategoryE);
                            InputFarm_description.setText(farmDescE);
                            InputFarm_contact.setText(scm_user.getFarmContacts());
                            InputFarm_county.setText(scm_user.getCounty());
                           // InputFarm_Country.setText(scm_user.getCountry());


                        } else {
                            ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });


    }


    private void NewFarm(){

        HashMap<String, Object> store1 = new HashMap<>();
        store1.put("timestamp", FieldValue.serverTimestamp());
        DriverDbRef.document(mAuth.getCurrentUser().getUid()).set(store1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    ExistingFarm();
                }else {
                    Toast.makeText(EditFarmActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ExistingFarm(){
        ImageUID = DriverDbRef.document().collection("Farm_Images").document().getId();
        HashMap<String, Object> store = new HashMap<>();
        store.put("image_id", ImageUID);
        store.put("image_Uid", mAuth.getCurrentUser().getUid());
        store.put("image", ImageUrl);
        store.put("lat",lat);
        store.put("lng",lng);
        store.put("timestamp", FieldValue.serverTimestamp());

        DriverDbRef.document(mAuth.getCurrentUser().getUid()).collection("Farm_Images").document(ImageUID).set(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            FetchUploadedImages();

                            Toast.makeText(getApplicationContext(), "Stored Successfully..", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private String County,Country;
    private boolean validation() {
        farmName = InputFarm_name.getText().toString();
        farmCategory = InputFarm_category.getText().toString();
        farmContact = InputFarm_contact.getText().toString();
        farmOffers = InputFarm_offers.getText().toString();
        farmDesc = InputFarm_description.getText().toString();
        country = InputFarm_country.getText().toString();
        county = InputFarm_county.getText().toString().trim();


        if (farmName.isEmpty()) {
            InputFarm_name.setError("Provide farm name");
            return false;
        } else if (farmCategory.isEmpty()) {
            InputFarm_category.setError("Provide farm category");
            return false;
        }else if (county.isEmpty()){
            InputFarm_county.setError("Please provide county/state/province");
            return false;
        }else if (country.isEmpty()){
            InputFarm_country.setError("Provide your country");
            return false;
        }else if (farmContact.isEmpty()) {
            InputFarm_contact.setError("Provide email");
            return false;
        } else if (farmOffers.isEmpty()) {
            InputFarm_offers.setError("Provide county");
            return false;
        }else if (farmDesc.isEmpty()) {
            InputFarm_description.setError("Tell us something about your farm");
            return false;
        } else {
            return true;
        }

    }

    private void UploadDetails(){
        HashMap<String, Object> store = new HashMap<>();
        store.put("farmName", farmName);
        store.put("farmCategory", farmCategory);
        store.put("image", ImageUrl);
        store.put("farmDescription", farmDesc);
        store.put("user_id", mAuth.getCurrentUser().getUid());
        store.put("farmOffers", farmOffers);
        store.put("farmContacts", farmContact);
        store.put("County", county);
        store.put("Country", country);
        store.put("lat",lat);
        store.put("lng",lng);
        store.put("rating",0);
        store.put("timestamp", FieldValue.serverTimestamp());

        HashMap<String, Object> registerB4 = new HashMap<>();
        registerB4.put("Farm_name", farmName);
        registerB4.put("farm_category", farmCategory);
        registerB4.put("farm_owner", farmName);
        registerB4.put("image",imageUri);
        registerB4.put("id", mAuth.getCurrentUser().getUid());


        DriverDbRef.document(mAuth.getCurrentUser().getUid()).update(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            SaveWhListDetails(registerB4);

                        } else {

                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    private Client client;
    private Index index;

    private void SaveWhListDetails(HashMap<String, Object> mapOne) {

        client = new Client("202QHE77ZG", "8eb0d05e4130d35b4c61ed544c1ca1c4");

        index = client.getIndex("i1-2Farm_Farms");




        List<JSONObject> productList = new ArrayList<>();
        productList.add(new JSONObject(mapOne));

        index.addObjectsAsync(new JSONArray(productList), new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                if (e!=null){
                    ToastBack("Error"+e.getMessage().toString());
                    Log.i("elmasha",e.getMessage().toString());
                }else {
                    ToastBack("Stored Successfully..");
                    FetchUploadedImages();
                    ResetFields();
                }
            }
        });



    }



    private void ResetFields(){
        InputFarm_name.setText("");
        InputFarm_category.setText("");
        InputFarm_contact.setText("");
        InputFarm_offers.setText("");
        InputFarm_description.setText("");
        Intent toreg = new Intent(getApplicationContext(), MyFarmActivity.class);
        toreg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toreg);
        finish();
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
        }
        else {
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

            ClientLocation();
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
        if (ClentMarker != null)ClentMarker.remove();
        if (ClentMarker != null) {
            ClentMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        ClentMarker = mMap.addMarker(markerOptions);

        CircleOptions addCircle = new CircleOptions().center(latLng).radius(3).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(addCircle);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

        mSettingsClient = LocationServices.getSettingsClient(EditFarmActivity.this);

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
                                    rae.startResolutionForResult(EditFarmActivity.this, REQUEST_CHECK_SETTINGS);
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
                                ActivityCompat.requestPermissions(EditFarmActivity.this,
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