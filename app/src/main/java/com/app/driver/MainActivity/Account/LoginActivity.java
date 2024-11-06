package com.app.driver.MainActivity.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.driver.Common;
import com.app.driver.MainActivity.MapsActivity;
import com.app.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.app.driver.Common.DB;
import static com.app.driver.Common.DriverDbRef;
import static com.app.driver.Common.TOKENRef;
import static com.app.driver.Common.mAuth;
import static com.app.driver.WalkThroughActivity.isConnectionAvailable;

public class LoginActivity extends AppCompatActivity {
    private TextView toRegister, toMain, toPhone, Resendit, timer;
    private String email, password;
    private Button LoginBtn, BtnVerify;

    private LinearLayout SmsLayOut, OtpLayOut;
    private boolean BtnState = true;
    private CountryCodePicker CodePicker;
    private EditText InputPhone, InputVerify;
    private String phone_number, verifyCode;

    private static final long START_TIME_IN_MILLIS_COUNT = 27000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId, phone;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private static final long START_TIME_IN_MILLIS_COUNT2 = 27000;
    private CountDownTimer mCountDownTimer2;
    private boolean mTimerRunning2;
    private long mTimeLeftInMillis2 = START_TIME_IN_MILLIS_COUNT2;


    private ProgressDialog progressDialog22;
    private String Phone_auth;
    private String User_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toRegister = findViewById(R.id.ToRegister);
        LoginBtn = findViewById(R.id.Btn_loginL);
        Btn_Verify = findViewById(R.id.Btn_verify_codeL);
        CodePicker = findViewById(R.id.Phone_picker_loginL);
        InputPhone = findViewById(R.id.phone_no_loginL);
        InputVerify = findViewById(R.id.otp_login_L);
        SmsLayOut = findViewById(R.id.Sms_LL);
        OtpLayOut = findViewById(R.id.Otp_LL);
        toPhone = findViewById(R.id.ToPhoneL);
        Resendit = findViewById(R.id.ResenditL);
        timer = findViewById(R.id.timerL);


        CodePicker.registerCarrierNumberEditText(InputPhone);
        CodePicker.getDefaultCountryCode();


        Resendit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsLayOut.setVisibility(View.VISIBLE);
                OtpLayOut.setVisibility(View.GONE);
                BtnState = true;
            }
        });


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = InputPhone.getText().toString();

                if (!isConnectionAvailable(getApplicationContext())) {
                    ToastBack("No internet connection");
                } else {
                    if (!validation()) {

                    } else {
                        if (phone.equals(Common.Phone)) {

                            Intent logout = new Intent(getApplicationContext(), MapsActivity.class);
                            logout.putExtra("Phone", phone);
                            logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logout);
                            finish();

                        } else {
                            Phone_auth = CodePicker.getFullNumberWithPlus();
                            QueryDatabase11(Phone_auth);
                        }
                    }
                }


            }
        });


        Btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validation2()) {
                } else {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifyCode);
                    signInWithPhoneAuthCredential(credential);

                }

//                if (!isConnectionAvailable(getApplicationContext())){
//                    ToastBack("No internet connection");
//                }else {
//
//
//                }
            }
        });


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toreg = new Intent(getApplicationContext(), RegisterActivity.class);
                toreg.putExtra("USER_STATE", User_state);
                toreg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toreg);
                finish();
            }
        });


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                SmsLayOut.setVisibility(View.VISIBLE);
                OtpLayOut.setVisibility(View.GONE);
                timer.setVisibility(View.GONE);
                BtnState = true;
                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                ToastBack("El_error" + e.getMessage());
                SmsLayOut.setVisibility(View.VISIBLE);
                OtpLayOut.setVisibility(View.GONE);
                timer.setVisibility(View.GONE);
                BtnState = true;
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken Token) {

                mVerificationId = verificationId;
                mToken = Token;
                SmsLayOut.setVisibility(View.GONE);
                OtpLayOut.setVisibility(View.VISIBLE);
                progressDialog22.dismiss();
                toPhone.setText(phone_number);
                BtnState = false;
                timer.setVisibility(View.VISIBLE);
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
                Resendit.setVisibility(View.VISIBLE);

            }
        }.start();
        mTimerRunning2 = true;

    }

    private EditText code1;
    private ProgressBar progressBar_verify;
    private TextView resend, sentTO, closeDia;
    private Button Btn_Verify;

    private ProgressDialog progressDialog;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {


        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Random dice = new Random();
                    int webCode = dice.nextInt(1000000) + 1;
                    String uid = mAuth.getCurrentUser().getUid();
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Please wait signing Up...");
                    progressDialog.show();
                    String token_Id = FirebaseInstanceId.getInstance().getToken();
                    timer.setVisibility(View.GONE);
                    HashMap<String, Object> updates = new HashMap<>();
                    updates.put("device_token", token_Id);
                    updates.put("webCode", webCode);


                    HashMap<String, Object> token = new HashMap<>();
                    token.put("token", token_Id);


                    DocumentReference sdRefUSer = DriverDbRef.document(mAuth.getCurrentUser().getUid());
                    DocumentReference sdRefToken = TOKENRef.document(mAuth.getCurrentUser().getUid());


                    DB.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                            // Note: this could be done without a transaction
                            //       by updating the population using FieldValue.increment()
                            transaction.update(sdRefUSer, updates);
                            transaction.set(sdRefToken, token);
                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Transaction success!");
                            ToastBack("Authentication Succeeded ");
                            Intent toreg = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(toreg);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Transaction failure.", e);
                            ToastBack("Error." + e.getMessage());
                        }
                    });


                    pauseTimer2();
                    resetTimer2();
                    //...
                } else {
                    // Sign in failed, display a message and update the UI
                    ToastBack("Sign in failed");
                    timer.setVisibility(View.GONE);
                    Btn_Verify.setVisibility(View.VISIBLE);
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
        if (timer != null) {
            timer.setText(timeLeftFormatted);
        }

    }
    ///----VERIFICATION Timer ----////


    private void QueryDatabase11(String tel) {
        progressDialog22 = new ProgressDialog(LoginActivity.this);
        progressDialog22.setMessage("Please wait..");
        progressDialog22.show();


        DriverDbRef.whereEqualTo("Phone_NO", tel).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {

                    String msg = "This account does not exist";
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText(msg).setConfirmText("Close").show();
                    ToastBack(msg);
                    progressDialog22.dismiss();

                } else {
                    phone_number = CodePicker.getFullNumberWithPlus();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 60, TimeUnit.SECONDS, LoginActivity.this, mCallBacks);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private boolean validation() {
        phone_number = InputPhone.getText().toString();

        if (phone_number.isEmpty()) {
            ToastBack("Provide Phone number");
            return false;
        } else {

            return true;
        }

    }


    private boolean validation2() {
        verifyCode = InputVerify.getText().toString().trim();

        if (verifyCode.isEmpty()) {
            ToastBack("Provide Verification sent to " + phone_number);
            return false;
        } else {
            return true;
        }

    }


    private void showToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }


    Toast backToast;

    public void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
//        View view = backToast.getView();
//
//        //Gets the actual oval background of the Toast then sets the colour filter
//        view.getBackground().setColorFilter(Color.parseColor("#0BF4DE"), PorterDuff.Mode.SRC_IN);
//
//        //Gets the TextView from the Toast so it can be editted
//        TextView text = view.findViewById(android.R.id.message);
//        text.setTextColor(Color.parseColor("#1C1B2B"));
        backToast.show();
    }

}