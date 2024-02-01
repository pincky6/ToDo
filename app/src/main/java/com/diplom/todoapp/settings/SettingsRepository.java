package com.diplom.todoapp.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class SettingsRepository {
    public static final String THEME_KEY = "THEME_KEY";
    public static final String AUTODELETE_KEY = "AUTODELETE_KEY";
    static SettingsRepository settings = null;
    int themeMode;
    boolean autodeleteFlag;
    public static SettingsRepository getInstance(Context context){
        if(settings == null && context != null){
            settings = new SettingsRepository(context);
        }
        return settings;
    }
    private SettingsRepository(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        themeMode = sharedPreferences.getInt(THEME_KEY, 0);
        if(themeMode == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        } else if(themeMode == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        autodeleteFlag = sharedPreferences.getBoolean(AUTODELETE_KEY, false);
    }
    public void setThemeMode(int themeMode, Context context){
        this.themeMode = themeMode;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(THEME_KEY, themeMode);
        editor.apply();
    }
    public int getThemeMode(){
        return themeMode;
    }
    public void setAutodeleteTask(boolean autodeleteFlag, Context context){
        this.autodeleteFlag = autodeleteFlag;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTODELETE_KEY, this.autodeleteFlag);
        editor.apply();
    }
    public boolean getAutodeleteTask(){
        return autodeleteFlag;
    }
}
