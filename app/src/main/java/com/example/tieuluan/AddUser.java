package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddUser extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edtFullName, edtAge, edtPhone;
    private Button btnAdd, btnCancel, btnBack;
    private RadioGroup radioButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        addControls(); //Khai báo control trên giao diện
        addEvent(); //khai báo sự kiện
    }

    private void addEvent() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//trở về màn hình trước đó
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xóa hết các text
                edtAge.setText("");
                edtPhone.setText("");
                edtFullName.setText("");
                radioButton.clearCheck();
                imageView.setImageResource(R.drawable.add);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName=edtFullName.getText().toString().trim();
                String age=edtAge.getText().toString().trim();
                String phone=edtPhone.getText().toString().trim();


                int selectedRadioId=radioButton.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioId);
                String radioButtonText = selectedRadioButton.getText().toString().trim();


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("dbUser");
                DatabaseReference myRef2 = database.getReference("dbAccount");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String id;
                        int maxInt = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.hasChild("id")) {
                                String currentId = snapshot.child("id").getValue(String.class);
                                try {
                                    int currentInt = Integer.parseInt(currentId);
                                    if (currentInt > maxInt) {
                                        maxInt = currentInt;
                                    }
                                } catch (NumberFormatException e) {
                                    Log.e("ConversionError", "Cannot convert string to int", e);
                                }
                            }
                        }

                        if (maxInt != 0) {
                            int incrementedValue = maxInt + 1;
                            id = String.valueOf(incrementedValue);
                        } else {
                            id = "10001";
                        }

                        User user = new User(id,"", fullName, age, phone, radioButtonText);
                        myRef.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //thêm thành công
                                Toast.makeText(getApplicationContext(), "Thêm user thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //them thất bại
                                Toast.makeText(getApplicationContext(), "Thêm user thất bại"+e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                        Account account = new Account(id,id,"User");
                        myRef2.child(id).setValue(account).addOnSuccessListener(unused -> finish()).addOnFailureListener(e -> {});
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FirebaseError", "Error reading data", databaseError.toException());
                    }
                });



            }
        });
    }


    @SuppressLint("WrongViewCast")
    private void addControls() {
        edtFullName = findViewById(R.id.edtFullName);
        edtAge = findViewById(R.id.edtAge);
        edtPhone = findViewById(R.id.edtPhone);
        radioButton = findViewById(R.id.btnStatus);
        imageView = findViewById(R.id.imgUser);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở Intent để chọn ảnh từ thư viện
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy Uri của ảnh được chọn
            Uri imageUri = data.getData();

            // Hiển thị ảnh trên ImageView
            Picasso.get().load(imageUri).into(imageView);
        }
    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}