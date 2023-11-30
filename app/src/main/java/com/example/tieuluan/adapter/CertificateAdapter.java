package com.example.tieuluan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tieuluan.ActivityDetailStudent;
import com.example.tieuluan.ActivityLogin;
import com.example.tieuluan.EditCertificate;
import com.example.tieuluan.EditStudent;
import com.example.tieuluan.EditUserActivity;
import com.example.tieuluan.ManagerStudent;
import com.example.tieuluan.R;
import com.example.tieuluan.model.Certificate;
import com.example.tieuluan.model.Student;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class CertificateAdapter extends ArrayAdapter<Certificate> {
    @NonNull
    private Activity activity;
    private int resource;
    @NonNull
    private List<Certificate> objects;
    public CertificateAdapter(@NonNull Activity activity, int resource, @NonNull List<Certificate> objects) {
        super(activity, resource, objects);
        this.activity=activity;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.activity.getLayoutInflater();
        View view=inflater.inflate(this.resource,null);

//        Khai báo TextView
        TextView tvNameCertificate = view.findViewById(R.id.tvNameCertificate);
        TextView tvLevel = view.findViewById(R.id.tvLevel);
        TextView tvDateStart = view.findViewById(R.id.tvDateStart);

        Certificate certificate = this.objects.get(position);

        if (certificate != null) {
            tvNameCertificate.setText(certificate.getName());
            tvLevel.setText(certificate.getLevel());
            tvDateStart.setText(certificate.getDateStart());
        }


        ImageView btnMenu = view.findViewById(R.id.btnMenuCertificate);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(activity,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.editCertificate){
                            Intent intent=new Intent(activity, EditCertificate.class);
                            intent.putExtra("CERTIFICATE",certificate);
                            //mở mh
                            activity.startActivity(intent);
                        }
                        else if(menuItem.getItemId()==R.id.deleteCertificate){
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference myRef= database.getReference("dbCertificate");
                            myRef.child(certificate.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(activity,
                                            "xóa thành công",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return false;
                    }
                });
//                truyền menu
                popupMenu.getMenuInflater().inflate(R.menu.menu_certificate,popupMenu.getMenu());

                try {
                    Field[] fields=popupMenu.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object popupMenuHelper = field.get(popupMenu);
                            Class<?> cls = Class.forName(popupMenuHelper.getClass().getName());
                            Method method = cls.getMethod("setForceShowIcon", boolean.class);
                            method.invoke(popupMenuHelper, true);
                        }
                    }
                }catch (Exception e){
                    Log.d("MYTAG","onClick: "+e.toString());
                }
                popupMenu.show();
            }
        });
        return view;
    }
}
