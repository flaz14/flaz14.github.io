package dto;

import java.util.Objects;

public class Address {
    private String country;
    private String town;
    private String street;
    private String buildingNumber;

    public Address country(String country) {
        this.country = country;
        return this;
    }

    public String country() {
        if (Objects.equals(country, "USA")) {
            throw new IllegalStateException("This is workaround for demonstrating nasty Java 8 stacktraces.");
        }
        return this.country;
    }

    public Address town(String town) {
        this.town = town;
        return this;
    }

    public String town() {
        return this.town;
    }

    public Address street(String street) {
        this.street = street;
        return this;
    }

    public String street() {
        return this.street;
    }

    public Address buildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
        return this;
    }

    public String buildingNumber() {
        return this.buildingNumber;
    }
}
