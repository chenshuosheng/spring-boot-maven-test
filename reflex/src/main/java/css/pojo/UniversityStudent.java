package css.pojo;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/12/3 21:54
 */


public class UniversityStudent extends Student {

    private String universitySchool;

    public UniversityStudent(Student student, String universitySchool){
        super(student);
        this.universitySchool = universitySchool;
    }

    @Override
    public void test1(){
        System.out.println("我是重写的test1方法");
    }

    public void test1(String admin){
        System.out.println("我是重载的test1方法，由"+admin+"执行");
    }

    public void test4() {
        System.out.println("我叫" + getName() + "就读于" + this.universitySchool);
    }

    @Override
    public String toString() {
        return "UniversityStudent{" +
                "universitySchool='" + universitySchool + '\'' +
                "} " + super.toString();
    }
}
