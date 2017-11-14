package stream;

import dto.Address;
import dto.Customer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ExtractingNestedFieldTest {
    @Test
    public void java7() throws Exception {
        Set<String> extracted = CountriesExtractor.java7(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Trivial() throws Exception {
        Set<String> extracted = CountriesExtractor.Java8.trivial(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8Stream() throws Exception {
        Set<String> extracted = CountriesExtractor.Java8.stream(Sample.Input.customers());
        assertThat(extracted, equalTo(Sample.outputCountries()));
    }

    @Test
    public void java8StreamWithCheckingForNull() throws Exception {
        Set<String> extracted = CountriesExtractor.Java8.streamWithHandlingNullAddresses(
                Sample.Input.withMalformedCustomer()
        );
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

        private static Set<String> outputCountries() {
            return new HashSet<>(asList("USA", "Russia", "China"));
        }
    }

    private static class CountriesExtractor {
        static Set<String> java7(final List<Customer> customers) {
            final Set<String> countries = new HashSet<>();
            for (final Customer customer : customers) {
                String country = customer.address().country();
                countries.add(country);
            }
            return unmodifiableSet(countries);
        }

        static class Java8 {
            static Set<String> trivial(final List<Customer> customers) {
                final Set<String> countries = new HashSet<>();
                customers.forEach(customer -> {
                    String country = customer.address().country();
                    countries.add(country);
                });
                return unmodifiableSet(countries);
            }

            static Set<String> stream(final List<Customer> customers) {
                return customers.
                        stream().
                        map(customer -> customer.address().country()).
                        collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
            }

            static Set<String> streamWithHandlingNullAddresses(final List<Customer> customers) {
                return customers.
                        stream().
                        filter(customer -> nonNull(customer.address())).
                        map(customer -> customer.address().country()).
                        collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
            }
        }
    }
}

