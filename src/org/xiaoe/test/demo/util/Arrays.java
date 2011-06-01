package org.xiaoe.test.demo.util;

public class Arrays {
	public static byte[] copyOfRange(byte[] buff, int from, int to) {
		byte[] res = new byte[to - from];
		for(int i = 0; i < res.length; ++i) {
			res[i] = buff[i + from];
		}
		return res;
	}
	
	public static boolean equals(byte[] a, byte[] b) {
		if(a.length != b.length) return false;
		for(int i = 0; i < a.length; ++i) {
			if(a[i] != b[i]) return false;
		}
		return true;
	}
	
	public static String toString(byte[] a) {
		return new String(a);
	}
}
