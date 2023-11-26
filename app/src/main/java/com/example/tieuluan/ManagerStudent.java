package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tieuluan.adapter.StudentAdapter;
import com.example.tieuluan.adapter.UserAdapter;
import com.example.tieuluan.model.Student;
import com.example.tieuluan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerStudent extends AppCompatActivity {
    private  ListView listStudent;
    private ArrayList<Student> studentArrayList;
    private StudentAdapter studentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_student);
//thanh actionbar
//        ImageView backIcon=findViewById(R.id.back_icon);
//        ImageView menuIcon=findViewById(R.id.menu_icon);
//        TextView title=findViewById(R.id.nameApp);
//
//        backIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "You were back", Toast.LENGTH_SHORT).show();
//            }
//        });
//        menuIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "You were choose menu", Toast.LENGTH_SHORT).show();
//            }
//        });
//        title.setText("User Application");

        Button back = findViewById(R.id.btnStdBackMain);
        back.setOnClickListener(v -> {
            finish();
        });

        listStudent=findViewById(R.id.listStudent);
        studentArrayList=new ArrayList<>();
        GetDataStudent();

        studentAdapter = new StudentAdapter(this,
                R.layout.activity_student_item,
                studentArrayList);

        listStudent.setAdapter(studentAdapter);

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy thông tin từ item được chọn
                Student selectedStudent = studentArrayList.get(position);
                Toast.makeText(ManagerStudent.this,"Item click at póition"+position,Toast.LENGTH_LONG).show();
                // Tạo Intent và chuyển dữ liệu sang ActivityDetailStudent
                Intent intent = new Intent(ManagerStudent.this, ActivityDetailStudent.class);
                intent.putExtra("STUDENT_NAME", selectedStudent.getName());
                intent.putExtra("STUDENT_DEPARTMENT", selectedStudent.getDepartment());
                intent.putExtra("STUDENT_PHONE", selectedStudent.getPhone());
                intent.putExtra("STUDENT_GENDER", selectedStudent.getGender());
//                intent.putExtra("STUDENT_IMAGE_URL", selectedStudent.getAvatar());
              // Bắt đầu ActivityDetailStudent
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            Intent intent = new Intent(this, AddStudent.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.menuDelete){

            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    //Lấy danh sách từ firebase
    private void  GetDataStudent(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRefer=database.getReference("dbStudent");

        myRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentAdapter.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    Student student = data.getValue(Student.class);

                    student.setId(data.getKey());
                    studentAdapter.add(student);
                }
                Toast.makeText(getApplicationContext(),"Load data success", Toast.LENGTH_LONG).show();
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Load data failed"+error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCacelled: "+error.toString());
            }
        });
    }

}