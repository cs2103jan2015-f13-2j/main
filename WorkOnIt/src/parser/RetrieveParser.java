package parser;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

import logic.Engine;
import resource.KeywordConstant;
import resource.Message;
import entity.Success;

public class RetrieveParser {

	private Map<String, String> keywordFullMap = null;
	private Engine engine = null;
	private DataParser dataParser = null;

	private static final Logger LOGGER = Logger.getLogger(RetrieveParser.class
			.getName());

	/**
	 * This constructor takes in a full map of keywords, if any. It will make
	 * use of the data in data parser to manipulate information.
	 * 
	 * @param keywordFullMap
	 *            a hash map of first string mapped onto second string
	 * @param dataParser
	 *            the data that contains current information
	 * @return
	 */
	//@author A0111837J
	public RetrieveParser(Map<String, String> keywordFullMap,
			DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		engine = new Engine();

		LOGGER.fine("Retrieve Parser instantiated");
	}

	/**
	 * Returns a Success object containing date retrieved from that will be
	 * passed to the UI tier of the program to handle. This method will always
	 * return the Success object containing relevant information regardless of
	 * successful retrieval or not.
	 *
	 * @param remainingCommand
	 *            command that is left after processing
	 * @return Success object
	 */

	protected Success parseRetrieveCommand(String remainingCommand) {

		assert (remainingCommand != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing retrieve command");

		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
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
		boolean isDay = false;
		boolean isWeek = false;
		boolean isMonth = false;
		boolean isResolved = false;
		boolean isDoneUndone = false;

		Date startDate = null;
		Date endDate = null;

		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {
				if (resolvedWord.equalsIgnoreCase(KeywordConstant.KEYWORD_FROM)) {

					remainingText = sc.nextLine();
					isSingleDate = true;
					isDoubleDate = true;
					isFrom = true;
					// isResolved = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {

					remainingText = sc.nextLine();
					isSingleDate = true;
					isDoubleDate = false;
					isOn = true;
					isResolved = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_ALL)) {
					isAll = true;
					// isResolved = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_PRIORITY)) {
					if (sc.hasNext()) {
						remainingText = sc.nextLine();
					}
					isPriority = true;
					isResolved = true;

				} else if (resolvedWord
						.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)
						|| resolvedWord
								.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {
					if (sc.hasNext()) {
						remainingText = resolvedWord + " " + sc.nextLine();
					} else {
						remainingText = currentWord;
					}

					isDoneUndone = true;
					isResolved = true;

				} else {
					searchString += " " + currentWord;
				}
			} else {
				searchString += " " + currentWord;

			}
		}

		String combinedSearch = searchString + " " + remainingText;

		// if captured by searchString and not remainingText, means the user
		// only typed a single date
		if (isResolved == false) {
			if (remainingText.trim().equals("")
					&& DateFixer.parseStringToDate(combinedSearch).size() > 0) {
				String[] mthArray = { "january", "february", "march", "april",
						"may", "june", "july", "august", "september",
						"october", "november", "december" };

				Scanner dateScanner = new Scanner(combinedSearch);
				while (dateScanner.hasNext()) {
					String currWord = dateScanner.next();

					boolean isCurrNumber = !auxParser.isNaN(currWord);
					// if there's number, it's a single day/time, else it's just
					// month
					if (isCurrNumber == true) {
						isDoubleDate = false;
						isDay = true;
					} else {
						for (int i = 0; i < mthArray.length; i++) {
							String currMonth = mthArray[i];
							if (currMonth.contains(currWord)
									|| currWord
											.equals(KeywordConstant.KEYWORD_MONTH)
									|| currWord
											.equals(KeywordConstant.KEYWORD_MONTHS)) {
								isMonth = true;
								isDoubleDate = true;
							} else if (currWord
									.equals(KeywordConstant.KEYWORD_WEEK)
									|| currWord
											.equals(KeywordConstant.KEYWORD_WEEKS)) {
								isWeek = true;
								isDoubleDate = true;
							} else {
								isDesc = true;
							}
						}
					}
				}

				if (isDay == false && isWeek == false && isMonth == true) {
					Date unfixedMonth = null;

					List<Date> dateList = DateFixer
							.parseStringToDate(searchString);

					if (!dateList.isEmpty()) {
						unfixedMonth = dateList.remove(0);
					}

					startDate = DateFixer.fixStartDateDisplay(unfixedMonth,
							KeywordConstant.KEYWORD_MONTH);
					endDate = DateFixer.fixEndDateDisplay(unfixedMonth,
							KeywordConstant.KEYWORD_MONTH);

				} else if (isDay == true && isWeek == false && isMonth == true) {
					isSingleDate = true;
					isDoubleDate = false;
					remainingText = searchString;
				} else if (isDay == false && isWeek == true && isMonth == false) {
					Date unfixedWeek = null;

					List<Date> dateList = DateFixer
							.parseStringToDate(searchString);
					if (!dateList.isEmpty()) {
						unfixedWeek = dateList.remove(0);
					}

					startDate = DateFixer.fixStartDateDisplay(unfixedWeek,
							KeywordConstant.KEYWORD_WEEK);
					endDate = DateFixer.fixEndDateDisplay(unfixedWeek,
							KeywordConstant.KEYWORD_WEEK);
				} else {
					isSingleDate = true;
					remainingText = searchString;
				}

				dateScanner.close();
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
		}
		try {
			if (isAll == true) {
				status = retrieveAllDates();
			} else if (isPriority == true) {
				status = retrievePriority(remainingText.trim());
			} else if (isDesc == true) {
				status = retrieveTaskDesc(combinedSearch.trim());
			} else if (isDoneUndone == true) {
				status = retrieveDoneUndone(remainingText.trim());
			} else {

				if (isSingleDate == true && isDoubleDate == false) {

					status = retrieveSingleDate(remainingText.trim());
				} else if (isSingleDate == true && isDoubleDate == true) {

					status = retrieveInBetween(remainingText.trim());
				} else if (isSingleDate == false && isDoubleDate == true) {
					if (isMonth == true || isWeek == true) {
						status = retrieveInBetween(startDate, endDate);
					} else {
						status = retrieveInBetween(remainingText.trim());
					}
				} else {
					status = new Success(false, Message.ERROR_RETRIEVE);
				}
			}
		} catch (NullPointerException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(false, Message.ERROR_RETRIEVE);
		} catch (NoSuchElementException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(false, Message.ERROR_RETRIEVE);
		}
		sc.close();
		LOGGER.fine("Retrieve command returns with Success value : "
				+ status.isSuccess());

		return status;
	}

