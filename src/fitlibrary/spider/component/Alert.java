package fitlibrary.spider.component;

import fitlibrary.spider.SpiderFixture;

public class Alert extends SpiderComponent {

	public Alert(SpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public boolean enterAlertText(String text) {
		webDriverAlert().sendKeys(text);
		return true;
	}
	public String getAlertMessage() {
		return webDriverAlert().getText();
	}
	public boolean acceptAlert() {
		webDriverAlert().accept();
		return true;
	}
	public boolean dismissAlert() {
		webDriverAlert().dismiss();
		return true;
	}
	private org.openqa.selenium.Alert webDriverAlert() {
		return webDriver().switchTo().alert();
	}
}
