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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tieuluan.EditStudent;
import com.example.tieuluan.EditUserActivity;
import com.example.tieuluan.R;
import com.example.tieuluan.model.Student;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    @NonNull
    private Activity activity;
    private int resource;
    @NonNull
    private List<Student> objects;
    public StudentAdapter(@NonNull Activity activity, int resource, @NonNull List<Student> objects) {
        super(activity, resource, objects);
        this.activity=activity;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//       một đối tượng sinh viên trả về một View, 1 view ứng với 1 object, sau đó lưu lại position
        LayoutInflater inflater=this.activity.getLayoutInflater();
        View view=inflater.inflate(this.resource,null);

//        Khai báo TextView
        ImageView imgPhotoStudent = view.findViewById(R.id.imgPhotoStudent);
        TextView tvNameStudent = view.findViewById(R.id.tvNameStudent);
        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        TextView tvGenderStudent = view.findViewById(R.id.tvGenderStudent);

//        Lấy đối tượng sinh viên và đưa lên textView
        Student student = this.objects.get(position);

        if (student != null) {
            tvNameStudent.setText(student.getName());
            tvStudentId.setText(student.getId());
            tvGenderStudent.setText(student.getGender());

            if (student.getAvatar() != null) {
                // Nếu đây là resource ID, sử dụng setImageResource
//                imgPhotoUser.setImageResourcee(user.getImage());
//            } else if (student.getImageUrl() != null && !student.getImageUrl().isEmpty()) {
//                // Nếu đây là URL, sử dụng Picasso
//                Picasso.get().load(student.getImageUrl()).into(imgPhotoStudent);
            } else {
                imgPhotoStudent.setImageResource(R.drawable.img_student);
            }
        }


        ImageView btnMenu = view.findViewById(R.id.btnMenuStudent);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(activity,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.curentUserDetails){

                        }
                        else if(menuItem.getItemId()==R.id.editCurentUser){
                            //nhấn btn update->mh update
                            Intent intent=new Intent(activity, EditStudent.class);
//                            STUDENT là khóa dùng nhận dạng gói tin, student là đối tượng cần implement serializable
                            intent.putExtra("STUDENT",student);
                            //mở mh
                            activity.startActivity(intent);
                        }
                        else if(menuItem.getItemId()==R.id.deleteCurentUser){
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference myRef= database.getReference("dbStudent");
                            myRef.child(student.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(activity,
                                            "xóa thành công",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
//                        else if(menuItem.getItemId()==R.id.menuBack){
//                            Toast.makeText(activity,
//                                    "you click button exit",Toast.LENGTH_LONG).show();
//                        }
                        return false;
                    }
                });
//                truyền menu
                popupMenu.getMenuInflater().inflate(R.menu.menu_detail,popupMenu.getMenu());

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
    public Student getItem(int position) {
        if (position >= 0 && position < objects.size()) {
            return objects.get(position);
        } else {
            return null;
        }
    }
}
