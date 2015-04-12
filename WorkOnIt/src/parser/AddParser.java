package parser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(AddParser.class
			.getName());

	/**
	 * This constructor takes in a full map of keywords, if any. It will make
	 * use of the data in data parser to manipulate information.
	 * 
	 * @param keywordFullMap
	 *            a hash map of first string mapped onto second string
	 * @return
	 */
	// @author A0111916M
	public AddParser(Map<String, String> keywordFullMap) {
		this.keywordFullMap = keywordFullMap;
		engine = new Engine();

		LOGGER.fine("Add Parser instantiated");
	}

	/**
	 * This method begins the process of add command.
	 * 
	 * @param remainingCommand
	 *            the remaining command after being truncated
	 * @param dataParser
	 *            the information consist within this session
	 * @return Success object
	 * 
	 */
	// @author A0111916M
	protected Success processAddCommand(String remainingCommand,
			DataParser dataParser) {

		LOGGER.fine("Processing add command");

		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

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

		LOGGER.fine("Add command returns with Success value : "
				+ status.isSuccess());

		return status;
	}

	/**
	 * Parse the add command. It will call the necessary interpreter, based on
	 * keywords.
	 * 
	 * @param remainingCommand
	 *            the remaining command after being truncated
	 * @return Success object
	 */
	// @author A0111916M
	protected Success parseAddCommand(String remainingCommand) {

		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());
		
		LOGGER.fine("Parsing add command");

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
					LOGGER.warning(Message.ERROR_NO_PRIORITY_FOUND);
				}
				taskDesc = taskDesc.trim();
				status = createFloatingTask(taskDesc, remainingPriority);
			}

			sc.close();

		} catch (NullPointerException e) {
			LOGGER.warning(e.getMessage());
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		}

		return status;
	}

	/**
	 * Interpret the commands into Normal Task object.
	 * 
	 * @param taskDesc
	 *            earlier truncated commands
	 * @param remainingDate
	 *            remains of the command
	 * @return Success object
	 */
	// @author A0111916M
	private Success createNormalTask(String taskDesc, String remainingDate) {

		assert (taskDesc != null && remainingDate != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing Normal Task");

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
				// in between startDate and endDate

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

						startDateString += " " + currentWord;
					}
				} else {
					startDateString += " " + currentWord;
				}
			} else if (isEndDate && !isPriority) {
				// in between endDate and Priority
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
				// after Priority

				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					LOGGER.warning(Message.FAIL_PARSE_PRIORITY);
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

		task = new NormalTask(taskDesc.trim(), priority, fromDate, toDate);
		status = new Success(task, true, null);

		sc.close();

		return status;
	}

	/**
	 * Interpret the commands into Deadline Task object.
	 * 
	 * @param taskDesc
	 *            earlier truncated commands
	 * @param remainingDate
	 *            remains of the command
	 * @return Success object
	 */
	// @author A0111916M
	private Success createDeadlineTask(String taskDesc, String remainingDate) {

		assert (taskDesc != null && remainingDate != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing Deadline Task");

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
				// in between dueDate and Priority

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
				// after Priority

				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					LOGGER.warning(Message.FAIL_PARSE_PRIORITY);
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
	 * Interpret the commands into Recurrence Task object.
	 * 
	 * @param taskDesc
	 *            earlier truncated commands
	 * @param remainingDate
	 *            remains of the command
	 * @return Success object
	 */
	// @author A0111916M - unused
	// dropped support for recurrence task
	private Success createRecurrenceTask(String taskDesc, String remainingDate) {

		assert (taskDesc != null && remainingDate != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing Recurrence Task");

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
				// in between startDate and endDate

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
				// in between endDate and Priority

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
				// After priority

				try {
					if (resolvedWord != null) {
						priority = Integer.parseInt(resolvedWord);
					} else {
						priority = Integer.parseInt(currentWord);
					}

				} catch (NumberFormatException e) {
					LOGGER.warning(Message.FAIL_PARSE_PRIORITY);
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
	 * Interpret the commands into Floating Task object.
	 * 
	 * @param taskDesc
	 *            earlier truncated commands
	 * @param remainingPriority
	 *            remains of the priority, if any
	 * @return
	 */
	// @author A0111916M
	private Success createFloatingTask(String taskDesc, String remainingPriority) {

		assert (taskDesc != null && remainingPriority != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing Floating Task");

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
					LOGGER.warning(Message.FAIL_PARSE_PRIORITY);
				}
			}

			sc.close();
		}

		task = new FloatingTask(taskDesc, priority);
		status = new Success(task, true, null);

		return status;
	}
}
