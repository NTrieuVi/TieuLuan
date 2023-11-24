package com.example.tieuluan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditStudentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edtFullName, edtAge, edtPhone;
    private Button btnUpdate, btnCancel, btnBack;
    private RadioGroup radioButton;
    private ImageView imageView;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student!=null){
                    edtFullName.setText(student.getName()+"");
                    edtAge.setText(student.getAge()+"");
                    edtPhone.setText(student.getPhone()+"");

                    String radioButtonText=student.getStatus();
                    RadioGroup radioGroup = findViewById(R.id.btnStatus);
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        View view = radioGroup.getChildAt(i);
                        if (view instanceof RadioButton) {
                            RadioButton radioButton = (RadioButton) view;
                            if (radioButtonText.equals(radioButton.getText().toString())) {
                                radioButton.setChecked(true);
                                break;
                            }
                        }
                    }
                    //                imageView
                    if (student.getImage() != null && student.getImage() instanceof String) {
                        String imageUrl = (String) student.getImage();
                        Picasso.get().load(imageUrl).into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.img_student);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
                }

            }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cập nhật
                String fullName=edtFullName.getText().toString();
                String phone=edtPhone.getText().toString();
                String age=edtAge.getText().toString();
                String id=student.getId();
                String status=student.getStatus().toString();
//                ImageView img=student.getImage();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference("dbStudent");
                myRef.child(id).child("Name").setValue(fullName);
                myRef.child(id).child("Age").setValue(age);
                myRef.child(id).child("Phone").setValue(phone);
                myRef.child(id).child("Status").setValue(status);

                //myRef.child(id).child("Image").setValue(img);
                finish();
                Toast.makeText(getApplicationContext(),"Chỉnh sửa thành công",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        edtFullName = findViewById(R.id.edtFullName);
        edtAge = findViewById(R.id.edtAge);
        edtPhone = findViewById(R.id.edtPhone);
        radioButton = findViewById(R.id.btnStatus);
        imageView = findViewById(R.id.imgStudent);
        btnUpdate = findViewById(R.id.btnUpdate);
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
        //lấy gói tin được gửi từ màn hình ngoài
        Intent intent=getIntent();
        //truyền khóa
        student= (Student) intent.getSerializableExtra("STUDENT");
        if(student!=null){
            edtFullName.setText(student.getName()+"");
            edtAge.setText(student.getAge()+"");
            edtPhone.setText(student.getPhone()+"");

            String radioButtonText=student.getStatus();
            for (int i = 0; i < radioButton.getChildCount(); i++) {
                View view = radioButton.getChildAt(i);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButtonText.equals(radioButton.getText().toString())) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
            //                imageView
            if (student.getImage() != null && student.getImage() instanceof String) {
                String imageUrl = (String) student.getImage();
                Picasso.get().load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.img_student);
            }

        }
        else {
            Toast.makeText(this,"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
        }
    }

}