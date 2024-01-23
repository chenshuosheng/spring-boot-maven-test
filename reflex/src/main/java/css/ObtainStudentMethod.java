package css;

import css.pojo.Student;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 通过反射获取Student的方法
 * @Author: CSS
 * @Date: 2023/12/1 16:01
 */


public class ObtainStudentMethod {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Class<?> clazz = Class.forName("css.pojo.Student");

        Class<?> superclass = clazz.getSuperclass();
        //父类所含方法
        Method[] superclassDeclaredMethods = superclass.getDeclaredMethods();
        List<Method> supperCollect = Arrays.stream(superclassDeclaredMethods).collect(Collectors.toList());

        //本类所含属性
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldName = Arrays.stream(fields).map(field -> field.getName().toLowerCase()).collect(Collectors.toList());

        //本类所含方法
        Method[] methods = clazz.getDeclaredMethods();

        //只保留本类自定义方法(排除set、get、toString、hashcode)
        List<Method> collect = Arrays.stream(methods).collect(Collectors.toList());

        Student student = (Student) clazz.newInstance();
        for (int i = 0; i < collect.size(); ) {
            Method method = collect.get(i);
            String methodName = method.getName();
            if (isSpecialMethod(methodName)) {
                collect.remove(method);
            } else if (isGetterOrSetter(methodName, fieldName)) {
                collect.remove(method);
            } /*else if (isSupperMethod(method, supperCollect)) {//getDeclaredMethods()不会包含继承的方法
                collect.remove(method);
            }*/ else {//直接保留
                i++;
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 0)
                    method.invoke(student);
            }
        }

        for (Method method : collect) {
            System.out.println(method);
        }
    }


    private static boolean isSpecialMethod(String methodName) {
        if ("equals".equals(methodName))
            return true;
        if ("toString".equals(methodName))
            return true;
        if ("hashCode".equals(methodName))
            return true;
        return false;
    }


    private static boolean isGetterOrSetter(String methodName, List<String> fieldName) {
        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            methodName = methodName.substring(3).toLowerCase();
            if (fieldName.contains(methodName))
                return true;
        }
        return false;
    }


    private static boolean isSupperMethod(Method method, List<Method> supperMethods) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Method supperMethod : supperMethods) {
            String supperMethodName = supperMethod.getName();
            Class<?>[] supperMethodParameterTypes = supperMethod.getParameterTypes();
            //只有当方法名和形参类型数组相同时，才说明是从父类继承而来的方法
            if (methodName.equals(supperMethodName) && parameterTypes.equals(supperMethodParameterTypes)) {
                return true;
            }
        }
        return false;
    }



    /*private static String getMethodName(Method method) {
        String name = method.getName();
        //获取最后一个“.”的下标
        int lastIndexOf = name.lastIndexOf(".");
        //去除修饰符、类型、包名等信息，剩下方法名(args)
        name = name.substring(lastIndexOf+1);
        //获取左括号下标
        int index = name.indexOf("(");
        //去除"("及后面的信息，剩下方法名
        return name.substring(0,index);
    }*/
}
