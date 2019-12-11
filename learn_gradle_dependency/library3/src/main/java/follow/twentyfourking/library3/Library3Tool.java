package follow.twentyfourking.library3;

import android.util.Log;

import follow.twentyfourking.library4.Library4Tool;

public class Library3Tool {
    public static void workForLib2() {
        Log.d("TTT", "Library3Tool.workForLib2() 为lib2工作 ");
        Library4Tool.workForLib3();
    }

    public static void workForLib1() {
        Log.d("TTT", "Library3Tool.workForLib1() 为lib1工作");
    }
}
