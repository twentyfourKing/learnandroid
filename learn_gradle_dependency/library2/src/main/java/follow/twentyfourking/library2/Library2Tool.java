package follow.twentyfourking.library2;

import android.util.Log;

import follow.twentyfourking.library3.Library3Tool;

public class Library2Tool {

    public static void workForLib1() {
        Log.d("TTT", "Library2Tool.workForLib1() 为lib1工作");
    }

    public static void workForLib1ByLib3() {
        Log.d("TTT", "Library2Tool.workForLib1ByLib3() 为lib1工作通过lib3");
        Library3Tool.workForLib2();
    }
}
