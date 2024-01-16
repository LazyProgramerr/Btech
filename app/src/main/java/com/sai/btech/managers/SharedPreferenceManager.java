package com.sai.btech.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.sai.btech.models.AppStatus;
import com.sai.btech.models.UserBioAuthStatus;
import com.sai.btech.models.UserData;
import com.sai.btech.models.UserLogin;
import com.sai.btech.models.UserTheme;
import com.sai.btech.models.VibrateStatus;
import com.sai.btech.constants.btech;

public class SharedPreferenceManager {
    //    saving and getting vibration status
    public static void saveVibrateStatus(Context context, boolean s){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_VIBRATION_STATUS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vStatus",s).apply();
    }
    public static VibrateStatus getVibrateStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_VIBRATION_STATUS,Context.MODE_PRIVATE);
        boolean vStat = sharedPreferences.getBoolean("vStatus",true);
        return new VibrateStatus(vStat);
    }
    //    saving and getting dark mode status
    public static void saveTheme(Context context, boolean theme){
        SharedPreferences sharedPreferencesSettings = context.getSharedPreferences(btech.SHARED_PREFS_DARK_MODE_STATUS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesSettings.edit();
        editor.putBoolean("Theme",theme);
        editor.apply();
    }
    public static UserTheme getUserTheme(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_DARK_MODE_STATUS,Context.MODE_PRIVATE);
        boolean theme = sharedPreferences.getBoolean("Theme",false);
        return new UserTheme(theme);
    }
    //    saving and getting user BioAuth status
    public static void saveBioAuthStatus(Context context,boolean bioAuthStatus){
        SharedPreferences sharedPreferencesSettings = context.getSharedPreferences(btech.SHARED_PREFS_BIO_AUTH_STATUS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesSettings.edit();
        editor.putBoolean("BioAuth",bioAuthStatus);
        editor.apply();
    }
    public static UserBioAuthStatus getBioAuthStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_BIO_AUTH_STATUS,Context.MODE_PRIVATE);
        boolean authStatus = sharedPreferences.getBoolean("BioAuth",false);
        return new UserBioAuthStatus(authStatus);
    }
    //    saving and getting userData status
    public static void saveUserData(Context context,String Name,String email,String phone,String image,String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_USER_DATA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",Name);
        editor.putString("email",email);
        editor.putString("phone",phone);
        editor.putString("image",image);
        editor.putString("userId",userId);
        editor.apply();
    }
    public static UserData getUserData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_USER_DATA,Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String email = sharedPreferences.getString("email","");
        String phone = sharedPreferences.getString("phone","");
        String img = sharedPreferences.getString("image","");
        String uid = sharedPreferences.getString("userId","");
        return new UserData(name,email,phone,img,uid);
    }
    //    saving and getting App Status
    public static void saveAppStatus(Context context,boolean appStatus){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_APP_HIDE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("AppStatus",appStatus);
        editor.apply();
    }
    public static AppStatus getAppStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_APP_HIDE,Context.MODE_PRIVATE);
        boolean appStatus = sharedPreferences.getBoolean("AppStatus",false);
        return new AppStatus(appStatus);
    }
    //    saving and getting user login status
    public static void saveLoginStatus(Context context,boolean loginStat){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_LOGIN_STATUS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginStatus",loginStat);
        editor.apply();
    }
    public static UserLogin getLoginStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(btech.SHARED_PREFS_LOGIN_STATUS,Context.MODE_PRIVATE);
        boolean lStat = sharedPreferences.getBoolean("loginStatus",false);
        return new UserLogin(lStat);
    }

}





