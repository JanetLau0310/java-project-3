import java.util.HashSet;
import java.util.Set;

public class genIntSet {
    int count = 0;
    public Object genIntSet() {
        Set s = new HashSet();
        for (int i=0; i<count; i++) { s.add(i); }
        count++;
        return s;
    }
}
