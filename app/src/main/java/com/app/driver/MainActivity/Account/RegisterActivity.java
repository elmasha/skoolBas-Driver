package com.app.driver.MainActivity.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.app.driver.MainActivity.MapsActivity;
import com.app.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.WalkThroughActivity.isConnectionAvailable;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout UserName,
            Email, InputFarmB;
    private EditText
            Telephone, VerifyInput;
    private String InputCode;
    private TextView toLogin, toPhone, timer;
    private LinearLayout OtpLayout_r, RegLayout, CatLayout;
    private EditText VerifyCode, Input_dis_Cat;

    private Button BtnRegister, BtnVerifyCode;

    private static final long START_TIME_IN_MILLIS_COUNT = 27000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;
    private String userName, email,
            telephone, VCode, FarmName;
    private String User_state;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private static final long START_TIME_IN_MILLIS_COUNT2 = 30000;
    private CountDownTimer mCountDownTimer2;
    private boolean mTimerRunning2;
    private long mTimeLeftInMillis2 = START_TIME_IN_MILLIS_COUNT2;


    private CountryCodePicker CodePicker;

    private boolean BtnState_r = true;
    private String Phone_auth;


    private RecyclerView mRecyclerView, mRecyclerViewCat;
    private SwipeRefreshLayout swipeRefreshLayout;
    // private CategoryAdapter adapter2;
    private String category, dis_category;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UserName = findViewById(R.id.FullName);
        Email = findViewById(R.id.Email);
        CodePicker = findViewById(R.id.Phone_code_register);
        InputFarmB = findViewById(R.id.FarmName);
        Telephone = findViewById(R.id.phone_no);
        OtpLayout_r = findViewById(R.id.Otp_L);
        RegLayout = findViewById(R.id.Sms_L);
        toPhone = findViewById(R.id.ToPhone);
        timer = findViewById(R.id.timer);
        VerifyInput = findViewById(R.id.otp_loginV);
        CodePicker.registerCarrierNumberEditText(Telephone);
        CodePicker.getDefaultCountryCode();
        toLogin = findViewById(R.id.ToLogin);
        CatLayout = findViewById(R.id.LayoutCat);
        mRecyclerViewCat = findViewById(R.id.dist1_category2);
        Input_dis_Cat = findViewById(R.id.Input_dis_Cat);


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toreg = new Intent(getApplicationContext(), LoginActivity.class);
                toreg.putExtra("USER_STATE", User_state);
                toreg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toreg);
                finish();
            }
        });


        BtnRegister = findViewById(R.id.Btn_register);
        BtnVerifyCode = findViewById(R.id.Btn_verify_code);


        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnectionAvailable(getApplicationContext())) {
                    ToastBack("No internet connection");
                } else {


                    if (!validation()) {

                    } else {

                        telephone = CodePicker.getFullNumberWithPlus();
                        QueryDatabase11(telephone);
                    }

                }


            }
        });

        BtnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnectionAvailable(getApplicationContext())) {
                    ToastBack("No internet connection");
                } else {
                    if (!validation2()) {

                    } else {

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, InputCode);
                        signInWithPhoneAuthCredential(credential);
                    }
                }

            }
        });


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                ToastBack(e.getMessage());
                OtpLayout_r.setVisibility(View.GONE);
                RegLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken Token) {

                mVerificationId = verificationId;
                mToken = Token;
                OtpLayout_r.setVisibility(View.VISIBLE);
                RegLayout.setVisibility(View.GONE);
                toPhone.setText(telephone);
                startTimer2();

            }
        };


    }


    ///----VERIFICATION Timer ----////
    private void startTimer2() {
        mCountDownTimer2 = new CountDownTimer(mTimeLeftInMillis2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis2 = millisUntilFinished;
                updateCountDownText2();
            }

            @Override
            public void onFinish() {


            }
        }.start();
        mTimerRunning2 = true;

    }

    private EditText code1;
    private ProgressBar progressBar_verify;
    private TextView resend, sentTO, closeDia;
    private Button Btn_Verify;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser() != null) {
                                // Sign in success, update UI with the signed-in user's information

                                RegisterUser();
                                pauseTimer2();
                                resetTimer2();
                            } else {
                                ToastBack("no user");
                                if (progressDialog != null) progressDialog.dismiss();
                            }

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            ToastBack("Sign in failed");
                            if (progressDialog != null) progressDialog.dismiss();
                            Btn_Verify.setVisibility(View.VISIBLE);
                            progressBar_verify.setVisibility(View.INVISIBLE);
                            pauseTimer2();
                            resetTimer2();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invali
                                ToastBack("The verification code entered was invalid");
                                pauseTimer2();
                                resetTimer2();
                            } else {
                                pauseTimer2();
                                resetTimer2();
                            }
                        }
                    }
                });
    }


    private void RegisterUser() {

        Random dice = new Random();
        String token_Id = FirebaseInstanceId.getInstance().getToken();

        HashMap<String, Object> registerB = new HashMap<>();
        registerB.put("diverName", userName);
        registerB.put("Email", email);
        registerB.put("Phone_NO", telephone);
        registerB.put("device_token", token_Id);
        registerB.put("bus_no", FarmName);
        registerB.put("User_ID", mAuth.getCurrentUser().getUid());
        registerB.put("bus_category", "");
        registerB.put("bus_type", "");
        registerB.put("timestamp", FieldValue.serverTimestamp());


        HashMap<String, Object> token = new HashMap<>();
        token.put("token", token_Id);


        UserDbRef.document(mAuth.getCurrentUser().getUid()).set(registerB);
        DriverDbRef.document(mAuth.getCurrentUser().getUid()).set(registerB).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ToastBack("Registration was successful");


                Intent toDis = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(toDis);
                finish();
                if (progressDialog != null) progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ToastBack(e.getMessage());
                if (progressDialog != null) progressDialog.dismiss();
            }
        });


        DB.collection("Tokens").document(mAuth.getCurrentUser().getUid()).set(token)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //   UploadIndex("Dukaan_WholeSeller",uid);

                        } else {
                            ToastBack(task.getException().getMessage());
                        }
                    }
                });


    }


    private void pauseTimer2() {
//        mCountDownTimer.cancel();
        mTimerRunning2 = false;

    }

    private void resetTimer2() {
        mTimeLeftInMillis2 = START_TIME_IN_MILLIS_COUNT2;
        updateCountDownText2();

    }

    private void updateCountDownText2() {
        int minutes = (int) (mTimeLeftInMillis2 / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis2 / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);

    }
    ///----VERIFICATION Timer ----////


    private ProgressDialog progressDialog, progressDialog23;

    private void QueryDatabase11(String tel) {
        DriverDbRef.whereEqualTo("Phone_NO", tel)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            telephone = CodePicker.getFullNumberWithPlus();
                            RequestOTp(telephone);
                        } else {
                            String msg = "This account already exists";
                            AlertUser(msg);
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

    private void AlertUser(String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(msg)
                .setConfirmText("Close")
                .show();

        progressDialog23.dismiss();
    }

    private void RequestOTp(String telephone) {
        ToastBack("!22");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                telephone,
                60,
                TimeUnit.SECONDS,
                RegisterActivity.this,
                mCallBacks);
    }

    private boolean validation() {
        userName = UserName.getEditText().getText().toString();
        FarmName = InputFarmB.getEditText().getText().toString();
        email = Email.getEditText().getText().toString();
        telephone = Telephone.getText().toString();


        if (userName.isEmpty()) {
            UserName.setError("Provide first name");
            return false;
        } else if (FarmName.isEmpty()) {
            InputFarmB.setError("Provide farm Name");
            return false;
        } else if (email.isEmpty()) {
            Email.setError("Provide email");
            return false;
        } else if (telephone.isEmpty()) {
            Telephone.setError("Provide county");
            return false;
        } else {

            return true;
        }

    }

    private boolean validation2() {
        InputCode = VerifyInput.getText().toString();

        if (InputCode.isEmpty()) {
            Toast.makeText(this, "Provide OTP Code sent to you", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }

    }


    private Toast backToast;

    private void ToastBack(String message) {

        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }


}