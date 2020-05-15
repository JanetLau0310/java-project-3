import java.util.HashMap;

public class MainTest {
    public static void TestJUnit() throws Exception {
        HashMap<String, Throwable> err = Unit.testClass("ClassForTest");
        System.out.println(err);
    }

    public static void TestAssert(){
        String s = "COMP180";
        String s1 = "";
        Integer i = 42;
        Integer j = 42;
        Object o = s;
        Unit unit = new Unit();
        Unit unit1 = null;
        Assertion.assertThat(s).isNotNull().startsWith("COMP").isEqualTo("COMP180");
        Assertion.assertThat(s).isNotNull().isEqualTo(o);
        Assertion.assertThat(o).isNotNull().isInstanceOf(String.class);
        Assertion.assertThat(s).isNotNull().isNotEqualTo(i);
        Assertion.assertThat(s1).isEmpty().isInstanceOf(Object.class);
        Assertion.assertThat(j).isNotNull().isInstanceOf(Integer.class).isEqualTo(i);
        Assertion.assertThat(unit).isNotNull().isEqualTo(unit).isInstanceOf(Unit.class).isNotEqualTo(null);
        Assertion.assertThat(unit1).isNull().isNotEqualTo(unit).isEqualTo(unit1);
        Assertion.assertThat(2).isGreaterThan(1).isLessThan(5);
        Assertion.assertThat(42).isEqualTo(42).isLessThan(43);
        Assertion.assertThat(true).isTrue().isEqualTo(true);
        Assertion.assertThat(false).isEqualTo(false).isFalse();
        Assertion.assertThat(false).isEqualTo(false);
    }

    public static void TestQuickCheck() throws Exception {
        HashMap<String, Object[]> err = Unit.quickCheckClass("ClassForTest");
        for(String string:err.keySet()){
            if(err.get(string) != null){
                System.out.print("Method = "+string + " ," + "ERR : ");
                for(Object objects:err.get(string)){
                    System.out.print("\t" + objects);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws Exception {
 //       TestJUnit();
//        TestAssert();
        TestQuickCheck();
    }
}
