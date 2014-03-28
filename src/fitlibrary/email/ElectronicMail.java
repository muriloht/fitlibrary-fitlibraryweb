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
	  AbstractMessageSelecter searchForwardsThroughMessagesUntilFound = new AbstractMessageSelecter(subjectPart) {
      
	    Message findMessage(SubjectTerm subjectTerm, Message[] msgs) throws MessagingException {
        for(Message msg: msgs){
          if (msg.match(subjectTerm)) {
            return msg;
          }
        }
        return null;
      }
		};
		
		return searchForwardsThroughMessagesUntilFound.select();
	}
	
	public boolean selectLastMessageWithSubjectMatching(String subjectPart) {
	  AbstractMessageSelecter searchBackwardsThroughMessagesUntilFound =  new AbstractMessageSelecter(subjectPart) {
	    
	    Message findMessage(SubjectTerm subjectTerm, Message[] msgs) throws MessagingException {
	      for(int i=msgs.length-1;i>=0;--i){
	        if (msgs[i].match(subjectTerm)) {
	          return msgs[i];
	        }
	      }
	      return null;
	    }
	  };
	  
	  return searchBackwardsThroughMessagesUntilFound.select();
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

	public int selectedFolderMessageCount() {
		try {
			return selectedFolder.getMessageCount();
		} catch (MessagingException e) {
			Log.logAndThrow(e);
		}
		// unreachable code
		return 0;
	}
  
	public boolean deleteMessagesRangedFromTo(int startMsg, int endMsg) {
		try {
			for (Message message : selectedFolder.getMessages(startMsg, endMsg)) {
				message.setFlag(Flags.Flag.DELETED, true);
			}

			selectedFolder.expunge();
		} catch (javax.mail.MessagingException e) {
			Log.logAndThrow(e);
			return false; // unreachable
		}

		return true;
	}
	
	public boolean hasAttachment(String attachmentFileName) {
		if (selectedMessage == null) {
			return false;
		}

		return findAttachment(attachmentFileName) != null;
	}

	private Part findAttachment(String attachmentFileNameInitial) {
		String attachmentFileName = attachmentFileNameInitial;
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

	public boolean downloadAttachmentToFile(String attachmentFileName, String targetFileName) throws IOException, MessagingException {
	  
	  if (selectedMessage == null) {
			return false;
		}

		Part part = findAttachment(attachmentFileName);

		if (part == null) {
			throw new FileNotFoundException(attachmentFileName);
		}

		return downloadPartToFile(targetFileName, part);
	}
	
	public boolean downloadMessageToFile(String targetFileName) throws IOException, MessagingException {
		if (selectedMessage == null) {
			return false;
		}
		
		return downloadPartToFile(targetFileName, (Part) selectedMessage);
	}
	
	private boolean downloadPartToFile(String targetFileName, Part part) throws IOException, MessagingException {
	    LocalFile globalFile = Traverse.getGlobalFile(targetFileName);
	    FileUtils.writeByteArrayToFile(globalFile.getFile(), IOUtils.toByteArray(part.getInputStream()));
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
	
  abstract class AbstractMessageSelecter   {
    private String subjectPart;

    abstract Message findMessage(SubjectTerm subjectTerm, Message[] msgs) throws MessagingException;

    public AbstractMessageSelecter(String subjectPart) {
      this.subjectPart = subjectPart;
    }

    private void sleepForOneSecond() {
      try {
        Thread.sleep(1 * 1000);
      }
      catch (InterruptedException e) { }
    }

    public boolean select() {
      selectedMessage = null;
      Message[] msgs = null;
      SubjectTerm subjectTerm = new SubjectTerm(subjectPart);
      try {
        for (int waitingFor = 0; waitingFor < waitTimeSeconds; waitingFor++) {
          msgs = selectedFolder.getMessages();
          selectedFolder.fetch(msgs, fetchProfile);

          selectedMessage = findMessage(subjectTerm, msgs);

          if (selectedMessage != null) {
            return true;
          }

          sleepForOneSecond();
        }
      }
      catch (MessagingException e) {
        Log.logAndThrow(e);
      }

      return false;
    }
  }
  
  public String getMessageContentAsString() throws IOException, MessagingException{
	  byte[] messageContentBytes = IOUtils.toByteArray(this.selectedMessage.getInputStream());
	  return new String(messageContentBytes);
  }
}
