package fitlibrary.ws.recorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueRecordingFolderSelector implements RecordingFolderSelector {
	@Override
	public String selectFileName() {
		String fileName = formattedDateTime();
		String fullFileName = fileName;
		if (new File(fullFileName).exists()) {
			for (int i = 1; i < 10000; i++) {
				String logFileName = fileName+"-"+i;
				if (!new File(logFileName).exists()) {
					return logFileName;
				}
			}
		}
		return fullFileName;
	}
	private static String formattedDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
	}
}
