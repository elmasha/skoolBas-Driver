package com.app.driver.MainActivity;

import static com.app.driver.Common.DB;
import static com.app.driver.Common.mAuth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driver.Adapters.InboxAdapter;
import com.app.driver.Models.Chatroom;
import com.app.driver.Models.Messages;
import com.app.driver.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InboxActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //widgets
    private ProgressBar mProgressBar;

    //vars
    private ArrayList<Chatroom> mChatrooms = new ArrayList<>();
    private Set<String> mChatroomIds = new HashSet<>();
    //    private Set<Chatroom> mChatrooms = new HashSet<>();
    private RecyclerView mChatroomRecyclerView;
    private ListenerRegistration mChatroomEventListener;
    private FirebaseFirestore mDb;
    private FloatingActionButton floatingActionButton;

    private TextView BackToMainInbox;
    private ImageView ErrorImage;

    private InboxAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
       FetchInbox();

    }



    @Override
    protected void onResume() {
        super.onResume();
        FetchInbox();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        mProgressBar = findViewById(R.id.progressBar);
        mChatroomRecyclerView = findViewById(R.id.chatrooms_recycler_view);
        floatingActionButton = findViewById(R.id.fab_create_chatroom);
        ErrorImage  = findViewById(R.id.ErrorImage);

        BackToMainInbox = findViewById(R.id.BackToMainInbox);

        BackToMainInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPress();
            }
        });

        FetchInbox();
        FetchInboxCount();

    }


    public static String roomID,to_user_uid;
    private void FetchInbox(){
        Query query = DB.collection("Inbox")
                .whereArrayContains("userId",mAuth.getCurrentUser().getUid())
                .orderBy("status",Query.Direction.ASCENDING)
                .orderBy("created_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Messages> options = new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(query, Messages.class)
                .setLifecycleOwner(this)
                .setLifecycleOwner(this)
                .build();
        adapter = new InboxAdapter(options);

        mChatroomRecyclerView.setHasFixedSize(false);
        mChatroomRecyclerView.setNestedScrollingEnabled(false);
        mChatroomRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mChatroomRecyclerView.setAdapter(adapter);





        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
               delete23();
            }
        }).attachToRecyclerView(mChatroomRecyclerView);




        adapter.setOnItemClickListener(new InboxAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                roomID = documentSnapshot.getId();
                Messages messages = documentSnapshot.toObject(Messages.class);
                List<String> list ;



                if (mAuth.getCurrentUser().getUid().equals(messages.getUserId().get(0))){
                    to_user_uid = messages.getUserId().get(1);
                }else {

                    to_user_uid = messages.getUserId().get(0);

                }

                if (roomID != null | to_user_uid !=null){
                    Intent toChat = new Intent(getApplicationContext(),ChatRoomActivity.class);
                    startActivity(toChat);
                    HashMap<String ,Object> notify = new HashMap<>();
                    notify.put("status",true);
                    adapter.UpdateItem(position,notify);
                }


            }
        });

    }



    //------Count Category---//
    ArrayList<Object> uniqueDatesReview3 = new ArrayList<Object>();
    int sumReview3 = 0;

    private void FetchInboxCount() {

        DB.collection("Inbox")
                .whereArrayContains("userId",mAuth.getCurrentUser().getUid())
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

                            if (sumReview3 > 0) {

                                ErrorImage.setVisibility(View.GONE);
                            } else {

                                ErrorImage.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    }
                });

    }
    //==end




    private Toast backToast;

    private void ToastBack(String message) {
        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        backToast.show();
    }

    private int deletePosition ;
    private void delete23(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Delete Conversation");
        builder.setMessage("Are you sure you want to delete this conversation ?");
//        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteItem(deletePosition);
                        Toast.makeText(getApplicationContext(), "Conversation deleted", Toast.LENGTH_SHORT).show();
                       FetchInbox();
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FetchInbox();
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }



    private void BackPress(){
        super.onBackPressed();
    }



    @Override
    public void onBackPressed() {
       BackPress();
    }
}