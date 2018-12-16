package com.yhh.lambda1;

import java.util.function.UnaryOperator;

//策略设计模式接口
@FunctionalInterface
public interface MyPredicate<T> {

	public boolean test(T t);



}
