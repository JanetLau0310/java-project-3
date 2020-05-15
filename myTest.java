import java.util.*;

public class myTest{
    public LinkedList<Integer> l;
    @BeforeClass
    public static void setUp() {
        System.out.println("before");
    }
    @Before
    public void beforetest(){
        l = new LinkedList<>();
        l.add(1); l.add(2); l.add(3);
        System.out.println(" beforetest() ");
    }
    @After
    public void destroy(){
        System.out.println("destory");
    }
    @AfterClass
    public static void end() {
        System.out.println("end");
    }
    @Test
    public void testA(){
        System.out.println(" a ");
    }
    @Test
    public void testB(){
        System.out.println(" b ");
    }
    @Test
    public void testC(){
        System.out.println(" c ");
    }



/*     @Property
    public boolean absNonNeg(@IntRange(min=-10, max=10) Integer i) {
        return Math.abs(i.intValue()) > 0;
    }

 */
         /*  @Property
           public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o1) {
               Set s = (Set) o1;
               s.add("foo");
               return s.contains("foo");
           }

         @Property
    public boolean testList( @ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer> l,@IntRange(min=-10, max=10) Integer i ){
             l.add(i);
             return Math.abs(i.intValue()) > 0;
    }*/
 @Property
    public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o) {
     Set s = (Set) o;
     s.add("foo");
     return s.contains("foo");
    }

    /*private static void printAnnotatedType(AnnotatedType annotatedType, int level) {
        print(level, "Type = " + annotatedType.getType().getTypeName());
        print(level, "Declared Annotations: " + Arrays.toString(annotatedType.getDeclaredAnnotations()));
        if (annotatedType instanceof AnnotatedParameterizedType) {
            printAnnotatedParameterizedType((AnnotatedParameterizedType) annotatedType, level);
        }
        if (annotatedType instanceof AnnotatedTypeVariable) {
            printAnnotatedTypeVariable((AnnotatedTypeVariable) annotatedType, level);
        }
    }
    private static void printAnnotatedTypeVariable(AnnotatedTypeVariable annotatedType, int level){
        AnnotatedType[] aType = annotatedType.getAnnotatedBounds();
        print(level, "AnnotatedTypeVariable#getAnnotatedBounds(): " + Arrays.toString(aType));

        for (AnnotatedType type : aType){
            print(level, "AnnotatedTypeVariable#bound : " + type);
            printAnnotatedType(type, level + 1);
        }
    }
    private static void printAnnotatedParameterizedType(AnnotatedParameterizedType annotatedType,int level){
        AnnotatedType[] aTypes = annotatedType.getAnnotatedActualTypeArguments();
        print(level, "AnnotatedParameterizedType#getAnnotatedActualTypeArguments() : " +
                Arrays.toString(aTypes));
        for (AnnotatedType aType : aTypes){
            print(level, "AnnotatedParameterizedType#annotatedActualTypeArgument: " + aType);
            printAnnotatedType(aType, level + 1);
        }
    }
    private static void print(int level, String string){
        System.out.printf("%" + (level * 4 - 3) + "s\u00A6- %s%n", "", string);
    }
    public static void main(String[] args) throws Exception {
        quickCheckClass("myTest");
    }*/

}
