package org.monarchinitiative.poet.utility;

import java.util.function.Predicate;

public class ContainsPubmed<E> implements Predicate<String> {

    @Override
    public boolean test(String s) {
        return s.contains("PMID:");
    }
}
