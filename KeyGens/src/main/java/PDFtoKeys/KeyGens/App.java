package PDFtoKeys.KeyGens;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Hello world!
 * 
 */
public class App {
	public App() {

	}

	public static void main(String[] args) {
		// BasicConfigurator.configure();

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

	private static void parsePDFtoKey() throws LangDetectException, IOException {
		PDFExtractor app = new PDFExtractor();
		String parsedText = app.parsePdftoString();

		LangDetect lang = new LangDetect();
		app.setLang(lang.detect(parsedText));
		System.out.println(app.getLang());
		// sentence detector -> tokenizer
		ArrayList<String> tokenheaven = app.getToken(parsedText);
		String[] tokenTest = new String[tokenheaven.size()];
		for (int ii = 0; ii < tokenheaven.size(); ii++) {
			tokenTest[ii] = tokenheaven.get(ii);
		}
		String[] tokens = app.generalToken(parsedText);
		ArrayList<String> keywords = app.getKeywordsfromPDF(tokens);
		String[] filter = app.posttags(tokenTest);
		// ArrayList<Integer> keys = app.filterNounVerb(filter);
		ArrayList<String> keys = app.filterNoun(filter, tokens);
		ArrayList<Keyword> keyOcc = app.keyOcc(keys);

		System.out.println("normal:" + tokens.length + ", optimiertNouns:"
				+ keys.size() + ", keywordsocc" + keyOcc.size());
		// go go stemming
		createTextExport(keyOcc);
		if (keywords.isEmpty()) {

			// empty - could not directly extract keywords
		} else {
			// use extracted keywords as ref. elements
		}
		// app.token();

	}

	private static void createTextExport(ArrayList<Keyword> keyOcc) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("test.txt"), "utf-8"));
			for (int ii = 0; ii < keyOcc.size(); ii++) {
				Keyword current = keyOcc.get(ii);

				writer.write(current.word + ";" + current.occ + ";");

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
