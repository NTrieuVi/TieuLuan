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

import com.example.tieuluan.EditUserActivity;
import com.example.tieuluan.R;

import com.example.tieuluan.model.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    @NonNull
    private Activity activity;
    private int resource;
    @NonNull
    private List<User> objects;
    public UserAdapter(@NonNull Activity activity, int resource, @NonNull List<User> objects) {
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
        ImageView imgPhotoUser=view.findViewById(R.id.imgPhotoUser);
        TextView tvFulName=view.findViewById(R.id.tvNameUser);
        TextView tvAgeUser=view.findViewById(R.id.tvAgeUser);
        TextView tvStatusUser=view.findViewById(R.id.tvStatusUser);

        User user = this.objects.get(position);

        if (user != null) {
            tvFulName.setText(user.getName());
            tvAgeUser.setText(user.getAge());
            tvStatusUser.setText(user.getStatus());

            if (user.getImage() != null) {
                // Nếu đây là resource ID, sử dụng setImageResource
//                imgPhotoUser.setImageResourcee(user.getImage());
            } else if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                // Nếu đây là URL, sử dụng Picasso
                Picasso.get().load(user.getImageUrl()).into(imgPhotoUser);
            } else {
                imgPhotoUser.setImageResource(R.drawable.img_teacher);
            }
        }

        ImageView btnMenu = view.findViewById(R.id.btnMenuUser);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(activity,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.editCurentUser){
                            //nhấn btn update->mh update
                            Intent intent=new Intent(activity, EditUserActivity.class);
//                            USER là khóa dùng nhận dạng gói tin, user là đối tượng cần implement serializable
                            intent.putExtra("USER",user);
                            //mở mh
                            activity.startActivity(intent);
                        }
                        else if(menuItem.getItemId()==R.id.deleteCurentUser){
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference myRef= database.getReference("dbUser");
                            myRef.child(user.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(activity,
                                    "xóa thành công",Toast.LENGTH_LONG).show();
                                }
                            });

                            //xoa trong account
                            DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("dbAccount");
                            myRef2.child(user.getId()).removeValue((error, ref) -> {});
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
}
