package com.example.auniruntest3.run;

import com.example.auniruntest3.entity.AppConfig;
import com.example.auniruntest3.entity.NewRecordBody;
import com.example.auniruntest3.entity.ResponceType.RunStandard;
import com.example.auniruntest3.entity.ResponceType.SchoolBound;
import com.example.auniruntest3.entity.ResponceType.UserInfo;
import com.example.auniruntest3.entity.Response;
import com.example.auniruntest3.utils.HTTP.HttpUtil;
import com.example.auniruntest3.utils.JsonUtils;
import com.example.auniruntest3.utils.MD5Utils;
import com.example.auniruntest3.utils.SignUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

public class Request {
    private final static HttpUtil http = new HttpUtil();
    private final String appKey = "389885588s0648fa";
    private final String HOST = "https://run-lb.tanmasports.com/";
    @Getter
    private String token;
    private AppConfig config;

    public Request(String token, AppConfig config){
        this.token = token;
        this.config = config;
    }

    public Response<UserInfo> login(String phone, String password) throws IOException {
        String pass = MD5Utils.stringToMD5(password);
        String API = "https://run-lb.tanmasports.com/v1/auth/login/password";
        try {
            Map<String, String> body = new HashMap<>();
            body.put("appVersion", config.getAppVersion());
            body.put("brand", config.getBrand());
            body.put("deviceToken", config.getDeviceToken());
            body.put("deviceType", config.getDeviceType());
            body.put("mobileType", config.getMobileType());
            body.put("password", pass);
            body.put("sysVersion", config.getSysVersion());
            body.put("userPhone", phone);

            Map<String, String> headers = new HashMap<>();
            String bodyStr = JsonUtils.obj2String(body);
            String sign = SignUtils.get(null, bodyStr);
            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");

            /*
             * 进行登录
             * 登录后返回bytes，从中拿去已验证的用户信息，和token
             * */
            byte[] bytes = http.doPostJson2Byte(API, headers, bodyStr);
            Response<UserInfo> userInfoResponse = JsonUtils.string2Obj(new String(bytes), new TypeReference<Response<UserInfo>>() {});
            int code = userInfoResponse.getCode();
            if(code == 10000){
                UserInfo userInfo = userInfoResponse.getResponse();
                this.token = userInfo.getOauthToken().getToken();
                return userInfoResponse;
            }else{
                throw new RuntimeException(userInfoResponse.getMsg());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public long getUserId(){
        String API = "https://run-lb.tanmasports.com/v1/auth/query/token";
        try {
            Map<String, String> headers = new HashMap<>();
            String sign = SignUtils.get(null, null);
            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            String tokenInfo = http.doGet2(API, headers);
            Response<UserInfo> userInfoResponse = JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<UserInfo>>() {});
            int code = userInfoResponse.getCode();
            if(code == 10000){
                return userInfoResponse.getResponse().getUserId();
            }else{
                throw new RuntimeException(userInfoResponse.getMsg());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Response<UserInfo> getUserInfo(){
        String API = HOST + "v1/auth/query/token";
        try {
            Map<String, String> headers = new HashMap<>();
            String sign = SignUtils.get(null, null);
            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");
            String tokenInfo = http.doGet2(API, headers);
            return JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<UserInfo>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    * 俱乐部相关方法
    * */
    /*public List<ClubInfo> getActivityList(String studentId, String date){
        String schoolId = "3680";

        String API = String.format(HOST + "v1/clubactivity/queryActivityList?queryTime=%s&studentId=%s&schoolId=%s&pageNo=1&pageSize=30", date, studentId, schoolId);
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("queryTime", date);
            params.put("studentId", studentId);
            params.put("schoolId", "3680");
            params.put("pageNo", "1");
            params.put("pageSize", "30");

            String sign = SignUtils.get(params, null);

            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");

            String tokenInfo = http.doGet2(API, headers);

            Response<List<ClubInfo>> standardResponse = JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<List<ClubInfo>>>() {});
            return standardResponse.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    /*public Response<JoinClubResult> joinClub(String studentId, String activityId){

        String API = String.format(HOST + "v1/clubactivity/joinClubActivity?studentId=%s&activityId=%s", studentId, activityId);
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("studentId", studentId);
            params.put("activityId", activityId);

            String sign = SignUtils.get(params, null);

            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");

            String joinResult = http.doGet2(API, headers);

            return JsonUtils.string2Obj(joinResult, new TypeReference<Response<JoinClubResult>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SignInTf getSignInTf(String studentId){
        String API = String.format(HOST + "v1/clubactivity/getSignInTf?studentId=%s", studentId);
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("studentId", studentId);

            String sign = SignUtils.get(params, null);

            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");

            String signInTf = http.doGet2(API, headers);

            Response<SignInTf> signInTfResponse = JsonUtils.string2Obj(signInTf, new TypeReference<Response<SignInTf>>() {});
            return signInTfResponse.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    /*public Response signInOrSignBack(SignInOrSignBackBody signInOrSignBackBody){
        String API = HOST + "v1/clubactivity/signInOrSignBack";
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();

            String body = JsonUtils.obj2String(signInOrSignBackBody);

            String sign = SignUtils.get(params, body);

            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");
            byte[] bytes = http.doPostJson2Byte(API, headers, body);

            String signInTf = new String(bytes);

            Response signResponse = JsonUtils.string2Obj(signInTf, new TypeReference<Response>() {});
            return signResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    /*public List<SportsClassStudentLearnClockingV0> getMySportsClassClocking(){

        String API = HOST + "v1/sports/class/getMySportsClassClocking";
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();

            String sign = SignUtils.get(params, null);

            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("User-Agent", "okhttp/3.12.0");

            String joinResult = http.doGet2(API, headers);

            Response<List<SportsClassStudentLearnClockingV0>> sportsClassStudentLearnClockingV0Response = JsonUtils.string2Obj(joinResult, new TypeReference<Response<List<SportsClassStudentLearnClockingV0>>>() {});
            return sportsClassStudentLearnClockingV0Response.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/


    public SchoolBound[] getSchoolBound(long schoolId){

        String API = "https://run-lb.tanmasports.com/v1/unirun/querySchoolBound?schoolId=" + schoolId;
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("schoolId", String.valueOf(schoolId));
            String sign = SignUtils.get(params, null);
            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            String tokenInfo = http.doGet2(API, headers);
            Response<SchoolBound[]> schoolBoundResponse = JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<SchoolBound[]>>() {});
            if(schoolBoundResponse.getCode() != 10000){
                throw new RuntimeException(schoolBoundResponse.getMsg());
            }
            return schoolBoundResponse.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RunStandard getRunStandard(long schoolId){

        String API = "https://run-lb.tanmasports.com/v1/unirun/query/runStandard?schoolId=" + schoolId;
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("schoolId", String.valueOf(schoolId));
            String sign = SignUtils.get(params, null);
            headers.put("sign", sign);//签名
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            String tokenInfo = http.doGet2(API, headers);
            Response<RunStandard> standardResponse = JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<RunStandard>>() {});
            if(standardResponse.getCode() != 10000){
                throw new RuntimeException(standardResponse.getMsg());
            }
            return standardResponse.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String recordNew(NewRecordBody body){
        String API = "https://run-lb.tanmasports.com/v1/unirun/save/run/record/new";
        try {
            Map<String, String> headers = new HashMap<>();
            String bodyStr = JsonUtils.obj2String(body);
            String sign = SignUtils.get(null, bodyStr);
            headers.put("sign", sign);
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            byte[] bytes = http.doPostJson2Byte(API, headers, bodyStr);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取跑步次数
    public Object getRunNum(String studentId,long schoolId){
        String API = "https://run-lb.tanmasports.com/v1/clubactivity/getJoinNum";
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            params.put("schoolId", String.valueOf(schoolId));
            params.put("studentId",studentId);
            String sign = SignUtils.get(params, null);
            headers.put("sign", sign);//签名
            headers.put("token", token);
            headers.put("appkey", appKey);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            String tokenInfo = http.doGet2(API, headers);
            Response<Object> runNumResponse = JsonUtils.string2Obj(tokenInfo, new TypeReference<Response<Object>>() {});
            if(runNumResponse.getCode() != 10000){
                throw new RuntimeException(runNumResponse.getMsg());
            }
            return runNumResponse.getResponse();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

