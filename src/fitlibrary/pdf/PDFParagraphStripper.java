/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.pdf;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import fitlibrary.log.Log;

/** version of text stripper that is aware of paragraphs */
class PDFParagraphStripper extends PDFTextStripper {
	private StringBuilder paragraphFound;
	private String headingToSearchFor;
	private TextPosition lineOfLastParagraphFound;
	private String ignoreTextBeforeThisHeading;
	private boolean stillIgnoring;
	
	public PDFParagraphStripper() throws IOException {
		super();
	}

	@Override
	protected void processTextPosition(TextPosition textPosition) {
		super.processTextPosition(textPosition);
		
		// this seems to be the best method to override as it is called just after the text is pulled off the output stream and added to the page objects. 
		// We can extract fonts and text and figure out when paragraphs are changing.
		
		String message = MessageFormat.format("font = {0}, y = {1}, height = {2}, font height = {3} ---> {4}", 
				textPosition.getFont().getBaseFont(),
				textPosition.getY(),
				textPosition.getFontSize(),
				textPosition.getHeight(),
				textPosition.getCharacter());
		
		System.out.println(message);
		
//		if (textPosition.getCharacter().startsWith("0800")) {
//			System.out.println("------------------------------- here is the baddie ----------------------------------");
//			System.out.println(textPosition.getCharacter());
//			for (byte b: textPosition.getCharacter().getBytes()) {
//				System.out.println(b);
//			}
//			System.out.println("-------------------------------------------------------------------------------------");
//		}
		
		processPossibleParagraphOrHeading(textPosition);
	}

	protected void processPossibleParagraphOrHeading(TextPosition textInformation) {
		String text = textInformation.getCharacter();
		
		if (stillIgnoring) {
			if (text.equals(ignoreTextBeforeThisHeading)) {
				stillIgnoring = false;
			} else {
				return ;
			}
		}
		
		if (headingToSearchFor == null) {
			return;
		}
		
		if (text.trim().equals(headingToSearchFor)) {
			paragraphFound = new StringBuilder();
			return;
		} 
		
		if (paragraphFound == null) {
			return;
		}
		
		if (text.trim().length()==0) {
			if (paragraphFound.length() > 0) {
				headingToSearchFor = null;
			}
			return;
		}
		
		if (lineOfLastParagraphFound != null && !sameFontName(lineOfLastParagraphFound,textInformation)) {
			headingToSearchFor = null;
			return;
		}
		
		if (paragraphFound.length()>0 && paragraphFound.charAt(paragraphFound.length()-1) != ' ' && !text.startsWith(" ")) {
			paragraphFound.append(' ');
		}
		paragraphFound.append(text);
		lineOfLastParagraphFound = textInformation;
	}
	
	private boolean sameFontName(TextPosition previousLine, TextPosition currentLine) {
		if (previousLine == null || currentLine == null) {
			return false;
		}
		
		boolean sameFont =previousLine.getFont().getBaseFont().equals(currentLine.getFont().getBaseFont());
		
		if (!sameFont) {
			return false;
		}
		
		if (currentLine.getY()-previousLine.getY()>currentLine.getHeight()*2) {
			return false;
		}
		
		return true;
	}
	
	public String getParagraphBelow(PDDocument doc, String heading) {
		headingToSearchFor = heading;
		stillIgnoring = ignoreTextBeforeThisHeading != null;
		
		try {
			getText(doc);		// call this to trigger processTextPosition recursively over the text.
			
			if (paragraphFound != null) {
				return paragraphFound.toString().trim();
			}
		} catch (IOException e) {
			Log.logAndThrow(e);
		} finally {
			headingToSearchFor = null;
			paragraphFound = null;
			lineOfLastParagraphFound = null;
		}
		
		return "";
	}

	public void ignoreTextBefore(String heading) {
		this.ignoreTextBeforeThisHeading = heading;
	}
	
	@Override
	public void setStartPage(int startPageValue) {
		this.ignoreTextBeforeThisHeading = null;
		super.setStartPage(startPageValue);
	}
	
	@Override
	public void setEndPage(int startPageValue) {
		this.ignoreTextBeforeThisHeading = null;
		super.setStartPage(startPageValue);
	}
}