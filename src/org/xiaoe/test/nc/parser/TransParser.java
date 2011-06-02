package org.xiaoe.test.nc.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.xiaoe.test.nc.struct.Sentence;

/**
 * Parse the Trans file.
 * 
 * @author aliguagua.zhengy
 * 
 */
public class TransParser {
	private Sentence sent;
	private String subject;
	private Scanner scan = null;

	public TransParser(String filename) throws FileNotFoundException {
		scan = new Scanner(new File(filename), "GBK");
		if (scan.hasNext()) {
			subject = scan.nextLine();
		}
	}

	public boolean hasNext() {
		int len = 2;
		String[] res = new String[len];
		int i = 0;
		
		for (i = 0; i < len; ++i) {
			if (scan.hasNext()) {
				res[i] = scan.nextLine();
			} else
				break;
		}
		if (i != len)
			return false;
		sent = new Sentence(res[0], res[1]);
		return true;
	}

	public Sentence next() {
		return sent;
	}

	public String getSubject() {
		return subject;
	}
}
