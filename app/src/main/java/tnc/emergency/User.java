package tnc.emergency;

/**
 * Created by Danyal on 27/04/17.
 */
public class User {
    public String name  , age  ,dob , bGroup  ;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String age, String dob, String bGroup )
    {
        this.name = name;
        this.age = age;
        this.dob = dob ;
        this.bGroup = bGroup;


    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getDob() {
        return dob;
    }

    public String getbGroup() {
        return bGroup;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setbGroup(String bGroup) {
        this.bGroup = bGroup;
    }
}
