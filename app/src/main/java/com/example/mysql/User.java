package com.example.mysql;

public class User {
    private int id;
    private String name;
    private String surname;
    private String middlename;
    private int access;
    private String rfidId;

    public User(int id, String name, String surname, String middlename, int access, String rfidId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middlename = middlename;
        this.access = access;
        this.rfidId = rfidId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public int getAccess() {
        return access;
    }

    public String getRfidId() {
        return rfidId;
    }
}
