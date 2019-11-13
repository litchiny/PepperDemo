package cn.softbankrobotics.testapi;

import android.content.Context;
import android.widget.Toast;

public class ToolUtil {

    public static void showToast(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
