package com.example.buddyhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
/**
 * Registering a new user on Firebase
 */
public class RegisterActivity extends AppCompatActivity {

    private TextView register_button;
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private FirebaseAuth authenticate;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // don't show the action bar if we can’t get the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        register_button = findViewById(R.id.register_button);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userReference = FirebaseDatabase.getInstance().getReference();
        authenticate = FirebaseAuth.getInstance();

        //when register button is clicked, user information will be registered and save into codebase
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(username.getText().toString(), name.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void register(String username, String name, String email, String password) {
        authenticate.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    String userid = authenticate.getCurrentUser().getUid();
                    userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                    // put user information in firebase
                    HashMap<String, Object> map = new HashMap<> ();
                    map.put("id",authenticate.getCurrentUser().getUid());
                    map.put("username",username);
                    map.put("name",name);
                    map.put("email",email);
                    map.put("password",password);
                    map.put("bio", "");
                    map.put("profile_image", "default");
                    userReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                    });
        }
        });
    }
}