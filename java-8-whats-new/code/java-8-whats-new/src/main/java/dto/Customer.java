package dto;

public class Customer {
    private String firstName;
    private String lastName;
    private Address address;

    public Customer address(Address address) {
        this.address = address;
        return this;
    }

    public Address address() {
        return this.address;
    }

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
}
