package error;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private static Logger logger;
	FileHandler fh;  
	public Log()
	{

		logger= Logger.getLogger("MyLog");  
		try {  
			// This block configure the logger with handler and formatter  
			fh = new FileHandler("log.log");  
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);
			
		} catch (SecurityException exception) {  
			exception.printStackTrace();  
		} catch (IOException exception) {  
			exception.printStackTrace();  
		}  	
	}
	public static void addLog(LogType logType,String log)
	{
		switch(logType)
		{
			case INFO:
				logger.info(log);			
				break;
			case ALERT:
				logger.warning(log);
				break;
			case ERROR:
				logger.severe(log);
				break;
		}
	}
}
