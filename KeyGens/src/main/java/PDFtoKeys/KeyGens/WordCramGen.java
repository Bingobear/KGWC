package PDFtoKeys.KeyGens;

import wordcram.*;
import processing.core.*;

import java.awt.*;
import java.net.URL;

/**
 * WordCram Processing works, however very slow
 * @author sbruns
 *
 */
public class WordCramGen extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2562164413223015554L;
	WordCram wordcram;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void setup() {

		// IMAGE - Be careful size has to be at least as big as underlying image
		URL url = getClass().getResource("/IMG/Text.gif");
		int test = 0;
		PImage image = loadImage(url.toString());
		Shape imageShape = new ImageShaper().shape(image, Color.BLACK.getRGB());
		ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);
		// size(1024, 860);
		size(image.width, image.height);
		background(255);

		// This code will print all the lines from the source text file.
		url = getClass().getResource("/IMG/test1.txt");
		String[] lines = loadStrings(url.toString());
		String[] data = split(lines[0], ';');
		/*
		 * println("there are " + lines.length +data.length+ " lines");
		 * println(data);
		 */
		// Each Word object has its word, and its weight. You can use whatever
		// numbers you like for their weights, and they can be in any order.
		Word[] wordArray = new Word[data.length / 2];
		// Word[] wordArray = new Word[data.length/2+1];

		int ii = 0;
		int jj = 0;

		while (ii < data.length - 1) {
			Word word = new Word(data[ii], Integer.parseInt(data[ii + 1]));
			wordArray[jj] = word;
			ii = ii + 2;
			jj++;
		}

		// Pass in the sketch (the variable "this"), so WordCram can draw to it.
		wordcram = new WordCram(this).

		// image properties
				withPlacer(placer).withNudger(placer).
				// Weight relation - size
				sizedByWeight(7, 40).withColor(Color.BLACK.getRGB())
				// Pass in the words to draw.
				.fromWords(wordArray)
				// Define angle
				.angledAt(0);
	}

	public void draw() {
		/*
		 * SAVE AS SVG try { new WordCram(this) .toSvg("text.svg", width,
		 * height) .fromWebPage("http://nytimes.com") .drawAll(); } catch
		 * (java.io.FileNotFoundException x) { println(x); }
		 */

		// Now we've created our WordCram, we can draw it:
		wordcram.drawAll();
		int test=0;
		// fill tagcloud word by word
		/*
		 * if (wordcram.hasMore()) { wordcram.drawNext(); } else {
		 * println("done"); noLoop(); }
		 */

	}
}
