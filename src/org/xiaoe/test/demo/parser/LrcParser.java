package org.xiaoe.test.demo.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xiaoe.test.demo.struct.Pair;


public class LrcParser {
	Matcher match;
	Pattern pattern;
	Scanner scan = null;

	public LrcParser(String filename) throws FileNotFoundException {
		// / [03:55.11]
		pattern = Pattern.compile("^\\[(\\d{2}):(\\d{2})\\.(\\d{2})\\]");
		//InputStream in = this.getClass().getResourceAsStream(filename);

		scan = new Scanner(new File(filename));
	}

	public boolean hasNext() {
		return scan.hasNext();
	}

	/**
	 * Check parameter line whether is valid format.
	 * 
	 * @param line
	 * @return
	 */
	boolean isValidFormat(String line) {
		match = pattern.matcher(line);
		if (match.find()) {
			return true;
		}
		return false;
	}

	/**
	 * Parsing a line of Lrc file, and fill stamps, used regular expression.
	 * 
	 * @param line
	 */
	public Pair<Integer, String> next() {
		String line = scan.nextLine();
		match = pattern.matcher(line);
		if (match.find()) {
			int m = Integer.valueOf(match.group(1));
			int s = Integer.valueOf(match.group(2));
			int ms = Integer.valueOf(match.group(3));

			// / ∫¡√Î
			int stamp = ms * 10 + (s * 1000) + (m * 60 * 1000);
			String rem = line.substring(match.end(3) + 1).trim();
			// stamps.put(stamp, rem);
			return new Pair<Integer, String>(stamp, rem);
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException {
		LrcParser lrc = new LrcParser("risis.lrc");
		while (lrc.hasNext()) {
			System.out.println(lrc.next());
		}
	}
}
