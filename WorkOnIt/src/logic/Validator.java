package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import data.ConfigIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;

public class Validator {

	public static final String KEYWORD_ADD = "add";
	public static final String KEYWORD_UPDATE = "update";
	public static final String KEYWORD_DELETE = "delete";
	public static final String KEYWORD_RETRIEVE = "retrieve";
	public static final String KEYWORD_BY = "by";
	public static final String KEYWORD_FROM = "from";
	public static final String KEYWORD_TO = "to";
	public static final String KEYWORD_EVERY = "every";
	public static final String KEYWORD_PRIORITY = "priority";
	public static final String KEYWORD_UNDO = "undo";
	public static final String KEYWORD_COMPLETE = "complete";
	public static final String KEYWORD_CLEAR = "clear";
	public static final String KEYWORD_EXPORT = "export";

	public static final String KEYWORD_DAILY = "daily";
	public static final String KEYWORD_WEEKLY = "weekly";
	public static final String KEYWORD_MONTHLY = "monthly";
	public static final String KEYWORD_YEARLY = "yearly";
	public static final String KEYWORD_DEFAULT_OCCURENCE = KEYWORD_WEEKLY;

	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_MEDIUM = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_DEFAULT_PRIORITY = PRIORITY_MEDIUM;

	private Map<String, String> keywordFullMap = null;

	public Validator() {

		loadConfigFile();
	}

	public boolean validateKeyword(String keyword) {

		boolean isKeyword = false;

		if (keywordFullMap.containsKey(keyword)) {
			isKeyword = true;
		}

		return isKeyword;
	}

