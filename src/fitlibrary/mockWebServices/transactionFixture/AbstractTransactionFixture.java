/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.transactionFixture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fitlibrary.DoFixture;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.mockWebServices.MockingWebServices;
import fitlibrary.mockWebServices.requestMatcher.AcceptAnyRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.UriRequestMatcher;
import fitlibrary.mockWebServices.responder.FileResponder;
import fitlibrary.mockWebServices.responder.LiteralResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.LeafTerm;
import fitlibrary.mockWebServices.term.RepeatingTerm;
import fitlibrary.mockWebServices.term.SequentialTerm;

// Pass the Term across to the MockingWebServices on tearDown().
public abstract class AbstractTransactionFixture extends DoFixture {
	protected int port;
	protected MockingWebServices mockingWebServices;
	protected RequestMatcher requestMatcher = new AcceptAnyRequestMatcher();
	protected List<Responder> responders = new ArrayList<Responder>();
	protected SequentialTerm sequentialTerm = new SequentialTerm();
	private boolean insertAtEnd = true;

	public AbstractTransactionFixture(int port, MockingWebServices mockingWebServices) {
		this.port = port;
		this.mockingWebServices = mockingWebServices;
	}
	public void matchesURL(String url) {
		requestMatcher = requestMatcher.and(new UriRequestMatcher(url));
	}
	public void response(String responseString) {
		LiteralResponder responder = new LiteralResponder(responseString);
		responder.setContentIsXml(isXml());
		responders.add(responder);
	}
	public void then() {
		addToSequence();
	}
	public void insert() {
		insertAtEnd = false;
	}
	public void repeat() {
		if (responders.size() != 1)
			throw new FitLibraryException("It only makes sense to repeat a single response.");
		sequentialTerm.add(new RepeatingTerm(new LeafTerm(requestMatcher,responders.get(0))));
		reset();
	}
	public void responseFromFile(String fileName) {
		FileResponder responder = new FileResponder(fileName);
		responder.setContentIsXml(isXml());
		responders.add(responder);
	}
	public void responsesFromFolder(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists())
			throw new FitLibraryException("Doesn't exist: "+folderName);
		List<String> list = Arrays.asList(folder.list());
		Collections.sort(list);
		for (String fileName : list)
			if (!fileName.endsWith(".svn"))
				responseFromFile(folderName+"/"+fileName);
	}
	public void responseCodeWith(int resultCode, String contents) { 
		LiteralResponder responder = new LiteralResponder(resultCode,contents);
		responder.setContentIsXml(isXml());
		responders.add(responder);
	}
	protected abstract boolean isXml();

	public void tearDown() throws Exception {
		addToSequence();
		mockingWebServices.or(port,sequentialTerm,insertAtEnd);
	}
	private void addToSequence() {
		if (responders.isEmpty()) {
			if (sequentialTerm.isEmpty())
				throw new FitLibraryException("Response has not been defined");
			return;
		}
		for (Responder responder : responders)
			sequentialTerm.add(new LeafTerm(requestMatcher,responder));
		reset();
	}
	private void reset() {
		requestMatcher = new AcceptAnyRequestMatcher();
		responders.clear();
	}
}
