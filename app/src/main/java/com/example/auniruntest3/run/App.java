package com.example.auniruntest3.run;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.TintContextWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.auniruntest3.entity.AppConfig;
import com.example.auniruntest3.entity.Location;
import com.example.auniruntest3.entity.NewRecordBody;
import com.example.auniruntest3.entity.ResponceType.NewRecordResult;
import com.example.auniruntest3.entity.ResponceType.RunStandard;
import com.example.auniruntest3.entity.ResponceType.SchoolBound;
import com.example.auniruntest3.entity.ResponceType.UserInfo;
import com.example.auniruntest3.entity.Response;
import com.example.auniruntest3.ui.login.LoginActivity;
import com.example.auniruntest3.utils.AppConfigUtils;
import com.example.auniruntest3.utils.FileUtils;
import com.example.auniruntest3.utils.JsonUtils;
import com.example.auniruntest3.utils.TrackUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import lombok.Setter;


public class App extends Thread {
    AppConfig config;
    @Setter
    private String mapConfig;


    @Setter
    private Context context;

    @Setter
    private TextView resultArea;

    @Setter
    ProgressBar loadingProgressBar;
    public static String ERROR;
    @Setter
    private String type;

    private final StringBuffer token;

    public App(AppConfig config) {
        this.config = config;
        token = config.getToken();
    }


    //登录验证
    private void runLogin() throws IOException {

        appendMsg("账号合法性验证");

        //----获取配置----
        String phone = config.getPhone();
        String password = config.getPassword();
        if (config.getBrand().isEmpty()) {
            appendMsg("请配置手机型号信息");
            return;
        }

        System.out.println("验证登录开始！！");
        Request request = new Request(token.toString(), config);
        Response<UserInfo> userInfoResponse = request.login(phone, password);
        UserInfo userInfo = userInfoResponse.getResponse();
        if (userInfo == null) {
            System.out.println("登录失败");
            appendMsg("账号验证失败，请检查用户名或密码！！");
            return;
        }

        SharedPreferences users = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = users.edit();
        editor.putString(phone, phone + "," + password);
        editor.apply();

        appendMsg("账号验证成功，用户信息已经保存。");
        System.out.println("登录成功");
    }

    private void runCheck() throws IOException {
        //----获取配置----
        String phone = config.getPhone();
        String password = config.getPassword();
        if (config.getBrand().isEmpty()) {
            appendMsg("请配置手机型号信息");
            return;
        }
        appendMsg("敬请期待!");
        System.out.println("验证登录开始！！");

    }


