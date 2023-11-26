package com.example.tieuluan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.tieuluan.model.Student;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EditStudent extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editNameStudent, editDepartmentStudent, editPhoneStudent;
    private Button btnUpdate, btnCancelEdit, btnBackEdit;
    private RadioGroup radioButton;
    private RadioButton radioEditMale, radioEditFemale;
    private ImageButton btnChoosePhoto;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBackEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student!=null){
                    editNameStudent.setText(student.getName()+"");
                    editDepartmentStudent.setText(student.getDepartment()+"");
                    editPhoneStudent.setText(student.getPhone()+"");


                    String radioButtonText = student.getGender();
                    RadioGroup radioGroup = findViewById(R.id.btnEditGender);
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
                    if (student.getAvatar() != null && student.getAvatar() instanceof String) {
                        String imageUrl = (String) student.getAvatar();
                        Picasso.get().load(imageUrl).into(btnChoosePhoto);
                    } else {
                        btnChoosePhoto.setImageResource(R.drawable.img_student);
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
                String fullName = editNameStudent.getText().toString();
                String phone = editPhoneStudent.getText().toString();
                String department = editDepartmentStudent.getText().toString();
                String id = student.getId();
                String gender;
                if(radioEditMale.isChecked()){
                    gender = "Male";
                }
                else gender = "Female";

//                ImageView img=student.getImage();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference("dbStudent");
                myRef.child(id).child("name").setValue(fullName);
                myRef.child(id).child("department").setValue(department);
                myRef.child(id).child("phone").setValue(phone);
                myRef.child(id).child("gender").setValue(gender);

                //myRef.child(id).child("Image").setValue(img);
                finish();
                Toast.makeText(getApplicationContext(),"Chỉnh sửa thành công",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        editNameStudent = findViewById(R.id.editNameStudent);
        editDepartmentStudent = findViewById(R.id.editDepartmentStudent);
        editPhoneStudent = findViewById(R.id.editPhoneStudent);
        radioButton = findViewById(R.id.btnEditGender);
        radioEditMale = findViewById(R.id.radioEditMale);
        radioEditFemale = findViewById(R.id.radioEditFemale);
        btnChoosePhoto = findViewById(R.id.btnChoosePhotoStudent);
        btnUpdate = findViewById(R.id.btnUpdateStudent);
        btnBackEdit = findViewById(R.id.btnBackEdit);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
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
        student = (Student) intent.getSerializableExtra("STUDENT");
        if(student != null){
            editNameStudent.setText(student.getName()+"");
            editDepartmentStudent.setText(student.getDepartment()+"");
            editPhoneStudent.setText(student.getPhone()+"");

            String radioButtonText = student.getGender();
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
//            if (student.getImage() != null && student.getImage() instanceof String) {
//                String imageUrl = (String) student.getImage();
//                Picasso.get().load(imageUrl).into(imageView);
//            } else {
//                imageView.setImageResource(R.drawable.img_student);
//            }

        }
        else {
            Toast.makeText(this,"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
        }
    }

}
