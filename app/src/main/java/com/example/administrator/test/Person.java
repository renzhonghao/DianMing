package com.example.administrator.test;

/**
 * Created by Administrator on 2016/10/30.
 */

public class Person {

    private String _id;
    private String name;
    private String banji;
    private String phone;
    private String salary;

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBanji() {
        return banji;
    }
    public void setBanji(String banji) {
        this.banji = banji;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSalary() {
        return salary;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return name + ", " + phone + ", " + salary;
    }
    public Person(String _id, String name,String banji, String phone, String salary) {
        super();
        this._id = _id;
        this.name = name;
        this.banji=banji;
        this.phone = phone;
        this.salary = salary;
    }


}
