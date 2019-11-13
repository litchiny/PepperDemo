package cn.softbankrobotics.testapi;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

    static int DEFAULT = -1;

    public static SharedPreferences initSP(Context context) {
        return context != null ? context.getSharedPreferences("pepper", Context.MODE_PRIVATE) : null;
    }

    public static void setConfigJsonVersion(Context context, int code) {
        SharedPreferences.Editor edit = initSP(context).edit();
        edit.putInt(SPConstant.SP_CONFIG_JSON_VERSION, code).commit();
    }

    public static int getConfigJsonVersion(Context context) {
        return initSP(context).getInt(SPConstant.SP_CONFIG_JSON_VERSION, DEFAULT);
    }

    public static void setMicrosoftFaceGroupId(Context context, String uuid) {
        SharedPreferences.Editor edit = initSP(context).edit();
        edit.putString(SPConstant.SP_MICROSOFT_FACE_UUID, uuid).commit();
    }

    public static String getMicrosoftFaceGroupId(Context context) {
        return initSP(context).getString(SPConstant.SP_MICROSOFT_FACE_UUID, "");
    }

    public static void setCameraTime(Context context, String code) {
        SharedPreferences.Editor edit = initSP(context).edit();
        edit.putString(SPConstant.SP_CAMERA_TIME, code).commit();
    }

    public static String getCameraTime(Context context) {
        return initSP(context).getString(SPConstant.SP_CAMERA_TIME,"");
    }
}
