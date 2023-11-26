package com.example.tieuluan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ActivityDetailStudent extends AppCompatActivity {

    private TextView tvDetailName;
    private TextView tvDetailDepartment;
    private TextView tvDetailPhone;
    private TextView tvDetailGender;
    private Button btnBackDetail;
    private ImageView imgStudentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);

        addControls(); // Khai báo control trên giao diện
        addEvent();    // Khai báo sự kiện

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String studentName = intent.getStringExtra("STUDENT_NAME");
            String studentDepartment = intent.getStringExtra("STUDENT_DEPARTMENT");
            String studentPhone = intent.getStringExtra("STUDENT_PHONE");
            String studentGender = intent.getStringExtra("STUDENT_GENDER");
//            String studentImageUrl = intent.getStringExtra("STUDENT_IMAGE_URL");

            Log.d("MyTag", "Name: " + studentName);
            Log.d("MyTag", "Department: " + studentDepartment);
            Log.d("MyTag", "Phone: " + studentPhone);
            Log.d("MyTag", "Gender: " + studentGender);
//            Log.d("MyTag", "Image URL: " + studentImageUrl);
            // Hiển thị thông tin chi tiết
            tvDetailName.setText("Name: " + studentName);
            tvDetailDepartment.setText("Department: " + studentDepartment);
            tvDetailPhone.setText("Phone: " + studentPhone);
            tvDetailGender.setText("Gender: " + studentGender);

            // Ảnh
//            if (studentImageUrl != null && !studentImageUrl.isEmpty()) {
//                Picasso.get().load(studentImageUrl).into(imgStudentDetail);
//            } else {
//                imgStudentDetail.setImageResource(R.drawable.img_student);
//            }
        }
    }

    private void addEvent() {
        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Trở về màn hình trước đó
            }
        });
    }

    private void addControls() {
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailDepartment = findViewById(R.id.tvDetailDepartment);
        tvDetailGender = findViewById(R.id.tvDetailGender);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        btnBackDetail = findViewById(R.id.btnBackDetail);
        imgStudentDetail = findViewById(R.id.imgDetailImage);
    }
}
