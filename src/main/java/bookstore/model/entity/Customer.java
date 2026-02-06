package bookstore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
@Table(name = "customer")
public class Customer {
    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", nullable = true)
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerID;

    public Customer() { }

    public Customer(String fullName, int age, String phoneNumber, String email, String address) {
        this.fullName = fullName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
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
}
