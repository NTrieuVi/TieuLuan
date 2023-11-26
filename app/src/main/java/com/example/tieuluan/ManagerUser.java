package com.example.tieuluan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tieuluan.adapter.UserAdapter;
import com.example.tieuluan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerUser extends AppCompatActivity {
    private  ListView listUser;
    private ArrayList<User> userArrayList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);

        Button back = findViewById(R.id.btnUserBackMain);
        back.setOnClickListener(v -> {
            finish();
        });

        listUser = findViewById(R.id.listUser);
        userArrayList = new ArrayList<>();
        GetDataUser();

        userAdapter = new UserAdapter(this,
                R.layout.activity_item_card,
                userArrayList);

        listUser.setAdapter(userAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            Intent intent = new Intent(this, AddUser.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.menuDelete){

            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    //Lấy danh sách từ firebase
    private void  GetDataUser(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRefer=database.getReference("dbUser");

        myRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);

                    user.setId(data.getKey());
                    userAdapter.add(user);
                }
                Toast.makeText(getApplicationContext(),"Load data success", Toast.LENGTH_LONG).show();
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Load data failed"+error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCacelled: "+error.toString());
            }
        });
    }
}