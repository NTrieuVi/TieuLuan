package com.example.tieuluan;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tieuluan-2cfe4-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName=findViewById(R.id.edtUsename);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=edtUserName.getText().toString();
                final String password=edtPassword.getText().toString();

                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your username or password",Toast.LENGTH_LONG).show();
                }
                else {
                    databaseReference.child("dbStudent").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot data : snapshot.getChildren()){
                                String username=data.child("Phone").getValue(String.class);
                                if(data.hasChild(username)){
                                    String getPassword = data.child("Password").getValue(String.class);

                                    Log.e(TAG, "onDataChange: "+username);

                                    if(getPassword.equals(password)){
                                        Toast.makeText(ActivityLogin.this,"Successfully login",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ActivityLogin.this,MainActivity.class));
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(ActivityLogin.this,"Wrong password",Toast.LENGTH_LONG).show();

                                    }
                                }
                                else {
                                    Toast.makeText(ActivityLogin.this,"Login failed",Toast.LENGTH_LONG).show();

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}