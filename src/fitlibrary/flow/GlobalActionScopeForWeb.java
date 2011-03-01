package fitlibrary.flow;

import fitlibrary.annotation.CompoundAction;
import fitlibrary.annotation.NullaryAction;
import fitlibrary.annotation.SimpleAction;
import fitlibrary.date.CreateDate;
import fitlibrary.email.ElectronicMail;
import fitlibrary.http.HttpClientFixture;
import fitlibrary.pdf.PDFDocument;
import fitlibrary.server.ProxyServerFixture;
import fitlibrary.server.WebServerForTestingFixture;
import fitlibrary.sh.ShellFixture;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.template.TemplateFixture;
import fitlibrary.ws.MockWebServicesFixture;
import fitlibrary.ws.WebServicesClientFixture;
import fitlibrary.ws.recorder.RecordingWebServicesFixture;
import fitlibrary.xml.XmlDoFixture;

public class GlobalActionScopeForWeb {
	@NullaryAction(tooltip="Test a web service.")
	public WebServicesClientFixture withWebServicesClient() {
		return new WebServicesClientFixture();
	}
	public HttpClientFixture withHttpClient() {
		return new HttpClientFixture();
	}
	@NullaryAction(tooltip="Test some xml.")
	public XmlDoFixture withXml() {
		return new XmlDoFixture();
	}
	@CompoundAction(wiki="",
			tooltip="Create some dates to use in the storytest, through actions that follow in the table.")
	public CreateDate withDate() {
		return new CreateDate();
	}
	@SimpleAction(wiki="|''<i>with PDF</i>''|",
			tooltip="Test the contents of a PDF")
	public PDFDocument withPDF() {
		return new PDFDocument();
	}
	@NullaryAction(tooltip="Test email.")
	public ElectronicMail withEmail() {
		return new ElectronicMail();
	}
	@NullaryAction(tooltip="Test an application through the command line.")
	public ShellFixture withShell() {
		return new ShellFixture();
	}
	@NullaryAction(tooltip="Mock one or more web services.")
	public MockWebServicesFixture withMockWebServices() {
		return new MockWebServicesFixture();
	}
	@NullaryAction(tooltip="Record the activity of a web services.")
	public RecordingWebServicesFixture withRecordingWebServices() {
		return new RecordingWebServicesFixture();
	}
	@NullaryAction(tooltip="Run a web server to support testing.")
	public WebServerForTestingFixture withWebServerForTesting() {
		return new WebServerForTestingFixture();
	}
	@NullaryAction(tooltip="Run a proxy server to support testing.")
	public ProxyServerFixture withProxyServerForTesting() {
		return new ProxyServerFixture();
	}
	@NullaryAction(tooltip="Test a web application through one of several browsers.")
	public SpiderFixture withSpider() {
		return new SpiderFixture();
	}
	@NullaryAction(tooltip="Use a template to create a file.")
	public TemplateFixture withTemplate() {
		return new TemplateFixture();
	}
}
