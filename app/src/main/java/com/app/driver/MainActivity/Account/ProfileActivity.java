package com.app.driver.MainActivity.Account;

import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.FeedBackRef;
import static com.app.driver.Common.G_UID2;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.Common.mAuth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.driver.MainActivity.Terms_and_ConditionsActivity;
import com.app.driver.Models.AllFarms;
import com.app.driver.Models.UserAccount;
import com.app.driver.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private TextView Farm_name, Farm_category, Farm_offers, User_name, Farm_contact, Farm_rating, Farm_description, BackMaps,ProfileID;
    private ImageView User_image;

    private TextView BackProfile,AddProfileImage;

    private RelativeLayout LogOut;

    private UploadTask uploadTask;
    FirebaseStorage storage;
    StorageReference storageReference;
    int PERMISSION_ALL = 20003;
    private Bitmap compressedImageBitmap;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    //private RangeBar rangeBar;

    private CircleImageView profileImage;

    private int EditState =0;

    private LinearLayout EditProfileLayout,EditProfile,LinearProfile,FarmFeedbackProfile,FarmTermsProfile;

    private EditText InputEditFUserName,InputEditFarmEmail,InputEditFarmName;

    private String  farmEmail;


    private Button BtnSaveChanges;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BackProfile = findViewById(R.id.BackProfile_my);

        Farm_name = findViewById(R.id.farm_name_profile);
        Farm_category = findViewById(R.id.farm_category_profilr);
        Farm_rating = findViewById(R.id.farm_rate_profile);
        User_image = findViewById(R.id.farm_image_profile);
        //  Farm_description = findViewById(R.id.farm_Description_profile);
        // Farm_contact = findViewById(R.id.farm_phone_profile);
        User_name = findViewById(R.id.ProfileName);
        LogOut = findViewById(R.id.LogOutLayOut);
        ProfileID = findViewById(R.id.ProfileID);
        AddProfileImage = findViewById(R.id.Add_profile);
        profileImage = findViewById(R.id.farm_image_profile);
        EditProfileLayout = findViewById(R.id.LinearEditProfile);
        EditProfile = findViewById(R.id.EditProfile);
        InputEditFarmEmail = findViewById(R.id.edit_email_profile);
        InputEditFUserName = findViewById(R.id.edit_username_profile);
        LinearProfile = findViewById(R.id.LinearProfile);
        BtnSaveChanges = findViewById(R.id.BtnSaveChanges);
        FarmFeedbackProfile = findViewById(R.id.FarmFeedbackProfile);
        FarmTermsProfile = findViewById(R.id.FarmTermsProfile);


        FarmTermsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), Terms_and_ConditionsActivity.class));
            }
        });


        FarmFeedbackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback_Alert();
            }
        });



        BtnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = InputEditFUserName.getText().toString().trim();
                String email = InputEditFarmEmail.getText().toString().trim();

                if (!user.isEmpty() |!email.isEmpty()){
                    HashMap<String, Object> registerB = new HashMap<>();
                    registerB.put("userName", userName);
                    registerB.put("farmName", farmNamee);
                    registerB.put("user_UID", mAuth.getCurrentUser().getUid());
                    registerB.put("timestamp", FieldValue.serverTimestamp());

                    FeedBackRef.document(mAuth.getCurrentUser().getUid()).set(registerB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                ToastBack("Feedback sent!.");
                                if (dialogFeedback != null) dialogFeedback.dismiss();
                            }else {
                                ToastBack(task.getException().getMessage().toString());
                            }
                        }
                    });
                }else {
                    ToastBack("Provide required details");
                }
            }
        });


        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EditState == 0){
                    EditProfileLayout.setVisibility(View.VISIBLE);
                    LinearProfile.setVisibility(View.GONE);
                    EditState =1;
                }else if (EditState ==1){
                    EditProfileLayout.setVisibility(View.GONE);
                    LinearProfile.setVisibility(View.VISIBLE);
                    EditState = 0;
                }
            }
        });




        User_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        AddProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser() != null) {
                    Logout_Alert(mAuth.getCurrentUser().getUid());
                } else {
                    Logout_Alert(G_UID2);
                }


            }
        });


        BackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });


        if (mAuth.getCurrentUser() != null) {
            LoadFarmDetails(mAuth.getCurrentUser().getUid());
            LoadUserDetails(mAuth.getCurrentUser().getUid());
        }
    }


    private TextView CloseLoginDialog;
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

                if (!feedback.isEmpty()){
                    HashMap<String, Object> registerB = new HashMap<>();
                    registerB.put("userName", userName);
                    registerB.put("farmName", farmNamee);
                    registerB.put("user_UID", mAuth.getCurrentUser().getUid());
                    registerB.put("timestamp", FieldValue.serverTimestamp());

                    FeedBackRef.document(mAuth.getCurrentUser().getUid()).set(registerB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                ToastBack("Feedback sent!.");
                                if (dialogFeedback != null) dialogFeedback.dismiss();
                            }else {
                                ToastBack(task.getException().getMessage().toString());
                            }
                        }
                    });

                }else {

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




    private Uri ImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // if (resultCode == RESULT_OK)
//            switch (requestCode) {
//                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    //data.getData returns the content URI for the selected Image
//                    ImageUri = result.getUri();
//                    if (ImageUri != null) {
//                        uploadImage(ImageUri);
//                    }
//
//                    break;
//            }
    }


    private void Upload(){

    }

    private ProgressDialog progressDialog3;
    private void uploadImage(Uri imageUri) {
        //if (dialogImage != null) dialogImage.dismiss();
        progressDialog3 = new ProgressDialog(ProfileActivity.this);
        progressDialog3.setMessage("Please wait uploading...");
        progressDialog3.show();
        progressDialog3.setCancelable(false);
        File newimage = new File(imageUri.getPath());
        try {
            Compressor compressor = new Compressor(this);
            compressor.setMaxHeight(400);
            compressor.setMaxWidth(400);
            compressor.setQuality(10);
            compressedImageBitmap = compressor.compressToBitmap(newimage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();


        final StorageReference ref = storageReference.child("Users/thumbs" + UUID.randomUUID().toString());
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
                    String profileImage = downloadUri.toString();

                    HashMap<String, Object> registerB = new HashMap<>();
                    registerB.put("farm_image", profileImage);

                    UserDbRef.document(mAuth.getCurrentUser().getUid()).update(registerB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ToastBack("Image uploaded successful");

                                progressDialog3.dismiss();
                            } else {
                                ToastBack(task.getException().getMessage());
                                progressDialog3.dismiss();
                            }
                        }
                    });


                } else {

                    ToastBack(task.getException().getMessage());
                    progressDialog3.dismiss();
                }
            }
        });


    }





    private String UserImage,userName,farmNamee;
    private void LoadUserDetails(String UID) {
        UserDbRef.document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);

                            userName = scm_user.getUserName();
                            farmNamee = scm_user.getFarm_name();
                            ProfileID.setText("ID: "+mAuth.getCurrentUser().getUid());
                            User_name.setText(scm_user.getUserName());
                            UserImage = scm_user.getFarm_image();
                            InputEditFarmEmail.setText(scm_user.getEmail());
                            InputEditFUserName.setText(scm_user.getUserName());
                            if (UserImage != null){
                                AddProfileImage.setVisibility(View.VISIBLE);
                                Picasso.with(profileImage.getContext()).load(UserImage).into(profileImage);
                            }else {
                                AddProfileImage.setVisibility(View.VISIBLE);
                            }
//                            Picasso.with(Farm_image.getContext()).load(farmImage).fit().into(Farm_image);

                            //CheckPaymentStatus(UID);

                        } else {
                            //ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });

    }



    private String farmName, farmCategory, farmContact, farmOffers, farmDesc, farmImage;
    private double load_lat, load_lng;

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


                            Farm_name.setText(farmName);
                            Farm_category.setText(farmCategory);
                            Farm_rating.setText(scm_user.getRating() + "");
//                          Farm_description.setText(scm_user.getFarmDescription());
//                          Farm_contact.setText(scm_user.getFarmContacts());
                            User_name.setText(scm_user.getUser());

//                            Picasso.with(Farm_image.getContext()).load(farmImage).fit().into(Farm_image);

                            //CheckPaymentStatus(UID);

                        } else {
                            //ToastBack("EMPTY DOC" + e.getMessage().toString());
                        }
                    }
                });

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