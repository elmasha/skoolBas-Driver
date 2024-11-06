package com.app.driver.MainActivity;

import static com.app.driver.Common.DB;
import static com.app.driver.Common.UserDbRef;
import static com.app.driver.Common.mAuth;
import static com.app.driver.MainActivity.InboxActivity.roomID;
import static com.app.driver.MainActivity.InboxActivity.to_user_uid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Adapters.ChatsAdapter;
import com.app.driver.Models.Messages;
import com.app.driver.Models.MsgBody;
import com.app.driver.Models.UserAccount;
import com.app.driver.Notify.FcmNotificationsSender;
import com.app.driver.R;
import com.app.driver.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity {

    private EditText InputMessage;
    private ImageButton BtnSend;

    private TextView farmChatUser,farmName,farmNameInt;
    private RatingBar ratingBar;
    private String Message = "";

    RecyclerView RecyclerViewChats;
    private ChatsAdapter adapter;
    private TextView BackToMainChat;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        InputMessage = findViewById(R.id.InputMessage);
        BtnSend = findViewById(R.id.BtnSendMessage);
        RecyclerViewChats = findViewById(R.id.RecyclerViewChats);
        BackToMainChat = findViewById(R.id.BackToMainChat);
        farmChatUser  = findViewById(R.id.chat_user_row);
        farmName = findViewById(R.id.chat_farm_row);
        ratingBar = findViewById(R.id.chat_rate_row);
        farmNameInt = findViewById(R.id.chat_name_II);

        BackToMainChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });


        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message = InputMessage.getText().toString().trim();
                if (Message.equals("")) {
                    ToastBack("write a message");
                } else {
                    ToastBack("Sending..");
                    BtnSend.setEnabled(false);
                    SendMessage();

                }
            }
        });


        FetchChats();
        LoadDeviceToken();
        LoadInboxLastID();
       // CreateInbox();

    }
    private void CreateInbox(){
        FirebaseUtils.getChatRoomRef(roomID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   Messages messagesList = task.getResult().toObject(Messages.class);

                   FirebaseUtils.getUsersFromChatRoom(messagesList.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           if (task.isSuccessful()){



                           }
                       }
                   });

                }
            }
        });
    }


    private void FetchChats() {

        Query query = FirebaseUtils.getUserChatRoomRef(roomID)

                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MsgBody> options = new FirestoreRecyclerOptions.Builder<MsgBody>()
                .setQuery(query, MsgBody.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new ChatsAdapter(options);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        RecyclerViewChats.setHasFixedSize(true);
        RecyclerViewChats.setNestedScrollingEnabled(false);
        RecyclerViewChats.setLayoutManager(manager);
        RecyclerViewChats.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                RecyclerViewChats.smoothScrollToPosition(0);
            }
        });


    }


    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }

    private void SendMessage() {
        if (roomID != null) {


            if (to_uid.equals(mAuth.getCurrentUser().getUid())){
                HashMap<String, Object> store = new HashMap<>();
                store.put("content", Message);
                store.put("uid", mAuth.getCurrentUser().getUid());
                store.put("type", "text");
                store.put("created_at", FieldValue.serverTimestamp());


                HashMap<String, Object> stores = new HashMap<>();
                stores.put("last_msg", Message);
                stores.put("last_uid",mAuth.getCurrentUser().getUid());
                stores.put("status",true);
                stores.put("created_at", FieldValue.serverTimestamp());


                DocumentReference sdRef2 = FirebaseUtils.getChatRoomRef(roomID).collection("chats").document();
                DocumentReference sdRef1 = FirebaseUtils.getChatRoomRef(roomID);


                DB.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        transaction.update(sdRef1, stores);
                        transaction.set(sdRef2, store);
                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Transaction success!");
                        BtnSend.setEnabled(true);

                        String Title = fromUserName;
                        String Msg = Message;
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender( deviceToken, Title, Msg,
                                getApplicationContext(),ChatRoomActivity.this);
                        notificationsSender.SendNotifications();
                        InputMessage.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Transaction failure.", e);
                        ToastBack("Error." + e.getMessage());
                        BtnSend.setEnabled(true);
                    }
                });

            }else {
                HashMap<String, Object> store = new HashMap<>();
                store.put("content", Message);
                store.put("uid", mAuth.getCurrentUser().getUid());
                store.put("type", "text");
                store.put("created_at", FieldValue.serverTimestamp());


                HashMap<String, Object> stores = new HashMap<>();
                stores.put("last_msg", Message);
                stores.put("last_uid",mAuth.getCurrentUser().getUid());
                stores.put("status",false);
                stores.put("created_at", FieldValue.serverTimestamp());


                DocumentReference sdRef2 = FirebaseUtils.getChatRoomRef(roomID).collection("chats").document();
                DocumentReference sdRef1 = FirebaseUtils.getChatRoomRef(roomID);


                DB.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        transaction.update(sdRef1, stores);
                        transaction.set(sdRef2, store);
                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Transaction success!");
                        BtnSend.setEnabled(true);

                        String Title = fromUserName;
                        String Msg = Message;
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender( deviceToken, Title, Msg,
                                getApplicationContext(),ChatRoomActivity.this);
                        notificationsSender.SendNotifications();
                        InputMessage.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Transaction failure.", e);
                        ToastBack("Error." + e.getMessage());
                        BtnSend.setEnabled(true);
                    }
                });

            }



        }


    }


    private String deviceToken,from_inbox_uid,from_inbox_name;
    private void LoadDeviceToken(){
        if (to_user_uid != null){
            UserDbRef.document(to_user_uid)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e!=null){
                                return;
                            }
                            if (documentSnapshot.exists()) {
                                UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                                from_inbox_name = scm_user.getUserName();
                                deviceToken = scm_user.getDevice_token();
                                farmName.setText(scm_user.getFarm_name());
                                farmChatUser.setText(scm_user.getUserName());
                                String s=scm_user.getUserName().substring(0,2);
                                farmNameInt.setText(s);

//                                ToastBack(deviceToken);

                            }else{
                                ToastBack("EMPTY DOC"+e.getMessage().toString());
                            }
                        }
                    });

        }


    }

    private String fromUserName;
    private void LoadFromUSerDetails(String UID){
            UserDbRef.document(UID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e!=null){
                                return;
                            }
                            if (documentSnapshot.exists()) {
                                UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                                fromUserName = scm_user.getUserName();
//                                ToastBack(deviceToken);

                            }else{
                                ToastBack("EMPTY DOC"+e.getMessage().toString());
                            }
                        }
                    });




    }


    private String ToUserName;
    private void LoadToUSerDetails(String UID){
            UserDbRef.document(UID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e!=null){
                                return;
                            }
                            if (documentSnapshot.exists()) {
                                UserAccount scm_user = documentSnapshot.toObject(UserAccount.class);
                                ToUserName = scm_user.getUserName();
//                                ToastBack(deviceToken);

                            }else{
                                ToastBack("EMPTY DOC"+e.getMessage().toString());
                            }
                        }
                    });




    }

    private String from_uid,to_uid,last_uid;
    private void LoadInboxLastID(){
        if (roomID != null){
            FirebaseUtils.getChatRoomRef(roomID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Messages scm_user = documentSnapshot.toObject(Messages.class);
                        from_uid = scm_user.getFrom_uid();
                        last_uid = scm_user.getLast_uid();
                        to_uid = scm_user.getTo_uid();

                        LoadFromUSerDetails(mAuth.getCurrentUser().getUid());
                        LoadToUSerDetails(to_uid);

                    }else{
                        ToastBack("EMPTY DOC"+e.getMessage().toString());
                    }
                }
            });


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