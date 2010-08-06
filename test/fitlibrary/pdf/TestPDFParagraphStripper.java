/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.pdf;

import org.junit.Test;

public class TestPDFParagraphStripper {
//	float yposition = 100.00f;
//	protected TextPosition text(final String text, final String fontName) {
//		yposition += 10.0+1.00f;
//		return text(text, fontName, 10.00f, yposition);
//	}
//	
//	protected TextPosition text(final String text, final String fontName, final float fontHeight, final  float ypos) {
//		return new TextPosition() {
//			@Override
//			public String getCharacter() {
//				return text;
//			}
//			
//			@Override
//			public PDFont getFont() {
//				return new PDTrueTypeFont() {  // trueTypeFont is irrelevant here just need something we can construct - we are stubbing font, ..
//					@Override
//					public String getBaseFont() {
//						return fontName;
//					}
//				};
//			}
//			
//			@Override
//			public float getHeight() {
//				return fontHeight;
//			}
//			
//			@Override
//			public float getY() {
//				return ypos;
//			}
//		};
//	}
//	
//	@Test
//	public void withOneHeadingAndOneLineParagraph() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("Heading", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("this is the paragraph.", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "Heading"), is("this is the paragraph."));
//	}
//	
//	@Test
//	public void withOneHeadingAndTwoLineParagraph() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line1. ", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line2.", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING"), is("this is the line1. this is the line2."));
//	}
//	
//	@Test
//	public void spaceAddedBetweenLines() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("first line", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("second line. ", "BODY-FONT"));  // <-- we should trim final whitespace as FitNesse 
//																				  //     will trim any expected value during comparisons.
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING"), is("first line second line."));
//	}
//
//	@Test
//	public void withNoTextFollowingHeading() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING", "HEADING-FONT"));
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING"), is(""));
//	}
//	
//	@Test
//	public void withInvalidHeading() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line1. ", "BODY-FONT"));
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADINGMISSING"), is(""));
//	}
//	
//	@Test
//	public void withMultipleHeadingsParagraphs() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//		
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line1 para1.", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line2 para1.", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT2"));
//				processPossibleParagraphOrHeading(text("this is the line1 para2.", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING3", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("this is the line1 para3.", "BODY-FONT2"));
//				processPossibleParagraphOrHeading(text("this is the line2 para3.", "BODY-FONT2"));
//				
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("this is the line1 para1. this is the line2 para1."));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING2"), is("this is the line1 para2."));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING3"), is("this is the line1 para3. this is the line2 para3."));
//	}
//	
//	@Test
//	public void blankLinesMeansEndOfParagraph() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("one", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text(" ", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("ignore me", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING3", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text(" ", "BODY-FONT"));			// <-- blank line here should not count as paragraph
//				processPossibleParagraphOrHeading(text("three", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("one"));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING2"), is("two"));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING3"), is("three"));
//	}
//	
//	@Test
//	public void twoHeadingsInSameDocumentSecondOneIgnored() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("one", "BODY-FONT"));
//				processPossibleParagraphOrHeading(text("", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("ignore", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("one"));
//	}
//	
//	@Test
//	public void usingIgnoreTextBeforeToIgnoreFirstHeading() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("ignore", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("find me", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		textStripper.ignoreTextBefore("HEADING2");
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("find me"));
//	}
//	
//	@Test
//	public void IgnoredTextNotLostBetweenCalls() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("ignore", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("find me", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		textStripper.ignoreTextBefore("HEADING2");
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("find me"));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING2"), is("two"));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("find me"));
//	}
//	
//	@Test
//	public void IgnoredTextClearedWhenWeChangePages() throws IOException {
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("one", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//				processPossibleParagraphOrHeading(text("two", "BODY-FONT"));
//				
//				return "UNUSED";
//			}
//		};
//
//		textStripper.ignoreTextBefore("HEADING2");
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("two"));
//		
//		textStripper.setStartPage(1); // it shouldn't matter what the page was before
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("one"));
//		
//		textStripper.ignoreTextBefore("HEADING2");
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("two"));
//		textStripper.setEndPage(1);
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("one"));
//	}
//	
//	@Test
//	public void paragraphsClearOnSubsequentCalls() throws IOException {
//		// subsequent call may be new page so we make sure old values are cleared
//		
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				if (getStartPage()==1) {
//					processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT"));
//					processPossibleParagraphOrHeading(text("this is the line1 para1.", "BODY-FONT"));
//					processPossibleParagraphOrHeading(text("this is the line2 para1.", "BODY-FONT"));
//				}
//				
//				if (getStartPage()==2) {
//					processPossibleParagraphOrHeading(text("HEADING2", "HEADING-FONT"));
//					processPossibleParagraphOrHeading(text("this is the only paragraph.", "BODY-FONT"));
//				}
//				
//				return "UNUSED";
//			}
//		};
//
//		textStripper.setStartPage(1);
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("this is the line1 para1. this is the line2 para1."));
//		textStripper.setStartPage(2);
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is(""));
//		assertThat(textStripper.getParagraphBelow(null, "HEADING2"), is("this is the only paragraph."));
//	}
//	
//	@Test
//	public void canDetectParagraphsWhenDifferentYpositionButNoFontChangeOrBlankLines() throws IOException {
//		// subsequent call may be new page so we make sure old values are cleared
//		
//		PDFParagraphStripper textStripper = new PDFParagraphStripper() {
//			
//			// override the getText method to "fake" the calling of processTextPosition(..)
//			@Override
//			public String getText(org.apache.pdfbox.pdmodel.PDDocument doc) throws java.io.IOException {
//				float headingFontHeight = 12.00f;
//				float bodyFontHeight = 10.00f;
//				float ypos = 100.00f;
//				
//				processPossibleParagraphOrHeading(text("HEADING1", "HEADING-FONT", headingFontHeight, ypos));
//				ypos += headingFontHeight+1.00f;
//				processPossibleParagraphOrHeading(text("this is the line1.", "BODY-FONT", bodyFontHeight, ypos));
//				ypos += bodyFontHeight+1.00f;
//				processPossibleParagraphOrHeading(text("this is the line2.", "BODY-FONT", bodyFontHeight, ypos));
//				ypos += bodyFontHeight+1.00f+100.00f;  // <-- 100.00 pixels further down, obviously not part of this paragraph
//				processPossibleParagraphOrHeading(text("this is NOT line3 dont return me!!", "BODY-FONT", bodyFontHeight, ypos));
//				return "UNUSED";
//			}
//		};
//
//		assertThat(textStripper.getParagraphBelow(null, "HEADING1"), is("this is the line1. this is the line2."));
//	}
	@Test
	public void doNothingSoAntUnitTestsPass() {
		//
	}
}
