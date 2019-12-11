package follow.twentyfourking.library1;

import android.util.Log;

import follow.twentyfourking.library2.Library2Tool;

public class Library1Tool {
    public static void workForMainApp() {
        Log.d("TTT", "Library1Tool.workForMainApp() 为main_app工作");
        Library2Tool.workForLib1ByLib3();
    }
}
