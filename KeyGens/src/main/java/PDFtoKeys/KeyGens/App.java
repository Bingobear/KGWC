package PDFtoKeys.KeyGens;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import PDFtoKeys.KeyGens.PDFExtractor;
import PDFtoKeys.KeyGens.PDF;
import PDFtoKeys.KeyGens.WordOcc;
import PDFtoKeys.KeyGens.Words;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Hello world!
 * 
 */
public class App {
	static boolean debug = true;

	public App() {

	}

	public static void main(String[] args) {
		// BasicConfigurator.configure();
		Text2Image image = new Text2Image();
		image.generateImage("INEC");
		if (!debug) {
			try {
				parsePDFtoKey();
			} catch (LangDetectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void parsePDFtoKey() throws LangDetectException, IOException {
		PDFExtractor extractor = new PDFExtractor();
		ArrayList<Words> words = extractor.parsePDFtoKey();

		ArrayList<WordOcc> occ = extractor.keyOcc(words);
		// createTextExport(occ);
		createTextExport(occ);

		PDF pdf = new PDF(occ, extractor.getLang());

	}

	private static void createTextExport(ArrayList<WordOcc> keyOcc) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("test.txt"), "utf-8"));
			for (int ii = 0; ii < keyOcc.size(); ii++) {
				WordOcc current = keyOcc.get(ii);

				writer.write(current.getWord().getWord() + ";"
						+ current.getOcc() + ";");

			}
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}

}
