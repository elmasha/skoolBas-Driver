package com.app.driver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.text.DecimalFormat;

import retrofit2.Retrofit;

public class Common {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static String Phone = "0746491229";
    public static  String G_UID3 = "Ch9jjQKJkjZOwQW5R7QnZdQiRtg2";
    public static  String G_UID2 =  "Ch9jjQKJkjZOwQW5R7QnZdQiRtg2";
    public static  String G_UID4 =  "Ch9jjQKJkjZOwQW5R7QnZdQiRtg2";
    //   public static  String G_UID3 =  "HXwd5mvD5ng3RMg5lgSLpZmE6t93";


    public static DecimalFormat formatter = new DecimalFormat("#,###,###");
    public static Retrofit retrofit;
    // public static RetrofitInterfaceFcm retrofitInterface;
    public static String FCM_URL = "https://fcmkey.herokuapp.com/";
    public static String FCM_TOKEN ="" ;

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore DB = FirebaseFirestore.getInstance();
    public static CollectionReference RouteDbRef = DB.collection("Routes");
    public static CollectionReference UserDbRef = DB.collection("skoolBas_User");
    public static CollectionReference DriverDbRef = DB.collection("skoolBas_Driver");
    public static CollectionReference CountyDbRef = DB.collection("Conties");
    public static CollectionReference CATERef = DB.collection("Category");
    public static CollectionReference TOKENRef = DB.collection("Tokens");
    public static CollectionReference AdsRef = DB.collection("Ads");

    public static CollectionReference FeedBackRef = DB.collection("skoolBas_FeedBack");
    public  static CollectionReference ParentsRef = DB.collection("skoolBas_parents");

    public  static CollectionReference DriverLocation = DB.collection("skoolBas_driver_location");
    public  static final GeoFirestore  DriverLocationGeoFire =  new GeoFirestore(DriverLocation);

    public  static CollectionReference FarmAvailable = DB.collection("skoolBas_location");


    public  static final GeoFirestore FarmGeoFire =  new GeoFirestore(FarmAvailable);
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageReference = storage.getReference();
    public static   DecimalFormat df = new DecimalFormat("#");

}
