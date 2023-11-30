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

import com.example.tieuluan.adapter.CertificateAdapter;
import com.example.tieuluan.adapter.UserAdapter;
import com.example.tieuluan.model.Certificate;
import com.example.tieuluan.model.Student;
import com.example.tieuluan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerCertificate extends AppCompatActivity {
    private  ListView listCertificate;
    private ArrayList<Certificate> certificateArrayList;
    private CertificateAdapter certificateAdapter;
    private String stdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_certificate);

        Button back = findViewById(R.id.btnCerBackMain);
        back.setOnClickListener(v -> {
            finish();
        });

        listCertificate = findViewById(R.id.listCertificate);
        certificateArrayList = new ArrayList<>();
        GetDataCertificate();


        certificateAdapter = new CertificateAdapter(this,
                R.layout.activity_certificate_item,
                certificateArrayList);

        listCertificate.setAdapter(certificateAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            Intent intent = new Intent(this, AddCertificate.class);
            intent.putExtra("STUDENTIDAdd",stdId);
            //mở mh
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.menuDelete){

            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    //Lấy danh sách từ firebase
    private void  GetDataCertificate(){

        Intent intent=getIntent();
        stdId = (String) intent.getSerializableExtra("STUDENTID");

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRefer=database.getReference("dbCertificate");

        myRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                certificateAdapter.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    String status = data.child("stdId").getValue(String.class);

                    if (stdId.equals(status)) {
                        Certificate certificate = data.getValue(Certificate.class);

                        certificate.setId(data.getKey());
                        certificateAdapter.add(certificate);
                    }

                }
                Toast.makeText(getApplicationContext(),"Load data success", Toast.LENGTH_LONG).show();
                certificateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Load data failed"+error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCacelled: "+error.toString());
            }
        });
    }
}