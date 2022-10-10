package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_CURR_USER_ID = "CURR_ID";
    public static final String EXTRA_OTHER_USER_ID = "OTHER_ID";

    private TextView tvTitle;
    private View onlineStatus;
    private RecyclerView rvMessages;
    private EditText etMyText;
    private ImageView imageViewSendMessage;

    private MessangerAdapter messangerAdapter;

    private String currUserId;
    private String otherUserId;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        init();

        currUserId = getIntent().getStringExtra(EXTRA_CURR_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);

        viewModelFactory = new ChatViewModelFactory(currUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messangerAdapter = new MessangerAdapter(currUserId);
        rvMessages.setAdapter(messangerAdapter);

        observeViewModel();

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(etMyText.getText().toString().trim(), currUserId, otherUserId);
                viewModel.sendMessage(message);
            }
        });

    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messangerAdapter.setMessages(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getMessageSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    etMyText.setText("");
                }
            }
        });
        viewModel.getOtherUSer().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String info = String.format("%s %s", user.getName(), user.getLastName());
                tvTitle.setText(info);
                int backgroundResId;
                if (user.isOnline()) {
                    backgroundResId = R.drawable.circle_green;
                } else {
                    backgroundResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this,backgroundResId);
                onlineStatus.setBackground(background);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.SetUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.SetUserOnline(false);
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        onlineStatus = findViewById(R.id.onlineStatus);
        rvMessages = findViewById(R.id.rvMessages);
        etMyText = findViewById(R.id.etMyText);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    public static Intent newIntent(Context context, String currUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURR_USER_ID, currUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}