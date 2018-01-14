package io.github.flaz14.util;

import io.github.flaz14.util.ClassExplorer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.rules.ExpectedException.none;

@RunWith(Enclosed.class)
public class ClassExplorerTest {

    public static class HumanReadableClassName {
        @Rule
        public final ExpectedException expectedException = none();

        @Test
        public void throwsException_whenInputNameIsNull() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("classNameInVm should not be null.");
            ClassExplorer.humanReadableClassName(null);
        }

        @Test
        public void returnsEmptyString_whenInputNameIsEmpty() {
            assertThat(
                    ClassExplorer.humanReadableClassName(""),
                    equalTo("")
            );
        }

        @Test
        public void happyPath() {
            assertThat(
                    ClassExplorer.humanReadableClassName("java/lang/String"),
                    equalTo("java.lang.String")
            );
        }
    }
}
