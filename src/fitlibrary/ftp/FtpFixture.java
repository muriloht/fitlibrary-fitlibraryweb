/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

import fitlibrary.service.ServicingFixture;

public class FtpFixture extends ServicingFixture {
	private FTPClient ftp = new FTPClient();

	public boolean retrieveToFile(String ftpFileName, String localFileName)
			throws IOException {
		File localFile = new File(localFileName);
		FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
		ftp.retrieveFile(ftpFileName, localFileOutputStream);
		localFileOutputStream.close();
		return true;
	}
	public boolean storeLocalFileAs(String localFileName, String ftpFileName)
			throws IOException {
		File localFile = new File(localFileName);
		FileInputStream localFileInputStream = new FileInputStream(localFile);
		ftp.storeFile(ftpFileName, localFileInputStream);
		localFileInputStream.close();
		return true;
	}
	public void connect(String serverAddress, int port) throws SocketException,
			IOException {
		ftp.connect(serverAddress, port);
	}
	@Override
	protected Object getService() {
		return ftp;
	}
}
