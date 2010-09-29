package fitlibrary.flow;

import fitlibrary.date.CreateDate;
import fitlibrary.email.ElectronicMail;
import fitlibrary.http.HttpClientFixture;
import fitlibrary.pdf.PDFDocument;
import fitlibrary.server.ProxyServerFixture;
import fitlibrary.server.WebServerForTestingFixture;
import fitlibrary.sh.ShellFixture;
import fitlibrary.ws.MockWebServicesFixture;
import fitlibrary.ws.WebServicesClientFixture;
import fitlibrary.ws.recorder.RecordingWebServicesFixture;
import fitlibrary.xml.XmlDoFixture;

public class GlobalActionScopeForWeb {
	public WebServicesClientFixture withWebServicesClient() {
		return new WebServicesClientFixture();
	}
	public HttpClientFixture withHttpClient() {
		return new HttpClientFixture();
	}
	public XmlDoFixture withXml() {
		return new XmlDoFixture();
	}
	public CreateDate withDate() {
		return new CreateDate();
	}
	public PDFDocument withPDF() {
		return new PDFDocument();
	}
	public ElectronicMail withEmail() {
		return new ElectronicMail();
	}
	public ShellFixture withShell() {
		return new ShellFixture();
	}
	public MockWebServicesFixture withMockWebServices() {
		return new MockWebServicesFixture();
	}
	public RecordingWebServicesFixture withRecordingWebServices() {
		return new RecordingWebServicesFixture();
	}
	public WebServerForTestingFixture withWebServerForTesting() {
		return new WebServerForTestingFixture();
	}
	public ProxyServerFixture withProxyServerForTesting() {
		return new ProxyServerFixture();
	}
}
