package com.example.covidpassproject;

public class Person {
    private String Name;
    private String Email;
    private String Phone;
    private String vacID;
    private String ID;
    private String Password;
    private String VaccinationStatus;


    // DON'T DELETE THIS
    // Firebase API requires an empty constructor for it's introspection systems
    public Person() {}

    public Person(String name, String email, String phone, String vacID, String ID, String password,String vaccinationStatus) {
        this.Name = name;
        this.Email = email;
        this.Phone = phone;
        this.vacID = vacID;
        this.ID = ID;
        this.Password = password;
        this.VaccinationStatus = vaccinationStatus;

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

    public String getVaccinationStatus() {
        return VaccinationStatus;
    }


}
