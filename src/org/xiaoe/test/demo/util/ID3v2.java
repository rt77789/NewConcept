package org.xiaoe.test.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.xiaoe.test.demo.struct.Frame;
import org.xiaoe.test.demo.struct.FrameHeader;
import org.xiaoe.test.demo.struct.Header;

/**
 * 
 * @author aliguagua.zhengy
 * 
 */
public class ID3v2 {
	public static String[] validTitle = { "AENC", "APIC", "COMM", "COMR",
			"ENCR", "EQUA", "ETCO", "GEOB", "GRID", "IPLS", "LINK", "MCDI",
			"MLLT", "OWNE", "PRIV", "PCNT", "POPM", "POSS", "RBUF", "RVAD",
			"RVRB", "SYLT", "SYTC", "TALB", "TBPM", "TCOM", "TCON", "TCOP",
			"TDAT", "TDLY", "TENC", "TEXT", "TFLT", "TIME", "TIT1", "TIT2",
			"TIT3", "TKEY", "TLAN", "TLEN", "TMED", "TOAL", "TOFN", "TOLY",
			"TOPE", "TORY", "TOWN", "TPE1", "TPE2", "TPE3", "TPE4", "TPOS",
			"TPUB", "TRCK", "TRDA", "TRSN", "TRSO", "TSIZ", "TSRC", "TSSE",
			"TYER", "TXXX", "UFID", "USER", "USLT", "WCOM", "WCOP", "WOAF",
			"WOAR", "WOAS", "WORS", "WPAY", "WPUB", "WXXX",
	// 6 * 12 + 2 = 74
	};

	public ID3v2(String fileName) throws Exception {
		this.fileName = fileName;
		frame = new ArrayList<Frame>();

		try {
			mp3 = new FileInputStream(new File(fileName));
			System.out.println("mp3 file open? " + mp3.available());
		} catch (Exception e) {
			if (mp3 != null)
				mp3.close();

			throw e;
		}
		deal();
	}

	public void close() throws IOException {
		if (mp3 != null) {
			mp3.close();
		}
	}

	public boolean getHeader() throws IOException {
		int size = 10;

		byte buff[] = new byte[size];

		if (mp3.read(buff, 0, size) != size)
			return false;

		byte[] id = Arrays.copyOfRange(buff, 0, 3);
		byte[] version = Arrays.copyOfRange(buff, 3, 5);

		int hs = 0;
		for (int i = 6; i < size; ++i) {
			hs = (hs << 7) | (buff[i] & 0x0000007f);
		}

		header = new Header(id, version, buff[5], hs);

		return true;
	}

	public FrameHeader getFrameHeader() throws IOException {

		byte[] buff = new byte[10];
		if (mp3.read(buff, 0, 10) != 10)
			return null;

		int size = 0;
		for (int i = 4; i < 8; ++i)
			size = (size << 8) | (buff[i] & 0x000000ff);

		byte[] id = Arrays.copyOfRange(buff, 0, 4);
		byte[] flag = Arrays.copyOfRange(buff, 8, 10);

		FrameHeader fh = new FrameHeader(id, size, flag);

		return fh;
	}

	// ????frame
	public boolean getFrame() throws Exception {
		FrameHeader fh = getFrameHeader();
		if (fh == null || !fh.isValid())
			return false;

		byte buff[] = new byte[fh.size()];

		if (mp3.read(buff, 0, fh.size()) != fh.size())
			return false;

		Frame fm = new Frame(buff, fh);

		dealTag(fm);
		frame.add(fm);

		return true;
	}

	public boolean deal() throws Exception {
		if (!getHeader()) {
			return false;
		}

		while (getFrame())
			;

		for (Frame fr : frame) {
			System.out.println(new String(fr.getHeader().getID()));
		}
		return true;
	}

	//
	public void dealTag(Frame fr) throws Exception {
		String headerID = new String(fr.getHeader().getID());
		if (headerID.equals("APIC")) {
			APIC(fr);
		} else {
			T0_Z(fr);
		}

		// if (headerID.equals("USLT")) {
		// USLT(fr);
		// } else if (headerID.equals("UFID")) {
		// UFID(fr);
		// } else if (headerID.equals("TXXX")) {
		// TXXX(fr);
		// } else if (headerID.equals("APIC")) {
		// APIC(fr);
		// } else if (headerID.equals("COMM")) {
		// COMM(fr);
		// } else if (headerID.equals("SYLT")) {
		// SYLT(fr);
		// } else if (headerID.equals("USLT")) {
		// USLT(fr);
		// } else {
		// T0_Z(fr);
		// }
	}

