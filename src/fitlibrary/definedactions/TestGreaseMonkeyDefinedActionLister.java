/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.definedactions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestGreaseMonkeyDefinedActionLister {
	private Map<File, List<String>> fakeFilesAndContent = new HashMap<File, List<String>>();
	
	File fakeLocatioWillNotBeUsed = new File("c:\\zzzzzzz");
	
	@SuppressWarnings("unchecked")
	public GreaseMonkeyDefinedActionLister createListerWithFakeFilesAndContent() {
		GreaseMonkeyDefinedActionLister lister = new GreaseMonkeyDefinedActionLister() {

			@Override
			protected Iterator<File> createDirectoryIterator(String[] extensions, File pathToDefinedActionsDir) {
				return fakeFilesAndContent.keySet().iterator();
			}
			
			@Override
			protected Iterator createLineListerForFile(File file) throws IOException {
				return fakeFilesAndContent.get(file).iterator();
			}
		};
		
		return lister;
	}
	
	private void fakeFileAndContent(File file, String... content) {
		List<String> fakeContent = new ArrayList<String>();
		
		for (String line: content) {
			fakeContent.add(line);
		}
		
		fakeFilesAndContent.put(file, fakeContent);
	}
	
	@Test
	public void oneFileOneDefinedAction() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"|set phone|",
		        "|some content");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(1));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertThat(list.size(), is(1));
	}
	
	@Test
	public void oneFileTwoDefinedActions() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"|set phone|",
		        "|some content",
		        "----",
		        "|second action|",
		        "|//textof|foo|");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(2));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertEquals("definedActions['second action'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(1));
	}
	
	@Test
	public void twoFilesOneDefinedActionEach() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"|set phone|",
				"",
		        "|some content");
		
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetFax\\content.txt"), 
		        "|set fax|",
		        "|//textof|foo|");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(2));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertEquals("definedActions['set fax'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetFax\";",list.get(1));
	}
	
	@Test
	public void pageNoTablesIgnored() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\CardPaymentFee\\PageHeader\\content.txt"), 
				"These are the aAction definitions - the actual tests can be found at <IsBook.CardPaymentFee",
		        "----");  
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(0));
	}
	
	@Test
	public void whiteSpaceIgnored() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"",
				"|set phone|  ",
				"",
		        "|some content|",
		        "",
		        "----",
		        "",
		        " | second action |",  // <-- note: white space either side of action name too
		        "|//textof|foo|",
		        "----");  // <-- ignore this final line too
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(2));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertEquals("definedActions['second action'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(1));
	}
	
	@Test
	public void actionNamesConvertedToLowerCase() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"|Set Phone|",
		        "|some content");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(1));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
	}
	
	@Test  // we could get clever and distinguish defined actions by the # of params but just index first cell at the moment
	public void definedActionWithParameters() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"|set phone|PHONENUMBER|",
		        "|some content");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(1));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
	}
	
	@Test
	public void commentLinesIgnored() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"#|old name|",
				"|set phone|",
				"#----",
		        "|some content|",
		        "",
		        "----",
		        " |second action|",
		        "|//textof|foo|");  
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(2));
		assertEquals("definedActions['set phone'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertEquals("definedActions['second action'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(1));
	}
	
	@Test
	public void fitNesseFormattingIgnored() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\SetPhone\\content.txt"), 
				"''|italic action|''",
				"",
		        "|some content|",
		        "",
		        "----",
		        "'''|bold action|'''",
		        "|//textof|foo|");  
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(2));
		assertEquals("definedActions['italic action'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(0));
		assertEquals("definedActions['bold action'] = \"/IsBook.ActionDefinitions.TravellerDetails.SetPhone\";",list.get(1));
	}
	
	@Test
	public void suitePagesIgnored() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\content.txt"), 
				"!contents -R");  
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(0));
	}
	
	@Test  
	public void someDefinedActionsStartWithSpiderFixtureCommandsRemoved() throws IOException {
		fakeFileAndContent(new File("c:\\working\\mydir\\FitNesseRoot\\IsBook\\ActionDefinitions\\TravellerDetails\\content.txt"), 
				"|''get url''|URL|''giving title''|TITLE|",
				"",
				"|''get url''|URL|\r\n", 
				"",
				"|''title''|'''becomes'''|TITLE|",
				"----",
				"|''click''|URL|''giving title''|TITLE|",
				"",
				"|''click''|URL|",
				"",
				"|''title''|'''becomes'''|TITLE|");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();

		// 'get url' and 'click' are spider fixture commands - this hardcoding of spider fixture names to exclude can be removed when we support multi parameter defined actions
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(0));
	}
	
	@Test
	public void differentFileLocationAndProject() throws IOException {
		fakeFileAndContent(new File("c:\\johnsDir\\mydir\\FitNesseRoot\\SilverStripeCms\\BafoonedActions\\Zip\\content.txt"), 
				"|set phone|",
		        "|some content");
		
		GreaseMonkeyDefinedActionLister lister = createListerWithFakeFilesAndContent();
		
		List<String> list = lister.listDefinedActions(fakeLocatioWillNotBeUsed);
		assertThat(list.size(), is(1));
		assertEquals("definedActions['set phone'] = \"/SilverStripeCms.BafoonedActions.Zip\";",list.get(0));
		assertThat(list.size(), is(1));
	}
}
