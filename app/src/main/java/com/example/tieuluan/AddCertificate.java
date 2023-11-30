package com.example.tieuluan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tieuluan.model.Account;
import com.example.tieuluan.model.Certificate;
import com.example.tieuluan.model.Student;
import com.example.tieuluan.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddCertificate extends AppCompatActivity {

    private EditText dateStart, edtNameCertificate, edtLevel;
    private Button btnAddCer, btnBackCer, btnCancelCer;
    private String stdId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);
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
                finish();//trở về màn hình trước đó
            }
        });

        btnCancelCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xóa hết các text
                edtNameCertificate.setText("");
                edtLevel.setText("");
                dateStart.setText("");
            }
        });

        btnAddCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameCer = edtNameCertificate.getText().toString().trim();
                String level = edtLevel.getText().toString().trim();
                String date = dateStart.getText().toString().trim();


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("dbCertificate");

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
                            id = "950001";
                        }

                        Certificate certificate = new Certificate(id, nameCer, level, date, stdId);
                        myRef.child(id).setValue(certificate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //thêm thành công
                                Toast.makeText(getApplicationContext(), "Thêm chứng chỉ thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //them thất bại
                                Toast.makeText(getApplicationContext(), "Thêm chứng chỉ thất bại"+e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
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
        dateStart = findViewById(R.id.dateStart);
        edtNameCertificate = findViewById(R.id.edtNameCertificate);
        edtLevel = findViewById(R.id.edtLevel);
        btnAddCer = findViewById(R.id.btnAddCer);
        btnBackCer = findViewById(R.id.btnBackCer);
        btnCancelCer = findViewById(R.id.btnCancelCer);

        Intent intent=getIntent();
        //truyền khóa
        stdId = (String) intent.getSerializableExtra("STUDENTIDAdd");
    }
}
