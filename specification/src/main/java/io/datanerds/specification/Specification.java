package io.datanerds.specification;

import java.util.Objects;

import static java.util.Arrays.stream;

/**
 * This interface is an Java 8 implementation of the Specification Pattern described by Eric Evans and Martin Fowler
 * <a href="http://martinfowler.com/apsupp/spec.pdf">here</a>. The difference in this implementation is the possibility
 * to pass multiple specifications to the {@link Specification#and(Specification[])} and
 * {@link Specification#or(Specification[])} methods.
 *
 * @param <T> The type you want to "compare"
 */
public interface Specification<T> {

    boolean isSatisfiedBy(T t);

    /**
     * This method will negate a given specification.
     *
     * @return the negated return value of the original specification
     */
    default Specification<T> not() {
        return spec -> !isSatisfiedBy(spec);
    }

    /**
     * And specification for a list of specifications.
     *
     * @param others specifications for comparison
     * @return a TRUE specification if it is satisfied by all passed specifications,
     * FALSE otherwise
     */
    default Specification<T> and(Specification<T>... others) {
        Objects.requireNonNull(others, "Specification(s) needs to be set");
        return spec -> this.isSatisfiedBy(spec) && stream(others).allMatch(o -> o.isSatisfiedBy(spec));
    }

    /**
     * Or specification for a list of specifications.
     *
     * @param others specifications for comparison
     * @return a TRUE specification if it is satisfied by one of the passed specifications,
     * FALSE otherwise
     */

    default Specification<T> or(Specification<T>... others) {
        Objects.requireNonNull(others, "Specification(s) needs to be set");
        return spec -> this.isSatisfiedBy(spec) || stream(others).anyMatch(o -> o.isSatisfiedBy(spec));
    }
}