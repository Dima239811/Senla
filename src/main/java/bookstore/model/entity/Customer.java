package bookstore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter

@Entity
@Table(name = "customer")
public class Customer {
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", nullable = true)
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerID;

    public Customer() { }

    public Customer(String fullName, int age, String phoneNumber, String email, String address, User user) {
        this.fullName = fullName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.user = user;
    }

    public Customer(String fullName, int age, String phoneNumber, String email, String address, int customerID) {
        this.fullName = fullName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.customerID = customerID;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "fullName='" + fullName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", customerID=" + customerID +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Customer customer = (Customer) object;
        return age == customer.age  && Objects.equals(fullName, customer.fullName) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(email, customer.email) && Objects.equals(address, customer.address) && Objects.equals(user, customer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, age, phoneNumber, email, address, user, customerID);
    }
}
