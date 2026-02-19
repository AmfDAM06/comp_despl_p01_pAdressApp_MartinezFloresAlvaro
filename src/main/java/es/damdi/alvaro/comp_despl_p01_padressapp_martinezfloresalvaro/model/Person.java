package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Person.
 *
 * @author Marco Jakob
 */
public class Person {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> birthday;


    /**
     * Default constructor.
     */
    public Person() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param firstName the first name
     * @param lastName  the last name
     */
    public Person(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);

        // Some initial dummy data, just for convenient testing.
        this.street = new SimpleStringProperty("some street");
        this.postalCode = new SimpleIntegerProperty(1234);
        this.city = new SimpleStringProperty("some city");
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * First name property string property.
     *
     * @return the string property
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * Last name property string property.
     *
     * @return the string property
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Gets street.
     *
     * @return the street
     */
    public String getStreet() {
        return street.get();
    }

    /**
     * Sets street.
     *
     * @param street the street
     */
    public void setStreet(String street) {
        this.street.set(street);
    }

    /**
     * Street property string property.
     *
     * @return the string property
     */
    public StringProperty streetProperty() {
        return street;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public int getPostalCode() {
        return postalCode.get();
    }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    /**
     * Postal code property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public String getCity() {
        return city.get();
    }

    /**
     * Sets city.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city.set(city);
    }

    /**
     * City property string property.
     *
     * @return the string property
     */
    public StringProperty cityProperty() {
        return city;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday.get();
    }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    /**
     * Birthday property object property.
     *
     * @return the object property
     */
    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    /**
     * From pojo person.
     *
     * @param d the d
     * @return the person
     */
    public static Person fromPOJO(PersonPOJO d) {
        Person p = new Person();
        p.setFirstName(d.firstName);
        p.setLastName(d.lastName);
        p.setStreet(d.street);
        p.setPostalCode(d.postalCode);
        p.setCity(d.city);
        p.setBirthday(d.birthday);
        return p;
    }

    /**
     * To pojo person pojo.
     *
     * @return the person pojo
     */
    public PersonPOJO toPOJO() {
        PersonPOJO d = new PersonPOJO ();
        d.firstName = getFirstName();
        d.lastName = getLastName();
        d.street = getStreet();
        d.postalCode = getPostalCode();
        d.city = getCity();
        d.birthday = getBirthday();
        return d;
    }
}