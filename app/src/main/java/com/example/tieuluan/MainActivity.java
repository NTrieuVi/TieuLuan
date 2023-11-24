package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tieuluan.adapter.StudentAdapter;
import com.example.tieuluan.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  ListView listStudent;
    private ArrayList<Student> studentArrayList;
    private StudentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
//        title.setText("Student Application");


        listStudent=findViewById(R.id.listStudent);
//        Khởi tạo danh sách
        studentArrayList=new ArrayList<>();
        GetData();
//        studentArrayList.add(new Student(R.drawable.img,"1","Nguyễn Triệu Vi",
//                "20","010201010","Normal"));
//        studentArrayList.add(new Student(R.drawable.img,"2","Nguyễn Văn A",
//                "18","191919199","Normal"));
//        studentArrayList.add(new Student(R.drawable.img,"3","Nguyễn Văn B",
//                "30","292010381","Normal"));
//        studentArrayList.add(new Student(R.drawable.img,"4","Nguyễn Văn C",
//                "22","394949499","Normal"));

//        tạo adapter gán cho listview
        adapter=new StudentAdapter(this,
                R.layout.activity_item_card,
                 studentArrayList);

//        set adapter cho listview
        listStudent.setAdapter(adapter);
    }

//Lấy danh sách từ firebase
    private void  GetData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRefer=database.getReference("dbStudent");


        myRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                xóa dl trên listview
                adapter.clear();
                for(DataSnapshot data : snapshot.getChildren()){
//convert data
                    Student student=data.getValue(Student.class);

//Thêm student vào list
                    student.setId(data.getKey());
                    adapter.add(student);
                    Log.d("MYTAG", "onDataChange: "+student.getName());

                }
                Toast.makeText(getApplicationContext(),"Load data success", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Load data failed"+error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCacelled: "+error.toString());
            }
        });
    }
}