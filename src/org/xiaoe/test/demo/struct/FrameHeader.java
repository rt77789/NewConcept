package org.xiaoe.test.demo.struct;


import org.xiaoe.test.demo.util.Arrays;
import org.xiaoe.test.demo.util.ID3v2;

public class FrameHeader {
	private byte id[];
	private int size;
	private byte flag[];

	public FrameHeader(byte[] _id, int _size, byte[] _flag) {
		id = _id;
		size = _size;
		flag = _flag;
	}

	public FrameHeader() {
	}

	public void print() {
		System.out.print("header: id-");

		for (int i = 0; i < id.length; ++i)
			System.out.print(id[i]);
		
		System.out.println("\tsize-" + size + "\tflag-" + flag[0] + flag[1]);
	}

	public boolean isValid() {
		for (int i = 0; i < ID3v2.validTitle.length; ++i)
			if (Arrays.equals(ID3v2.validTitle[i].getBytes(), id))
				return true;

		return false;
	}
	
	public byte[] getID() {
		return id;
	}
	
	public int size() {
		return size;
	}
}
