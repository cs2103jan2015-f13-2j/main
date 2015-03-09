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
	public static final String KEYWORD_ON = "on";
	public static final String KEYWORD_FROM = "from";
	public static final String KEYWORD_SINCE = "since";
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

	public static final String DATE_MAX = "31 DECEMBER 9999";

	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_MEDIUM = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_DEFAULT_PRIORITY = PRIORITY_MEDIUM;

	private Map<String, String> keywordFullMap = null;

	private ArrayList<Task> retrievedTaskList = null;

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

	public boolean validateKeywordSequence(List<String> keywordList) {

		KeywordStructure keySequence = new KeywordStructure();
		boolean isValidSequence = keySequence.checkKeyword(keywordList);

		return isValidSequence;
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
				String remainingCommand = sc.nextLine();

				Success status = parseRetrieveCommand(remainingCommand);

				retrievedTaskList = (ArrayList<Task>) status.getObj();

				obj = status;

				// temporary to show the retrieved list
				for (Task t : retrievedTaskList) {
					System.out.println(t.getTaskName());
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
				if (resolvedWord.equalsIgnoreCase(KEYWORD_FROM)
						|| resolvedWord.equalsIgnoreCase(KEYWORD_ON)) {

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
					} else if (resolvedWord.equalsIgnoreCase(KEYWORD_PRIORITY)) {
						isPriority = true;
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

		String combinedDate = startDateString + " to " + endDateString;
		List<Date> dateList = parseStringToDate(combinedDate);

		Date fromDate = null;
		Date toDate = null;

		if (!dateList.isEmpty()) {
			fromDate = dateList.remove(0);

			if (!dateList.isEmpty()) {
				toDate = dateList.remove(0);
			}
		}

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

		List<Date> dateList = parseStringToDate(deadlineDateString);
		Date deadlineDate = null;

		if (!dateList.isEmpty()) {
			deadlineDate = dateList.remove(0);
		}

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

		String combinedDate = startRecurrenceDateString + " to "
				+ endRecurrenceDateString;
		List<Date> dateList = parseStringToDate(combinedDate);

		Date startRecurrenceDate = null;
		Date endRecurrenceDate = null;

		if (!dateList.isEmpty()) {
			startRecurrenceDate = dateList.remove(0);

			if (!dateList.isEmpty()) {
				endRecurrenceDate = dateList.remove(0);
			}
		}

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

	private static List<Date> parseStringToDate(String dateInfo) {

		List<Date> dates = new ArrayList<Date>();

		if (dateInfo != null) {

			Parser parser = new Parser();

			List<DateGroup> groups = parser.parse(dateInfo);

			if (!groups.isEmpty()) {
				DateGroup firstDate = groups.get(0);
				dates = firstDate.getDates();
			}
		}
		return dates;
	}

	private Success parseRetrieveCommand(String remainingCommand) {

		Success status = null;
		Scanner sc = new Scanner(remainingCommand);
		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				// inbetween
				if (resolvedWord.equalsIgnoreCase(KEYWORD_FROM)) {

					String remainingDate = sc.nextLine();
					status = retrieveInBetween(remainingDate);

					break;

				} else if (resolvedWord.equalsIgnoreCase(KEYWORD_ON)) {
					String remainingDate = sc.nextLine();
					status = retrieveSingleDate(remainingDate);

					break;
				} else {
					String remainingDate = sc.nextLine();
					status = retrieveSingleDate(remainingDate);

					break;
				}

			} else {
				// taskDesc += " " + currentWord;
			}

		}

		return status;
	}

	private Success retrieveSingleDate(String remainingDate) {
		Scanner sc = new Scanner(remainingDate);
		Engine engineObj = new Engine();
		String dateString = "";
		Success status = null;

		while (sc.hasNext()) {
			String currentWord = sc.next();
			dateString += " " + currentWord;
		}

		try {
			Date onDate = null;

			List<Date> dateList = parseStringToDate(dateString);

			if (!dateList.isEmpty()) {
				onDate = dateList.remove(0);
			}

			status = engineObj.retrieveTask(onDate);

		} catch (IOException e) {
			System.err.println("retrieveSingleDate: Retrieval fail.");
		}
		sc.close();
		return status;
	}

	private Success retrieveInBetween(String remainingDate) {
		Scanner sc = new Scanner(remainingDate);
		Engine engineObj = new Engine();
		String startDateString = "";
		String endDateString = "";

		Success status = null;
		boolean isEndDate = false;
		boolean noEndDate = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isEndDate) {
				if (resolvedWord != null) {
					if (resolvedWord.equalsIgnoreCase(KEYWORD_TO)) {
						isEndDate = true;
					}
				} else {
					startDateString += " " + currentWord;
				}
			} else {
				endDateString += " " + currentWord;
			}
		}
		System.out.println("Start " + startDateString);

		System.out.println("End " + endDateString);
		try {
			Date fromDate = null;

			List<Date> dateListFrom = parseStringToDate(startDateString);

			if (!dateListFrom.isEmpty()) {
				fromDate = dateListFrom.remove(0);
			}

			Date toDate = null;

			if (!endDateString.trim().equals("")) {
				toDate = parseStringToDate(endDateString).get(0);
				status = engineObj.retrieveTask(fromDate, toDate);
			} else {
				System.out.println("no end date");

				List<Date> dateListTo = parseStringToDate(DATE_MAX);

				if (!dateListTo.isEmpty()) {
					toDate = dateListTo.remove(0);
				}

				status = engineObj.retrieveTask(fromDate, toDate);
			}

		} catch (IOException e) {
			System.err.println("retrieveInBetween: Retrieval fail.");
		}

		sc.close();
		return status;
	}

}