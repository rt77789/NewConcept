package org.xiaoe.test.demo.struct;

public class Pair<T1, T2> {
	public T1 first;
	public T2 second;
	public Pair(T1 first, T2 second) {
		super();
		this.first = first;
		this.second = second;
	}
	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
}
