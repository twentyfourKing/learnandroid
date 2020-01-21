package follow.twentyfourking.learning_arouter.testinject;

import java.io.Serializable;

public class TestSerializable implements Serializable {
    public int id;

    public String name;

    public TestSerializable() {
    }

    public TestSerializable(String paramString, int paramInt) {
        this.name = paramString;
        this.id = paramInt;
    }
}