package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUSer = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSend = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    private String currUserId;
    private String otherUserId;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = firebaseDatabase.getReference("Users");
    private DatabaseReference referenceMessanges = firebaseDatabase.getReference("Messages");

    public ChatViewModel(String currUserId, String otherUserId) {
        this.currUserId = currUserId;
        this.otherUserId = otherUserId;
        referenceUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUSer.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceMessanges.child(currUserId).child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messages.setValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUSer() {
        return otherUSer;
    }

    public LiveData<Boolean> getMessageSend() {
        return messageSend;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendMessage(Message message){
        referenceMessanges
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceMessanges
                                .child(message.getReceiverId())
                                .child(message.getSenderId())
                                .push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageSend.setValue(true);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }

    public void SetUserOnline(Boolean isOnline){
        referenceUsers.child(currUserId).child("online").setValue(isOnline);
    }
}
