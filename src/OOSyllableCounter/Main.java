package OOSyllableCounter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import stopwatch.Stopwatch;

/**
 * A main class to count the syllable from the text file and measure the used to
 * count.
 * 
 * @author Chawakorn Suphepre
 * @version 2017.04.02
 */
public class Main {
	private static InputStream in = null;

	/**
	 * Set the input file to in by using url or file name if the file is in the
	 * project folder.
	 * 
	 * @param name
	 *            is the name of the file to read.
	 */
	public static void setInput(String name) {
		in = null;
		final String URLPATTERN = "^\\w\\w+://\\S+";
		if (name.matches(URLPATTERN)) {
			try {
				URL url = new URL(name);
				in = url.openStream();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {

			try {
				in = new FileInputStream(name);
			} catch (FileNotFoundException e) {
				// ignore it and try other way
			}
		}
		if (in != null)
			return;
		ClassLoader loader = Main.class.getClassLoader();
		in = loader.getResourceAsStream(name);
		if (in == null)
			throw new RuntimeException();
	}

	/**
	 * Read word from each line of text file and add it into List.
	 * 
	 * @return List of words in the text file.
	 */
	public static List<String> readWords() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<String> words = new ArrayList<String>();
		while (true) {
			String word = "";
			try {
				word = reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (word == null)
				break;
			words.add(word);
		}
		return words;
	}

	/**
	 * Run the SyllableCounter to count the syllable, measure the time taken,
	 * and print out the result.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final String DICT_URL = "http://se.cpe.ku.ac.th/dictionary.txt";
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		// setInput(DICT_URL);
		setInput("dictionary.txt");
		List<String> words = readWords();
		OOSyllableCounter wordCounter = new OOSyllableCounter();
		int count = 0;
		for (String word : words) {
			count += wordCounter.countSyllables(word);
		}
		stopwatch.stop();
		System.out
				.println("Reading word from http://se.cpe.ku.ac.th/dictionary.txt");
		System.out.println("Counted " + count + " syllables in " + words.size()
				+ " words");
		System.out.printf("Elapsed time: %.3f sec", stopwatch.getElapsed());
	}
}
