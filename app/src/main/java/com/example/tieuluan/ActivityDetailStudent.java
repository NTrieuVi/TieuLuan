package com.example.tieuluan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tieuluan.model.Student;
import com.squareup.picasso.Picasso;

public class ActivityDetailStudent extends AppCompatActivity {

    private String StudentId;
    private TextView tvDetailName;
    private TextView tvDetailDepartment;
    private TextView tvDetailPhone;
    private TextView tvDetailGender;
    private Button btnBackDetail, btnCertificate;
    private ImageView imgStudentDetail;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);

        addControls(); // Khai báo control trên giao diện
        addEvent();    // Khai báo sự kiện

        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailStudent.this, ManagerCertificate.class);
                intent.putExtra("STUDENTID",StudentId);
                //mở mh
                startActivity(intent);
            }
        });
    }

    private void addEvent() {
        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String userType = preferences.getString("userType", "");
            @Override
            public void onClick(View v) {
                if ("User".equals(userType)) {

                    finish();
                } else {
                    Intent intent = new Intent(ActivityDetailStudent.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void addControls() {
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailDepartment = findViewById(R.id.tvDetailDepartment);
        tvDetailGender = findViewById(R.id.tvDetailGender);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        btnBackDetail = findViewById(R.id.btnBackDetail);
        btnCertificate = findViewById(R.id.btnCertificate);
        imgStudentDetail = findViewById(R.id.imgDetailImage);

        Intent intent=getIntent();
        //truyền khóa
        student = (Student) intent.getSerializableExtra("STUDENT");
        if(student != null){
            StudentId = student.getId();
            tvDetailName.setText(student.getName()+"");
            tvDetailDepartment.setText(student.getDepartment()+"");
            tvDetailPhone.setText(student.getPhone()+"");
            tvDetailGender.setText(student.getGender()+"");
        }
        else {
            Toast.makeText(this,"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
        }
    }
}
