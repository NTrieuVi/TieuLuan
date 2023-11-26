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

import com.example.tieuluan.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EditUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edtFullName, edtAge, edtPhone;
    private Button btnUpdate, btnCancel, btnBack;
    private RadioGroup radioButton;

    private ImageButton btnChoosePhoto;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
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
                if(user!=null){
                    edtFullName.setText(user.getName()+"");
                    Log.e("TAG", "onClick: "+edtFullName );
                    edtAge.setText(user.getAge()+"");
                    Log.e("TAG", "onClick: "+edtAge );

                    edtPhone.setText(user.getPhone()+"");
                    Log.e("TAG", "onClick: "+edtPhone );



                    String radioButtonText=user.getStatus();
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
                    if (user.getImage() != null && user.getImage() instanceof String) {
                        String imageUrl = (String) user.getImage();
                        Picasso.get().load(imageUrl).into(btnChoosePhoto);
                    } else {
                        btnChoosePhoto.setImageResource(R.drawable.img_teacher);
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
                String id=user.getId();
                String status=user.getStatus().toString();
                Log.e("TAG", "onClick: "+phone );
                Log.e("TAG", "onClick: "+age );
                Log.e("TAG", "onClick: "+id);
                Log.e("TAG", "onClick: "+status);
//                ImageView img=user.getImage();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference("dbUser");
                myRef.child(id).child("name").setValue(fullName);
                myRef.child(id).child("age").setValue(age);
                myRef.child(id).child("phone").setValue(phone);
                myRef.child(id).child("status").setValue(status);

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
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);

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
        user= (User) intent.getSerializableExtra("USER");
        if(user!=null){
            edtFullName.setText(user.getName()+"");
            edtAge.setText(user.getAge()+"");
            edtPhone.setText(user.getPhone()+"");

            String radioButtonText=user.getStatus();
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
//            if (user.getImage() != null && user.getImage() instanceof String) {
//                String imageUrl = (String) user.getImage();
//                Picasso.get().load(imageUrl).into(imageView);
//            } else {
//                imageView.setImageResource(R.drawable.img_teacher);
//            }

        }
        else {
            Toast.makeText(this,"Lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
        }
    }

}