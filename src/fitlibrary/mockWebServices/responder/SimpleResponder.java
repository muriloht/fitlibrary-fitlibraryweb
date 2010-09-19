/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleResponder implements Responder {
	private boolean contentIsXml;
	private int resultCode = 200;
	
	public SimpleResponder() {
		this(200);
	}
	public SimpleResponder(int resultCode) {
		this.resultCode = resultCode;
	}
	@Override
	public String getUri() {
		return "";
	}
	@Override
	public String contentsOf(String line) {
		return line;
	}
	public void setContentIsXml(boolean contentIsXml) {
		this.contentIsXml = contentIsXml;
	}
	@Override
	public Map<String, String> getHeaders() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		if (contentIsXml) {
			hashMap.put("Content-Type", "text/xml;charset-UTF-8");
		}
		hashMap.put("Server", "MockingServer");
		return hashMap;
	}
	@Override
	public int getResultCode() {
		return resultCode ;
	}
}
