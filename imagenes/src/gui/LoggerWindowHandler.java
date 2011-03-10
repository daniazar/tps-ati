package gui;

import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;

/**
 * Demo of how to log into window.
 * 
 * LogRecord r = new LogRecord(Level.WARNING, "The Handler publish method...");
 * LoggerWindowHandler.getInstance().publish(r);
 * 
 * Logger logger = Logger.getLogger("Window");
 * logger.addHandler(LoggerWindowHandler.getInstance());
 * 
 * logger.info("Hello from WindowHandler...");
 * 
 */
public class LoggerWindowHandler extends Handler {
	// the window to which the logging is done
	private LogFrame window = null;

	// the singleton instance
	private static LoggerWindowHandler handler = null;

	/**
	 * private constructor, preventing initialization
	 */
	private LoggerWindowHandler() {
		configure();
		//if (window == null)
		//	window = new LogFrame(pane, "Console", 500, 200);
	}

	/**
	 * The getInstance method returns the singleton instance of the
	 * WindowHandler object It is synchronized to prevent two threads trying to
	 * create an instance simultaneously. @ return WindowHandler object
	 */

	public static synchronized LoggerWindowHandler getInstance() {

		if (handler == null) {
			handler = new LoggerWindowHandler();
		}
		return handler;
	}

	public static LogFrame getInternalFrame(JDesktopPane pane) {

		if (handler == null) {
			handler = new LoggerWindowHandler();
		}
		if(handler.window ==  null)
			handler.window = new LogFrame(pane, "Console", 200, 200);
		
		return handler.window;
	}
	
	public static Logger getLogger(String name) {
		Logger log = Logger.getLogger(name);
		if (handler == null) {
			handler = new LoggerWindowHandler();
		}
		log.addHandler(handler);
		return log;
	}

	/**
	 * This method loads the configuration properties from the JDK level
	 * configuration file with the help of the LogManager class. It then sets
	 * its level, filter and formatter properties.
	 */
	private void configure() {
		LogManager manager = LogManager.getLogManager();
		String className = this.getClass().getName();
		String level = manager.getProperty(className + ".level");
		String filter = manager.getProperty(className + ".filter");
		String formatter = manager.getProperty(className + ".formatter");

		// accessing super class methods to set the parameters
		setLevel(level != null ? Level.parse(level) : Level.INFO);
		setFilter(makeFilter(filter));
		setFormatter(makeFormatter(formatter));

	}

	/**
	 * private method constructing a Filter object with the filter name.
	 * 
	 * @param filterName
	 *            the name of the filter
	 * @return the Filter object
	 */
	private Filter makeFilter(String filterName) {
		
		Class<?> c = null;
		Filter f = null;
		try {
			c = Class.forName(filterName);
			f = (Filter) c.newInstance();
		} catch (Exception e) {
			/*System.out.println("There was a problem to load the filter class: "
					+ filterName);*/
		}
		return f;
	}

	/**
	 * private method creating a Formatter object with the formatter name. If no
	 * name is specified, it returns a SimpleFormatter object
	 * 
	 * @param formatterName
	 *            the name of the formatter
	 * @return Formatter object
	 */
	private Formatter makeFormatter(String formatterName) {
		Class<?> c = null;
		Formatter f = null;

		try {
			c = Class.forName(formatterName);
			f = (Formatter) c.newInstance();
		} catch (Exception e) {
	     f = new Formatter() {

	            @Override
	            public String format(LogRecord arg0) {
	                StringBuilder b = new StringBuilder();
	                Level l =arg0.getLevel();
	                b.append(arg0.getLevel());
	                b.append(" ");
	                if(!l.equals(Level.INFO))
	                {
	                	b.append("in file " +arg0.getSourceClassName());
		                b.append(" at method ");
		                b.append(arg0.getSourceMethodName());
		                b.append(" ");		                	
	                }
	                b.append(arg0.getMessage());
	                b.append(System.getProperty("line.separator"));
	                return b.toString();
	            }

	        };
		}
		return f;
	}

	/**
	 * This is the overridden publish method of the abstract super class
	 * Handler. This method writes the logging information to the associated
	 * Java window. This method is synchronized to make it thread-safe. In case
	 * there is a problem, it reports the problem with the ErrorManager, only
	 * once and silently ignores the others.
	 * 
	 * @record the LogRecord object
	 * 
	 */
	public synchronized void publish(LogRecord record) {
		String message = null;
		// check if the record is loggable
		if (!isLoggable(record))
			return;
		try {
			message = getFormatter().format(record);
		} catch (Exception e) {
			reportError(null, e, ErrorManager.FORMAT_FAILURE);
		}

		try {
			if(window != null)
				window.showInfo(message);
		} catch (Exception ex) {
			reportError(null, ex, ErrorManager.WRITE_FAILURE);
		}

	}

	public void close() {
	}

	public void flush() {
	}
}
