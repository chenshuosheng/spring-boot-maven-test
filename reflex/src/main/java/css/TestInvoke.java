package css;

import css.pojo.Student;
import css.pojo.UniversityStudent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Description: 练习invoke方法
 * @Author: CSS
 * @Date: 2023/12/3 22:00
 */


public class TestInvoke {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<?> clazz1 = Class.forName("css.pojo.Student");
        Class<?> clazz2 = Class.forName("css.pojo.UniversityStudent");

        Student student = (Student) clazz1.newInstance();
        student.setName("CSS");
        student.setAge(23);
        student.setSex('男');
        student.setBirthPlace("广东揭阳");
        student.setBirthday(new Date());
        student.setWeight(50d);
        student.setHeight(168.5d);

        Constructor<?> constructor = clazz2.getConstructor(Student.class,String.class);
        UniversityStudent universityStudent = (UniversityStudent) constructor.newInstance(student, "广州大学");
        System.out.println(universityStudent);


        Method method1 = clazz1.getMethod("test1");
        Method method2 = clazz2.getMethod("test1");
        Method method3 = clazz2.getMethod("test1",String.class);

        try {
            method1.invoke(student);            //-----------------------------test1被调用
            method1.invoke(universityStudent);  //我是重写的test1方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        try {
            method2.invoke(universityStudent);  //我是重写的test1方法
            method2.invoke(student);            //父类对象不能调用子类的方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            String admin = "css";
            method3.invoke(universityStudent,admin);//我是重载的test1方法，由css执行
            method3.invoke(student,admin);          //父类对象不能调用子类的方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
