package logic;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;

public class Validator {
	
	private static final String KEYWORD_ADD = "add";
	private static final String KEYWORD_UPDATE = "update";
	private static final String KEYWORD_DELETE = "delete";
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_FROM = "from";
	private static final String KEYWORD_TO = "to";
	private static final String KEYWORD_EVERY = "every";
	private static final String KEYWORD_UNTIL = "until";
	private static final String KEYWORD_PRIORITY = "priority";
	
	private static final int PRIORITY_LOW = 0;
	private static final int PRIORITY_MEDIUM = 1;
	private static final int PRIORITY_HIGH = 2;
	private static final int PRIORITY_DEFAULT = PRIORITY_MEDIUM;
	
	public Object parseCommand(String fullCommand) {
		
		Object obj = null;
		
		Scanner sc = new Scanner(fullCommand);
		
		String command = sc.next();
		
		if(command.equalsIgnoreCase(KEYWORD_ADD)) {
			String remainingCommand = sc.nextLine();
			obj = parseAddCommand(remainingCommand);
		
		} else if(command.equalsIgnoreCase(KEYWORD_UPDATE)) {
			//obj = parseUpdateCommand() : return Error object (contain isSuccess)
		
		} else if(command.equalsIgnoreCase(KEYWORD_DELETE)) {
			//obj = parseDeleteCommand() : return Error object (contain isSuccess)
		
		} else {
			sc.close();
			throw new UnsupportedOperationException("Unrecognized command.");
		}
		
		sc.close();
		
		return obj;
	}
	
	private Task parseAddCommand(String remainingCommand) {
		
		Task task = null;
		Scanner sc = new Scanner(remainingCommand);
		
		boolean isNormalTask = false;
		boolean isDeadlineTask = false;
		boolean isRecurrenceTask = false;
		boolean isFloatingTask = false;
		
		String taskDesc = "";
		
		while(sc.hasNext()) {
			
			String currentWord = sc.next();
			
			if(currentWord.equalsIgnoreCase(KEYWORD_FROM)) {
				
				isNormalTask = true;
				
				String remainingDate = sc.nextLine();
				task = createNormalTask(taskDesc, remainingDate);
				
				break;
				
			} else if(currentWord.equalsIgnoreCase(KEYWORD_BY)) {
				
				isDeadlineTask = true;
				
				String remainingDate = sc.nextLine();
				task = createDeadlineTask(taskDesc, remainingDate);
				
				break;
				
			} else if(currentWord.equalsIgnoreCase(KEYWORD_EVERY)) {
				
				isRecurrenceTask = true;
				
				String remainingDate = sc.nextLine();
				task = createRecurrenceTask(taskDesc, remainingDate);
				
				break;
				
			} else if(currentWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
				
				isFloatingTask = true;
				
				break;
			}
				else {
				taskDesc += " " + currentWord;
			}
		}
		
		if(isFloatingTask || 
				(!isNormalTask && !isDeadlineTask && !isRecurrenceTask)) {
			
			String remainingPriority = null;
			try {
				remainingPriority = sc.nextLine();
			} catch(NoSuchElementException e) {
				System.err.println("No priority found for floating task.");
			}
			
			task = createFloatingTask(taskDesc, remainingPriority);
		}
		
		sc.close();
		return task;
	}
	
	private NormalTask createNormalTask(String taskDesc, String remainingDate) {

		NormalTask task = null;
		Scanner sc = new Scanner(remainingDate);
		
		String startDateString = null;
		String endDateString = null;
		int priority = PRIORITY_DEFAULT;
		
		boolean isEndDate = false;
		boolean isPriority = false;
		
		while(sc.hasNext()) {
			
			String currentWord = sc.next();
			
			if(!isEndDate && !isPriority) {
			
				if(currentWord.equalsIgnoreCase(KEYWORD_TO)) {
					isEndDate = true;
				} else {
					startDateString += " " + currentWord;
				}
			} else if(isEndDate && !isPriority){
				
				if(currentWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
					isPriority = true;
				} else {
					endDateString += " " + currentWord;
				}
			} else if(isPriority) {
				try {
					priority = Integer.parseInt(currentWord);
					
				} catch(NumberFormatException e) {
					System.err.println("createNormalTask: Cannot parse priority.");
				}
			}
		}
		
		Date fromDate = parseStringToDate(startDateString);
		Date toDate = parseStringToDate(endDateString);
		
		task = new NormalTask(taskDesc, priority, fromDate, toDate);

		sc.close();
		return task;
	}

