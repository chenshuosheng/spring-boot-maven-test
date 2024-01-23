package css;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/12/1 9:48
 */


public class ObtainStudentClass {

    public static void main(String[] args) throws ClassNotFoundException {

/*        //获取class对象
        //1.通过创建一个对象，然后调用方法获取其静态属性class       //相比2有些多此一举
        Student student = new Student();                        //产生一个Student对象和一个Class对象
        Class<?> clazz = student.getClass();                    //获取产生的Class对象
        System.out.println(clazz);

        //2.直接获取类的静态成员(任何数据类型都有自己的一个"静态"属性)//一般也不推荐使用，需要导入类的包，依赖太强
        Class<?> clazz2 = Student.class;                        //只产生Class对象
        System.out.println(clazz2);
        System.out.println(clazz == clazz2);

        //3.通过调用Class类的静态方法forName
        Class<?> clazz3 = Class.forName("css.pojo.Student");    //只产生Class对象，便捷，一个字符串可以传入也可写在配置文件中等多种方法
        System.out.println(clazz3);
        System.out.println(clazz2 == clazz3);*/
    }
}