	private void UFID(Frame fr) {
		byte[] data = fr.getData();
		fr.getHeader().print();
		System.out.print("Owner identifier: " + new String(data));
		int i = 0;
		while (data[i] != 0)
			++i;
		System.out.print("Identifier: ");
		for (++i; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void TXXX(Frame fr) {
		byte[] data = fr.getData();
		fr.getHeader().print();
		System.out.print("Text encoding: " + data[0] + "\nDescription: ");
		int i = 4;
		while (data[i] != 0) {
			System.out.print(data[i]);
			++i;
		}
		System.out.print("\nValue: ");
		for (++i; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void WXXX(Frame fr) {
		byte[] data = fr.getData();
		fr.getHeader().print();
		System.out.print("Text encoding: " + data[0] + "\nDescription: ");
		int i = 4;
		while (data[i] != 0) {
			System.out.print(data[i]);
			++i;
		}
		System.out.print("\nURL: ");
		for (++i; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void IPLS(Frame fr) {
		byte[] data = fr.getData();
		fr.getHeader().print();
		System.out
				.print("Text Encoding: " + data[0] + "\nPeople list string: ");
		for (int i = 1; i < fr.getHeader().size() && data[i] != 0; ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void MCDI(Frame fr) {
		byte[] data = fr.getData();
		System.out.print("CD TOC: ");
		for (int i = 0; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void ETCO(Frame fr) {
		byte[] data = fr.getData();
		System.out.print("Time stamp format: " + data[0] + "\n");
		// termnited none;
	}

	private void MLLT(Frame fr) {
	}

	private void SYTC(Frame fr) {
	}

	private void USLT(Frame fr) {
		byte[] data = fr.getData();
		String lang = new String(Arrays.copyOfRange(data, 1, 4));

		fr.getHeader().print();
		System.out.print("Text encoding: " + data[0] + " \nLanguage: " + lang
				+ " \nContent descriptor: ");
		int i = 4;
		while (data[i] != 0) {
			System.out.print(data[i]);
			++i;
		}
		System.out.print("\nLyrics text: ");
		for (++i; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void SYLT(Frame fr) {
		byte[] data = fr.getData();
		String lang = new String(Arrays.copyOfRange(data, 1, 4));
		fr.getHeader().print();
		System.out.print("Text encoding: " + data[0] + "\nLanguage: " + lang
				+ "\nTime stamp format: " + data[4] + "\nContent type: "
				+ data[5] + "\n");
		System.out.print("Content descriptor: "
				+ new String(Arrays.copyOfRange(data, 6, data.length)));
	}

	private void COMM(Frame fr) {
		byte[] data = fr.getData();
		String lang = new String(Arrays.copyOfRange(data, 1, 4));
		System.out.print("Text encoding: " + data[0] + "\nLanguage: " + lang
				+ "\nShort content descrip: ");
		int i = 4;
		while (data[i] != 0) {
			System.out.print(data[i]);
			++i;
		}
		System.out.print("\nThe actual text: ");

		for (++i; i < fr.getHeader().size(); ++i) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	private void RVAD(Frame fr) {
	}

	private void EQUA(Frame fr) {
	}

	private void RVRB(Frame fr) {
	}

	private void APIC(Frame fr) throws Exception {
		byte[] data = fr.getData();
		fr.getHeader().print();
		// System.out.print("Text encoding: " + new String);

		int i = 1;
		while (data[i++] != 0)
			;
		String image = new String(Arrays.copyOfRange(data, 1, 7));
		if (!image.equals("image/")) {
			throw new Exception("APIC image not match.");
		}
		int to = 7;
		while (data[to] != 0)
			++to;
		String imageFileName = fileName + "."
				+ new String(Arrays.copyOfRange(data, 7, to));
		System.out.println("imageFileName: " + imageFileName);

		while (data[++i] != 0) {
			System.out.print(data[i]);
		}

		// # write into APIC byte[].
		APIC = Arrays.copyOfRange(data, i + 1, fr.getHeader().size());

	}

	private void GEOB(Frame fr) {
	}

	private void PCNT(Frame fr) {
	}

	private void POPM(Frame fr) {
	}

	private void RBUF(Frame fr) {
	}

	private void AENC(Frame fr) {
	}

	private void LINK(Frame fr) {
	}

	private void POSS(Frame fr) {
	}

	private void USER(Frame fr) {
	}

	private void OWNE(Frame fr) {
	}

	private void COMR(Frame fr) {
	}

	private void ENCR(Frame fr) {
	}

	private void GRID(Frame fr) {
	}

	private void PRIV(Frame fr) {
	}

	private void T0_Z(Frame fr) throws UnsupportedEncodingException {
		byte[] data = fr.getData();

		System.out.println(new String(fr.getHeader().getID()));

		// If ISO-8859-1 is used this byte should be $00, if Unicode is used it
		// should be $01.

		System.out.println("Encoding: " + data[0]);

		String oriText = null;
		if (data[0] == 0) {
			oriText = new String(Arrays.copyOfRange(data, 1, data.length),
					"iso-8859-1");
		} else {
			oriText = new String(Arrays.copyOfRange(data, 1, data.length),
					"utf16");
		}

		System.out.println("Text: " + oriText);
	}

	private void W0_Z(Frame fr) {
	}

	public byte[] getAPIC() {
		return APIC;
	}

	private InputStream mp3;
	private String fileName;
	private Header header;
	private List<Frame> frame;
	private byte[] APIC;

	public static void main(String[] args) {
		try {
			String fileName = "D:/TDdownload/cic-disc-1/Outro-Music.mp3";
			ID3v2 id3v2 = new ID3v2(fileName);

			FileOutputStream fw = null;
			try {

				byte[] data = id3v2.getAPIC();
				// for (++i; i < fr.getHeader().size(); ++i) {
				if (data != null) {
					fw = new FileOutputStream(new File(fileName + "." + "jpg"));
					fw.write(data, 0, data.length);
				}
				// }
			} finally {
				if (fw != null)
					fw.close();
			}

			id3v2.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
};
