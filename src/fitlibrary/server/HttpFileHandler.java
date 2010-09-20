/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.FileEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;

public class HttpFileHandler extends AbstractHttpRequestHandler  {
	private static Logger logger = FixturingLogger.getLogger(HttpFileHandler.class);
    private final File docRoot;
    
    public HttpFileHandler(final String docRoot) {
        super();
        this.docRoot = new File(docRoot);
    }
    @Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
    	logger.trace("Received request: "+request.getRequestLine());
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported"); 
        }
        String target = request.getRequestLine().getUri();
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            byte[] entityContent = EntityUtils.toByteArray(entity);
            logger.trace("Incoming entity content (bytes): " + entityContent.length);
        }
        String fileName = URLDecoder.decode(target,"utf-8");
		final File file = new File(docRoot,fileName);
        if (!file.exists() || attemptToEscapeDirectory(file))
        	errorInHtmlH1(response,HttpStatus.SC_NOT_FOUND,
        			"File "+file.getPath()+" not found in "+docRoot.getPath());
        else if (!file.canRead() || file.isDirectory())
        	errorInHtmlH1(response,HttpStatus.SC_FORBIDDEN,
        			"Access denied: File is a directory or cannot be read: "+file.getPath());
        else {
            response.setStatusCode(HttpStatus.SC_OK);
            String contentType = mime(fileName)+"; charset=UTF-8";
    		logger.trace("Reply Content-type: "+contentType);
			FileEntity body = new FileEntity(file, contentType);
            response.setEntity(body);
            logger.trace("Serving file " + file.getPath());
        }
    }
	private boolean attemptToEscapeDirectory(File file) {
		return !file.getAbsolutePath().startsWith(docRoot.getAbsolutePath());
	}
	private String mime(String fileName) {
		return new MimetypesFileTypeMap().getContentType(fileName);
	}
}