	/**
	 * This method will process a retrieve command for done or undone Task(s).
	 * It will parse the retrieve command and call method in Engine to retrieve
	 * the appropriate Task(s).
	 * 
	 * @param remainingText
	 *            the remaining command after being truncated
	 * @return Success object
	 */

	private Success retrieveDoneUndone(String remainingText) {
		assert (remainingText != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve done/undone command");

		Success status = null;
		Scanner sc = new Scanner(remainingText);

		boolean isStatusResolved = false;
		boolean isDone = false;
		boolean isSingleDate = false;
		boolean isDoubleDate = false;

		String startDateString = "";
		String endDateString = "";

		while (sc.hasNext()) {
			String currentWord = sc.next();
			String resolvedWord = keywordFullMap.get(currentWord);

			if (resolvedWord != null) {

				if (isStatusResolved == false) {

					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {
						isDone = true;
						isStatusResolved = true;
					} else if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {
						isDone = false;
						isStatusResolved = true;
					}

				} else {

					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_AT)
							|| resolvedWord
									.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {
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
				}
			} else {
				startDateString += " " + currentWord;
			}
		}

		startDateString = startDateString.trim();
		endDateString = endDateString.trim();

		if (isStatusResolved == true) {

			if (isSingleDate == false && isDoubleDate == false) {
				status = engine.getCompleteTask(isDone);

			} else if (isSingleDate == true && isDoubleDate == false) {
				Date fromDate = null;

				List<Date> dateList = DateFixer
						.parseStringToDate(startDateString);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
				}
				Date fixedSingleDate = DateFixer.fixStartDate(fromDate);
				status = engine
						.getCompleteTaskWithDate(isDone, fixedSingleDate);

			} else if (isSingleDate == true && isDoubleDate == true) {

				List<Date> dateList = DateFixer
						.parseStringToDate(startDateString);

				Date fromDate = null;

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
				}

				Date maxDate = null;
				List<Date> dateMaxList = DateFixer
						.parseStringToDate(KeywordConstant.DATE_MAX);

				if (!dateMaxList.isEmpty()) {
					maxDate = dateMaxList.remove(0);
				}

				Date fixedSingleDate = DateFixer.fixStartDate(fromDate);
				status = engine.getCompleteTaskBetweenDate(isDone,
						fixedSingleDate, maxDate);

			} else if (isSingleDate == false && isDoubleDate == true) {
				String combinedDate = startDateString + " to " + endDateString;
				combinedDate = combinedDate.trim();

				List<Date> dateList = DateFixer.parseStringToDate(combinedDate);

				Date fromDate = null;
				Date toDate = null;

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);

					if (!dateList.isEmpty()) {
						toDate = dateList.remove(0);
					}
				}

