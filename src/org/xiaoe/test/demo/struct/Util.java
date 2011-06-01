package org.xiaoe.test.demo.struct;

public class Util {
	public static String printStackTrace(Exception e) {
		String res = "";
		StackTraceElement[] array = e.getStackTrace();
		for(int i = 0; i < array.length; ++i) {
			res += array[i] + "\n";
		}
		return res;
	}
	
	public static void main(String[] args) {
		System.out.println(Util.printStackTrace(new Exception("hello")));
	}
}
