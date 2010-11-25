/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.definedactions;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import fitlibrary.DoFixture;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.FixturingLogger;

public class GreaseMonkeyDefinedActionLister extends DoFixture {
	static Logger logger = FixturingLogger.getLogger(GreaseMonkeyDefinedActionLister.class);

	public boolean showAndListDefinedActionsForGreaseMonkey(
			File pathToDefinedActionsDir) {

		StringBuilder builder = new StringBuilder();
		GreaseMonkeyDefinedActionLister lister = new GreaseMonkeyDefinedActionLister();
		try {
			for (String line : lister
					.listDefinedActions(pathToDefinedActionsDir)) {
				builder.append(line);
				builder.append("<br/>");
			}

			showAfterTable(builder.toString());
			return true;
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
	}

	private boolean isSpiderFixtureCommand(String action) {
		return (action.equals("click") || action.equals("get url"));
	}

	@SuppressWarnings("rawtypes")
	public List<String> listDefinedActions(File pathToDefinedActionsDir)
			throws IOException {
		List<String> returnList = new ArrayList<String>();

		String[] extensions = {"txt"};
		Iterator<File> i = createDirectoryIterator(extensions,
				pathToDefinedActionsDir);

		while (i.hasNext()) {
			File file = i.next();
			Iterator l = createLineListerForFile(file);

			boolean nextLineIsSignature = true;
			while (l.hasNext()) {
				String line = (String) l.next();
				line = line.trim();

				if (line.startsWith("!contents"))
					break;

				if (line.length() == 0 || line.startsWith("#"))
					continue;

				if (line.startsWith("----")) {
					nextLineIsSignature = true;
					continue;
				}

				if (nextLineIsSignature) {
					line = extractDefinedActionNameFromTable(line);

					if (line == null) {
						continue;
					}
					nextLineIsSignature = false;

					if (isSpiderFixtureCommand(line)) {
						continue;
					}

					String path = convertFilePathToFitnesseLink(file);

					// definedActions['set one way'] =
					// "/IsBook.ActionDefinitions.SearchForFlight.SetOneWay";
					String javaScript = MessageFormat.format(
							"definedActions[''{0}''] = \"{1}\";", line, path);

					if (returnList.contains(javaScript)) {
						System.err
								.println("note: duplicate defiend action (probably due to same name with multiple parameters), already generated: "
										+ javaScript);
					}

					returnList.add(javaScript);
				} else {
					continue;
				}
			}
		}

		return returnList;
	}

	private String extractDefinedActionNameFromTable(String lineInitial) {
		String line = lineInitial;
		line = line.replace("'''", "");
		line = line.replace("''", "");
		int posofSecondBar = line.indexOf("|", 1);
		if (line.startsWith("|") || posofSecondBar > 0) {
			line = line.substring(1, posofSecondBar).toLowerCase();
			return line.trim();
		}

		return null;
	}

	private String convertFilePathToFitnesseLink(File file) {
		String path = file.getPath();
		int indexOfFitNesseRoot = path.indexOf("FitNesseRoot");
		int indexOfContent = path.indexOf("\\content.txt");
		
		if (indexOfContent == -1 || indexOfFitNesseRoot == -1)
			throw new FitLibraryException("Cannot find either FitNesseRoot or content.txt in file path ["+path+"].");
			
		path = path.substring(indexOfFitNesseRoot + 13, indexOfContent);
		path = path.replace('\\', '.');
		path = "/" + path;
		return path;
	}

	@SuppressWarnings("rawtypes")
	protected Iterator createLineListerForFile(File file) throws IOException {
		return FileUtils.lineIterator(file);
	}

	@SuppressWarnings("unchecked")
	protected Iterator<File> createDirectoryIterator(String[] extensions,
			File pathToDefinedActionsDir) {
		return FileUtils
				.iterateFiles(pathToDefinedActionsDir, extensions, true);
	}
}
