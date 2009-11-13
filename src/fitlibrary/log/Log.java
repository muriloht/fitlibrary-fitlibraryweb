/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.log;

import java.io.PrintStream;

import fitlibrary.exception.FitLibraryException;

/**
 * Simple logger that just dumps to system out and err - which is captured in
 * FitNesse. Wrapped in a class so we can override later in tests or maybe turn
 * off individual logs
 */
public class Log {
	static PrintStream out;
	static PrintStream err;

	static boolean DEBUG_ON = false;

	static {
		out = System.out;
		err = System.out;
	}

	public static void setDebugging(boolean debugging) {
		DEBUG_ON = debugging;
	}
	
	public static void redirectErrorLoggingTo(PrintStream stream) {
		err = stream;
	}

	public static void info(String message) {
		out.println(message);
	}
	public static void info(Object object, String message) {
		info(identify(object) + message);
	}

	public static void error(String message) {
		err.println(message);
	}
	public static void error(Object object, String message) {
		error(identify(object) + message);
	}

	public static void error(String message, Throwable t) {
		error(message);
		error(t);
	}

	public static void error(Throwable t) {
		error(t.getMessage());
		t.printStackTrace(err);
	}

	public static void logAndThrow(String message, Throwable ex)
			throws FitLibraryException {
		error(message, ex);
		rethrow(ex);
	}

	public static void logAndThrow(Exception ex) throws FitLibraryException {
		error(ex);
		rethrow(ex);
	}

	public static void debug(String message) {
		if (DEBUG_ON) {
			out.println(message);
		}
	}
	public static void debug(Object object, String message) {
		debug(identify(object)+message);
	}
	
	private static void rethrow(Throwable ex) {
		if (ex instanceof FitLibraryException) {
			throw (FitLibraryException) ex;
		}
		throw new FitLibraryException((Exception) ex);
	}

	private static String identify(Object object) {
		String name = object.getClass().getName();
		int lastDot = name.lastIndexOf(".");
		if (lastDot >= 0)
			name = name.substring(lastDot+1);
		return name+": ";
	}

}
