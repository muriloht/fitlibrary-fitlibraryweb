/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.email;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.BodyTerm;
import javax.mail.search.SubjectTerm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import fitlibrary.differences.LocalFile;
import fitlibrary.log.Log;
import fitlibrary.traverse.Traverse;

public class ElectronicMail {
	private static int port = 143;

	private Store store;
	private Folder selectedFolder;
	private int waitTimeSeconds = 0;
	private final FetchProfile fetchProfile;
	private Message selectedMessage;

	public ElectronicMail() {
		fetchProfile = new FetchProfile();
		fetchProfile.add(FetchProfile.Item.ENVELOPE);
		fetchProfile.add(FetchProfile.Item.FLAGS);
		fetchProfile.add("X-Mailer");
	}

	public void connectUsingHostAndProtocolWithUserNameAndPassword(String host,
			String protocol, String username, String password) {
		Session session = Session.getInstance(System.getProperties());

		try {
			store = session.getStore(protocol);
			store.connect(host, port, username, password);
		} catch (NoSuchProviderException e) {
			Log.logAndThrow(e);
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}
	}

	public void waitUpToSecondsForMessageToArrive(int seconds) {
		this.waitTimeSeconds = seconds;
	}

	public boolean checkMessageBodyContains(String bodyPart) {
		if (selectedMessage == null) {
			return false;
		}

		try {
			return selectedMessage.match(new BodyTerm(bodyPart));
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}

		return false;
	}

	public boolean selectMessageWithSubjectMatching(String subjectPart) {
		selectedMessage = null;
		Message[] msgs = null;
		SubjectTerm subjectTerm = new SubjectTerm(subjectPart);

		try {
			for (int waitingFor = 0; waitingFor < waitTimeSeconds; waitingFor++) {
				msgs = selectedFolder.getMessages();
				selectedFolder.fetch(msgs, fetchProfile);

				for (Message message : msgs) {
					if (message.match(subjectTerm)) {
						selectedMessage = message;
						return true;
					}
				}
				sleepForOneSecond();
			}
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}

		return false;
	}

	private void sleepForOneSecond() {
		try {
			Thread.sleep(1 * 1000);
		} catch (InterruptedException e) {
			//
		}
	}

	public boolean openFolder(String folderName) {
		try {
			selectedFolder = store.getDefaultFolder();
			selectedFolder = selectedFolder.getFolder(folderName);
			selectedFolder.open(Folder.READ_WRITE);

			return true;
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}
		return false;
	}

	public boolean hasAttachment(String attachmentFileName) {
		if (selectedMessage == null) {
			return false;
		}

		return findAttachment(attachmentFileName) != null;
	}

	private Part findAttachment(String attachmentFileName) {
		attachmentFileName = attachmentFileName.toLowerCase();

		try {
			Multipart multipart = (Multipart) selectedMessage.getContent();

			for (int i = 0, n = multipart.getCount(); i < n; i++) {
				Part part = multipart.getBodyPart(i);

				String disposition = part.getDisposition();

				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT) || (disposition
								.equals(Part.INLINE))))) {
					if (part.getFileName().toLowerCase().equals(
							attachmentFileName)) {
						return part;
					}
				}
			}
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		} catch (IOException e) {
			Log.logAndThrow(e);
		}
		return null;
	}

	public boolean downloadAttachmentToFile(String attachmentFileName,
			String targetFileName) throws IOException, MessagingException {
		if (selectedMessage == null) {
			return false;
		}

		Part part = findAttachment(attachmentFileName);

		if (part == null) {
			throw new FileNotFoundException(attachmentFileName);
		}

		LocalFile globalFile = Traverse.getGlobalFile(targetFileName);

		FileUtils.writeByteArrayToFile(globalFile.getFile(), IOUtils
				.toByteArray(part.getInputStream()));

		return true;
	}

	public boolean deleteMessage() {
		if (selectedMessage == null) {
			return false;
		}

		try {
			selectedMessage.setFlag(Flags.Flag.DELETED, true);
			selectedFolder.expunge();
			return selectedMessage.isExpunged();
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}

		return true;
	}

	public boolean disconnect() {
		try {
			if (selectedFolder != null) {
				selectedFolder.close(false);
			}
			if (store != null) {
				store.close();
			}
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}
		return true;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		disconnect();
	}
}
