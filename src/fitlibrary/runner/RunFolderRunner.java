/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.runner;

import java.io.IOException;
import java.text.ParseException;

public class RunFolderRunner {
    public static void main(String[] argsIgnore) throws ParseException, IOException {
    	String DIRY = "folderRunner/";
        run(new String[]{ DIRY+"tests", DIRY+"reports" });
    }
	private static void run(String[] args) throws ParseException, IOException {
		Report report = new FolderRunner(args).run();
        report.displayCounts();
	}
}
