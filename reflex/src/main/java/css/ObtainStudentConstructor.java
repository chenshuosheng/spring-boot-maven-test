package css;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 通过反射获取Student构造方法
 * @Author: CSS
 * @Date: 2023/12/1 10:41
 */
public class ObtainStudentConstructor {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> clazz = Class.forName("css.pojo.Student");

        //Constructor<?> constructor = clazz.getConstructor();    //获取无参构造方法（如果Student中自定义了带参构造且不包含无参构造，则会抛出异常）
        //System.out.println(constructor);                        //public css.pojo.Student()

        //Object o = constructor.newInstance();                   //用得到的无参构造方法创建一个对象
        //System.out.println(o);                                  //Student(name=null, age=0, sex= , birthday=null)


        //获取构造方法对象数组
        //1.获取公共构造方法
        Constructor<?>[] constructors = clazz.getConstructors();
        List<Constructor<?>> list1 = Arrays.stream(constructors).collect(Collectors.toList());
        extracted(list1);

        System.out.println("——————————————————————————————————————————————————————————————————————————————");
        System.out.println("——————————————————————————————————————————————————————————————————————————————");

        //2.获取所有构造方法
        Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();
        List<Constructor<?>> list2 = Arrays.stream(allConstructors).collect(Collectors.toList());
        //获取非公共构造方法
        for (Constructor<?> constructor : list1) {
            if(list2.contains(constructor))
                list2.remove(constructor);
        }
        //Class css.pojo.ObtainStudentMethod can not access a member of class css.pojo.Student with modifiers "private"
        //需要解除对非公共构造方法的访问限制
        for (Constructor<?> constructor : list2) {
            constructor.setAccessible(true);
        }
        extracted(list2);
    }


    private static void extracted(List<Constructor<?>> constructors) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        for (Constructor<?> constructor : constructors) {
            System.out.println("\n--------------------------------------------------------------");
            System.out.println(constructor);                //方法，含修饰符
            System.out.println(constructor.getName());      //方法名，不含修饰符

            //通过在pom.xml中添加“ <arg>-parameters</arg>” 编译选项来实现保留参数名信息
            //获取参数名
            Parameter[] parameters = constructor.getParameters();
            String[] paramsName = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                String name = parameters[i].getName();
                //去除包名
                int index = name.lastIndexOf(".");
                paramsName[i] = index > 0 ? name.substring(index + 1) : name;
            }

            //获取参数类型
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            //存储调用方法传入实参
            Object[] objects = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == String.class) {
                    if ("name".equals(paramsName[i]))
                        objects[i] = "CSS";
                    else //birthPlace
                        objects[i] = "广东揭阳";
                } else if (parameterTypes[i] == int.class && "age".equals(paramsName[i])) {
                    objects[i] = 23;
                } else if (parameterTypes[i] == char.class && "sex".equals(paramsName[i])) {
                    objects[i] = '男';                               //切勿写成双引号
                } else if (parameterTypes[i] == Date.class) {
                    objects[i] = new Date();
                } else if (parameterTypes[i] == double.class) {
                    if ("weight".equals(paramsName[i]))
                        objects[i] = 50.0;
                    else//"height".equals(paramsName[i])
                        objects[i] = 168.5;
                } else {
                    objects[i] = new Object();
                }
            }

            //创建实例
            Object o = constructor.newInstance(objects);
            System.out.println(o);
        }
    }
}
