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

import com.example.tieuluan.model.Account;
import com.example.tieuluan.model.Student;
import com.example.tieuluan.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tieuluan-2cfe4-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("dbAccount");

        Account account = new Account("admin", "admin", "Admin");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("admin").exists()) {
                } else {
                    myRef.child("admin").setValue(account);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        edtUserName=findViewById(R.id.edtUsename);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();

                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your username or password",Toast.LENGTH_LONG).show();
                }
                else {
                    databaseReference.child("dbAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean userFound = false;

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String id = userSnapshot.child("id").getValue(String.class);
                                String pass = userSnapshot.child("password").getValue(String.class);
                                String role = userSnapshot.child("role").getValue(String.class);

                                if (id != null && pass != null &&
                                        id.equals(username) && pass.equals(password)) {
                                    // Đăng nhập thành công
                                    Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    userFound = true;
                                    if(Objects.equals(role, "Admin"))
                                        startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                    else if(Objects.equals(role, "User"))
                                        startActivity(new Intent(ActivityLogin.this, ManagerStudent.class));
                                    else {
                                        DatabaseReference myRefChild= FirebaseDatabase.getInstance().getReference("dbStudent").child(username);
                                        myRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Student student = dataSnapshot.getValue(Student.class);
                                                    Intent intent = new Intent(ActivityLogin.this, ActivityDetailStudent.class);
                                                    intent.putExtra("STUDENT",student);
                                                    //mở mh
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });
                                    }

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