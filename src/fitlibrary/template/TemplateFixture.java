/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.template;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.traverse.workflow.DoTraverse;

public class TemplateFixture extends DoTraverse {
	public String getTemplate(String fileName) {
		try {
			StringWriter stringWriter = new StringWriter();
			processTemplate(fileName, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public void getTemplateAsFile(String fileName, String outFileName) throws Exception {
		FileWriter fileWriter = new FileWriter(new File(outFileName));
		processTemplate(fileName, fileWriter);
		fileWriter.close();
	}
	private void processTemplate(String fileName, Writer writer) throws Exception {
		Template template = Velocity.getTemplate(fileName);
		VelocityContext context = new VelocityContext(getDynamicVariables().getMap());
		template.merge(context, writer);
	}
}
