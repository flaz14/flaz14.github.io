package stream;

import dto.Address;
import dto.Customer;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class ExtractingNestedValueTest {
    @Test
    public void java7() throws Exception {
        Set<String> extracted = CountriesExtractor.java7(Sample.inputCustomers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Trivial() throws Exception {
        Set<String> extracted = CountriesExtractor.Java8.trivial(Sample.inputCustomers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Stream() throws Exception {
        Set<String> extracted = CountriesExtractor.Java8.stream(Sample.inputCustomers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    private static class Sample {
        static List<Customer> inputCustomers() {
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
                                    buildingNumber("some building number")),
                    // This particular customer will have `address` field equal to `null`.
                    new Customer()
            );
        }

        private static Set<String> outputCountries() {
            return new HashSet<>(asList("USA", "Russia", "China"));
        }
    }
}

class CountriesExtractor {
    public static Set<String> java7(final List<Customer> customers) {
        final Set<String> countries = new HashSet<>();
        for (final Customer customer : customers) {
            if (customer != null) {
                if (customer.address() != null) {
                    String country = customer.address().country();
                    if (country != null) {
                        countries.add(country);
                    }
                }
            }
        }
        return unmodifiableSet(countries);
    }

    public static class Java8 {
        static Set<String> trivial(final List<Customer> customers) {
            final Set<String> countries = new HashSet<>();
            customers.forEach(customer -> {
                if (customer != null) {
                    if (customer.address() != null) {
                        String country = customer.address().country();
                        if (country != null) {
                            countries.add(country);
                        }
                    }
                }
            });
            return unmodifiableSet(countries);
        }

        static Set<String> stream(final List<Customer> customers) {
            return customers.
                    stream().
                    filter(Objects::nonNull).
                    filter(customer -> nonNull(customer.address())).
                    map(customer -> customer.address().country()).
                    filter(Objects::nonNull).
                    collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        }
    }
}