    //跑步
    @SuppressLint("DefaultLocale")
    public void runRun() throws IOException, ParseException {

        appendMsg("开始");
        String phone = config.getPhone();
        String password = config.getPassword();
        int schoolSite = 0;

        // 计算平均配速
        double average = 1.0 * config.getRunTime() / config.getDistance() * 1000;
        appendMsg(String.format("平均配速：%.2f\n", average));


        Request request = new Request(token.toString(), config);
        appendMsg("开始登录");
        Response<UserInfo> userInfoResponse = request.login(phone, password);
        UserInfo userInfo = userInfoResponse.getResponse();
        if (userInfo == null) {
            appendMsg("登录失败");
            appendMsg("请检查用户或密码是否经过更改，进入配置页面重新验证账号合法性！！");
            return;
        }
        long userId = userInfo.getUserId();
        if (userId != -1) {
            token.delete(0, token.length());
            token.append(request.getToken());

            appendMsg("获取跑步标准");
            RunStandard runStandard = request.getRunStandard(userInfo.getSchoolId());
            appendMsg("获取学校经纬度区域信息");
            SchoolBound[] schoolBounds = request.getSchoolBound(userInfo.getSchoolId());


            // 新增跑步数据
            appendMsg("生成跑步数据");
            NewRecordBody recordBody = new NewRecordBody();
            recordBody.setUserId(userId);
            recordBody.setAppVersions(config.getAppVersion());
            recordBody.setBrand(config.getBrand());
            recordBody.setMobileType(config.getMobileType());
            recordBody.setSysVersions(config.getSysVersion());
            recordBody.setRunDistance(config.getDistance());
            recordBody.setRunTime(config.getRunTime());
            recordBody.setYearSemester(runStandard.getSemesterYear());
            recordBody.setRealityTrackPoints(schoolBounds[schoolSite].getSiteBound() + "--");

            // 今天日期 年-月-日
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd");
            Date date = new Date();
            String formatTime = sdf.format(date);
            recordBody.setRecordDate(formatTime);

            // 生成跑步数据
            String tack = genTack(config.getDistance());
            recordBody.setTrackPoints(tack);

            //发送数据
            appendMsg("提交跑步数据");
            String result = request.recordNew(recordBody);
            Response<NewRecordResult> response = JsonUtils.string2Obj(result, new TypeReference<Response<NewRecordResult>>() {
            });
            appendMsg("");
            appendMsg("返回原始数据：" + result);
            appendMsg("解析数据：");
            appendMsg("跑步结果：" + response.getCode() + " - " + response.getMsg());
            NewRecordResult response1 = response.getResponse();
            appendMsg("生成的跑步ID：" + response1.getRecordId());
            appendMsg("结果状态：" + response1.getResultStatus());
            appendMsg("结果描述：" + response1.getResultDesc());
            appendMsg("超速警告次数：" + response1.getOverSpeedWarn());
            appendMsg("警告内容：" + response1.getWarnContent());
        } else {
            appendMsg("用户Id获取失败");
        }
    }


    public void run() {
        try {
            if ("run".equals(type)) {
                runRun();
            } else if ("login".equals(type)) {
                runLogin();
            } else if ("check".equals(type)) {
                runCheck();
            } else {
                appendMsg("未知操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg;
            if (e instanceof RuntimeException) {
                StackTraceElement traceElement = e.getStackTrace()[0];
                msg = e.getMessage() + "\n异常来源：" + traceElement.getClassName() + " - line:" + traceElement.getLineNumber();
            } else {
                msg = e.getMessage();
            }
            appendMsg(msg);
        } finally {
            stopLoading();
        }
    }


    @SuppressLint("RestrictedApi")
    public void appendMsg(String msg) {
        Context context = resultArea.getContext();

        Activity activity = null;
        if (context instanceof LoginActivity) {
            activity = (Activity) context;
        } else if (context instanceof TintContextWrapper) {
            activity = (Activity) ((TintContextWrapper) context).getBaseContext();
        } else if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultArea.append("\n" + msg);
                }
            });
        }
        if (activity != null)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultArea.append("\n" + msg);
                }
            });
    }


    public String genTack(long distance) {

        InputStream mapInput;
        //地图选择和使用部分
        switch (mapConfig) {
            case "龙泉":
                mapInput = com.example.auniruntest3.MainActivity.class.getResourceAsStream("/map2.json");
                break;
            case "航空港":
            default:
                mapInput = com.example.auniruntest3.MainActivity.class.getResourceAsStream("/map.json");
                break;
        }
        //return null;

        //待分析和优化
        /*if (mapInput == null)
            mapInput = com.example.auniruntest3.MainActivity.class.getResourceAsStream("/map.json");*/

        String json = FileUtils.ReadFile(mapInput);
        try {
            mapInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json.length() == 0) {
            System.out.println("配置读取失败");
            return null;
        }
        Location[] locations = JsonUtils.string2Obj(json, Location[].class);
        return TrackUtils.gen(distance, locations);
    }

    @SuppressLint("RestrictedApi")
    public void stopLoading() {
        Context context = resultArea.getContext();
        // TintContextWrapper
        // LoginActivity
        // StartFragment

        Activity activity = null;
        if (context instanceof LoginActivity) {
            activity = (Activity) context;
        } else if (context instanceof TintContextWrapper) {
            activity = (Activity) ((TintContextWrapper) context).getBaseContext();
        }
        else if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        if (activity != null)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            });
    }
}