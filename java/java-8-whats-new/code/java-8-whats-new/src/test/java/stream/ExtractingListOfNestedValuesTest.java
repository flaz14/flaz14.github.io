package stream;

import dto.Customer;
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


public class ExtractingListOfNestedValuesTest {
    @Test
    public void java7() throws Exception {
        final List<String> extracted = PhoneNumbersExtractor.java7(Sample.inputCustomers());
        assertThat(extracted, equalTo(Sample.outputPhoneNumbers()));
    }

    @Test
    public void java8() throws Exception {
        final List<String> extracted = PhoneNumbersExtractor.java8(Sample.inputCustomers());
        assertThat(extracted, equalTo(Sample.outputPhoneNumbers()));
    }

    private static class Sample {
        private static List<Customer> inputCustomers() {
            return asList(
                    new Customer().
                            firstName("John").
                            lastName("Smith").
                            phoneNumbers(asList("48-12-54", "30-80-20")),
                    new Customer().
                            firstName("Ivan").
                            lastName("Ivanov").
                            phoneNumbers(asList("007", "555", "777")),
                    // The latter customer will have `phoneNumbers` field equal to `null`.
                    new Customer().
                            firstName("Samo").
                            lastName("Law")
            );
        }

        private static List<String> outputPhoneNumbers() {
            return asList("48-12-54", "30-80-20", "007", "555", "777");
        }
    }
}

class PhoneNumbersExtractor {
    public static List<String> java7(final List<Customer> customers) {
        final List<String> phoneNumbers = new ArrayList<>();
        for (final Customer customer : customers) {
            if (customer.phoneNumbers() != null) {
                phoneNumbers.addAll(customer.phoneNumbers());
            }
        }
        return unmodifiableList(phoneNumbers);
    }

    public static List<String> java8(final List<Customer> customers) {
        return customers.
                stream().
                filter(customer -> nonNull(customer.phoneNumbers())).
                flatMap(customer -> customer.phoneNumbers().stream()).
                collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
