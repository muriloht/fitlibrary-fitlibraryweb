/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import fitlibrary.PrimitiveArrayFixture;
import fitlibrary.differences.LocalFile;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.Log;
import fitlibrary.object.DomainObjectSetUpTraverse;
import fitlibrary.traverse.Traverse;
import fitlibrary.traverse.workflow.DoTraverse;

public class PDFDocument extends DoTraverse {
	private PDFParagraphStripper stripper = null;
	private PDDocument document;
	private File sourceFile;
	private List<String> paragraphs = null;
	private boolean loaded = false;
//	private int basicParagraphDrop = 6;
//	private float heightSpaceFactor = 1.1f;

	public void open(String file) {
		this.sourceFile = Traverse.getLocalFile(file).getFile();
		try {
			document = PDDocument.load(sourceFile);
			stripper = new PDFParagraphStripper(document);
			stripper.setLineSeparator(" ");
			selectAllPages();
			document.getNumberOfPages(); // Needs to be done here to ensure we can see all the pages
		} catch (IOException e) {
			Log.logAndThrow(e);
		}
	}
	public String text() {
		loadParagraphs();
		StringBuilder result = new StringBuilder();
		for (String paragraph: getParagraphs()) {
			result.append(paragraph);
			result.append(" ");
		}
		return result.toString();
	}
	private List<String> getParagraphs() {
		return paragraphs;
	}
//	public String rawText() {
//		try {
//			return stripper.getText(document);// So processTextPosition() is called over the text.
//		} catch (IOException e) {
//			Log.logAndThrow(e);
//		}
//		return "";
//	}
	public String paragraphedText() {
		loadParagraphs();
		StringBuilder result = new StringBuilder();
		for (String paragraph: getParagraphs()) {
			result.append(paragraph);
			result.append("<br/>");
		}
		return result.toString();
	}
	public PrimitiveArrayFixture paragraphs() {
		loadParagraphs();
		return new PrimitiveArrayFixture(getParagraphs());
	}
	public PrimitiveArrayFixture paragraphsFromTo(int from, int to) {
		loadParagraphs();
		if (from < 0 || from >= getParagraphs().size() || to < from || to >= getParagraphs().size())
			throw new FitLibraryException("Out of bounds: 0 .. "+(getParagraphs().size()-1));
		List<String> subList = getParagraphs().subList(from, to+1);
		return new PrimitiveArrayFixture(subList);
	}
	public String paragraphBelowHeading(String heading) {
		loadParagraphs();
		for (int i = 0; i < getParagraphs().size() - 1; i++) {
			if (getParagraphs().get(i).equals(heading))
					return getParagraphs().get(i+1);
		}
		throw new FitLibraryException("Heading not found");
	}
	public String getParagraphAt(int i) {
		loadParagraphs();
		return getParagraphs().get(i);
	}
	public String paragraphAfterContaining(String s) {
		loadParagraphs();
		for (int i = 0; i < getParagraphs().size() - 1; i++) {
			if (getParagraphs().get(i).contains(s))
					return getParagraphs().get(i+1);
		}
		throw new FitLibraryException("Heading not found");
	}
	public void selectPage(int pageToSelect) {
		stripper.setStartPage(pageToSelect);
		stripper.setEndPage(pageToSelect);
		loaded = false;
	}
	public void selectAllPages() {
		stripper.setStartPage(1);
		stripper.setEndPage(numberOfPages());
		loaded = false;
	}
	public int numberOfPages() {
		return document.getNumberOfPages();
	}
	public boolean closePDFFile() throws IOException {
		if (document != null) {
			document.close();
		}
		document = null;
		return true;
	}
	public void setBasicParagraphDrop(int basicParagraphDrop) {
		stripper.setBasicParagraphDrop(basicParagraphDrop);
		loaded = false;
	}
	public void setHeightSpaceFactor(float heightSpaceFactor) {
		stripper.setHeightSpaceFactor(heightSpaceFactor);
		loaded = false;
	}
	public DomainObjectSetUpTraverse customise() {
		return new DomainObjectSetUpTraverse(this);
	}
	public boolean getShowPdfAsImage() throws IOException {
        final ImageWriter imageWriter = findImageWriter("png");
        ImageWriteParam writerParams = imageWriter.getDefaultWriteParam();
        if(writerParams.canWriteCompressed()) {
            writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writerParams.setCompressionQuality(1.0f);
        }
        @SuppressWarnings("unchecked")
    	List<PDPage> pages = document.getDocumentCatalog().getAllPages();
    	String imageFileNameRelativePath = null;
        int pageNumber = 1;
    	for(PDPage page: pages) {
    		ImageOutputStream output = null;
            try {
                BufferedImage image = page.convertToImage();
                imageFileNameRelativePath = "images/"+sourceFile.getName() + (pageNumber++)+".png";
                
                output = ImageIO.createImageOutputStream(Traverse.getGlobalFile(imageFileNameRelativePath).getFile());
                imageWriter.setOutput( output );
                
		        imageWriter.write( null, new IIOImage( image, null, null), writerParams );
            } finally {
            	imageWriter.dispose();
                if( output != null ) {
                    output.flush();
                    output.close();
                    
                    LocalFile localFile = Traverse.getLocalFile(imageFileNameRelativePath);
                    showAfterTable(localFile.htmlImageLink());
                }
            }
        }
        return true;
    }
	
