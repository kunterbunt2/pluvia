package de.bushnaq.abdalla.engine.util.logger;

public class LoggerFactory {

	public static Logger getLogger(Class<?> class1) {
		return new Logger(class1.getName());
	}

//	public static Class<?> getCallingClass() {
//		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
//	}

}
