package org.xiaoe.test.demo.struct;


public class Frame {
	private FrameHeader header;
	byte[] data;
	
	public Frame(byte[] _data, FrameHeader _header) {
		data = _data;
		header = _header;
	}
	
	void setHeader(FrameHeader fh) {
		header = fh;
	}
	
	public FrameHeader getHeader() { return header; }
	public byte[] getData() { return data; }
	public void print() {
		header.print();
		System.out.print("encoding: " + data[0]);
		for(int i = 0; i < header.size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}
}
