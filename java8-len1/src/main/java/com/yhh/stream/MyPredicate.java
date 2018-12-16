package com.yhh.stream;


import java.util.function.UnaryOperator;

@FunctionalInterface
public interface MyPredicate<T> {

	public boolean test(T t);

    //t哪里来的
    static <T> UnaryOperator<T> identity() {
        return t -> t;
    }

}