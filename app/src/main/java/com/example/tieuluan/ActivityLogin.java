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
                    databaseReference.child("dbUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean userFound = false;

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String phone = userSnapshot.child("Phone").getValue(String.class);
                                String pass = userSnapshot.child("Password").getValue(String.class);

                                if (phone != null && pass != null &&
                                        phone.equals(username) && pass.equals(password)) {
                                    // Đăng nhập thành công
                                    Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    userFound = true;
                                    startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                    finish();
                                }
                            }

                            if (!userFound) {
                                Toast.makeText(ActivityLogin.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Error fetching data: " + error.getMessage());
                            Toast.makeText(ActivityLogin.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}