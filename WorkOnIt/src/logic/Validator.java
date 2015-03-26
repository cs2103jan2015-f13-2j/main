package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import data.ConfigIO;
import resource.KeywordConstant;
import resource.Message;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.SuccessDisplay;
import entity.Task;
import entity.Success;

public class Validator {

	private Engine engine;
	private Map<String, String> keywordFullMap = null;
	private ArrayList<Task> retrievedTaskList = null;
	private Task taskToRemove = null;
	private int delayInMillisec = 3000; // 3 sec optimal delay time for process

	public Validator() {
		engine = new Engine();
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

		fullCommand = fullCommand.toLowerCase();
		Scanner sc = new Scanner(fullCommand);
		String commandInput = sc.next();
		String commandResolved = keywordFullMap.get(commandInput);

		if (commandResolved != null) {

			if (commandResolved.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = parseAddCommand(remainingCommand);
					Task task;
					if (status.isSuccess()) {
						task = (Task) status.getObj();
						status = engine.addTask(task);

						if (status.isSuccess()) {
							retrievedTaskList = null;
						}
					}
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = parseUpdateCommand(remainingCommand);

				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = parseDeleteCommand(remainingCommand);

					if (status.isSuccess()) {
						retrievedTaskList = null;
					}
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_RETRIEVE)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = parseRetrieveCommand(remainingCommand);

					retrievedTaskList = null;

					if (status.getObj() != null) {
						retrievedTaskList = (ArrayList<Task>) status.getObj();
					}

					// System.out.println("No. of records: "
					// + retrievedTaskList.size());
					// temporary to show the retrieved list
					// for (int i = 0; i < retrievedTaskList.size(); i++) {
					// Task t = retrievedTaskList.get(i);
					// System.out.println((i + 1) + ") Task Desc: "
					// + t.getTaskName() + "\t ; Task Type: "
					// + t.getClass());
					// }
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DISPLAY)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = parseDisplayCommand(remainingCommand);

					retrievedTaskList = null;

					if (status.getObj() != null) {
						retrievedTaskList = (ArrayList<Task>) status.getObj();
					}
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = doneCommand(remainingCommand);

					if (status.isSuccess()) {
						retrievedTaskList = null;
					}
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				if (sc.hasNext()) {
					String remainingCommand = sc.nextLine();
					remainingCommand = remainingCommand.trim();

					status = undoneCommand(remainingCommand);

					if (status.isSuccess()) {
						retrievedTaskList = null;
					}
				} else {
					status = new Success(false, Message.FAIL_PARSE_COMMAND);
				}

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDO)) {
				status = undoCommand();

				if (status.isSuccess()) {
					retrievedTaskList = null;
				}
			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_REDO)) {
				status = redoCommand();

				if (status.isSuccess()) {
					retrievedTaskList = null;
				}
			} else {
				status = new Success(false, Message.FAIL_PARSE_COMMAND);
			}

		} else {
			sc.close();
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		}

		sc.close();

		return status;
	}

	private Success parseAddCommand(String remainingCommand) {

		Success status = null;

		try {
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
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)
							|| resolvedWord
									.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {

						isNormalTask = true;

						String remainingDate = sc.nextLine();
						taskDesc = taskDesc.trim();
						status = createNormalTask(taskDesc, remainingDate);

						break;

					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_BY)) {

						isDeadlineTask = true;

						String remainingDate = sc.nextLine();
						taskDesc = taskDesc.trim();
						status = createDeadlineTask(taskDesc, remainingDate);

						break;

					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_EVERY)) {

						isRecurrenceTask = true;

						String remainingDate = sc.nextLine();
						taskDesc = taskDesc.trim();
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
					System.err.println(Message.ERROR_NO_PRIORITY_FOUND);
				}
				taskDesc = taskDesc.trim();
				status = createFloatingTask(taskDesc, remainingPriority);
			}

			sc.close();

		} catch (NullPointerException e) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} catch (Exception e) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		}

		return status;
	}

	private Success createNormalTask(String taskDesc, String remainingDate) {

		taskDesc = taskDesc.trim();

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
			currentWord = currentWord.trim();
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
					System.err.println(Message.FAIL_PARSE_PRIORITY);
				}
			}
		}

		String combinedDate = startDateString + " to " + endDateString;
		combinedDate = combinedDate.trim();
		List<Date> dateList = parseStringToDate(combinedDate);

		Date fromDate = null;
		Date toDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedStartDate = dateList.remove(0);
			fromDate = fixStartDate(unprocessedStartDate);

			if (!dateList.isEmpty()) {
				Date unprocessedEndDate = dateList.remove(0);
				toDate = fixEndDate(unprocessedEndDate);
			}
		}
		// System.out.println(combinedDate);
		task = new NormalTask(taskDesc.trim(), priority, fromDate, toDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	private Success createDeadlineTask(String taskDesc, String remainingDate) {

		taskDesc = taskDesc.trim();
		remainingDate = remainingDate.trim();

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
					System.err.println(Message.FAIL_PARSE_PRIORITY);
				}
			}
		}
		deadlineDateString = deadlineDateString.trim();
		List<Date> dateList = parseStringToDate(deadlineDateString);
		Date deadlineDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedDate = dateList.remove(0);
			deadlineDate = fixEndDate(unprocessedDate);
		}

		task = new DeadlineTask(taskDesc, priority, deadlineDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	private Success createRecurrenceTask(String taskDesc, String remainingDate) {

		taskDesc = taskDesc.trim();
		remainingDate = remainingDate.trim();

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
					System.err.println(Message.FAIL_PARSE_PRIORITY);
				}
			}
		}

		String combinedDate = startRecurrenceDateString + " to "
				+ endRecurrenceDateString;
		combinedDate = combinedDate.trim();
		List<Date> dateList = parseStringToDate(combinedDate);

		Date startRecurrenceDate = null;
		Date endRecurrenceDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedStartDate = dateList.remove(0);
			startRecurrenceDate = fixStartDate(unprocessedStartDate);

			if (!dateList.isEmpty()) {
				Date unprocessedEndDate = dateList.remove(0);
				endRecurrenceDate = fixEndDate(unprocessedEndDate);
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

		taskDesc = taskDesc.trim();

		Success status = null;
		FloatingTask task = null;

		int priority = KeywordConstant.PRIORITY_DEFAULT_PRIORITY;

		if (remainingPriority != null) {

			remainingPriority = remainingPriority.trim();
			Scanner sc = new Scanner(remainingPriority);

			if (sc.hasNext()) {

				String currentWord = sc.next();
				String resolvedWord = keywordFullMap.get(currentWord);

				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					System.err.println(Message.FAIL_PARSE_PRIORITY);
				}
			} else {
				System.err.println(Message.FAIL_PARSE_PRIORITY);
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

			List<DateGroup> groups = parser.parse(dateInfo.trim());

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
		String remainingText = "";
		String searchString = "";
		boolean isSingleDate = false;
		boolean isDoubleDate = false;
		boolean isPriority = false;
		boolean isAll = false;
		boolean isFrom = false;
		boolean isOn = false;
		boolean isDesc = false;
		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {

					remainingText = sc.nextLine();
					isSingleDate = true;
					isDoubleDate = true;
					isFrom = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {

					remainingText = sc.nextLine();
					isSingleDate = true;
					isDoubleDate = false;
					isOn = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ALL)) {
					isAll = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {

					remainingText = sc.nextLine();
					isPriority = true;

				}
			} else {
				searchString += " " + currentWord;

			}
		}

		String combinedSearch = searchString + " " + remainingText;
		// if captured by searchString and not remainingText, means the user
		// only typed a single date
		if (remainingText.trim().equals("")
				&& parseStringToDate(combinedSearch).size() > 0) {

			isSingleDate = true;
			isDoubleDate = false;
			remainingText = searchString;

		} else {

			// retrieve using description
			if (isOn == true) {
				combinedSearch = searchString.trim() + " on "
						+ remainingText.trim();
			} else if (isFrom == true) {
				combinedSearch = searchString.trim() + " from "
						+ remainingText.trim();
			}
			isDesc = true;

		}

		if (isAll == true) {
			status = retrieveAllDates();
		} else if (isPriority == true) {
			status = retrievePriority(remainingText.trim());
		} else if (isDesc == true) {
			status = retrieveTaskDesc(combinedSearch.trim());
		} else {
			if (isSingleDate == true && isDoubleDate == false) {
				status = retrieveSingleDate(remainingText.trim());
			} else if (isSingleDate == true && isDoubleDate == true) {
				status = retrieveInBetween(remainingText.trim());
			}
		}
		sc.close();
		return status;
	}

	private Success retrieveTaskDesc(String remainingText) {
		Success status = null;

		Scanner sc = new Scanner(remainingText);
		String searchString = "";

		String startDateString = "";
		String endDateString = "";
		boolean isDescResolved = false;
		boolean isSingleDate = false;
		boolean isDoubleDate = false;

		while (sc.hasNext()) {

			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_AT)) {
					startDateString = sc.nextLine();
					isDescResolved = true;
					isSingleDate = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {
					isDescResolved = true;
					isSingleDate = true;
					isDoubleDate = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
					endDateString = sc.nextLine();
					isDescResolved = true;
					isSingleDate = false;
					isDoubleDate = true;
				} else {
					startDateString += " " + currentWord;
				}

			} else {
				// isDescResolved = true;
				if (isDescResolved == false) {
					searchString += " " + currentWord;
				} else {
					startDateString += " " + currentWord;
				}
			}

		}
		searchString = searchString.trim();
		startDateString = startDateString.trim();
		endDateString = endDateString.trim();
		try {
			if (isSingleDate == false && isDoubleDate == false) {
				System.out.println("Description only");
				status = engine.searchTask(searchString);
			} else if (isSingleDate == true && isDoubleDate == false) {
				Date fromDate = null;

				List<Date> dateList = parseStringToDate(startDateString);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
				}

				status = engine.searchTask(searchString, fromDate);

			} else if (isSingleDate == true && isDoubleDate == true) {
				Date fromDate = null;
				Date maxDate = null;

				List<Date> dateList = parseStringToDate(startDateString);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
				}

				dateList = parseStringToDate(KeywordConstant.DATE_MAX);
				if (!dateList.isEmpty()) {
					maxDate = dateList.remove(0);
				}

				// System.out.println("searchString: " + searchString +
				// " from: "
				// + fromDate + " end: " + maxDate);
				status = engine.searchTask(searchString, fromDate, maxDate);

			} else if (isSingleDate == false && isDoubleDate == true) {
				Date fromDate = null;
				Date endDate = null;

				List<Date> dateList = parseStringToDate(startDateString);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
				}

				dateList = parseStringToDate(endDateString);
				if (!dateList.isEmpty()) {
					endDate = dateList.remove(0);
				}

				// System.out.println("searchString: " + searchString +
				// " from: "
				// + fromDate + " end: " + endDate);
				status = engine.searchTask(searchString, fromDate, endDate);

			}

		} catch (Exception e) {
			System.err.println(Message.ERROR_RETRIEVE);
		}
		sc.close();
		return status;
	}

	private boolean isANumber(String text) {
		boolean isNumber;

		try {
			Integer.parseInt(text);
			isNumber = true;
		} catch (NumberFormatException e) {
			isNumber = false;
		}

		return isNumber;

	}

	private Success retrievePriority(String remainingPriority) {

		Scanner sc = new Scanner(remainingPriority);
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

			System.out.println("curr: " + currentWord);
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
						isSingleDate = true;

					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {
						isSingleDate = true;
						isDoubleDate = true;

					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_TO)) {
						endDateString = sc.nextLine();
						isSingleDate = false;

					} else {
						startDateString += " " + currentWord;
					}

				} else {
					startDateString += " " + currentWord;
				}
			}

		}
		startDateString = startDateString.trim();
		endDateString = endDateString.trim();
		try {
			if (priority >= KeywordConstant.PRIORITY_MIN
					&& priority <= KeywordConstant.PRIORITY_MAX) {
				if (isSingleDate == false && isDoubleDate == false) {
					status = engine.retrieveTask(priority);
				} else if (isSingleDate == true && isDoubleDate == false) {
					Date fromDate = null;

					List<Date> dateList = parseStringToDate(startDateString);

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);
					}

					status = engine.retrieveTask(priority, fromDate);

				} else if (isSingleDate == true && isDoubleDate == true) {

					List<Date> dateList = parseStringToDate(startDateString);

					Date fromDate = null;

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);
					}

					Date maxDate = null;
					List<Date> dateMaxList = parseStringToDate(KeywordConstant.DATE_MAX);

					if (!dateMaxList.isEmpty()) {
						maxDate = dateMaxList.remove(0);
					}

					status = engine.retrieveTask(priority, fromDate, maxDate);

				} else if (isSingleDate == false && isDoubleDate == true) {
					String combinedDate = startDateString + " to "
							+ endDateString;
					combinedDate = combinedDate.trim();
					List<Date> dateList = parseStringToDate(combinedDate);

					Date fromDate = null;
					Date toDate = null;

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);

						if (!dateList.isEmpty()) {
							toDate = dateList.remove(0);
						}
					}
					System.out.println(fromDate + " " + toDate);
					status = engine.retrieveTask(priority, fromDate, toDate);
				}

			}

		} catch (IOException e) {
			System.err.println(Message.ERROR_RETRIEVE);
		}
		sc.close();
		return status;
	}

	private Success retrieveAllDates() {

		Success status = null;
		status = engine.retrieveTask();

		return status;
	}

	private Success retrieveSingleDate(String remainingDate) {
		Scanner sc = new Scanner(remainingDate);
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
		dateString = dateString.trim();
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
				status = engine.retrieveTask(onDate);
			}

		} catch (IOException e) {
			System.err.println(Message.ERROR_RETRIEVE);
		}
		sc.close();
		return status;
	}

	private Success retrieveInBetween(String remainingDate) {
		Scanner sc = new Scanner(remainingDate);
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
		startDateString = startDateString.trim();
		endDateString = endDateString.trim();
		try {
			Date fromDate = null;
			Date toDate = null;

			List<Date> dateListFrom = parseStringToDate(startDateString);

			if (!dateListFrom.isEmpty()) {
				fromDate = dateListFrom.remove(0);
			}

			if (!endDateString.trim().equals("")) {

				String combinedDate = startDateString + " to" + endDateString;

				combinedDate = combinedDate.trim();
				List<Date> dateList = parseStringToDate(combinedDate);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);

					if (!dateList.isEmpty()) {
						toDate = dateList.remove(0);
					}
				}

				status = engine.retrieveTask(fromDate, toDate);

			} else {

				List<Date> dateListTo = parseStringToDate(KeywordConstant.DATE_MAX);

				if (!dateListTo.isEmpty()) {
					toDate = dateListTo.remove(0);
				}

				status = engine.retrieveTask(fromDate, toDate);
			}

		} catch (IOException e) {
			System.err.println("retrieveInBetween: Retrieval fail.");
		}

		sc.close();
		return status;
	}

	private SuccessDisplay parseDisplayCommand(String remainingCommand) {

		SuccessDisplay status = null;
		Scanner sc = new Scanner(remainingCommand);
		String displayType = KeywordConstant.KEYWORD_DATE;
		Date startDate = null;
		Date endDate = null;

		List<Date> dateList = parseStringToDate(remainingCommand);

		if (!dateList.isEmpty()) {

			while (sc.hasNext()) {

				String currWord = sc.next();

				if (currWord.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
						|| currWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_DAYS)) {

					displayType = KeywordConstant.KEYWORD_DAY;
					break;

				} else if (currWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)
						|| currWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEKS)) {

					displayType = KeywordConstant.KEYWORD_WEEK;
					break;

				} else if (currWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)
						|| currWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTHS)) {

					displayType = KeywordConstant.KEYWORD_MONTH;
					break;

				} else if (currWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)
						|| currWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_YEARS)) {

					displayType = KeywordConstant.KEYWORD_YEAR;
					break;
				}
			}

			Date unfixedStartDate = dateList.remove(0);
			startDate = fixStartDateDisplay(unfixedStartDate, displayType);
			if (!dateList.isEmpty()) {
				Date unfixedEndDate = dateList.remove(0);
				endDate = fixEndDateDisplay(unfixedEndDate, displayType);
			} else {
				endDate = fixEndDateDisplay(unfixedStartDate, displayType);
			}

			try {
				status = engine
						.retrieveDisplay(startDate, endDate, displayType);
			} catch (IOException e) {
				status = new SuccessDisplay(false, Message.ERROR_GENERAL);
			}

		} else {
			status = new SuccessDisplay(false, Message.FAIL_PARSE_COMMAND);
		}

		return status;
	}

	private Success parseUpdateCommand(String remainingCommand) {

		Success status = null;
		remainingCommand = remainingCommand.trim();

		try {
			int indexOffset = Integer.parseInt(remainingCommand) - 1;
			taskToRemove = retrievedTaskList.get(indexOffset);
			String taskDisplay = KeywordConstant.KEYWORD_UPDATE;
			taskDisplay += " " + taskToRemove.toDisplay();

			status = new Success(taskDisplay, true, null);

		} catch (NumberFormatException e) {
			if (taskToRemove != null) {
				Success statusTask = parseAddCommand(remainingCommand);
				if (statusTask.isSuccess()) {
					Task updatedTask = (Task) statusTask.getObj();
					status = engine.updateTask(updatedTask, taskToRemove);
					if (status.isSuccess()) {
						taskToRemove = null;
						retrievedTaskList = null;
					}
				}
			} else {
				status = new Success(false, Message.ERROR_UPDATE_NO_TASK_LIST);
			}
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_UPDATE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_UPDATE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_UPDATE);
		}

		return status;
	}

	private Success parseDeleteCommand(String index) {

		Success status = null;
		index = index.trim();

		try {
			int indexOffset = Integer.parseInt(index) - 1;
			Task taskToRemove = retrievedTaskList.get(indexOffset);

			if (taskToRemove instanceof RecurrenceTask) {
				status = engine.deleteRecurTask(taskToRemove);
			} else {
				status = engine.deleteTask(taskToRemove);
			}
		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_DELETE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_DELETE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_DELETE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_DELETE);
		}

		return status;
	}

	private Success undoCommand() {
		Success status = engine.undoTask();

		return status;
	}

	private Success redoCommand() {
		Success status = engine.redoTask();

		return status;
	}

	private Success doneCommand(String remainingCommand) {
		Success status = null;
		remainingCommand = remainingCommand.trim();
		Scanner sc = new Scanner(remainingCommand);
		List<Task> doneList = new ArrayList<Task>();

		try {
			while (sc.hasNext()) {

				String currentValue = sc.next();
				int indexOffset = Integer.parseInt(currentValue) - 1;
				Task doneTask = retrievedTaskList.get(indexOffset);
				doneList.add(doneTask);
			}

			status = engine.markAsDone(doneList);

		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_DONE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_DONE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_DONE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_DONE);
		}

		sc.close();

		return status;
	}

	private Success undoneCommand(String remainingCommand) {
		Success status = null;
		remainingCommand = remainingCommand.trim();
		Scanner sc = new Scanner(remainingCommand);
		List<Task> undoneList = new ArrayList<Task>();

		try {
			while (sc.hasNext()) {

				String currentValue = sc.next();
				int indexOffset = Integer.parseInt(currentValue) - 1;
				Task undoneTask = retrievedTaskList.get(indexOffset);
				undoneList.add(undoneTask);
			}

			status = engine.markAsUndone(undoneList);

		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_UNDONE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_UNDONE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_UNDONE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_UNDONE);
		}

		sc.close();

		return status;
	}

	private Date fixStartDate(Date inDate) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		if (0 <= timeDifference && timeDifference <= delayInMillisec) {
			inCalendar.set(Calendar.HOUR_OF_DAY, 0);
			inCalendar.set(Calendar.MINUTE, 0);
			inCalendar.set(Calendar.SECOND, 1);
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	private Date fixEndDate(Date inDate) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		if (0 <= timeDifference && timeDifference <= delayInMillisec) {
			inCalendar.set(Calendar.HOUR_OF_DAY, 23);
			inCalendar.set(Calendar.MINUTE, 59);
			inCalendar.set(Calendar.SECOND, 59);
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	private Date fixStartDateDisplay(Date inDate, String displayType) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		int minDateInMonth = inCalendar.getActualMinimum(Calendar.DATE);
		int minMonthInYear = inCalendar.getActualMinimum(Calendar.MONTH);
		int minHour = inCalendar.getActualMinimum(Calendar.HOUR);
		int minMinute = inCalendar.getActualMinimum(Calendar.MINUTE);
		int minSec = inCalendar.getActualMinimum(Calendar.SECOND);

		if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
				|| displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DATE)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)) {

			Calendar firstDateOfWeek = (Calendar) inCalendar.clone();
			firstDateOfWeek.add(
					Calendar.DAY_OF_WEEK,
					firstDateOfWeek.getFirstDayOfWeek()
							- firstDateOfWeek.get(Calendar.DAY_OF_WEEK));

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {
				inCalendar.set(Calendar.DATE,
						firstDateOfWeek.get(Calendar.DATE));
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, minDateInMonth);
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, minDateInMonth);
				inCalendar.set(Calendar.MONTH, minMonthInYear);
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	private Date fixEndDateDisplay(Date inDate, String displayType) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		int maxDateInMonth = inCalendar.getActualMaximum(Calendar.DATE);
		int maxMonthInYear = inCalendar.getActualMaximum(Calendar.MONTH);
		int maxHour = inCalendar.getActualMaximum(Calendar.HOUR);
		int maxMinute = inCalendar.getActualMaximum(Calendar.MINUTE);
		int maxSec = inCalendar.getActualMaximum(Calendar.SECOND);

		if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
				|| displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DATE)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)) {

			Calendar firstDateOfWeek = (Calendar) inCalendar.clone();
			firstDateOfWeek.add(
					Calendar.DAY_OF_WEEK,
					firstDateOfWeek.getFirstDayOfWeek()
							- firstDateOfWeek.get(Calendar.DAY_OF_WEEK));

			Calendar lastDateOfWeek = (Calendar) firstDateOfWeek.clone();
			lastDateOfWeek.add(Calendar.DAY_OF_WEEK, 6);

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {
				inCalendar
						.set(Calendar.DATE, lastDateOfWeek.get(Calendar.DATE));
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, maxDateInMonth);
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, maxDateInMonth);
				inCalendar.set(Calendar.MONTH, maxMonthInYear);
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}
}