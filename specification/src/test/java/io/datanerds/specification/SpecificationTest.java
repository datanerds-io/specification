package io.datanerds.specification;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SpecificationTest {

    private final Specification<String> containsDatanerds = spec -> spec.contains("datanerds");
    private final Specification<String> isLongerThan9 = spec -> spec.length() > 9;
    private final Specification<String> isShorterThan15 = spec -> spec.length() < 15;
    private final Specification<String> startsWithD = spec -> spec.length() > 0 && spec.startsWith("d");

    @Test
    public void not() {
        assertThat(containsDatanerds.not().isSatisfiedBy("datafreaks"), is(true));
        assertThat(containsDatanerds.not().isSatisfiedBy("datanerds"), is(false));
        assertThat(isLongerThan9.not().isSatisfiedBy("datanerdss"), is(false));
        assertThat(isLongerThan9.not().isSatisfiedBy("data"), is(true));
        assertThat(startsWithD.not().isSatisfiedBy("datanerds"), is(false));
        assertThat(startsWithD.not().isSatisfiedBy("atanerds"), is(true));
    }

    @Test
    public void and() {
        assertThat(containsDatanerds.and(startsWithD).isSatisfiedBy("datanerds"), is(true));
        assertThat(containsDatanerds.and(startsWithD).isSatisfiedBy("atanerds"), is(false));
        assertThat(containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy("datanerdsss"),
                is(true));
        assertThat(
                containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy("datanerdsdatanerds"),
                is(false));
    }

    @Test
    public void or() {
        assertThat(containsDatanerds.or(startsWithD).isSatisfiedBy("datafreaks"), is(true));
        assertThat(containsDatanerds.or(startsWithD).isSatisfiedBy("atafreaks"), is(false));
        assertThat(startsWithD.or(isLongerThan9).isSatisfiedBy("1234567890"), is(true));
        assertThat(startsWithD.or(isLongerThan9).isSatisfiedBy("1"), is(false));
        assertThat(containsDatanerds.or(startsWithD, isLongerThan9).isSatisfiedBy("1234567890"), is(true));
        assertThat(containsDatanerds.or(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy("1234567890"),
                is(true));

    }

    @Test
    public void andOr() {
        Specification<String> and = containsDatanerds.and(isShorterThan15);
        assertThat(and.or(startsWithD).isSatisfiedBy("d"), is(true));
        assertThat(and.or(startsWithD).isSatisfiedBy("d"), is(true));
        assertThat(and.or(startsWithD).isSatisfiedBy("datafreaks"), is(true));
        assertThat(and.or(startsWithD).isSatisfiedBy("atafreaksdatafreaks"), is(false));
        assertThat(and.not().or(startsWithD).isSatisfiedBy("atafreaksdatafreaks"), is(true));
    }

    @Test
    public void complex() {
        Specification<String> complex1 = containsDatanerds.and(isLongerThan9.not()).and(startsWithD.not());
        assertThat(complex1.isSatisfiedBy("datanerdsdatanerds"), is(false));
        assertThat(complex1.isSatisfiedBy("atanerdsdatanerds"), is(false));
        assertThat(complex1.isSatisfiedBy("datanerds"), is(false));

        Specification<String> complex2 = containsDatanerds.not().or(isLongerThan9.not()).or(startsWithD.not());
        assertThat(complex2.isSatisfiedBy("datanerdsdatanerds"), is(false));
        assertThat(complex2.isSatisfiedBy("atanerdsdatanerds"), is(true));
        assertThat(complex2.isSatisfiedBy("d"), is(true));
        assertThat(complex2.isSatisfiedBy("12345678"), is(true));
    }
}
