package css;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Description: 通过反射获取Student类的属性
 * @Author: CSS
 * @Date: 2023/12/1 14:33
 */
public class ObtainStudentField {

    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> clazz = Class.forName("css.pojo.Student");

        //获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //修饰符+" "+属性类型+" "+属性名
            System.out.println(getModifier(field.getModifiers()) + " " + field.getType() + " " + field.getName());
            //属性上的注解数组
            Annotation[] annotations = field.getDeclaredAnnotations();
            //注解的个数
            System.out.println(annotations.length);
            for (Annotation annotation : annotations) {
                //注解的类型
                System.out.println(annotation.annotationType());
            }
            System.out.println("————————————————————————————————————————————\n");
        }
    }


    private static String getModifier(int i) {
        /*String modifier = "";
        switch (i) {
            case 1:
                modifier = "public";
                break;
            case 2:
                modifier = "private";
                break;
            case 4:
                modifier = "protected";
                break;
        }

        return modifier;*/

        if(Modifier.isProtected(i))
            return "protected";
        if(Modifier.isPrivate(i))
            return "private";
        return "public";
    }
}