	private DeadlineTask createDeadlineTask(String taskDesc, String remainingDate) {
		
		DeadlineTask task = null;
		Scanner sc = new Scanner(remainingDate);
		
		String deadlineDateString = null;
		int priority = PRIORITY_DEFAULT;
		
		boolean isPriority = false;
		
		while(sc.hasNext()) {
			
			String currentWord = sc.next();
			
			if(!isPriority) {
				if(currentWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
					isPriority = true;
					
				} else {
					deadlineDateString += " " + currentWord;
				}
				
			} else {
				try {
					priority = Integer.parseInt(currentWord);
					
				} catch(NumberFormatException e) {
					System.err.println("createDeadlineTask: Cannot parse priority.");
				}
			}
		}
		
		Date deadlineDate = parseStringToDate(deadlineDateString);

		task = new DeadlineTask(taskDesc, priority, deadlineDate);
		
		sc.close();
		return task;
	}
	
	private RecurrenceTask createRecurrenceTask(String taskDesc, String remainingDate) {
		
		RecurrenceTask task = null;
		Scanner sc = new Scanner(remainingDate);
		
		String startRecurrenceDateString = null;
		String endRecurrenceDateString = null;
		int priority = PRIORITY_DEFAULT;
		
		boolean isEndRecurrenceDate = false;
		boolean isPriority = false;
		
		while(sc.hasNext()) {
			
			String currentWord = sc.next();
			
			if(!isEndRecurrenceDate && !isPriority) {
				if(currentWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
					isPriority = true;
					
				} else if(currentWord.equalsIgnoreCase(KEYWORD_TO)) {
					isEndRecurrenceDate = true;
					
				} else {
					startRecurrenceDateString += " " + currentWord;
				}
				
			} else if(isEndRecurrenceDate && !isPriority) {
				if(currentWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
					isPriority = true;
					
				} else {
					endRecurrenceDateString += " " + currentWord;
				}
				
			} else if(isPriority) {
				try {
					priority = Integer.parseInt(currentWord);
					
				} catch(NumberFormatException e) {
					System.err.println("createRecurrenceTask: Cannot parse priority.");
				}
			}
		}
		
		Date startRecurrenceDate = parseStringToDate(startRecurrenceDateString);
		Date endRecurrenceDate = parseStringToDate(endRecurrenceDateString);

		task = new RecurrenceTask(taskDesc, priority, startRecurrenceDate, endRecurrenceDate);
		
		sc.close();
		return task;
	}
	
	private FloatingTask createFloatingTask(String taskDesc, String remainingPriority) {
		
		FloatingTask task = null;
		
		int priority = PRIORITY_DEFAULT;
		
		if(remainingPriority != null) {
			
			Scanner sc = new Scanner(remainingPriority);
			
			String currentWord = sc.next();
			
			try {
				priority = Integer.parseInt(currentWord);
				
			} catch(NumberFormatException e) {
				System.err.println("createFloatingTask: Cannot parse priority.");
			}
			
			sc.close();
		}
		
		task = new FloatingTask(taskDesc, priority);
		
		return task;
	}
	
	private static Date parseStringToDate(String dateInfo) {
		
		if(dateInfo == null) {
			return null;
			
		} else {
		
			Parser parser = new Parser();
			
			List<DateGroup> groups = parser.parse(dateInfo);
			DateGroup firstDate = groups.get(0);
			List<Date> dates = firstDate.getDates();
			
			return dates.get(0);
		}
	}
}
