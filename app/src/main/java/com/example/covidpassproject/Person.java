package com.example.covidpassproject;

public class Person {
    String Name;
    String Email;
    String Phone;
    String vacID;
    String ID;
    String Password;


    // DON'T DELETE THIS
    // Firebase API requires an empty constructor for it's introspection systems
    public Person() {}

    public Person(String name, String email, String phone, String vacID, String ID, String password) {
        Name = name;
        Email = email;
        Phone = phone;
        this.vacID = vacID;
        this.ID = ID;
        Password = password;

    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getVacID() {
        return vacID;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return Password;
    }


}
