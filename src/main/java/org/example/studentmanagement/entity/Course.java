package org.example.studentmanagement.entity;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int id;
    private String code;
    private String name;
    private Teacher teacher;
    private List<Student> students;

    public Course() {

    }


    public Course(int id, String code, String name, Teacher teacher, List<Student> students) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Teacher getTeacher() {
        return teacher;
    }



    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.addCourse(this);
    }



    public List<Student> getStudents() {
        return students;
    }


    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int studentListSize() {
        return students.size();
    }

    public void addStudent(Student student) {
        if(students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
    }

    public void removeStudent(Student student) {
        if(students.contains(student)) {
            students.remove(student);
        }
    }

    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof Course)) {
            return false;
        }

        Course comparedCourse = (Course) comparedObject;

        if (this.id == comparedCourse.id) {
            return true;
        }

        return false;
    }

}
