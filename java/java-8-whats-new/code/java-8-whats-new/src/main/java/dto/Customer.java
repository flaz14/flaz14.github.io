package dto;

import java.util.List;

public class Customer {
    private String firstName;
    private String lastName;
    private Address address;
    private List<String> phoneNumbers;

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String firstName() {
        return this.firstName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String lastName() {
        return lastName;
    }

    public Customer address(Address address) {
        this.address = address;
        return this;
    }

    public Address address() {
        return this.address;
    }

    public Customer phoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public List<String> phoneNumbers() {
        return this.phoneNumbers;
    }
}
