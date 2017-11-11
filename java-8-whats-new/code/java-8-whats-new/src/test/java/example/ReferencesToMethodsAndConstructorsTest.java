package example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 */
public class ReferencesToMethodsAndConstructorsTest {
    @Test
    public void java7() throws Exception {
        List<String> extracted = extractCountriesJava7(customers());
        assertThat(extracted, equalTo(expectedCountries()));
    }

    @Test
    public void java8Trivial() throws Exception {
        List<String> extracted = extractCountriesJava8Trivial(customers());
        assertThat(extracted, equalTo(expectedCountries()));
    }

    @Test
    public void java8Stream() throws Exception {
        List<String> extracted = extractCountriesJava8Stream(customers());
        assertThat(extracted, equalTo(expectedCountries()));
    }

    @Test
    public void java8StreamWithCheckingForNull() throws Exception {
        List<String> extracted = extractCountriesJava8StreamWithCheckingForNull(customersWithMalformedCustomer());
        assertThat(extracted, equalTo(expectedCountries()));
    }

    private List<String> extractCountriesJava7(final List<Customer> customers) {
        final List<String> countries = new ArrayList<>();
        for (final Customer customer : customers) {
            String country = customer.address.country;
            countries.add(country);
        }
        return unmodifiableList(countries);
    }

    private List<String> extractCountriesJava8Trivial(
            final List<Customer> customers) {
        final List<String> countries = new ArrayList<>();
        customers.forEach(customer -> {
            String country = customer.address.country;
            countries.add(country);
        });
        return unmodifiableList(countries);
    }

    private List<String> extractCountriesJava8Stream(
            final List<Customer> customers) {
        return customers.
                stream().
                map(customer -> customer.address.country).
                collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    private List<String> extractCountriesJava8StreamWithCheckingForNull(final List<Customer> customers) {
        return customers.
                stream().
                filter(customer -> nonNull(customer.address)).
                map(customer -> customer.address.country).
                collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    private static List<Customer> customers() {
        final Address address1 = new Address();
        address1.country = "USA";
        address1.town = "New York";
        address1.street = "Brighton Beach";
        address1.buildingNumber = "19";

        final Address address2 = new Address();
        address2.country = "Russia";
        address2.town = "Moscow";
        address2.street = "Arbat";
        address2.buildingNumber = "2";

        final Address address3 = new Address();
        address3.country = "China";
        address3.town = "Peking";
        address3.street = "N/A";
        address3.buildingNumber = "N/A";

        // All the fields will be equal to null by default.
        final Address address4 = new Address();

        final Customer customer1 = new Customer();
        customer1.firstName = "John";
        customer1.lastName = "Smith";
        customer1.address = address1;

        final Customer customer2 = new Customer();
        customer2.firstName = "Ivan";
        customer2.lastName = "Ivanov";
        customer2.address = address2;

        final Customer customer3 = new Customer();
        customer3.firstName = "Samo";
        customer3.lastName = "Law";
        customer3.address = address3;

        final Customer customer4 = new Customer();
        customer4.address = address4;

        return Arrays.asList(customer1, customer2, customer3, customer4);
    }

    private static List<Customer> customersWithMalformedCustomer() {
        final List<Customer> customers = new ArrayList<>();
        customers().addAll(customers());
        customers.add(new Customer()); // this particular customer with have address == null
        return customers;
    }


    private static List<String> expectedCountries() {
        return Arrays.asList("USA", "Russia", "China", null);
    }

}

class Customer {
    String firstName;
    String lastName;
    Address address;
}

class Address {
    String country;
    String town;
    String street;
    String buildingNumber;
}
