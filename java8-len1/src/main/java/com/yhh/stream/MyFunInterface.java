package com.yhh.stream;


@FunctionalInterface
public interface MyFunInterface<T> {


    int compare(T o1, T o2);

    //有这个为什么还是函数式接口?  因为是Object中的方法
    boolean equals(Object obj);
    String toString();

}
