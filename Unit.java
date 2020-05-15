import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class Unit {
    //public List<List<Object>>result;
    //in testClass the input is the way to find methods
    public static HashMap<String, Throwable> testClass(String name) throws Exception {
        HashMap<String,Throwable> tmp = new HashMap<>();
        Class<?> cls = Class.forName(name);
        Object obj = cls.getDeclaredConstructor().newInstance();
        Method[] testMethod = cls.getMethods();

        List<Method> metBefore = new ArrayList<Method>();
        List<Method> metBeforeClass = new ArrayList<Method>();
        List<Method> metAfter = new ArrayList<Method>();
        List<Method> metAfterClass = new ArrayList<Method>();
        List<Method> metTest = new ArrayList<Method>();

        //sort the array to get alphabetical order
        Method change;
        for(int i = 0; i<testMethod.length;i++){
            for(int j = i+1; j<testMethod.length;j++){
               if(testMethod[i].toString().compareTo(testMethod[j].toString())>0){
                   change = testMethod[i];
                   testMethod[i] = testMethod[j];
                   testMethod[j] = change;
               }
            }
        }

        for(Method m: testMethod){
            Annotation[] myJunit = m.getAnnotations();
            //A method can have only one annotation {@Test, @BeforeClass, @Before, @AfterClass, @After}
            if(myJunit.length>1){ throw new IllegalAccessException(); }
            if(myJunit.length == 0)continue;
            if(myJunit[0].annotationType().getSimpleName().endsWith("Before")){
                metBefore.add(m);
            }else if(myJunit[0].annotationType().getSimpleName().endsWith("BeforeClass")) {
                //@BeforeClass and @AfterClass can only appear on static methods.
                if (Modifier.isStatic(m.getModifiers())) { metBeforeClass.add(m);
                }else{ throw new IllegalAccessError(); }
            }
            else if(myJunit[0].annotationType().getSimpleName().endsWith("Test")){
                metTest.add(m);
            }else if(myJunit[0].annotationType().getSimpleName().endsWith("After")){
                metAfter.add(m);
            }else if(myJunit[0].annotationType().getSimpleName().endsWith("AfterClass")) {
                if (Modifier.isStatic(m.getModifiers())) { metAfterClass.add(m);
                }else{ throw new IllegalAccessError(); }
            }
        }
        //testClass should catch all throwable from invoking test methods and return them in its result
        //BeforeClass annotated methods should be run even if no @test
        if(metBeforeClass.size()>0){
            for(Method i:metBeforeClass){
                try { i.invoke(obj);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //@Before should be run before each execution of a test method
        if(metTest.size()>0){
            if(metBefore.size()>0){
                for(Method i:metBefore){
                    try { i.invoke(obj);
                    } catch (Throwable e) { throw new RuntimeException(e); }
                }
            }
            for(Method m: metTest){
                int pass = 1;
                if(m!=null){
                    try{ m.invoke(obj);
                    } catch (Exception e) {
                        tmp.put(m.getName(),e.getCause()); pass = 0;
                    }
                    if(pass == 1){ tmp.put(m.getName(),null); }
                }
            }
        }

        if(metAfterClass.size()>0){
            for(Method i:metAfterClass){
                try { i.invoke(obj);
                } catch (Throwable e) { throw new RuntimeException(e); }
            }
        }
        if(metAfter.size()>0 && metTest.size()>0){
            for(Method i:metAfter){
                try { i.invoke(obj);
                } catch (Throwable e) { throw new RuntimeException(e); }
            }
        }
        return tmp;
    }

    //in quickCheck the input is also a class but has a property defined inside
    public static HashMap<String, Object[]> quickCheckClass(String name) throws Exception {
        HashMap<String,Object[]> quick = new HashMap<>();
        Class<?> cls = Class.forName(name);
        Object obj = cls.getDeclaredConstructor().newInstance();
        Method[] testMethod = cls.getDeclaredMethods();

        //sort the array to get alphabetical order
        Method change;
        for(int i = 0; i<testMethod.length;i++){
            for(int j = i+1; j<testMethod.length;j++){
                if(testMethod[i].toString().compareTo(testMethod[j].toString())>0){
                    change = testMethod[i];
                    testMethod[i] = testMethod[j];
                    testMethod[j] = change;
                }
            }
        }

        for(Method m:testMethod){
            if(m.getAnnotation(Property.class)!=null){
                try{
                    //use this in List part
                    AnnotatedType[] annotate = m.getAnnotatedParameterTypes();
                    //use this to know every Raw Annotation in every parameter
                    //in this case it accepts multiple annotations in one parameter
                    //but we only consider the first one
                    Annotation[][] a = m.getParameterAnnotations();

                    Parameter[] parameters = m.getParameters();
                    Class[] paramTypes = m.getParameterTypes();

                    List<List<Object>> args = new ArrayList<>();
                    Object[] test = new Object[parameters.length];
                    //check all the parameters with proper annotationType

                    //System.out.println("AnnotatedType[] annotate "+Arrays.toString(annotate));
                    //System.out.println("Annotation[][] a[0] "+ Arrays.toString(a[0]));
                    for(int i=0; i<parameters.length;i++){
                        if(paramTypes[i].getSimpleName().equals("Integer") &&
                                a[i][0].annotationType().equals(IntRange.class)){

                            IntRange tmp = annotate[i].getAnnotation(IntRange.class);
                            int max = tmp.max();
                            int min = tmp.min();
                            List<Object> input = new ArrayList<>(max-min);
                            for(int j = 0; j< max-min+1;j++){
                                input.add(j+min);
                            }
                            args.add(input);

                        }else if (paramTypes[i].getSimpleName().equals("String") &&
                                a[i][0].annotationType().equals(StringSet.class)){

                            String[] s = annotate[i].getAnnotation(StringSet.class).strings();
                            List<Object> input = new ArrayList<>(s.length);
                            input.addAll(Arrays.asList(s));
                            args.add(input);
                        }else if(paramTypes[i].getSimpleName().equals("List") &&
                                a[i][0].annotationType().equals(ListLength.class)){
                            ListLength list_len = (ListLength)a[i][0];
                            int min_len = list_len.min();
                            int max_len = list_len.max();
                            List<Object> input = new ArrayList<>(max_len-min_len);
                            myList(annotate[i],input,1);
                            //build the result for all the possible list
                            args.add(input);
                        }else if(paramTypes[i].getSimpleName().equals("Object") &&
                                a[i][0].annotationType().equals(ForAll.class)){

                            ForAll o = annotate[i].getAnnotation(ForAll.class);
                            List<Object> input = new ArrayList<>();

                            for(Method k : testMethod){
                                if(o.name().equals(k.getName())){
                                    for(int call = 1; call <o.times()+1; call++){
                                        input.add((Object)k.invoke(obj));
                                    }
                                }
                            }
                            args.add(input);
                        }else{ throw new UnsupportedOperationException(); }
                    }
                    List<Object> perm = new ArrayList<>();
                    List<List<Object>> result = new ArrayList<>();
                    ParameterPerm(args,0,perm,result);

                    int mark = 0;
                    for(int k=0; k<result.size() && k<100; k++){
                        for(int j=0; j<parameters.length;j++){
                            test[j] = (Object)result.get(k).get(j);
                        }
                        if(!(boolean)m.invoke(obj,test)){
                            quick.put(m.getName(),test);
                            mark = 1;
                            break;
                        }
                    }
                    if(mark == 0){
                        quick.put(m.getName(),null);
                    }
                    //map to the array of arguments for which the method returned false or threw a Throwable
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return quick;
    }

    public static List<Object> getArgmentInput(AnnotatedType annotatedType){
        List<Object> input = new ArrayList<>();

        AnnotatedParameterizedType typeWithAnnotation = (AnnotatedParameterizedType)annotatedType;
        ParameterizedType typeWithParametre = (ParameterizedType)annotatedType.getType();
        if(typeWithParametre.getRawType().equals(IntRange.class)){
            IntRange tmp = annotatedType.getAnnotation(IntRange.class);
            int max = tmp.max();
            int min = tmp.min();
            for(int j = 0; j< max-min+1;j++){
                input.add(j+min);
            }
        }else if (typeWithParametre.getRawType().equals(StringSet.class)){
            String[] s = null;
            input.addAll(Arrays.asList(s));
        }else if(typeWithParametre.getRawType().equals(ListLength.class)){
            ListLength list_len = (ListLength)annotatedType.getAnnotations()[0];
            int min_len = list_len.min();
            int max_len = list_len.max();
            myList(annotatedType,input,1);
            //build the result for all the possible list
        }else if(typeWithParametre.getRawType().equals(ForAll.class)){
            ForAll o = annotatedType.getAnnotation(ForAll.class);
            String method = o.name();
            int times = o.times();
            for(int k=1; k<times+1;k++){
                List<Object> tmp = new ArrayList<>();
                tmp.add(k);
                tmp.add(new Object());
                input.add(tmp);
            }
        }else{ throw new UnsupportedOperationException(); }
         /*for(AnnotatedType t:annotate) {
             AnnotatedParameterizedType typeWithAnnotation = (AnnotatedParameterizedType)t;
             ParameterizedType typeWithParametre = (ParameterizedType)t.getType();

         }*/
         return input;
    }

    private static void ParameterPerm(List<List<Object>> ParaList,int index,List<Object> perm,List<List<Object>>result){
        if(index<0){
            throw new UnsupportedOperationException();
        }
        if(index == ParaList.size()){
            List<Object> update = new ArrayList<>(perm);
            result.add(update);
            return;
        }
        for(Object o:ParaList.get(index)){
            perm.add(o);
            ParameterPerm(ParaList,index+1,perm,result);
            perm.remove(perm.size()-1);
        }
    }

    private static void myList(AnnotatedType annotatedType,List<Object> input, int level) {
        //System.out.println(annotatedType.toString());
        if (annotatedType instanceof AnnotatedParameterizedType) {
            if(level>10){ throw new UnsupportedOperationException("too much recursive"); }
            AnnotatedParameterizedType typeWithAnnotation = (AnnotatedParameterizedType)annotatedType;
            AnnotatedType[] aTypes = typeWithAnnotation.getAnnotatedActualTypeArguments();
            for (AnnotatedType aType : aTypes){
                ParameterizedType test = (ParameterizedType)aType.getType();
               // System.out.println(aType.toString());
                if(test.getRawType().equals(IntRange.class)){
                 //   System.out.println("intarange");
                }
                myList(aType, input,level + 1);
            }
        }else {
            if (annotatedType.getType().equals(IntRange.class)) {
 //               System.out.println("intarange");
                IntRange tmp = annotatedType.getAnnotation(IntRange.class);
                int max = tmp.max();
                int min = tmp.min();
                for (int j = 0; j < max - min + 1; j++) {
                    input.add(j + min);
                }
            } else if (annotatedType.getType().equals(StringSet.class)) {

                String[] s = annotatedType.getAnnotation(StringSet.class).strings();
                for (String str : s) {
                    input.add(s);
                }
            } else if (annotatedType.getType().equals(ListLength.class)) {
                ListLength list_len = (ListLength) annotatedType.getAnnotations()[0];
                int min_len = list_len.min();
                int max_len = list_len.max();
            } else if (annotatedType.getType().equals(ForAll.class)) {
                //System.out.println(a[i][0].annotationType());
                ForAll o = annotatedType.getAnnotation(ForAll.class);
                String method = o.name();
                int times = o.times();
                for (int k = 1; k < times + 1; k++) {
                    List<Object> tmp = new ArrayList<>();
                    tmp.add(k);
                    tmp.add(new Object());
                    input.add(tmp);
                }
            }
        }
    }
}