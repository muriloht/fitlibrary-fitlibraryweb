/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.template.specify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fitlibrary.template.TemplateFixture;

public class SpecifyTemplateFixture extends TemplateFixture {
	public SpecifyTemplateFixture() throws Exception {
		super();
	}
	public void withFile(String content) throws IOException {
		File file = new File("templateDiry");
		file.mkdirs();
		writeFile(new File(file,"test.txt"),content.replaceAll("\\@", "\\$"));
	}
	private void writeFile(File file, String content) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(content);
		fileWriter.close();
	}
	public void setListTo(String key, List<String> list) {
		setDynamicVariable(key, list);
	}
	public void setNamedListTo(String key, List<String> list) {
		List<Person> personList = new ArrayList<Person>();
		for (String name : list)
			personList.add(new Person(name));
		setDynamicVariable(key, personList);
	}
	
	public static class Person {
		private String name;

		public String getName() {
			return name;
		}
		public Person(String name) {
			this.name = name;
		}
	}
}
