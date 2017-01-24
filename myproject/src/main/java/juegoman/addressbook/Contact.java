package juegoman.addressbook;

import java.io.Serializable;

public class Contact implements Serializable {
    
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    
    public Contact() {}
    
    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = "";
        this.email = "";
        this.phone = "";
    }
    
    @Override
    public String toString() {
        return getFullName(); 
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    
}
