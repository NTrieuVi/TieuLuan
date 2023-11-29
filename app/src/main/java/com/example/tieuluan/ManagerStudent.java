package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.tieuluan.adapter.StudentAdapter;
import com.example.tieuluan.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManagerStudent extends AppCompatActivity {
    private ListView listStudent;
    private ArrayList<Student> studentArrayList;

    private StudentAdapter studentAdapter;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_student);

        listStudent = findViewById(R.id.listStudent);
        studentArrayList = new ArrayList<>();
        GetDataStudent();

        studentAdapter = new StudentAdapter(this,
                R.layout.activity_student_item,
                studentArrayList);
        listStudent.setAdapter(studentAdapter);
        GetDataStudent();

        // Back home
        Button back = findViewById(R.id.btnStdBackMain);
        back.setOnClickListener(v -> {
            finish();
        });

        // Search student
        edtSearch = findViewById(R.id.edtSearchStudent);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Gọi phương thức để lọc danh sách sinh viên khi người dùng nhập liệu
                filterStudentList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    GetDataStudent();
                }
            }
        });
        //sort student

        ImageView imgSort=findViewById(R.id.imgSort);
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu(v);
            }
        });

        // click item ->detail student
        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy thông tin từ item được chọn
                Student selectedStudent = studentArrayList.get(position);
                Log.d("ListViewClick", "Item clicked at position " + position);
                Toast.makeText(ManagerStudent.this, "Item click at position" + position, Toast.LENGTH_LONG).show();
                // Tạo Intent và chuyển dữ liệu sang ActivityDetailStudent
                Intent intent = new Intent(ManagerStudent.this, ActivityDetailStudent.class);
                intent.putExtra("STUDENT_NAME", selectedStudent.getName());
                intent.putExtra("STUDENT_DEPARTMENT", selectedStudent.getDepartment());
                intent.putExtra("STUDENT_PHONE", selectedStudent.getPhone());
                intent.putExtra("STUDENT_GENDER", selectedStudent.getGender());
                startActivity(intent);
            }
        });
    }

    // search
    private void filterStudentList(String searchCriteria) {
        ArrayList<Student> filteredList = new ArrayList<>();
        for (Student student : studentArrayList) {
            // tìm kiếm sinh viên
            if (student.getName().toLowerCase().contains(searchCriteria.toLowerCase())) {
                filteredList.add(student);
            }
        }
        // Cập nhật Adapter với danh sách đã lọc
        studentAdapter.clear();
        studentAdapter.addAll(filteredList);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menuAdd) {
            Intent intent = new Intent(this, AddStudent.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.menuDelete) {
            // Handle delete action
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    // Hiển thị PopupMenu khi nhấn vào nút sắp xếp
    private void showSortMenu(View v) {
        PopupMenu sortMenu = new PopupMenu(this, v);
        MenuInflater inflater = sortMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_sort, sortMenu.getMenu());
        GetDataStudent();

        // Xử lý sự kiện khi chọn một tùy chọn trong menu
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuSortByName) {
                    sortByName();
                    return true;
                }
                else if (item.getItemId() == R.id.menuSortByGenderFemale) {
                    sortByGender("Female");
                    return true;

                }
                else if (item.getItemId() == R.id.menuSortByGenderMale) {
                    sortByGender("Male");
                    return true;

                }
                else if (item.getItemId() == R.id.menuSortByStudentId) {
                    sortByStudentId();
                    return true;

                }
                return false;
            }
        });

        // Hiển thị PopupMenu
        sortMenu.show();
    }

    // Sắp xếp theo tên
    private void sortByName() {
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                return student1.getName().compareTo(student2.getName());
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // Sắp xếp theo giới tính
    private void sortByGender(String gender) {
        ArrayList<Student> filteredList = new ArrayList<>();
        for (Student student : studentArrayList) {
            if (student.getGender().equalsIgnoreCase(gender)) {
                filteredList.add(student);
            }
        }
        studentAdapter.clear();
        studentAdapter.addAll(filteredList);
        studentAdapter.notifyDataSetChanged();
    }

    // Sắp xếp theo mã số sinh viên
    private void sortByStudentId() {
        Collections.sort(studentArrayList, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                return student1.getId().compareTo(student2.getId());
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // Lấy danh sách từ firebase
    private void GetDataStudent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefer = database.getReference("dbStudent");

        myRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentAdapter.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Student student = data.getValue(Student.class);

                    student.setId(data.getKey());
                    studentAdapter.add(student);
                }
                Toast.makeText(getApplicationContext(), "Load data success", Toast.LENGTH_LONG).show();
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Load data failed" + error.toString(), Toast.LENGTH_LONG).show();
                Log.d("MYTAG", "onCancelled: " + error.toString());
            }
        });
    }
}