				Date fixedStartDate = DateFixer.fixStartDate(fromDate);
				Date fixedEndDate = DateFixer.fixEndDate(toDate);
				status = engine.getCompleteTaskBetweenDate(isDone,
						fixedStartDate, fixedEndDate);
			}

		}

		sc.close();
		return status;
	}

	/**
	 * This method will process a retrieve command command by retrieving Task(s)
	 * object that contains words in remainingText. It will parse the retrieve
	 * command and call method in Engine to retrieve the appropriate Task(s).
	 * 
	 * @param remainingText
	 *            The text that need to be retrieved
	 * @return Success object
	 */

	private Success retrieveTaskDesc(String remainingText) {

		assert (remainingText != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve task description command");

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

		if (isSingleDate == false && isDoubleDate == false) {

			status = engine.searchTask(searchString);
		} else if (isSingleDate == true && isDoubleDate == false) {
			Date fromDate = null;

			List<Date> dateList = DateFixer.parseStringToDate(startDateString);

			if (!dateList.isEmpty()) {
				fromDate = dateList.remove(0);
			}
			Date fixedSingleDate = DateFixer.fixStartDate(fromDate);
			status = engine.searchTask(searchString, fixedSingleDate);

		} else if (isSingleDate == true && isDoubleDate == true) {
			Date fromDate = null;
			Date maxDate = null;

			List<Date> dateList = DateFixer.parseStringToDate(startDateString);

			if (!dateList.isEmpty()) {
				fromDate = dateList.remove(0);
			}

			dateList = DateFixer.parseStringToDate(KeywordConstant.DATE_MAX);
			if (!dateList.isEmpty()) {
				maxDate = dateList.remove(0);
			}
			Date fixedSingleDate = DateFixer.fixStartDate(fromDate);

			status = engine.searchTask(searchString, fixedSingleDate, maxDate);

		} else if (isSingleDate == false && isDoubleDate == true) {
			Date fromDate = null;
			Date endDate = null;

			List<Date> dateList = DateFixer.parseStringToDate(startDateString);

			if (!dateList.isEmpty()) {
				fromDate = dateList.remove(0);
			}

			dateList = DateFixer.parseStringToDate(endDateString);
			if (!dateList.isEmpty()) {
				endDate = dateList.remove(0);
			}
			Date fixedStartDate = DateFixer.fixStartDate(fromDate);
			Date fixedEndDate = DateFixer.fixEndDate(endDate);

			try {
				if (searchString.length() == 0) {

					status = engine.retrieveTask(fixedStartDate, fixedEndDate);

				} else {
					status = engine.searchTask(searchString, fixedStartDate,
							fixedEndDate);
				}
			} catch (IOException e) {
				LOGGER.warning(Message.ERROR_RETRIEVE);
				status = new Success(false, Message.ERROR_RETRIEVE);
			}
		}

		sc.close();
		return status;
	}

	/**
	 * This method will process a retrieve command command by retrieving Task(s)
	 * object that have the specified priority. It will parse the retrieve
	 * command and call method in Engine to retrieve the appropriate Task(s).
	 * 
	 * @param remainingPriority
	 *            priority level that need to be retrieved
	 * @return Success object
	 */

	private Success retrievePriority(String remainingPriority) {

		assert (remainingPriority != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve priority command");

		Scanner sc = new Scanner(remainingPriority);
		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
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
					if (auxParser.isNaN(resolvedWord) == false) {
						priority = Integer.parseInt(resolvedWord);
						isPriorityResolved = true;
					}
				} else {
					if (auxParser.isNaN(currentWord) == false) {
						priority = Integer.parseInt(currentWord);
						isPriorityResolved = true;
					}
				}

			} else {

				if (resolvedWord != null) {
					if (resolvedWord
							.equalsIgnoreCase(KeywordConstant.KEYWORD_AT)
							|| resolvedWord
									.equalsIgnoreCase(KeywordConstant.KEYWORD_ON)) {
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

					List<Date> dateList = DateFixer
							.parseStringToDate(startDateString);

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);
					}
					Date fixedSingleDate = DateFixer.fixStartDate(fromDate);
					status = engine.retrieveTask(priority, fixedSingleDate);

				} else if (isSingleDate == true && isDoubleDate == true) {

					List<Date> dateList = DateFixer
							.parseStringToDate(startDateString);

					Date fromDate = null;

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);
					}

					Date maxDate = null;
					List<Date> dateMaxList = DateFixer
							.parseStringToDate(KeywordConstant.DATE_MAX);

					if (!dateMaxList.isEmpty()) {
						maxDate = dateMaxList.remove(0);
					}

					Date fixedSingleDate = DateFixer.fixStartDate(fromDate);
					status = engine.retrieveTask(priority, fixedSingleDate,
							maxDate);

				} else if (isSingleDate == false && isDoubleDate == true) {

					String combinedDate = startDateString + " to "
							+ endDateString;
					combinedDate = combinedDate.trim();

					List<Date> dateList = DateFixer
							.parseStringToDate(combinedDate);

					Date fromDate = null;
					Date toDate = null;

					if (!dateList.isEmpty()) {
						fromDate = dateList.remove(0);

						if (!dateList.isEmpty()) {
							toDate = dateList.remove(0);
						}
					}

					Date fixedStartDate = DateFixer.fixStartDate(fromDate);
					Date fixedEndDate = DateFixer.fixEndDate(toDate);
					status = engine.retrieveTask(priority, fixedStartDate,
							fixedEndDate);
				}

			} else {
				status = new Success(false, Message.ERROR_RETRIEVE);
			}

		} catch (IOException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(null, false, Message.ERROR_RETRIEVE);
		}
		sc.close();
		return status;
	}

	/**
	 * This method will process a retrieve command command by retrieving all
	 * Task(s) object. It will parse the retrieve command and call method in
	 * Engine to retrieve the appropriate Task(s).
	 * 
	 * @return Success object
	 */

	private Success retrieveAllDates() {

		LOGGER.fine("Processing retrieve all task command");
		Success status = null;
		status = engine.retrieveTask();

		return status;
	}

	/**
	 * This method will process a retrieve command command by retrieving all
	 * Task(s) object that is on a particular Date. It will parse the retrieve
	 * command and call method in Engine to retrieve the appropriate Task(s).
	 * 
	 * @param remainingDate
	 *            a String that resembles a Date
	 * @return Success object
	 */

	private Success retrieveSingleDate(String remainingDate) {

		assert (remainingDate != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve task with single date command");

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

				status = retrieveInBetween(preparedStatement);

			} else {
				Date onDate = null;
				Date fixedStartDate = null;
				Date fixedEndDate = null;
				List<Date> dateList = DateFixer.parseStringToDate(dateString);

				if (!dateList.isEmpty()) {
					onDate = dateList.remove(0);
					fixedStartDate = DateFixer.fixStartDate(onDate);
					fixedEndDate = DateFixer.fixEndDate(onDate);
				}
				status = engine.retrieveTask(fixedStartDate, fixedEndDate);
			}

		} catch (IOException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(null, false, Message.ERROR_RETRIEVE);
		}
		sc.close();
		return status;
	}

	/**
	 * This method will process a retrieve command by retrieving Task(s) object
	 * that lies in between two Date objects. It will parse the retrieve command
	 * and call method in Engine to retrieve the appropriate Task(s).
	 * 
	 * @param start
	 *            start Date object
	 * @param end
	 *            end Date object
	 * @return Success object
	 */

	private Success retrieveInBetween(Date start, Date end) {

		assert (start != null && end != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve task within a date range command");

		Success status = null;

		try {
			status = engine.retrieveTask(start, end);
		} catch (IOException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(null, false, Message.ERROR_RETRIEVE);
		}
		return status;
	}

	/**
	 * This method will process a retrieve command command by retrieving all
	 * Task(s) object that is between a range of two Dates. It will parse the
	 * retrieve command and call method in Engine to retrieve the appropriate
	 * Task(s).
	 * 
	 * @param remainingDate
	 *            a String that resembles two Date objects
	 * @return Success object
	 */

	private Success retrieveInBetween(String remainingDate) {

		assert (remainingDate != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Processing retrieve task within a date range (String format) command");

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
			Date fixedFromDate = null;
			Date fixedToDate = null;
			List<Date> dateListFrom = DateFixer
					.parseStringToDate(startDateString);

			if (!dateListFrom.isEmpty()) {
				fromDate = dateListFrom.remove(0);
				fixedFromDate = DateFixer.fixStartDate(fromDate);
			}
			if (!endDateString.equals("")) {

				String combinedDate = startDateString + " to " + endDateString;

				combinedDate = combinedDate.trim();
				List<Date> dateList = DateFixer.parseStringToDate(combinedDate);

				if (!dateList.isEmpty()) {
					fromDate = dateList.remove(0);
					fixedFromDate = DateFixer.fixStartDate(fromDate);
					if (!dateList.isEmpty()) {
						toDate = dateList.remove(0);
						fixedToDate = DateFixer.fixEndDate(toDate);

					}
				}

				status = engine.retrieveTask(fixedFromDate, fixedToDate);

			} else {

				List<Date> dateListTo = DateFixer
						.parseStringToDate(KeywordConstant.DATE_MAX);

				if (!dateListTo.isEmpty()) {
					toDate = dateListTo.remove(0);
				}

				status = engine.retrieveTask(fromDate, toDate);
			}

		} catch (IOException e) {
			LOGGER.warning(Message.ERROR_RETRIEVE);
			status = new Success(null, false, Message.ERROR_RETRIEVE);
		}

		sc.close();
		return status;
	}
}
