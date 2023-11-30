package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManagerStudent extends AppCompatActivity {
    private ListView listStudent;
    private ArrayList<Student> studentArrayList;
    private DatabaseReference myRefer;
    private StudentAdapter studentAdapter;
    private EditText edtSearch;
    private static final int READ_REQUEST_CODE = 42;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_student);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefer = database.getReference("dbStudent");
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
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String userType = preferences.getString("userType", "");

            if ("User".equals(userType)) {
                // Quay về màn hình đăng nhập
                Intent intent = new Intent(ManagerStudent.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            } else if ("Admin".equals(userType)) {
                // Quay về màn hình chính cho admin
                finish();
            }
        });


        // Search student
        edtSearch = findViewById(R.id.edtSearchStudent);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
        
        //import CSV


        Button btnImportStudents = findViewById(R.id.btnImportFile);
        btnImportStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        //Export CSV

        Button btnExportToCSV = findViewById(R.id.btnExportFile);
        btnExportToCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportFromFirebaseToCSV();
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

    // Import students CSV
    private void importStudentsToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("dbStudent");
        for (Student student : studentArrayList) {
            DatabaseReference newStudentRef = myRef.push();
            newStudentRef.setValue(student);
        }

        Toast.makeText(getApplicationContext(), "Students imported to Firebase successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String filePath = getPathFromURI(uri);
                if (filePath != null && !filePath.isEmpty()) { // Check if the file path is valid
                    importStudentsFromCSV(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid file path", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String filePath = "";
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (index != -1) {
                filePath = cursor.getString(index);
            }
            cursor.close();
        }
        return filePath;
    }
    private void importStudentsFromCSV(String filePath) {
        if (myRefer == null) {
            Toast.makeText(getApplicationContext(), "Firebase reference is not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String id = data[0].trim();
                    String img = data[1].trim();
                    String name = data[2].trim();
                    String gender = data[3].trim();
                    String phone = data[4].trim();
                    String department = data[5].trim();

                    Student student = new Student(id, img, name, gender, phone, department);

                    DatabaseReference newStudentRef = myRefer.push();
                    newStudentRef.setValue(student);

                    studentArrayList.add(student);
                }
            }

            studentAdapter.notifyDataSetChanged();
            reader.close();
            Toast.makeText(getApplicationContext(), "Students imported successfully", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error importing students from file", Toast.LENGTH_LONG).show();
        }
    }



    //Export CSV
    private String prepareCSVData(DataSnapshot dataSnapshot) {
        StringBuilder csvData = new StringBuilder();
        csvData.append("ID,Name,Gender,Phone,Department\n");

        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
            Student student = studentSnapshot.getValue(Student.class);

            if (student != null) {
                csvData.append(student.getId()).append(',')
                        .append(student.getName()).append(',')
                        .append(student.getGender()).append(',')
                        .append(student.getPhone()).append(',')
                        .append(student.getDepartment()).append('\n');
            }
        }

        return csvData.toString();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                exportToCSV();
            } else {
                Toast.makeText(this, "Permission denied. Cannot export to CSV.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void exportFromFirebaseToCSV() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("dbStudent");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String csvData = prepareCSVData(dataSnapshot);

                    if (!csvData.isEmpty()) {
                        String csvFileName = "firebase_data.csv";
                        boolean success = writeCSVToFile(csvFileName, csvData);

                        if (success) {
                            Toast.makeText(getApplicationContext(), "Data exported to " + csvFileName, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Export failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No data available to export", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No data available to export", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error reading data from Firebase: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void exportToCSV() {
        if (isExternalStorageWritable()) {
            StringBuilder csvData = new StringBuilder();

            csvData.append("ID,Image,Name,Gender,Phone,Department\n");

            for (Student student : studentArrayList) {
                if (student != null) {
                    csvData.append(student.getId()).append(',')
                            .append(student.getAvatar()).append(',')
                            .append(student.getName()).append(',')
                            .append(student.getGender()).append(',')
                            .append(student.getPhone()).append(',')
                            .append(student.getDepartment()).append('\n');
                }
            }

            String csvFileName = "student_data.csv";
            boolean success = writeCSVToFile(csvFileName, csvData.toString());

            if (success) {
                Toast.makeText(getApplicationContext(), "Data exported to " + csvFileName, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Export failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "External storage not available", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean writeCSVToMediaStore(String fileName, String csvData) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "text/csv");

            Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            Uri uri = getContentResolver().insert(contentUri, contentValues);

            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        outputStream.write(csvData.getBytes());
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean writeCSVToFile(String fileName, String csvData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return writeCSVToMediaStore(fileName, csvData);
        } else {
            try {
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                if (!directory.exists() && !directory.mkdirs()) {
                    Log.e("CSV Export", "Failed to create directory");
                    return false;
                }

                File file = new File(directory, fileName);

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(csvData);
                fileWriter.flush();
                fileWriter.close();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CSV Export", "Error writing CSV to file: " + e.getMessage());
                return false;
            }
        }
    }

    // search
    private void filterStudentList(String searchCriteria) {
        ArrayList<Student> filteredList = new ArrayList<>();
        for (Student student : studentArrayList) {
            // Tìm kiếm sinh viên theo tên
            if (student.getName().toLowerCase().contains(searchCriteria.toLowerCase()) ||
                    student.getId().contains(searchCriteria) ||
                    student.getDepartment().contains(searchCriteria)) {

                // Kiểm tra xem sinh viên đã tồn tại trong danh sách lọc chưa
                if (!filteredList.contains(student)) {
                    filteredList.add(student);
                }
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
