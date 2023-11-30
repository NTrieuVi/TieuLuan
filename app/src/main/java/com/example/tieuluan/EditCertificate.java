package com.example.tieuluan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tieuluan.model.Certificate;
import com.example.tieuluan.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditCertificate extends AppCompatActivity {
    private EditText dateStart, edtNameCertificate, edtLevel;
    private Button btnUpdateCer, btnBackCer, btnCancelCer;
    private Certificate certificate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_certificate);
        addControls();
        addEvent();

        dateStart.setOnFocusChangeListener((v, hasFocus) -> {
            showDatePickerDialog(v);
        });
    }

    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, month1, day1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = sdf.format(selectedCalendar.getTime());

            dateStart.setText(selectedDate);

        }, year, month, day);

        datePickerDialog.show();
    }

    private void addEvent() {
        btnBackCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancelCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(certificate != null){
                    edtNameCertificate.setText(certificate.getName()+"");
                    edtLevel.setText(certificate.getLevel()+"");
                    dateStart.setText(certificate.getDateStart()+"");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
                }

            }

        });
        btnUpdateCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cập nhật
                String nameCer = edtNameCertificate.getText().toString();
                String level = edtLevel.getText().toString();
                String date = dateStart.getText().toString();
                String id = certificate.getId();

//                ImageView img=user.getImage();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference("dbCertificate");
                myRef.child(id).child("name").setValue(nameCer);
                myRef.child(id).child("level").setValue(level);
                myRef.child(id).child("dateStart").setValue(date);

                finish();
                Toast.makeText(getApplicationContext(),"Chỉnh sửa thành công",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        dateStart = findViewById(R.id.editDateStart);
        edtNameCertificate = findViewById(R.id.editNameCertificate);
        edtLevel = findViewById(R.id.editLevel);
        btnUpdateCer = findViewById(R.id.btnUpdateCer);
        btnBackCer = findViewById(R.id.btnBackCerEdit);
        btnCancelCer = findViewById(R.id.btnCancelCerEdit);

        Intent intent = getIntent();
        certificate = (Certificate) intent.getSerializableExtra("CERTIFICATE");
        if(certificate != null){
            edtNameCertificate.setText(certificate.getName()+"");
            edtLevel.setText(certificate.getLevel()+"");
            dateStart.setText(certificate.getDateStart()+"");
        }
        else {
            Toast.makeText(this,"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
        }
    }

}