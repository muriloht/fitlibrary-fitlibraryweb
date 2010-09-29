package fitlibrary.ws.recorder;

import java.io.IOException;

public class RecordingWebServicesFixture {
	private WebServicesRecorder recorder;
	
	public boolean startRecorderWithPropertiesAtAddingResultsToInFolderWithSoapVersion(String propertyFileName, 
			final String recording, String resultsFolder, String soapVersion) throws IOException {
		if (recorder != null)
			stopRecorder();
		RecordingFolderSelector recordingFolderSelector = new RecordingFolderSelector() {
			@Override
			public String selectFileName() {
				return recording;
			}
		};
		recorder = new WebServicesRecorder(propertyFileName,resultsFolder,soapVersion, recordingFolderSelector);
		return true;
	}
	public boolean stopRecorder() throws IOException {
		if (recorder != null)
			recorder.stop();
		recorder = null;
		return true;
	}
}
