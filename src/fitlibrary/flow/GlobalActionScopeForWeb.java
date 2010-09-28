package fitlibrary.flow;

import fitlibrary.date.CreateDate;
import fitlibrary.email.ElectronicMail;
import fitlibrary.http.HttpClientFixture;
import fitlibrary.pdf.PDFDocument;
import fitlibrary.runtime.RuntimeContextInternal;
import fitlibrary.sh.ShellFixture;
import fitlibrary.traverse.RuntimeContextual;
import fitlibrary.ws.MockWebServicesFixture;
import fitlibrary.ws.WebServicesClientFixture;
import fitlibrary.xml.XmlDoFixture;

public class GlobalActionScopeForWeb implements RuntimeContextual {
	private RuntimeContextInternal runtimeContext;

	@Override
	public void setRuntimeContext(RuntimeContextInternal runtime) {
		this.runtimeContext = runtime;
	}
	@Override
	public Object getSystemUnderTest() {
		return null;
	}
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
}
