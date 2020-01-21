package follow.twentyfourking.learning_arouter.testinject;

import android.os.Parcel;
import android.os.Parcelable;

public class TestParcelable implements Parcelable {
    public int id;
    public String name;

    public static final Parcelable.Creator<TestParcelable> CREATOR
            = new Parcelable.Creator<TestParcelable>() {
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };

    protected TestParcelable(Parcel paramParcel) {
        String str = paramParcel.readString();
        this.name = str;
        int i = paramParcel.readInt();
        this.id = i;
    }

    public TestParcelable(String paramString, int paramInt) {
        this.name = paramString;
        this.id = paramInt;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        String str = this.name;
        paramParcel.writeString(str);
        int i = this.id;
        paramParcel.writeInt(i);
    }
}
