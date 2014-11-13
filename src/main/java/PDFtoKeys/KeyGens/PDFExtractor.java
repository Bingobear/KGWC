package PDFtoKeys.KeyGens;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFExtractor {
	// TODO: get Title via extracting names then creating offset to them via
	// first words
	/**
	 * PDF Extractor
	 * 
	 * @throws IOException
	 */

	private String language;

	PDFExtractor() {

	}

	PDFExtractor(String lang) {
		this.language = lang;
	}

	public void setLang(String lang) {
		this.language = lang;
	}

	public String getLang() {
		return this.language;
	}

	public String parsePdftoString() throws IOException {
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		URL url = getClass().getResource("/text/test.pdf");
		File file = new File(url.getPath());

		PDFParser parser = new PDFParser(new FileInputStream(file));
		parser.parse();
		cosDoc = parser.getDocument();
		pdfStripper = new PDFTextStripper();
		pdDoc = new PDDocument(cosDoc);
		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(97);
		String parsedText = pdfStripper.getText(pdDoc);
		// System.out.println(parsedText);
		return parsedText;
	}

	public ArrayList<String> getKeywordsfromPDF(String[] text) {
		ArrayList<String> keywords = new ArrayList<String>();
		ArrayList<String> textPDF = new ArrayList(Arrays.asList(text));
		if (textPDF.contains("Keywords")) {
			int counter = textPDF.indexOf("Keywords");
			counter++;
			int offset = 0;
			while (counter != 0) {
				if ((textPDF.get(counter).equals(",") && (offset != 0))) {
					String currkey = "";
					for (int ii = counter - offset; ii < counter; ii++) {
						currkey = currkey + textPDF.get(ii);
						if (!textPDF.get(ii + 1).equals(",")) {
							currkey = currkey + " ";
						}
					}
					counter++;
					keywords.add(currkey);
					offset = 0;
				} else if (offset > 4) {
					counter = 0;
				} else {
					counter++;
					offset++;
				}
			}
			// System.out.print(counter);
		}
		return keywords;

	}

	/**
	 * @param parsedText
	 * @return
	 * 
	 */
	public String[] generalToken(String parsedText) {
		Tokenizer _tokenizer = null;

		InputStream modelIn = null;
		String[] tokens = null;
		try {
			// Loading tokenizer model
			if (this.getLang().equals("en")) {
				modelIn = getClass().getResourceAsStream("/eng/en-token.bin");
			} else {
				modelIn = getClass().getResourceAsStream("/ger/de-token.bin");
			}
			final TokenizerModel tokenModel = new TokenizerModel(modelIn);
			modelIn.close();

			_tokenizer = new TokenizerME(tokenModel);
			tokens = _tokenizer.tokenize(parsedText);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}
		return tokens;
	}

	/**
	 * Hello world OpenNLP!
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 * 
	 */

	public void NameFinder(String[] sentences) throws InvalidFormatException, IOException {
//TEST STUFF
//		String[] sentences = {
//				"If President John F. Kennedy, after visiting France in 1961 with his immensely popular wife,"
//						+ " famously described himself as 'the man who had accompanied Jacqueline Kennedy to Paris,'"
//						+ " Mr. Hollande has been most conspicuous on this state visit for traveling alone.",
//				"Mr. Draghi spoke on the first day of an economic policy conference here organized by"
//						+ " the E.C.B. as a sort of counterpart to the annual symposium held in Jackson"
//						+ " Hole, Wyo., by the Federal Reserve Bank of Kansas City. " };

		// Load the model file downloaded from OpenNLP
		// http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin
		InputStream modelIn = null;
		if (this.getLang().equals("en")) {
			modelIn = getClass().getResourceAsStream("/eng/en-ner-person.bin");
		} else {
			modelIn = getClass().getResourceAsStream("/ger/de-ner-person.bin");
		}
		TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

		// Create a NameFinder using the model
		NameFinderME finder = new NameFinderME(model);

		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

		for (String sentence : sentences) {

			// Split the sentence into tokens
			String[] tokens = tokenizer.tokenize(sentence);

			// Find the names in the tokens and return Span objects
			Span[] nameSpans = finder.find(tokens);

			// Print the names extracted from the tokens using the Span data
			System.out.println(Arrays.toString(Span.spansToStrings(nameSpans,
					tokens)));
		}
	}

	public SentenceDetector sentencedetect() {
		// TODO Auto-generated method stub
		SentenceDetector _sentenceDetector = null;

		InputStream modelIn = null;
		try {
			// Loading sentence detection model
			if (this.getLang().equals("en")) {
				modelIn = getClass().getResourceAsStream("/eng/en-sent.bin");
			} else {
				modelIn = getClass().getResourceAsStream("/ger/de-sent.bin");
			}

			final SentenceModel sentenceModel = new SentenceModel(modelIn);
			modelIn.close();

			_sentenceDetector = new SentenceDetectorME(sentenceModel);

		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}
		return _sentenceDetector;
	}

	public ArrayList<String> getToken(String parsedText) {
		SentenceDetector sentdetector = sentencedetect();
		String[] sentence = sentdetector.sentDetect(parsedText);
		ArrayList<String> tokensA = new ArrayList<String>();
		for (int ii = 0; ii < sentence.length; ii++) {
			String[] tokenSen = generalToken(sentence[ii]);
			for (int jj = 0; jj < tokenSen.length; jj++) {
				tokensA.add(tokenSen[jj]);
			}
		}
		return tokensA;
	}

	public String[] posttags(String[] text) {
		POSTaggerME posttagger = createposttagger();
		String[] result = posttagger.tag(text);
		return result;

	}

	private POSTaggerME createposttagger() {


		InputStream modelIn = null;
		POSTaggerME _posTagger = null;
		try {
			// Loading tokenizer model
			if (this.getLang().equals("en")) {
				modelIn = getClass().getResourceAsStream(
						"/eng/en-pos-maxent.bin");
			} else {
				modelIn = getClass().getResourceAsStream(
						"/ger/de-pos-maxent.bin");
			}

			final POSModel posModel = new POSModel(modelIn);
			modelIn.close();

			_posTagger = new POSTaggerME(posModel);

		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}
		return _posTagger;

	}

	public ArrayList<String> filterNounVerb(String[] filter, String[] tokens) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int ii = 0; ii < filter.length; ii++) {
			if ((filter[ii].contains("NN")) || (filter[ii].contains("VB"))) {
				result.add(ii);
			}
		}
		ArrayList<String> words = new ArrayList<String>();
		for (int ii = 0; ii < result.size(); ii++) {
			words.add(tokens[result.get(ii)]);
		}
		return words;

	}

	// TODO Delete references from PDF

	public ArrayList<String> filterNoun(String[] filter, String[] tokens) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int ii = 0; ii < filter.length; ii++) {
			if ((filter[ii].contains("NN"))) {
				result.add(ii);
			}
		}
		ArrayList<String> words = new ArrayList<String>();
		for (int ii = 0; ii < result.size(); ii++) {
			words.add(tokens[result.get(ii)]);
		}
		return words;
	}

	public ArrayList<Keyword> keyOcc(ArrayList<String> keys) {
		ArrayList<String> keywords = new ArrayList<String>();
		keywords = (ArrayList<String>) keys.clone();
		int arraySize = keywords.size();
		ArrayList<Keyword> result = new ArrayList<Keyword>();
		while (arraySize > 0) {
			int count = 0;
			String current = keywords.get(0);
			int index = 0;
			while (index >= 0) {

				index = keywords.indexOf(current);
				if (index >= 0) {
					count++;
					keywords.remove(index);
					arraySize--;
				}
			}
			result.add(new Keyword(count, current));
		}
		return result;
	}

}