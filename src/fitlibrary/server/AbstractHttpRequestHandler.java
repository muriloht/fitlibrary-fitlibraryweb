/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;

public abstract class AbstractHttpRequestHandler implements HttpRequestHandler {
	private static Logger logger = FixturingLogger.getLogger(AbstractHttpRequestHandler.class);

	protected void errorInHtmlH1(HttpResponse response, int status, final String error) {
		error(response,status,"<html><body><h1>"+error+"</h1></body></html>");
	}
	protected void error(HttpResponse response, int status, final String error) {
		response.setStatusCode(status);
		EntityTemplate body = makeBody(error,"text/html; charset=UTF-8");
		response.setEntity(body);
		logger.error(error);
	}
	protected EntityTemplate makeBody(final String contents, String contentType) {
		EntityTemplate body = new EntityTemplate(new ContentProducer() {
			@Override
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write(contents);
                writer.flush();
			}
		});
		body.setContentType(contentType);
		return body;
	}
}
