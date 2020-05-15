import java.util.*;

public class Result {
    public String s;
    public boolean b;
    public int i;
    public Object obj;

    //Object o
    public Result isNotNull(){
        if(obj == null) throw new UnsupportedOperationException();
        else{ return this;}
    }
    public Result isNull (){
        if(obj != null)throw new UnsupportedOperationException();
        return this;
    }
    public Result isEqualTo(Object o2){
        if(obj == null){
            if(o2 == null){ return this; }
            throw new UnsupportedOperationException();
        }
        if(obj.equals(o2)) { return this; }
        throw new UnsupportedOperationException();
    }
    public Result isNotEqualTo(Object o2){
        if(obj == null){
            if(o2 == null){ throw new UnsupportedOperationException(); }
            return this;
        }
        if(obj.equals(o2))throw new UnsupportedOperationException();
        return this;
    }
    public Result isInstanceOf(Class c){
        if(c.isInstance(obj)){ return this; }
        else{ throw new UnsupportedOperationException(); }
    }

    //String s
    public Result startsWith(String s2) {
        if(!s.startsWith(s2)){
            throw new UnsupportedOperationException();
        }
        return this;
    }
    public Result isEmpty(){
        if(!"".equals(s)) {
            throw new UnsupportedOperationException();
        }
        return this;
    }
    public Result contains(String s2) {
        if(s.contains(s2)){
            return this;
        }
        throw new UnsupportedOperationException();
    }

    //boolean b
    public Result isEqualTo(boolean b2){
        if(Objects.equals(b, b2)){
            return this;
        }
        throw new UnsupportedOperationException();
    } //raises an exception if b != b2.
    public Result isTrue() {
        if(b)return this;
        throw new IllegalArgumentException();
    }
    public Result isFalse(){
        if(!b)return this;
        throw new IllegalArgumentException();
    }

    //int i
    public Result isEqualTo(int i2) {
        if(i2 == i) return this;
        throw new IllegalArgumentException();
    }
    public Result isLessThan(int i2){
        if(i<i2){ return this; }
        throw new IllegalArgumentException();
    }
    public Result isGreaterThan(int i2){
        if(i>i2){ return this;}
        else{
            throw new IllegalArgumentException();
        }
    }
}