	public Object parseCommand(String fullCommand) {

		Object obj = null;
		Engine engineObj = new Engine();

		fullCommand = fullCommand.toLowerCase();
		Scanner sc = new Scanner(fullCommand);
		String commandInput = sc.next();
		String commandResolved = keywordFullMap.get(commandInput);

		if (commandResolved != null) {

			if (commandResolved.equalsIgnoreCase(KEYWORD_ADD)) {

				String remainingCommand = sc.nextLine();
				Task task = parseAddCommand(remainingCommand);

				Success status = engineObj.insertIntoFile(task);

				obj = status;

			} else if (commandResolved.equalsIgnoreCase(KEYWORD_UPDATE)) {
				// obj = parseUpdateCommand() : return Success object (contain
				// isSuccess)

			} else if (commandResolved.equalsIgnoreCase(KEYWORD_DELETE)) {
				// obj = parseDeleteCommand() : return Success object (contain
				// isSuccess)

			} else if (commandResolved.equalsIgnoreCase(KEYWORD_RETRIEVE)) {
				try {
					parseRetrieveCommand();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

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

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KEYWORD_FROM)) {

					isNormalTask = true;

					String remainingDate = sc.nextLine();
					task = createNormalTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_BY)) {

					isDeadlineTask = true;

					String remainingDate = sc.nextLine();
					task = createDeadlineTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_EVERY)) {

					isRecurrenceTask = true;

					String remainingDate = sc.nextLine();
					task = createRecurrenceTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {

					isFloatingTask = true;
					break;

				} else {
					taskDesc += " " + currentWord;
				}

			} else {
				taskDesc += " " + currentWord;
			}
		}

		if (isFloatingTask
				|| (!isNormalTask && !isDeadlineTask && !isRecurrenceTask)) {

			String remainingPriority = null;
			try {
				remainingPriority = sc.nextLine();
			} catch (NoSuchElementException e) {
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

		String startDateString = "";
		String endDateString = "";
		int priority = PRIORITY_DEFAULT_PRIORITY;

		boolean isEndDate = false;
		boolean isPriority = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isEndDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_TO)) {
						isEndDate = true;
					} else {
						startDateString += " " + currentWord;
					}
				} else {
					startDateString += " " + currentWord;
				}
			} else if (isEndDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
						isPriority = true;
					} else {
						endDateString += " " + currentWord;
					}
				} else {
					endDateString += " " + currentWord;
				}
			} else if (isPriority) {
				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					System.err
							.println("createNormalTask: Cannot parse priority.");
				}
			}
		}

		Date fromDate = parseStringToDate(startDateString);
		Date toDate = parseStringToDate(endDateString);

		task = new NormalTask(taskDesc, priority, fromDate, toDate);

		sc.close();
		return task;
	}

	private DeadlineTask createDeadlineTask(String taskDesc,
			String remainingDate) {

		DeadlineTask task = null;
		Scanner sc = new Scanner(remainingDate);

		String deadlineDateString = "";
		int priority = PRIORITY_DEFAULT_PRIORITY;

		boolean isPriority = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isPriority) {
				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
						isPriority = true;
					} else {
						deadlineDateString += " " + currentWord;
					}
				} else {
					deadlineDateString += " " + currentWord;
				}

			} else {
				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					System.err
							.println("createDeadlineTask: Cannot parse priority.");
				}
			}
		}

		Date deadlineDate = parseStringToDate(deadlineDateString);

		task = new DeadlineTask(taskDesc, priority, deadlineDate);

		sc.close();
		return task;
	}

	private RecurrenceTask createRecurrenceTask(String taskDesc,
			String remainingDate) {

		RecurrenceTask task = null;
		Scanner sc = new Scanner(remainingDate);

		String startRecurrenceDateString = "";
		String endRecurrenceDateString = "";
		int priority = PRIORITY_DEFAULT_PRIORITY;

		boolean isEndRecurrenceDate = false;
		boolean isPriority = false;

		String occurenceType = "";

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KEYWORD_DAILY)) {
					occurenceType = KEYWORD_DAILY;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_WEEKLY)) {
					occurenceType = KEYWORD_WEEKLY;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_MONTHLY)) {
					occurenceType = KEYWORD_MONTHLY;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_YEARLY)) {
					occurenceType = KEYWORD_YEARLY;
				}
			}

			if (!isEndRecurrenceDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
						isPriority = true;

					} else if (resolvedWord.equalsIgnoreCase(KEYWORD_TO)) {
						isEndRecurrenceDate = true;

					} else {
						startRecurrenceDateString += " " + currentWord;
					}
				} else {
					startRecurrenceDateString += " " + currentWord;
				}

			} else if (isEndRecurrenceDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
						isPriority = true;

					} else {
						endRecurrenceDateString += " " + currentWord;
					}
				} else {
					endRecurrenceDateString += " " + currentWord;
				}

			} else if (isPriority) {
				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					System.err
							.println("createRecurrenceTask: Cannot parse priority.");
				}
			}
		}

		Date startRecurrenceDate = parseStringToDate(startRecurrenceDateString);
		Date endRecurrenceDate = parseStringToDate(endRecurrenceDateString);

		if (occurenceType.isEmpty()) {
			task = new RecurrenceTask(taskDesc, priority, startRecurrenceDate,
					endRecurrenceDate);
		} else {
			task = new RecurrenceTask(taskDesc, priority, startRecurrenceDate,
					endRecurrenceDate, occurenceType);
		}

		sc.close();
		return task;
	}

	private FloatingTask createFloatingTask(String taskDesc,
			String remainingPriority) {

		FloatingTask task = null;

		int priority = PRIORITY_DEFAULT_PRIORITY;

		if (remainingPriority != null) {

			Scanner sc = new Scanner(remainingPriority);

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			try {
				if (resolvedWord != null) {
					priority = Integer.parseInt(resolvedWord);
				} else {
					priority = Integer.parseInt(currentWord);
				}

			} catch (NumberFormatException e) {
				System.err
						.println("createFloatingTask: Cannot parse priority.");
			}

			sc.close();
		}

		task = new FloatingTask(taskDesc, priority);

		return task;
	}

	private void loadConfigFile() {

		ConfigIO config = new ConfigIO();
		keywordFullMap = config.getFullKeywordMap();
	}

	private static Date parseStringToDate(String dateInfo) {

		if (dateInfo == null) {
			return null;

		} else {

			Parser parser = new Parser();

			List<DateGroup> groups = parser.parse(dateInfo);
			if (groups.isEmpty()) {
				return null;
			} else {
				DateGroup firstDate = groups.get(0);
				List<Date> dates = firstDate.getDates();

				return dates.get(0);
			}
		}
	}

	private void parseRetrieveCommand() throws IOException {
		Engine en = new Engine();

		// body (To read and determine what parameter to put into retrieve)
		// Take Note : Anis

		// ArrayList<Task> taskList = en.retrieveTask();

		// display Task.
		/*
		 * for (int i = 0; i < taskList.size(); i++) {
		 * System.out.println(taskList.get(i)); }
		 */

	}

}