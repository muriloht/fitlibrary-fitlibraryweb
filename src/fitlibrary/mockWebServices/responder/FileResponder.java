/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.ws.message.ContentType;

public class FileResponder extends SimpleResponder {
	private String fileName;
	
	public FileResponder(String fileName, ContentType responseContentType) {
		super(responseContentType);
		this.fileName = fileName;
		if (!new File(fileName).exists())
			throw new FitLibraryException("File doesn't exist: "+fileName);
	}
	@Override
	public String getContents() {
		try {
			return readFile();
		} catch (IOException e) {
			return "Problem reading file '"+fileName+"': "+e.getMessage();
		}
	}
	private String readFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
		String result = "";
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			result += line;
		}
		reader.close();
		return result;
	}
	@Override
	public boolean isOK() {
		return true;
	}
}
