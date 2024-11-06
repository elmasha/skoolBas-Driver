package com.app.driver;

import static com.app.driver.Common.mAuth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.driver.Adapters.SlideAdapter;
import com.app.driver.MainActivity.DirectionsMapsActivity;
import com.app.driver.MainActivity.MapsActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

public class WalkThroughActivity extends AppCompatActivity {
    private ViewPager mSliderViewPager;
    private LinearLayout mDotsLayout, ChooseLayout;
    private TextView[] mDots;
    private SlideAdapter slideAdpter;
    private FloatingActionButton NextBtn, BackBtn;
    private int mCurrentPage;
    private LinearLayout PrivacyLayout,privacy;
    private TextView Skip, walkId,walkId2;
    private Button GetStarted;

    private CheckBox checkBox;



    public static SettingsClient mSettingsClient;
    public static LocationSettingsRequest mLocationSettingsRequest;
    public static final int REQUEST_CHECK_SETTINGS = 214;
    public static final int REQUEST_ENABLE_GPS = 516;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        Check_Location_enable();
        mSliderViewPager = (ViewPager) findViewById(R.id.Slide_viewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.Dot_layout);
        NextBtn = (FloatingActionButton) findViewById(R.id.Next);
        BackBtn = (FloatingActionButton) findViewById(R.id.Back);
        PrivacyLayout = findViewById(R.id.RR);
        checkBox = findViewById(R.id.checkbox12);
        privacy = findViewById(R.id.viewPolicy);
        walkId =findViewById(R.id.walkId);
        walkId2 =findViewById(R.id.walkId2);


        Skip = findViewById(R.id.Skipp);
        GetStarted = findViewById(R.id.workHelper);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        slideAdpter = new SlideAdapter(this);
        ChooseLayout = findViewById(R.id.LayoutChoose);

        addDotsIndicator(0);
        mSliderViewPager.setAdapter(slideAdpter);
        mSliderViewPager.addOnPageChangeListener(viewListener);


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPage == 1) {
                    mSliderViewPager.setCurrentItem(-1);
                }

            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSliderViewPager.setCurrentItem(+1);
            }
        });


        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentPage == 0) {
                    mSliderViewPager.setCurrentItem(+1);
                }


            }
        });

        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    Intent push = new Intent(getApplicationContext(), DirectionsMapsActivity.class);
                    startActivity(push);
                }else {
                    if (mCurrentPage > 2) {
                        mSliderViewPager.setCurrentItem(3);
                    }
                    Terms_Alert();
                }

            }
        });



            if (mAuth.getCurrentUser() != null){
                    Intent logout = new Intent(getApplicationContext(), DirectionsMapsActivity.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout);
                    finish();

            }else {

            }




    }


    private boolean LocationCheck = false;
    public void Check_Location_enable() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(WalkThroughActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here

                        if (locationSettingsResponse.getLocationSettingsStates().isGpsPresent() == true) {

                            LocationCheck = true;
                        } else {

                            LocationCheck = false;
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
                                    LocationCheck = false;
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(WalkThroughActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS", "Unable to execute request.");
                                    LocationCheck = false;
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                LocationCheck = false;
                                Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        LocationCheck = false;
                        Log.e("GPS", "checkLocationSettings -> onCanceled");
                    }
                });


    }



    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null)
            return false;
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            return networkInfo.isConnected();
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            return networkInfo.isConnected();
        }
        return networkInfo.isConnected();
    }


    private AlertDialog dialog21;

    public void Terms_Alert() {
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog21 = builder.create();
        dialog21.show();
        builder.setIcon(R.drawable.ic_policy_24);
        builder.setTitle("Please Accept Privacy Policy and Terms & Condition");
        builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mCurrentPage == 3) {
                    mSliderViewPager.setCurrentItem(1);
                }
                dialog21.dismiss();
            }
        });
        builder.show();
    }




    private void addDotsIndicator(int position) {

        mDots = new TextView[2];
        mDotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.lightGrey));
            mDotsLayout.addView(mDots[i]);

        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPage = position;
        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0) {
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(false);
                BackBtn.setVisibility(View.GONE);
                NextBtn.setVisibility(View.VISIBLE);
                GetStarted.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
                PrivacyLayout.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.GONE);
                Skip.setVisibility(View.VISIBLE);
                walkId.setText("Welcome to i1-2Farm");
                walkId2.setVisibility(View.GONE);
                walkId2.setText("Get your hands dirty with us! Welcome to the farm!");

            } else if (position == 1) {

                NextBtn.setEnabled(false);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);
                GetStarted.setVisibility(View.VISIBLE);
                NextBtn.setVisibility(View.GONE);
                PrivacyLayout.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);
                ChooseLayout.setVisibility(View.VISIBLE);
                walkId.setText("Start an new farming adventure. ");
                Skip.setVisibility(View.GONE);
                walkId2.setText("");
                walkId2.setVisibility(View.GONE);
            }  else {

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
}