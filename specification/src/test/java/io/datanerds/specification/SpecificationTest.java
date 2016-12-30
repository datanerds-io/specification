package io.datanerds.specification;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class SpecificationTest {

    public abstract static class TestBase {

        protected final Specification<String> containsDatanerds = spec -> spec.contains("datanerds");
        protected final Specification<String> isLongerThan9 = spec -> spec.length() > 9;
        protected final Specification<String> isShorterThan15 = spec -> spec.length() < 15;
        protected final Specification<String> startsWithD = spec -> spec.length() > 0 && spec.startsWith("d");

    }

    public static class Not extends TestBase {

        @Test
        public void not() {
            assertThat(containsDatanerds.not().isSatisfiedBy("datafreaks"), is(true));
        }

        @Test
        public void not1() {
            assertThat(containsDatanerds.not().isSatisfiedBy("datanerds"), is(false));
        }

        @Test
        public void not2() {
            assertThat(isLongerThan9.not().isSatisfiedBy("datanerdss"), is(false));
        }

        @Test
        public void not3() {
            assertThat(isLongerThan9.not().isSatisfiedBy("data"), is(true));
        }

        @Test
        public void not4() {
            assertThat(startsWithD.not().isSatisfiedBy("datanerds"), is(false));
        }

        @Test
        public void not5() {
            assertThat(startsWithD.not().isSatisfiedBy("atanerds"), is(true));
        }

    }

    public static class And extends TestBase {

        @Test
        public void and() {
            assertThat(containsDatanerds.and(startsWithD).isSatisfiedBy("datanerds"), is(true));
        }

        @Test
        public void and1() {
            assertThat(containsDatanerds.and(startsWithD).isSatisfiedBy("atanerds"), is(false));
        }

        @Test
        public void and2() {
            assertThat(containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy("datanerdsss"),
                    is(true));
        }

        @Test
        public void and3() {
            assertThat(
                    containsDatanerds.and(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy
                            ("datanerdsdatanerds"),
                    is(false));
        }

    }

    public static class Or extends TestBase {

        @Test
        public void or() {
            assertThat(containsDatanerds.or(startsWithD).isSatisfiedBy("datafreaks"), is(true));
        }

        @Test
        public void or1() {
            assertThat(containsDatanerds.or(startsWithD).isSatisfiedBy("atafreaks"), is(false));
        }

        @Test
        public void or2() {
            assertThat(startsWithD.or(isLongerThan9).isSatisfiedBy("1234567890"), is(true));
        }

        @Test
        public void or3() {
            assertThat(startsWithD.or(isLongerThan9).isSatisfiedBy("1"), is(false));
        }

        @Test
        public void or4() {
            assertThat(containsDatanerds.or(startsWithD, isLongerThan9).isSatisfiedBy("1234567890"), is(true));
        }

        @Test
        public void or5() {
            assertThat(containsDatanerds.or(startsWithD, isLongerThan9, isShorterThan15).isSatisfiedBy("1234567890"),
                    is(true));
        }

    }

    public static class AndOr extends TestBase {

        Specification<String> and = containsDatanerds.and(isShorterThan15);

        @Test
        public void andOr() {
            assertThat(and.or(startsWithD).isSatisfiedBy("d"), is(true));
        }

        @Test
        public void andOr1() {
            assertThat(and.or(startsWithD).isSatisfiedBy("d"), is(true));
        }

        @Test
        public void andOr2() {
            assertThat(and.or(startsWithD).isSatisfiedBy("datafreaks"), is(true));
        }

        @Test
        public void andOr3() {
            assertThat(and.or(startsWithD).isSatisfiedBy("atafreaksdatafreaks"), is(false));
        }

        @Test
        public void andOr4() {
            assertThat(and.not().or(startsWithD).isSatisfiedBy("atafreaksdatafreaks"), is(true));
        }

    }

    public static class Complex extends TestBase {

        Specification<String> complex1 = containsDatanerds.and(isLongerThan9.not()).and(startsWithD.not());
        Specification<String> complex2 = containsDatanerds.not().or(isLongerThan9.not()).or(startsWithD.not());

        @Test
        public void complex1() {
            assertThat(complex1.isSatisfiedBy("datanerdsdatanerds"), is(false));
        }

        @Test
        public void complex11() {
            assertThat(complex1.isSatisfiedBy("atanerdsdatanerds"), is(false));
        }

        @Test
        public void complex12() {
            assertThat(complex1.isSatisfiedBy("datanerds"), is(false));
        }

        @Test
        public void complex2() {
            assertThat(complex2.isSatisfiedBy("datanerdsdatanerds"), is(false));
        }

        @Test
        public void complex21() {
            assertThat(complex2.isSatisfiedBy("atanerdsdatanerds"), is(true));
        }

        @Test
        public void complex22() {
            assertThat(complex2.isSatisfiedBy("d"), is(true));
        }

        @Test
        public void complex23() {
            assertThat(complex2.isSatisfiedBy("12345678"), is(true));
        }

    }

}
