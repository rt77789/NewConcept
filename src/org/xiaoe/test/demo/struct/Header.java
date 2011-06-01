package org.xiaoe.test.demo.struct;

import org.xiaoe.test.demo.util.Arrays;


public class Header {
	private byte id[];
	private byte version[];
	private byte flag;

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public byte[] getVersion() {
		return version;
	}

	public void setVersion(byte[] version) {
		this.version = version;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private int size;
	
	public Header(byte[] _id, byte[] _version, byte _flag, int _size) {
		id = _id;
		version = _version;
		flag = _flag;
		size = _size;
	}
	
	@Override
	public String toString() {
		return "Header [flag=" + flag + ", id=" + Arrays.toString(id)
				+ ", size=" + size + ", version=" + Arrays.toString(version)
				+ "]";
	}
}
