package fitlibrary.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.Transform;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.global.PlugBoard;
import fitlibrary.traverse.workflow.DoTraverse;

public class XmlDoFixture extends DoTraverse {
	protected Map<String,String> nameSpaceMap = new HashMap<String,String>();
	
	static {
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}

	public XmlDoFixture() {
		nameSpace("");
	}
	public void nameSpace(String prefix) {
	    nameSpaceMap.put(prefix,"urn:"+prefix);
	    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(nameSpaceMap));
	}
	public boolean xmlSameAs(String actualXml, String expectedXml) {
		Diff diff = diff(actualXml, expectedXml);
		if (diff.identical())
			return true;
		return mismatch(diff);
	}
	public boolean xmlSimilarTo(String actualXml, String expectedXml) {
		Diff diff = diff(actualXml, expectedXml);
		if (diff.similar())
			return true;
		return mismatch(diff);
	}
	public boolean xpathExistsIn(String xPathExpression, String xml	) {
		try {
			NodeList nodeList = xpathEngine().getMatchingNodes(xPathExpression,doc(xml));
			return nodeList.getLength() > 0;
		} catch (XpathException e) {
			throw new FitLibraryException("invalid xpath");
		}
	}
	public String xpathIn(String xPathExpression, String xml) {
		try {
			return xpathEngine().evaluate(xPathExpression, doc(xml));
		} catch (XpathException e) {
			throw new FitLibraryException("invalid xpath");
		}
	}
	public String transformWith(String xml, String xslt) {
		try {
			Transform transform = new Transform(xml,xslt);
	        transform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			return transform.getResultString();
		} catch (TransformerException e) {
			throw xmlError(e);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String transformWithFile(String xml, String fileName) {
		try {
			Transform transform = new Transform(xml,new File(fileName));
	        transform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			return transform.getResultString();
		} catch (TransformerException e) {
			throw xmlError(e);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public static Diff diff(String actualXml, String expectedXml) {
		return diff(doc(actualXml), doc(expectedXml));
	}
	private static Document doc(String xml) {
		try {
			return XMLUnit.buildControlDocument(removeEmptyNameSpace(xml));
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	private static String removeEmptyNameSpace(String xmlInitial) {
		String xml = xmlInitial;
		xml = removeEmptyNameSpace(xml,"'");
		return removeEmptyNameSpace(xml,"\"");
	}
	// Need to get rid of it because it messes up xpath processing
	// See www.xml.com/pub/a/2004/02/25/quanda.html for an explanation
	private static String removeEmptyNameSpace(String xmlInitial, String quote) {
		String xml = xmlInitial;
		while (true) {
			int start = xml.indexOf(" xmlns="+quote);
			if (start < 0)
				return xml;
			if (start >= 0) {
				int end = xml.indexOf(quote,start+ " xmlns=".length()+1);
				if (end < 0)
					return xml;
				xml = xml.substring(0,start)+xml.substring(end+1);
			}
		}
	}
	private XpathEngine xpathEngine() {
	    XpathEngine engine = XMLUnit.newXpathEngine();
		engine.setNamespaceContext(new SimpleNamespaceContext(nameSpaceMap));
	    // above is not needed if the global settings work OK
		return engine;
	}
	private static Diff diff(Document actualXml, Document expectedXml) {
		try {
			return new Diff(expectedXml, actualXml);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	private static boolean mismatch(Diff diff) {
		final String DIFFERENT = "[different] ";
		String difference = new DetailedDiff(diff).toString();
		int at = difference.indexOf(DIFFERENT);
		if (at >= 0)
			difference = difference.substring(at+DIFFERENT.length());
		difference = difference.replaceAll("\n\\[different\\] ", "<hr>");
		throw new FitLibraryException(difference.trim());
	}
	private FitLibraryException xmlError(TransformerException e) {
		final String ERROR_MSG = "XML document structures must start and end within the same entity.";
		String message = PlugBoard.exceptionHandling.unwrapThrowable(e).getMessage().trim();
		if (message.endsWith(ERROR_MSG))
			return new FitLibraryException(ERROR_MSG);
		return new FitLibraryException(message);
	}
}
