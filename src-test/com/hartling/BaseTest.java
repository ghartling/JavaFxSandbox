package com.hartling;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;

public class BaseTest {

	private static final Logger logger = Logger.getLogger(BaseTest.class);

	@BeforeClass
	public static final void setUp() {
		// Setup database connections
		Properties properties = new Properties();
		try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("app.properties");) {
			properties.load(inputStream);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			properties.list(pw);

			logger.info(sw.getBuffer().toString());

		} catch (Throwable t) {
			t.printStackTrace();
			Assert.fail("Could not initialize JUnit environment: " + t.getMessage());
		}
	}

	protected static String getStackTrace(Exception e) {
		StackTraceElement[] elements = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage()).append("\n");
		for (StackTraceElement el : elements) {
			sb.append(el.toString()).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Load the properties file as a resource bundle
	 * 
	 * @param filename
	 * @return
	 */
	public static Properties loadPropertiesFromResourceBundle(String filename) {
		Properties result = new Properties();

		try {
			// Throws MissingResourceException on lookup failures:
			final ResourceBundle rb = ResourceBundle.getBundle(filename);
			System.out.println("");

			for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
				final String key = keys.nextElement();
				final String value = rb.getString(key);

				System.out.println("key = " + key + ", value = " + value);

				result.put(key, value);
			}
		} catch (MissingResourceException e) {
			System.out.println("Unable to load properties file.");
			e.printStackTrace();
		}

		return result;
	}

}
