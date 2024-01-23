package css.pojo;

//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/12/1 9:41
 */

@Data
public class Student {

    /*@ApiModelProperty(value = "名字")*/
    private String name;

    /*@ApiModelProperty(value = "年龄")*/
    private int age;

    private char sex;

    /*@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")*/
    private Date birthday;

    private String birthPlace;

    private double weight;

    private double height;

    public Student(){}

    public Student(Student student) {
        this.name = student.name;
        this.age = student.age;
        this.sex = student.sex;
        this.birthday = student.birthday;
        this.birthPlace = student.birthPlace;
        this.weight = student.weight;
        this.height = student.height;
    }

    protected Student(String name, int age, char sex, Date birthday, String birthPlace, double weight, double height) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.weight = weight;
        this.height = height;
    }

    public Student(String name){
        this.name = name;
    }

    public Student(String name, int age){
        this.name = name;
        this.age = age;
    }

    public Student(String name, int age, char sex){
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Student(String name, double weight, double height){
        this.name = name;
        this.weight = weight;
        this.height = height;
    }


    public void test1(){
        System.out.println("-----------------------------test1被调用");
    }

    protected void test2(){
        System.out.println("-----------------------------test2被调用");
    }

    private void test3(){
        System.out.println("-----------------------------test3被调用");
    }
}
