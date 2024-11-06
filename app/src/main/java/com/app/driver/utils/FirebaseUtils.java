package com.app.driver.utils;

import static com.app.driver.Common.mAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseUtils {

    public static DocumentReference getChatRoomRef(String chatroomId){
        return FirebaseFirestore.getInstance().collection("Inbox").document(chatroomId);
    }


    public static CollectionReference getAllUSerRef(){
        return FirebaseFirestore.getInstance().collection("i1-2Farm_User");
    }

    public static CollectionReference getUserChatRoomRef(String chatroomId){
        return getChatRoomRef(chatroomId).collection("chats");
    }

    public static String getChatRoomId(String userId1,String userId2){
        if (userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else {
            return userId2+"_"+userId1;
        }
    }


    public static DocumentReference getUsersFromChatRoom(List<String> userId){
        if (userId.get(0).equals(mAuth.getCurrentUser().getUid())){
            return getAllUSerRef().document(userId.get(0));
        }else {
            return getAllUSerRef().document(userId.get(1));
        }

    }

}
