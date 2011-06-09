package org.xiaoe.test.nc.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.xiaoe.test.nc.struct.Word;

/**
 * Parse the word file.
 * 
 * @author aliguagua.zhengy
 * 
 */
public class WordParser {
	private Word word;
	private Scanner scan = null;

	public WordParser(String filename) throws FileNotFoundException {
		scan = new Scanner(new File(filename), "UTF-8");
	}

	public boolean hasNext() {
		int len = 3;
		String[] res = new String[len];
		int i = 0;
		
		for (i = 0; i < len; ++i) {
			if (scan.hasNext()) {
				res[i] = scan.nextLine();
			}
			else break;
		}
		if (i != len)
			return false;
		word = new Word(res[0], res[1], res[2]);
		return true;
	}

	public Word next() {
		return word;
	}
}
