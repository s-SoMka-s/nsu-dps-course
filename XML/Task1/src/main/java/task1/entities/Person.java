package task1.entities;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String id = "";
    private String firstName = "";
    private String surname = "";
    private Gender gender = Gender.Unknown;
    private List<Person> parents = new ArrayList<>();
    private List<Person> siblings = new ArrayList<>();
    private Person spouse = null;

    public Person() {

    }

    public Person(String fullName) {
        var splitted = fullName.split(" ");
        firstName = splitted[0];
        if (splitted.length > 1) {
            surname = splitted[1];
        }
    }

    public Person(String fullName, Gender gender) {
        this(fullName);
        this.gender = gender;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getName() {
        return this.firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getFullName() {
        return this.firstName + " " + this.surname;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean hasGender() {
        return gender != Gender.Unknown;
    }

    public boolean hasFirstName() {
        return !firstName.isBlank();
    }

    public boolean hasSurname() {
        return !surname.isBlank();
    }

    public boolean hasSpouse() {
        return spouse != null;
    }

    public void setSpouse(Person spouse) {
        spouse = spouse;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        builder.append("id: " + this.getId() + "\n");
        builder.append("full name: " + this.getFullName() + "\n");
        builder.append("gender: " + gender + "\n");
        builder.append("has spouse: " + this.hasSpouse() + "\n");
        if (this.hasSpouse()) {
            builder.append("spouse id: " + this.spouse.getId() + "\n");
            builder.append("spouse full name: " + this.spouse.getFullName() + "\n");
        }

        return builder.toString();
    }
}
