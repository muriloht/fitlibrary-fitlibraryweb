/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.utility;

import org.openqa.selenium.WebElement;

public class WebElementIdentifier {
	private final WebElement element;
	private final String tag;
	private String comma = "";

	public WebElementIdentifier(WebElement element, String tag) {
		this.element = element;
		this.tag = tag;
	}
	public String identify() {
		StringBuilder s = new StringBuilder();
		s.append("<"+tag);
		String attributes = addAttributes();
		if (!"".equals(attributes)) {
			s.append(" ");
			s.append(attributes);
		}
		String text = element.getText();
		if (!"".equals(text)) {
			s.append(">");
			s.append(text);
			s.append("</"+tag+">");
		} else
			s.append("/>");
		return s.toString();
	}
	private String addAttributes() {
		comma = "";
		StringBuilder s = new StringBuilder();
		addUnless(s,"name",element.getTagName(),tag);
		addAttribute(s, "id");
		
		String attributes = 
				"abbr accept accept-charset accesskey action align alt archive axis "+
				"bgcolor border "+
				"cellpadding cellspacing char charoff charset checked/false class classid codebase "+
				"codetype cols colspan compact content coords "+
				"data declare dir disabled/false "+
				"enctype "+
				"frame frameborder "+
				"headers heigth hspace href hreflang hspace http-equiv "+
				"label lang longdesc "+
				"marginheight marginwidth maxlength media method multiple "+
				"noresize noshade nowrap "+
				"onclick ondblclick onmousedown onmousemove onmouseout onmouseover onmouseup "+
				"onkeydown onkeypress onkeyup onload onunload "+
				"readonly/false rel rev rows rowspan rules "+
				"scheme scope scrolling shape size src standby start style summary "+
				"tabindex target title type "+
				"usemap "+
				"valign valuetype vspace "+
				"width "+
				"xml:lang "+
				"yalign";
		String[] attrs = attributes.split(" ");
		for (String attributeName : attrs) {
			String[] split = attributeName.split("/");
			if (split.length == 1)
				addAttribute(s, attributeName);
			else
				for (int i = 1; i < split.length; i++)
					addUnless(s, split[0],split[i].replace("_", " "));
		}
		addUnless(s,"value",element.getValue(),"Submit Query");
		addUnless(s,"enabled",""+element.isEnabled(),"true");
		try {
			addUnless(s,"selected",""+element.isSelected(),"false");
		} catch (Exception e) {
			//
		}
		return s.toString();
	}
	private void addAttribute(StringBuilder s, String attributeName) {
		add(s, attributeName, element.getAttribute(attributeName));
	}
	private void add(StringBuilder s, String attributeName, String value) {
		if (value != null && !"".equals(value)) {
			s.append(comma+attributeName+"='"+value+"'");
			comma = ", ";
		}
	}
	private void addUnless(StringBuilder s, String attributeName, String unless) {
		String value = element.getAttribute(attributeName);
		if (value != null && !value.equals(unless)) {
			s.append(comma+attributeName+"='"+value+"'");
			comma = ", ";
		}
	}
	private void addUnless(StringBuilder s, String attributeName, String value, String unless) {
		if (value != null && !value.equals(unless)) {
			s.append(comma+attributeName+"='"+value+"'");
			comma = ", ";
		}
	}
}
