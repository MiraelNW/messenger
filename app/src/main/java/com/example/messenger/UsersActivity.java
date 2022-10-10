package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private UsersAdapter usersAdapter;
    private  UsersViewModel usersViewModel;
    public static final String EXTRA_CURR_USER_ID = "CURR_ID";
    private String currUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();
        currUserId = getIntent().getStringExtra(EXTRA_CURR_USER_ID );

        init();
        usersAdapter = new UsersAdapter();
        rvUsers.setAdapter(usersAdapter);
        usersAdapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = ChatActivity.newIntent(UsersActivity.this, currUserId, user.getId());
                startActivity(intent);
            }
        });
    }

    private void init(){
        rvUsers = findViewById(R.id.rvUsers);
    }

    private void observeViewModel(){
        usersViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser == null){
                    Intent intent = LoginActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
        usersViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersAdapter.setUsers(users);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item_logout){
            usersViewModel.logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersViewModel.SetUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersViewModel.SetUserOnline(false);
    }

    public  static Intent newIntent(Context context, String currUserId){
        Intent intent =  new Intent(context,UsersActivity.class);
        intent.putExtra(EXTRA_CURR_USER_ID , currUserId);
        return intent;
    }
}