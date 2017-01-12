package io.datanerds.specification;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class SpecificationTest {

    private static final Specification<String> containsDatanerds = spec -> spec.contains("datanerds");
    private static final Specification<String> isLongerThan9 = spec -> spec.length() > 9;
    private static final Specification<String> isShorterThan15 = spec -> spec.length() < 15;
    private static final Specification<String> startsWithD = spec -> spec.length() > 0 && spec.startsWith("d");
    private static final Specification<String> containsDatanerdsAndIsShorterThan15 =
            containsDatanerds.and(isShorterThan15);
    private static final Specification<String> contDNAndIsNotLTh9AndNotStartsWD =
            containsDatanerds.and(isLongerThan9.not()).and(startsWithD.not());
    private static final Specification<String> contDNOrIsNotLTh9OrNotStartsWD =
            containsDatanerds.not().or(isLongerThan9.not()).or(startsWithD.not());

    @Parameterized.Parameters(name = "{index}: spec using value {1} should satisfy: {2}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                        {containsDatanerds.not(), "datafreaks", true},
                        {containsDatanerds.not(), "datanerds", false},
                        {isLongerThan9.not(), "datanerdss", false},
                        {isLongerThan9.not(), "data", true},
                        {startsWithD.not(), "datanerds", false},
                        {startsWithD.not(), "atanerds", true},
                        {containsDatanerds.and(startsWithD), "datanerds", true},
                        {containsDatanerds.and(startsWithD), "atanerds", false},
                        {containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15), "datanerdsss", true},
                        {containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15),
                                "datanerdsdatanerds", false},
                        {containsDatanerds.or(startsWithD), "datafreaks", true},
                        {containsDatanerds.or(startsWithD), "atafreaks", false},
                        {startsWithD.or(isLongerThan9), "1234567890", true},
                        {startsWithD.or(isLongerThan9), "1", false},
                        {containsDatanerds.or(startsWithD, isLongerThan9), "1234567890", true},
                        {containsDatanerds.or(startsWithD, isLongerThan9, isShorterThan15), "1234567890", true},
                        {containsDatanerdsAndIsShorterThan15.or(startsWithD), "d", true},
                        {containsDatanerdsAndIsShorterThan15.or(startsWithD), "d", true},
                        {containsDatanerdsAndIsShorterThan15.or(startsWithD), "datafreaks", true},
                        {containsDatanerdsAndIsShorterThan15.or(startsWithD), "atafreaksdatafreaks", false},
                        {containsDatanerdsAndIsShorterThan15.not().or(startsWithD), "atafreaksdatafreaks", true},
                        {contDNAndIsNotLTh9AndNotStartsWD, "datanerdsdatanerds", false},
                        {contDNAndIsNotLTh9AndNotStartsWD, "atanerdsdatanerds", false},
                        {contDNAndIsNotLTh9AndNotStartsWD, "datanerds", false},
                        {contDNOrIsNotLTh9OrNotStartsWD, "datanerdsdatanerds", false},
                        {contDNOrIsNotLTh9OrNotStartsWD, "atanerdsdatanerds", true},
                        {contDNOrIsNotLTh9OrNotStartsWD, "d", true},
                        {contDNOrIsNotLTh9OrNotStartsWD, "12345678", true},

                }
        );
    }

    @Parameterized.Parameter
    public Specification<String> spec;

    @Parameterized.Parameter(value = 1)
    public String value;

    @Parameterized.Parameter(value = 2)
    public boolean expected;

    @Test
    public void satisfies() {
        assertThat(spec.isSatisfiedBy(value), is(expected));
    }

}
