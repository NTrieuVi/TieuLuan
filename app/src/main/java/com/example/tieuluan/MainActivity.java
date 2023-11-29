package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity {
    private ImageButton btnManagerStudent, btnManagerUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnManagerStudent = findViewById(R.id.btnMangerStudent);
        btnManagerUser = findViewById(R.id.btnMangerUser);

        btnManagerStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagerStudent.class);
                startActivity(intent);
            }
        });

        btnManagerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagerUser.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_option, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item) {
//        if(item.getItemId() == R.id.menuAdd){
//            Intent intent = new Intent(this, AddUser.class);
//            startActivity(intent);
//            return true;
//        }
//        if(item.getItemId() == R.id.menuDelete){
//            Intent intent = new Intent(this, AddStudent.class);
//            startActivity(intent);
//            return true;
//        }
//        else return super.onOptionsItemSelected(item);
//    }

}