	private void loadParagraphs() {
		if (loaded)
			return;
		paragraphs  = stripper.collectParagraphs();
		loaded = true;
	}
	// The following code doesn't work properly when several pages are selected
//	private void loadParagraphs22() {
//		if (loaded)
//			return;
//		loaded = true;
//		paragraphs  = new ArrayList<String>();
//		StringIterator spacedTextIterator = new StringIterator(rawText());
//		StringBuilder paragraph = new StringBuilder();
//		TextPosition previousPosition = null;
//		Iterator<?> articleIterator = stripper.getCharactersByArticle().iterator();
//		while (articleIterator.hasNext()) {
//			Iterator<?> textPositionsIterator = ((List<?>)articleIterator.next()).iterator();
//			while (textPositionsIterator.hasNext()) {
//				TextPosition currentPosition = (TextPosition) textPositionsIterator.next();
//				String rawText = currentPosition.getCharacter();
//				for (int i = 0; i < rawText.length(); i++) {
//					String spaced = next(spacedTextIterator);
//					String raw = rawText.substring(i,i+1);
//					if (!spaced.equals(raw)) {
//						paragraph.append(spaced);
//						spaced = next(spacedTextIterator);
//					}
//					if (isNewParagraph(previousPosition,currentPosition)) {
//						getParagraphs().add(paragraph.toString().trim());
//						paragraph = new StringBuilder();
//					}
//					paragraph.append(raw);
//				}
//				previousPosition = currentPosition;
//			}
//		}
//		if (paragraph.length() > 0)
//			getParagraphs().add(paragraph.toString().trim());
//	}
//	private String next(StringIterator spacedTextIterator) {
//		if (!spacedTextIterator.hasNext())
//			throw new FitLibraryException("Insufficient characters");
//		return spacedTextIterator.next();
//	}
//	private boolean isNewParagraph(TextPosition previousPosition, TextPosition currentPosition) {
//		if (previousPosition == null || currentPosition == null)
//			return false;
//		return paragraphDrop(previousPosition, currentPosition) || 
//			   differentFontOnNewLine(previousPosition, currentPosition);
//	}
//	private boolean paragraphDrop(TextPosition previousPosition,
//			TextPosition currentPosition) {
//		float maxHeight = Math.max(currentPosition.getHeight(), previousPosition.getHeight());
//		float sufficientSpace = basicParagraphDrop + maxHeight * heightSpaceFactor;
//		return currentPosition.getY() - previousPosition.getY() > sufficientSpace ;
//	}
//	private boolean differentFontOnNewLine(TextPosition previousPosition, TextPosition currentPosition) {
//		return currentPosition.getY() - previousPosition.getY() > basicParagraphDrop &&
//		       previousPosition.getHeight() - currentPosition.getHeight() > 2 &&
//			   !previousPosition.getFont().getBaseFont().equals(currentPosition.getFont().getBaseFont());
//	}
	private ImageWriter findImageWriter(String imageType) {
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName( imageType );
		
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new FitLibraryException( "Error: no writer found for image type '" + imageType + "'" );
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closePDFFile();
	}
}
