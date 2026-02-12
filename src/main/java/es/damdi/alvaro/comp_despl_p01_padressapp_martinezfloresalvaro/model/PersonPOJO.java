package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model;

import java.time.LocalDate;

public class PersonPOJO {
    public String firstName;
    public String lastName;
    public String street;
    public int postalCode;
    public String city;
    public LocalDate birthday;

    public PersonPOJO() {
    }

    public PersonPOJO(String firstName, String lastName, String street, int postalCode, String city, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }


}