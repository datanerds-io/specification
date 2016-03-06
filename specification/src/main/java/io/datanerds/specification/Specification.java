package io.datanerds.specification;

import java.util.Objects;

public interface Specification<T> {

    boolean isSatisfiedBy(T t);

    default Specification<T> and(Specification<T>... others) {
        Objects.requireNonNull(others, "Specification needs to be set");
        return spec -> {
            boolean result = true;
            for (Specification<T> other : others) {
                result &= this.isSatisfiedBy(spec) && other.isSatisfiedBy(spec);
            }
            return result;
        };
    }

    default Specification<T> or(Specification<T>... others) {
        Objects.requireNonNull(others, "Specification needs to be set");
        return spec -> {
            boolean result = false;
            for (Specification<T> other : others) {
                result |= this.isSatisfiedBy(spec) || other.isSatisfiedBy(spec);
            }
            return result;
        };
    }

    default Specification<T> not() {
        return spec -> !isSatisfiedBy(spec);
    }
}