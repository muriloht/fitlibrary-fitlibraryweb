/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.traverse.Traverse;
import fitlibrary.ws.client.WebService;
import fitlibrary.ws.message.ContentType;
import fitlibrary.ws.soap.Soap;

public class WebServicesClientFixture extends WebService {
	public String toPostText(String url, String s) {
		try {
			return postHttp(url, s, ContentType.PLAIN);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostTextFromFile(String url, String fileName) {
		try {
			return toPostText(url, readFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostTextFromRelativeFile(String url, String fileName) {
		try {
			return toPostText(url, readRelativeFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toAsPostFullSoap(String url, ContentType contentType, String xmlOut) {
		try {
			reply = postHttp(url, xmlOut,contentType).trim();
			return reply;
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toAsPostFullSoapFromFile(String url, ContentType contentType, String fileName) {
		try {
			return toAsPostFullSoap(url,contentType,readFile(fileName)).trim();
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toAsPostFullSoapFromRelativeFile(String url, ContentType contentType, String fileName) {
		try {
			return toAsPostFullSoap(url,contentType,readRelativeFile(fileName)).trim();
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap11(String url, String xmlOut) {
		try {
			reply = Soap.unwrap11(postHttp(url, Soap.wrap11(xmlOut), ContentType.SOAP11)).trim();
			return reply;
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap11FromFile(String url, String fileName) {
		try {
			return toPostSoap11(url,readFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap11FromRelativeFile(String url, String fileName) {
		try {
			return toPostSoap11(url,readRelativeFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap12(String url, String xmlOut) {
		try {
			reply = Soap.unwrap12(postHttp(url, Soap.wrap12(xmlOut), ContentType.SOAP12)).trim();
			return reply;
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap12FromFile(String url, String fileName) {
		try {
			return toPostSoap12(url,readFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String toPostSoap12FromRelativeFile(String url, String fileName) {
		try {
			return toPostSoap12(url,readRelativeFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String postingTextWithAndTimeOut(String url, String text, int timeoutInMilliseconds) {
		timeout(timeoutInMilliseconds);
		return postHttp(url, text,ContentType.PLAIN);
	}
	public String postTextWith(String url, String s) {
		showAfterTable("Deprecated: use |''to''|"+url+"|''post text''|"+s+"| instead");
		return toPostText(url,s);
	}
	public String postWith(String url, String xmlOut) {
		showAfterTable("Deprecated: use |''to''|"+url+"|''post soap11''|"+xmlOut+"| instead");
		return toPostSoap11(url,xmlOut);
	}
	public String postTextFromFile(String url, String fileName) {
		showAfterTable("Deprecated: use |''to''|"+url+"|''post text from file''|"+fileName+"| instead");
		return toPostTextFromFile(url,fileName);
	}
	public String postSoapFromFile(String url, String fileName) {
		showAfterTable("Deprecated: use |''to''|"+url+"|''post soap12 from file''|"+fileName+"| instead");
			return toPostSoap12FromFile(url,fileName);
	}
	public String postFromFile(String url, String fileName, ContentType contentType) {
		showAfterTable("Deprecated: use |''to''|"+url+"|''post full soap from file''|"+fileName+"| instead");
		try {
			return toAsPostFullSoapFromFile(url,contentType,readFile(fileName));
		} catch (IOException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	private String postHttp(String url, String contents, ContentType contentType) {
		httpPost(url, contents, contentType.getContentType(), "utf-8");
		return getReply();
	}
	private String readFile(String fileName) throws IOException {
		return readFile(new File(fileName));
	}
	private String readRelativeFile(String localFileName) throws IOException {
		return readFile(Traverse.getLocalFile(localFileName).getFile());
	}
	private String readFile(File file) throws FileNotFoundException,
			IOException {
		InputStream inputStream = new FileInputStream(file);
		try {
			return IOUtils.toString(inputStream);
		} finally {
			inputStream.close();
		}
	}
}
