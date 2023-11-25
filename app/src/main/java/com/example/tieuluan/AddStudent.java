package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tieuluan.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddStudent extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edtFullName, edtAge, edtPhone;
    private Button btnAdd, btnCancel, btnBack;
    private RadioGroup radioButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
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
                //thêm sinh viên vào database
                String fullName=edtFullName.getText().toString().trim();
                String age=edtAge.getText().toString().trim();
                String phone=edtPhone.getText().toString().trim();


                int selectedRadioId=radioButton.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioId);
                String radioButtonText = selectedRadioButton.getText().toString().trim();


//                imageView
//                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//                String imgBase64 = bitmapToBase64(bitmap);
                String imgUrl="";
//                String imageUrl = editText.getText().toString().trim();



                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("dbStudent");
//                tạo id ngẫu nhiên
                String id=myRef.push().getKey();
//                thêm dl sinh viên
                Student student = new Student(id,"", fullName, age, phone, radioButtonText);
                myRef.child(id).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //thêm thành công
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                        finish();//thoát màn hình thêm
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //them thất bại
                        Toast.makeText(getApplicationContext(), "Thêm thất bại"+e.toString(), Toast.LENGTH_LONG).show();
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
        imageView = findViewById(R.id.imgStudent);
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