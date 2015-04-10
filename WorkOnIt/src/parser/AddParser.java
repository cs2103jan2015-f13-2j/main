package parser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import logic.Engine;
import resource.KeywordConstant;
import resource.Message;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Success;
import entity.Task;

public class AddParser {

	private Map<String, String> keywordFullMap = null;
	private Engine engine = null;
	private DataParser dataParser = null;

	public AddParser(Map<String, String> keywordFullMap, DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		engine = new Engine();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected Success processAddCommand(String remainingCommand, DataParser dataParser) {
		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		remainingCommand = remainingCommand.trim();

		status = parseAddCommand(remainingCommand);
		Task task = null;
		if (status.isSuccess()) {
			task = (Task) status.getObj();
			status = engine.addTask(task);
			auxParser.secondaryListRetrieval(status);
		}
		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected Success parseAddCommand(String remainingCommand) {

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
									.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)
							|| resolvedWord
									.equalsIgnoreCase(KeywordConstant.KEYWORD_AT)) {

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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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
		List<Date> dateList = DateFixer.parseStringToDate(combinedDate);

		Date fromDate = null;
		Date toDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedStartDate = dateList.remove(0);
			fromDate = DateFixer.fixStartDate(unprocessedStartDate);

			if (!dateList.isEmpty()) {
				Date unprocessedEndDate = dateList.remove(0);
				toDate = DateFixer.fixEndDate(unprocessedEndDate);
			}
		}
		// System.out.println(combinedDate);
		task = new NormalTask(taskDesc.trim(), priority, fromDate, toDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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
		List<Date> dateList = DateFixer.parseStringToDate(deadlineDateString);
		Date deadlineDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedDate = dateList.remove(0);
			deadlineDate = DateFixer.fixEndDate(unprocessedDate);
		}

		task = new DeadlineTask(taskDesc, priority, deadlineDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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
		List<Date> dateList = DateFixer.parseStringToDate(combinedDate);

		Date startRecurrenceDate = null;
		Date endRecurrenceDate = null;

		if (!dateList.isEmpty()) {
			Date unprocessedStartDate = dateList.remove(0);
			startRecurrenceDate = DateFixer.fixStartDate(unprocessedStartDate);

			if (!dateList.isEmpty()) {
				Date unprocessedEndDate = dateList.remove(0);
				endRecurrenceDate = DateFixer.fixEndDate(unprocessedEndDate);
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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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
}
