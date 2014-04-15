package com.zhangbin.connpool.utils;

public class Test {

	public static void main(String[] args) {
		A a = null;
		try {
			a=new A("zhanbgin");
			System.out.println(a.getName());
		} catch (Exception e) {
			System.out.println("error,"+a.getName());
		}
	}
}
class A{
	private String name;
	
	public A(String name) throws Exception {
		this.name = name;
		B b=new B(this);
		System.out.println("---------------");
		//throw new Exception("");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
class B{
	private A a;
	public B(A a) {
		this.a = a;
		System.out.println("B:"+a.getName());
	}
	public A getA() {
		return a;
	}
	public void setA(A a) {
		this.a = a;
	}
}