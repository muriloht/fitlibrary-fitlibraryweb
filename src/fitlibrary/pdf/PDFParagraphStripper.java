/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.Log;

/** version of text stripper that is aware of paragraphs */
class PDFParagraphStripper extends PDFTextStripper {
	private List<String> paragraphs = null;
	StringBuilder paragraph = null;
	StringIterator spacedTextIterator = null;
	TextPosition previousPosition = null;
	private boolean loading = false;
	private int basicParagraphDrop = 6;
	private float heightSpaceFactor = 1.1f;

	public PDFParagraphStripper(PDDocument document) throws IOException {
		super();
		this.document = document;
	}
	@Override
	public List<?> getCharactersByArticle() {
		return super.getCharactersByArticle();
	}
		@Override
	protected void processTextPosition(TextPosition textPosition) {
		super.processTextPosition(textPosition);
		
		// this seems to be the best method to override as it is called just after the text is pulled off 
		// the output stream and added to the page objects. 
		// We can extract fonts and text and figure out when paragraphs are changing.
		// It appears as if stripper.getCharactersByArticle() should be used instead, but this
		// doesn't work when all of the pages have been selected.
		if (loading)
			processPossibleParagraphOrHeading(textPosition);
	}

	public String rawText() {
		try {
			return getText(document);// So processTextPosition() may be called over the text.
		} catch (IOException e) {
			Log.logAndThrow(e);
		}
		return "";
	}
	public List<String> collectParagraphs() {
		paragraphs = new ArrayList<String>();
		paragraph = new StringBuilder();
		spacedTextIterator = new StringIterator(rawText()); // first pass, to get spaced text
		previousPosition = null;
		loading = true;
		
		rawText(); // Second pass to extract paragraphs through processTextPosition() above
		
		if (paragraph.length() > 0)
			paragraphs.add(paragraph.toString().trim());
		loading = false;
		return paragraphs;
	}
	public void setBasicParagraphDrop(int basicParagraphDrop) {
		this.basicParagraphDrop = basicParagraphDrop;
	}
	public void setHeightSpaceFactor(float heightSpaceFactor) {
		this.heightSpaceFactor = heightSpaceFactor;
	}
	protected void processPossibleParagraphOrHeading(TextPosition currentPosition) {
		String rawText = currentPosition.getCharacter();
		for (int i = 0; i < rawText.length(); i++) {
			String spaced = nextSpacedText();
			String raw = rawText.substring(i,i+1);
			if (!spaced.equals(raw)) {
				paragraph.append(spaced);
				spaced = nextSpacedText();
			}
			if (isNewParagraph(currentPosition)) {
				paragraphs.add(paragraph.toString().replaceAll("\n"," ").replaceAll("  "," ").trim());
				paragraph = new StringBuilder();
			}
			paragraph.append(raw);
		}
		previousPosition = currentPosition;
	}
	private String nextSpacedText() {
		if (!spacedTextIterator.hasNext())
			throw new FitLibraryException("Insufficient characters");
		return spacedTextIterator.next();
	}
	private boolean isNewParagraph(TextPosition currentPosition) {
		if (previousPosition == null || currentPosition == null)
			return false;
		return paragraphDrop(currentPosition) || differentFontOnNewLine(currentPosition);
	}
	private boolean paragraphDrop(TextPosition currentPosition) {
		float maxHeight = Math.max(currentPosition.getHeight(), previousPosition.getHeight());
		float sufficientSpace = basicParagraphDrop + maxHeight * heightSpaceFactor;
		return currentPosition.getY() - previousPosition.getY() > sufficientSpace ;
	}
	private boolean differentFontOnNewLine(TextPosition currentPosition) {
		return currentPosition.getY() - previousPosition.getY() > basicParagraphDrop &&
		       previousPosition.getHeight() - currentPosition.getHeight() > 2 &&
			   !previousPosition.getFont().getBaseFont().equals(currentPosition.getFont().getBaseFont());
	}
}