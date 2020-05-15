import java.util.*;

public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static Result assertThat(Object o)  {
        Result r = new Result();
        r.obj = o;
        return r;
    }
    static Result assertThat(String s) {
        Result r = new Result();
        r.s = s;
        r.obj = s;
        return r;
    }
    static Result assertThat(boolean b) {
        Result r = new Result();
        r.b = b;
        r.obj = b;
        return r;
    }
    static Result assertThat(int i) {
        Result r = new Result();
        r.i = i;
        r.obj = i;
        return r;
    }

 public static void main(String[] args) throws Exception {
        String s = "COMP is a test";
        Object obj = new Object();
        Object obj2 = null;
        boolean b = true;
        Assertion.assertThat(obj2).isEqualTo(obj);
    }
}