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

import fitlibrary.differences.LocalFile;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.Log;
import fitlibrary.traverse.Traverse;
import fitlibrary.traverse.workflow.DoTraverse;

public class PDFDocument extends DoTraverse {
	private PDFParagraphStripper stripper = null;
	private PDDocument document;
	private File sourceFile;

	public PDFDocument() {
		//
	}
	
	public void open(String file) {
		this.sourceFile = Traverse.getGlobalFile(file).getFile();
		
		try {
			document = PDDocument.load(sourceFile);
			stripper = new PDFParagraphStripper();
			stripper.setLineSeparator("\n");
		} catch (IOException e) {
			Log.logAndThrow(e);
		}
	}
	
	public String text() {
		try {
			return stripper.getText(document);
		} catch (IOException e) {
			Log.logAndThrow(e);
		}
		return null;
	}
	
	public void ignoreTextBefore(String heading) {
		stripper.ignoreTextBefore(heading);
	}
	
	public String paragraphBelowHeading(String heading) {
		return stripper.getParagraphBelow(document, heading);
	}
	
	public void selectPage(int pageToSelect) {
		stripper.setStartPage(pageToSelect);
		stripper.setEndPage(pageToSelect);
	}
	
	public void selectAllPages() {
		stripper.setStartPage(1);
		stripper.setEndPage(numberOfPages());
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
	
	@SuppressWarnings("unchecked")
	public boolean getShowPdfAsImage() throws IOException {
        ImageWriter imageWriter = findImageWriter("png");
        ImageWriteParam writerParams = imageWriter.getDefaultWriteParam();
        if(writerParams.canWriteCompressed() )
        {
            writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writerParams.setCompressionQuality(1.0f);
        }
    
    	List<PDPage> pages = document.getDocumentCatalog().getAllPages();
    	String imageFileNameRelativePath = null;
        int pageNumber = 1;
    	for(PDPage page: pages) {
    		ImageOutputStream output = null;
            try
            {
                BufferedImage image = page.convertToImage();
                imageFileNameRelativePath = "images/"+sourceFile.getName() + (pageNumber++)+".png";
                
                output = ImageIO.createImageOutputStream(Traverse.getGlobalFile(imageFileNameRelativePath).getFile());
                imageWriter.setOutput( output );
                
		        imageWriter.write( null, new IIOImage( image, null, null), writerParams );
            }
            finally
            {
            	if( imageWriter != null )
		        {
		            imageWriter.dispose();
		        }
                if( output != null )
                {
                    output.flush();
                    output.close();
                    
                    LocalFile localFile = Traverse.getLocalFile(imageFileNameRelativePath);
                    showAfterTable(localFile.htmlImageLink());
                }
            }
        }
        
        return true;
    }

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

