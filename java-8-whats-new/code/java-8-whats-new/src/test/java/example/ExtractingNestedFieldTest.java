package example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ExtractingNestedFieldTest {
    @Test
    public void java7() throws Exception {
        List<String> extracted = CountriesExtractor.java7(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Trivial() throws Exception {
        List<String> extracted = CountriesExtractor.Java8.trivial(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Stream() throws Exception {
        List<String> extracted = CountriesExtractor.Java8.stream(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8StreamWithCheckingForNull() throws Exception {
        List<String> extracted = CountriesExtractor.Java8.streamWithHandlingNullAddresses(Sample.Input.withMalformedCustomer());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    private static class Sample {
        static class Input {
            static List<Customer> customers() {
                return asList(
                        new Customer().
                                firstName("John").
                                lastName("Smith").
                                address(new Address().
                                        country("USA").
                                        town("New York").
                                        street("Brighton Beach").
                                        buildingNumber("19")),
                        new Customer().
                                firstName("Ivan").
                                lastName("Ivanov").
                                address(new Address().
                                        country("Russia").
                                        town("Moscow").
                                        street("Arbat").
                                        buildingNumber("2")),
                        new Customer().
                                firstName("Samo").
                                lastName("Law").
                                address(new Address().
                                        country("China").
                                        town("Peking").
                                        street("some street").
                                        buildingNumber("some building number"))
                );
            }

            private static List<Customer> withMalformedCustomer() {
                final List<Customer> customers = new ArrayList<>();
                customers.addAll(customers());

                // This particular customer will have `address` field equal to `null`.
                Customer malformedCustomer = new Customer();
                customers.add(malformedCustomer);

                return customers;
            }
        }

        private static List<String> outputCountries() {
            return asList("USA", "Russia", "China");
        }
    }

    private static class CountriesExtractor {
        static List<String> java7(final List<Customer> customers) {
            final List<String> countries = new ArrayList<>();
            for (final Customer customer : customers) {
                String country = customer.address().country();
                countries.add(country);
            }
            return unmodifiableList(countries);
        }

        static class Java8 {
            static List<String> trivial(
                    final List<Customer> customers) {
                final List<String> countries = new ArrayList<>();
                customers.forEach(customer -> {
                    String country = customer.address().country();
                    countries.add(country);
                });
                return unmodifiableList(countries);
            }

            static List<String> stream(
                    final List<Customer> customers) {
                return customers.
                        stream().
                        map(customer -> customer.address().country()).
                        collect(collectingAndThen(toList(), Collections::unmodifiableList));
            }

            static List<String> streamWithHandlingNullAddresses(final List<Customer> customers) {
                return customers.
                        stream().
                        filter(customer -> nonNull(customer.address())).
                        map(customer -> customer.address().country()).
                        collect(collectingAndThen(toList(), Collections::unmodifiableList));
            }
        }
    }
}

class Customer {
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

class Address {
    private String country;
    private String town;
    private String street;
    private String buildingNumber;

    public Address country(String country) {
        this.country = country;
        return this;
    }

    public String country() {
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
