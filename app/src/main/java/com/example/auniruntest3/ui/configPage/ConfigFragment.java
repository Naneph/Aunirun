package com.example.auniruntest3.ui.configPage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.auniruntest3.R;
import com.example.auniruntest3.databinding.FragmentConfigBinding;
import com.example.auniruntest3.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigFragment extends Fragment {

    private FragmentConfigBinding binding;
    private Button addUserBtn, changeMapBtn, inputMapBtn;
    private ImageView mapView;
    private ListView listView;
    private EditText newPhone;
    private EditText newPassword;

    private List<Map<String, Object>> userList;
    private int nowMap;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        userList = getUsers();
        initListView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = FragmentConfigBinding.inflate(inflater, container, false);
        //root = inflater.inflate(R.layout.fragment_config, container, false);


        addUserBtn = binding.addUserBtn;
        changeMapBtn = binding.changeMapBtn;
        inputMapBtn = binding.inputMapBtn;
        mapView = binding.mapView;
        listView = binding.userListView;


        userList = getUsers();
        initListView();
        initMap();

        //temp
        List<Integer> mapList = new ArrayList<>();
        mapList.add(R.drawable.hkg);
        mapList.add(R.drawable.lqy);


        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(editIntent);
            }
        });
        nowMap = 0;
        changeMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nowMap == mapList.size() - 1) {
                    nowMap = 0;
                    mapView.setImageResource(mapList.get(nowMap));
                    return;
                }
                nowMap = nowMap + 1;
                mapView.setImageResource(mapList.get(nowMap));
            }
        });
        inputMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String username = (String) userList.get(position).get("name");

                //dialog
                customDialog(username);

            }
        });


        return binding.getRoot();

    }


    //初始化已保存用户列表
    private void initListView() {

        List<Map<String, Object>> users = userList;
        int imgId = R.drawable.people;

        List<Map<String, Object>> list = new ArrayList<>();
        if (users != null) {

            for (int i = 0; i < users.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("avatar", imgId);
                item.put("name", users.get(i).get("name"));
                list.add(item);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.userlist_item, new String[]{"avatar", "name"}, new int[]{R.id.avatarView, R.id.nameText});
        listView.setAdapter(adapter);
    }


    //初始化地图视图
    private void initMap() {
        setMapView(R.drawable.hkg);
    }

    private void setMapView(int imgId) {
        mapView.setImageResource(R.drawable.hkg);
    }


    //获取已经存储的用户  Utils
    private List<Map<String, Object>> getUsers() {
        SharedPreferences settings = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = settings.getAll();

        System.out.println("users的数量：" + allEntries.size() + "setting:" + settings);
        if (allEntries.isEmpty()) return null;

        List<Map<String, Object>> userList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] userInfo = ((String) entry.getValue()).split(",");
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", userInfo[0]);
            userMap.put("password", userInfo[1]);
            userList.add(userMap);
        }
        return userList;
    }


    /**
     * 自定义对话框
     */
    private void customDialog(String deleteValue) {
        @SuppressLint("ResourceType") final Dialog dialog = new Dialog(getContext(), R.xml.dialog);
        View view = View.inflate(getContext(), R.layout.dialog, null);
        Button cancel = view.findViewById(R.id.dialog_cancel_btn);
        Button okay = view.findViewById(R.id.dialog_ok_btn);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        Window dialogWindow = dialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 800;
        lp.height = 400;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除账号
                SharedPreferences settings = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.remove(deleteValue);
                editor.apply();

                userList = getUsers();
                initListView();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}