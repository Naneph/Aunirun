package com.example.auniruntest3.ui.startPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.auniruntest3.R;
import com.example.auniruntest3.databinding.FragmentConfigBinding;
import com.example.auniruntest3.databinding.FragmentStartBinding;
import com.example.auniruntest3.entity.AppConfig;
import com.example.auniruntest3.run.App;
import com.example.auniruntest3.utils.AppConfigUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StartFragment extends Fragment {


    private Spinner userSpinner;
    private Spinner mapSpinner;
    private Spinner configSpinner;
    private Button startBtn;
    private Button checkBtn;
    private ProgressBar progressBar;
    private TextView result;
    private ScrollView scrollStart;
    private FragmentStartBinding binding;


    private String username;
    private String password;
    private String mapName;
    private String runConfig;
    private List<Map<String, Object>> userList;
    int mapPos;
    int userPos;
    int configPos;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false);


        userSpinner = binding.spinUser;
        mapSpinner = binding.spinMap;
        configSpinner = binding.spinConfig;
        startBtn = binding.startBtn;
        checkBtn = binding.checkRunStatusBtn;
        progressBar = binding.startPageLoading;
        result = binding.startResult;
        scrollStart = binding.scrollStart;

        //获取已保存的用户列表
        userList = getUsers();

        /*initUserSpin();
        initConfigSpin();
        initMapSpin();*/
        initSpin();


        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                username = (String) userList.get(position).get("name");
                password = (String) userList.get(position).get("password");
                userPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("userSpinner的默认选择！");
            }
        });
        configSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                runConfig = parent.getItemAtPosition(position).toString();
                configPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                runConfig = "5km级";
            }
        });
        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapPos = position;
                if (parent.getItemAtPosition(position).toString().contains("航空港")) {
                    mapName = "航空港";
                    return;
                }
                mapName = "龙泉";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //======username&password======
                //==========runConfig==========
                AppConfig appConfig;
                switch (runConfig) {
                    case "5km级":
                        appConfig = AppConfigUtils.getRunConfig5(username, password);
                        break;
                    case "10km级":
                        appConfig = AppConfigUtils.getRunConfig10(username, password);
                        break;
                    case "4km级":
                        appConfig = AppConfigUtils.getRunConfig4(username, password);
                        break;
                    case "3km级":
                        appConfig = AppConfigUtils.getRunConfig3(username, password);
                        break;
                    case "1km级":
                    default:
                        appConfig = AppConfigUtils.getRunConfig(username, password);
                        break;
                }


                //==========startRun==========
                App app = new App(appConfig);
                app.setType("run");
                app.setResultArea(result);
                app.setLoadingProgressBar(progressBar);

                //==========mapConfig==========
                app.setMapConfig(mapName);

                System.out.println("=======================================");
                System.out.println("user:" + username);
                System.out.println("地图配置：" + mapName);
                System.out.println("跑步配置：");
                System.out.println("时间:" + appConfig.getRunTime() + "分钟    距离：" + appConfig.getDistance());
                System.out.println("=======================================");

                app.start();

                //==================临时
                /*AppConfig appConfig = AppConfigUtils.getLoginConfig("18784736983", "egftrty1256");
                appConfig.setRunTime(45);
                appConfig.setDistance(4369);
                App app = new App(appConfig);
                app.setResultArea(result);
                app.setLoadingProgressBar(progressBar);
                app.setType("run");
                app.start();*/
                savaSpinner();
                scrollStart.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("检测");
                AppConfig appConfig = AppConfigUtils.getLoginConfig(username, password);
                App app = new App(appConfig);
                app.setResultArea(result);
                app.setLoadingProgressBar(progressBar);
                app.setType("check");
                app.start();

                scrollStart.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        return binding.getRoot();
    }


    //保存本次使用的跑步方案
    private void savaSpinner() {
        SharedPreferences setting = getContext().getSharedPreferences("quickConfig", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = setting.edit();
        editor.putInt("userPos", userPos);
        editor.putInt("mapPos", mapPos);
        editor.putInt("configPos", configPos);
        editor.apply();
    }

    //初始化上次使用的跑步方案
    private void initSpin() {
        SharedPreferences setting = getContext().getSharedPreferences("quickConfig", Context.MODE_PRIVATE);
        userPos = setting.getInt("userPos", 0);
        mapPos = setting.getInt("mapPos", 0);
        configPos = setting.getInt("configPos", 0);

        //初始化spinner
        initUserSpin();
        initConfigSpin();
        initMapSpin();

        //设置spinner选项卡
        configSpinner.setSelection(configPos);
        mapSpinner.setSelection(mapPos);

        if (userList == null || userList.size() <= userPos) return;
        userSpinner.setSelection(userPos);
    }

    //初始化地图选项
    private void initMapSpin() {
        List<Map<String, Object>> list = new ArrayList<>();
        int imgId = R.drawable.location_orange;
        Map<String, Object> item = new HashMap<>();
        item.put("name", "航空港");
        item.put("avatar", imgId);
        list.add(item);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "龙泉");
        item2.put("avatar", imgId);
        list.add(item2);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.userlist_item, new String[]{"avatar", "name"}, new int[]{R.id.avatarView, R.id.nameText});
        mapSpinner.setAdapter(adapter);
    }

    //初始化配置选项
    private void initConfigSpin() {

    }

    //初始化已保存用户列表
    private void initUserSpin() {
        int imgId = R.drawable.people;
        List<Map<String, Object>> list = new ArrayList<>();

        if (userList != null) {

            for (int i = 0; i < userList.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("avatar", imgId);
                item.put("name", userList.get(i).get("name"));
                list.add(item);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.userlist_item, new String[]{"avatar", "name"}, new int[]{R.id.avatarView, R.id.nameText});
        userSpinner.setAdapter(adapter);
    }


    //获取已经存储的用户  Utils
    private List<Map<String, Object>> getUsers() {
        SharedPreferences settings = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = settings.getAll();

        System.out.println("users的数量：" + allEntries.size());
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
}