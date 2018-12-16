package com.yhh.method;

public class SubClass /*extends MyClass*/ implements MyFun, MyInterface{

	@Override
	public String getName() {
//	    return "SubClass";
		return MyInterface.super.getName();
	}

}
