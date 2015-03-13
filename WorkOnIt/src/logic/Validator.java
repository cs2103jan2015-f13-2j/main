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
import resource.KeywordConstant;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;

public class Validator {

	private Map<String, String> keywordFullMap = null;
	private ArrayList<Task> retrievedTaskList = null;
	private Task taskToRemove = null;

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

	public Success parseCommand(String fullCommand) {

		Success status = null;
		Engine engineObj = new Engine();

		fullCommand = fullCommand.toLowerCase();
		Scanner sc = new Scanner(fullCommand);
		String commandInput = sc.next();
		String commandResolved = keywordFullMap.get(commandInput);

		if (commandResolved != null) {

			if (commandResolved.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {

				String remainingCommand = sc.nextLine();
				status = parseAddCommand(remainingCommand);
				Task task;
				if (status.isSuccess()) {
					task = (Task) status.getObj();
					status = engineObj.addTask(task);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				String remainingCommand = sc.nextLine();

				status = parseUpdateCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				String remainingCommand = sc.nextLine();

				status = parseDeleteCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_RETRIEVE)) {
				String remainingCommand = sc.nextLine();

				status = parseRetrieveCommand(remainingCommand);

				retrievedTaskList = new ArrayList<Task>();

				retrievedTaskList = (ArrayList<Task>) status.getObj();

				System.out.println("No. of records: "
						+ retrievedTaskList.size());
				// temporary to show the retrieved list
				for (int i = 0; i < retrievedTaskList.size(); i++) {
					Task t = retrievedTaskList.get(i);
					System.out.println((i + 1) + ") Task Desc: "
							+ t.getTaskName() + "\t ; Task Type: "
							+ t.getClass());
				}
			}

		} else {
			sc.close();
			throw new UnsupportedOperationException("Unrecognized command.");
		}

		sc.close();

		return status;
	}

	private Success parseAddCommand(String remainingCommand) {

		Success status = null;
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
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)
						|| resolvedWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {

					isNormalTask = true;

					String remainingDate = sc.nextLine();
					status = createNormalTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_BY)) {

					isDeadlineTask = true;

					String remainingDate = sc.nextLine();
					status = createDeadlineTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_EVERY)) {

					isRecurrenceTask = true;

					String remainingDate = sc.nextLine();
					status = createRecurrenceTask(taskDesc, remainingDate);

					break;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {

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

			status = createFloatingTask(taskDesc, remainingPriority);
		}

		sc.close();
		
		if(status.isSuccess()) {
			retrievedTaskList = null;
		}
		
		return status;
	}

	private Success createNormalTask(String taskDesc, String remainingDate) {

		NormalTask task = null;
		Success status = null;
		Scanner sc = new Scanner(remainingDate);

		String startDateString = "";
		String endDateString = "";
		int priority = KeywordConstant.PRIORITY_DEFAULT_PRIORITY;

		boolean isEndDate = false;
		boolean isPriority = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isEndDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
						isEndDate = true;
					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
						isPriority = true;
					} else if (!(resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM) || resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_ON))) {
						// System.out.println(resolvedWord);
						startDateString += " " + currentWord;
					}
				} else {
					startDateString += " " + currentWord;
				}
			} else if (isEndDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
						isPriority = true;
					} else if (!(resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM) || resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_ON))) {
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
		// System.out.println(combinedDate);
		task = new NormalTask(taskDesc, priority, fromDate, toDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	private Success createDeadlineTask(String taskDesc, String remainingDate) {

		Success status = null;
		DeadlineTask task = null;
		Scanner sc = new Scanner(remainingDate);

		String deadlineDateString = "";
		int priority = KeywordConstant.PRIORITY_DEFAULT_PRIORITY;

		boolean isPriority = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isPriority) {
				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
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
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	private Success createRecurrenceTask(String taskDesc, String remainingDate) {

		Success status = null;
		RecurrenceTask task = null;
		Scanner sc = new Scanner(remainingDate);

		String startRecurrenceDateString = "";
		String endRecurrenceDateString = "";
		int priority = KeywordConstant.PRIORITY_DEFAULT_PRIORITY;

		boolean isEndRecurrenceDate = false;
		boolean isPriority = false;

		String occurenceType = "";

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_DAILY)) {
					occurenceType = KeywordConstant.KEYWORD_DAILY;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEKLY)) {
					occurenceType = KeywordConstant.KEYWORD_WEEKLY;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTHLY)) {
					occurenceType = KeywordConstant.KEYWORD_MONTHLY;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_YEARLY)) {
					occurenceType = KeywordConstant.KEYWORD_YEARLY;
				}
			}

			if (!isEndRecurrenceDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
						isPriority = true;

					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
						isEndRecurrenceDate = true;

					} else {
						startRecurrenceDateString += " " + currentWord;
					}
				} else {
					startRecurrenceDateString += " " + currentWord;
				}

			} else if (isEndRecurrenceDate && !isPriority) {

				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
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
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	private Success createFloatingTask(String taskDesc, String remainingPriority) {

		Success status = null;
		FloatingTask task = null;

		int priority = KeywordConstant.PRIORITY_DEFAULT_PRIORITY;

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
		status = new Success(task, true, null);

		return status;
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
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {

					String remainingDate = sc.nextLine();
					status = retrieveInBetween(remainingDate);

					break;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {

					String remainingDate = sc.nextLine();

					status = retrieveSingleDate(remainingDate);

					break;
				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ALL)) {

					status = retrieveAllDates();

					break;
				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {

					String remainingPriority = sc.nextLine();

					status = retrievePriority(remainingPriority);

					break;
				}

			} else {

				String remainingText = "";

				// re append chopped off text
				remainingText += currentWord;
				if (sc.hasNextLine()) {
					remainingText += sc.nextLine();
				}
				// if it's a date
				if (parseStringToDate(remainingText).size() > 0) {
					status = retrieveSingleDate(remainingText);
					break;
				} else {

					// retrieve using description
					status = retrieveTaskDesc(remainingText);
					break;
				}

			}

		}

		return status;
	}

	private Success retrieveTaskDesc(String searchString) {
		Success status = null;

		Engine engineObj = new Engine();

		status = engineObj.searchTask(searchString);

		return status;
	}

	private boolean isANumber(String text) {
		boolean isNumber;

		try {
			int temp = Integer.parseInt(text);
			isNumber = true;
		} catch (NumberFormatException e) {
			isNumber = false;
		}

		return isNumber;

	}

	private Success retrievePriority(String remainingPriority) {
		Scanner sc = new Scanner(remainingPriority);
		Engine engineObj = new Engine();
		Success status = null;
		int priority = -1;
		String startDateString = "";
		String endDateString = "";
		boolean isSingleDate = false;
		boolean isDoubleDate = false;

		boolean isPriorityResolved = false;
		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (isPriorityResolved == false) {
				if (resolvedWord != null) {
					if (isANumber(resolvedWord) == true) {
						priority = Integer.parseInt(resolvedWord);
						isPriorityResolved = true;
					}
				} else {
					if (isANumber(currentWord) == true) {
						priority = Integer.parseInt(currentWord);
						isPriorityResolved = true;
					}
				}

			} else {
				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_AT)) {
						startDateString = sc.nextLine();
						System.out.println(startDateString);
						isSingleDate = true;
					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {
						startDateString = sc.nextLine();
						System.out.println("keyword from: " + startDateString);
						endDateString = KeywordConstant.DATE_MAX;
						isSingleDate = true;
						isDoubleDate = true;
					}
				}
			}

			try {
				if (priority >= KeywordConstant.PRIORITY_MIN
						&& priority <= KeywordConstant.PRIORITY_MAX) {
					if (isSingleDate == false && isDoubleDate == false) {
						status = engineObj.retrieveTask(priority);
					} else if (isSingleDate == true && isDoubleDate == false) {
						Date fromDate = null;

						List<Date> dateList = parseStringToDate(startDateString);

						if (!dateList.isEmpty()) {
							fromDate = dateList.remove(0);
						}

						status = engineObj.retrieveTask(priority, fromDate);

					} else if (isSingleDate == true && isDoubleDate == true) {
						String combinedDate = startDateString + " to "
								+ endDateString;

						List<Date> dateList = parseStringToDate(combinedDate);

						Date fromDate = null;
						Date toDate = null;

						if (!dateList.isEmpty()) {
							fromDate = dateList.remove(0);

							if (!dateList.isEmpty()) {
								toDate = dateList.remove(0);
							}
						}

						status = engineObj.retrieveTask(priority, fromDate,
								toDate);

					}

				}

			} catch (IOException e) {
				System.err
						.println("retrievePriority: Retrieval fail (IO Exception).");
			}

		}

		return status;
	}

	private Success retrieveAllDates() {

		Engine engineObj = new Engine();
		Success status = null;
		status = engineObj.retrieveTask();

		return status;
	}

	private Success retrieveSingleDate(String remainingDate) {
		Scanner sc = new Scanner(remainingDate);
		Engine engineObj = new Engine();
		String dateString = "";
		Success status = null;
		boolean isInBetweenTime = false;
		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {
					isInBetweenTime = true;
				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
					isInBetweenTime = true;
					dateString += " " + currentWord;
				} else {
					dateString += " " + currentWord;
				}
			} else {
				dateString += " " + currentWord;
			}

		}

		try {
			if (isInBetweenTime == true) {
				String preparedStatement = KeywordConstant.KEYWORD_FROM
						+ dateString;
				// System.out.println("prep stmt: "+preparedStatement);
				status = retrieveInBetween(preparedStatement);

			} else {
				Date onDate = null;
				// System.out.println("dateString: "+dateString);
				List<Date> dateList = parseStringToDate(dateString);

				if (!dateList.isEmpty()) {
					onDate = dateList.remove(0);
				}

				status = engineObj.retrieveTask(onDate);
			}

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

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (!isEndDate) {
				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
						isEndDate = true;
					}
				} else {
					startDateString += " " + currentWord;
				}
			} else {

				endDateString += " " + currentWord;
			}
		}

		try {
			Date fromDate = null;
			Date toDate = null;

			List<Date> dateListFrom = parseStringToDate(startDateString);

			if (!dateListFrom.isEmpty()) {
				fromDate = dateListFrom.remove(0);
			}

			if (!endDateString.trim().equals("")) {

				String combinedDate = startDateString + " to" + endDateString;
				// System.out.println("startdatestring "+combinedDate);
				List<Date> dateList = parseStringToDate(combinedDate);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);

					if (!dateList.isEmpty()) {
						toDate = dateList.remove(0);
					}
				}

				status = engineObj.retrieveTask(fromDate, toDate);

			} else {
				// System.out.println("no end date");

				List<Date> dateListTo = parseStringToDate(KeywordConstant.DATE_MAX);

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

	private Success parseUpdateCommand(String remainingCommand) {

		Engine engineObj = new Engine();
		Success status = null;
		remainingCommand = remainingCommand.trim();

		try {
			int indexOffset = Integer.parseInt(remainingCommand) - 1;
			taskToRemove = retrievedTaskList.get(indexOffset);
			String taskDisplay = KeywordConstant.KEYWORD_UPDATE;
			taskDisplay += taskToRemove.toDisplay();

			status = new Success(taskDisplay, true, null);
			System.out.println("found : " + taskDisplay);

		} catch (NumberFormatException e) {
			Success statusTask = parseAddCommand(remainingCommand);
			if (statusTask.isSuccess()) {
				Task updatedTask = (Task) statusTask.getObj();
				status = engineObj.updateTask(updatedTask, taskToRemove);
				if (status.isSuccess()) {
					taskToRemove = null;
					retrievedTaskList = null;
				}
				// System.out.println("Task deleted");
				// System.out.println("updated task added : " +
				// updatedTask.getTaskName());
			}
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false,
					"Invalid index to update. Please enter a valid range.");
		} catch (NullPointerException e) {
			status = new Success(false,
					"No task to update. Please retrieve task first.");
		} catch (Exception e) {
			status = new Success(false,
					"Unknown error occured while parsing update command.");
		}

		return status;
	}

	private Success parseDeleteCommand(String index) {

		Success status = null;
		Engine engineObj = new Engine();
		index = index.trim();

		try {
			int indexOffset = Integer.parseInt(index) - 1;
			Task taskToRemove = retrievedTaskList.get(indexOffset);
			status = engineObj.deleteTask(taskToRemove);
			
			if(status.isSuccess()) {
				retrievedTaskList = null;
			}
			System.out.println("Deleted : \"" + taskToRemove.getTaskName()
					+ "\"");

		} catch (NumberFormatException e) {
			status = new Success(false,
					"Index is not a number. Please enter a numerical value.");
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false,
					"Invalid index to delete. Please enter a valid range.");
		} catch (NullPointerException e) {
			status = new Success(false,
					"No task to update. Please retrieve task first.");
		} catch (Exception e) {
			status = new Success(false,
					"Unknown error occured while parsing delete command.");
		}

		return status;
	}
}