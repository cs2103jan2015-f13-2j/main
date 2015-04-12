package parser;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import logic.Engine;
import resource.KeywordConstant;
import resource.Message;
import entity.SuccessDisplay;

public class DisplayParser {

	private Engine engine = null;
	private static final Logger LOGGER = Logger.getLogger(DisplayParser.class
			.getName());

	/**
	 * This constructor will instantiate an Engine instance.
	 * 
	 */
	//@author A0111916M
	public DisplayParser() {
		engine = new Engine();

		LOGGER.fine("Display Parser instantiated");
	}

	/**
	 * This will parse the Display command, for viewing of DAY, WEEK or MONTH
	 * agenda. It will return a SuccessDisplay object, which contains the view
	 * type and list of Task(s) retrieved.
	 * 
	 * @param remainingCommand
	 *            the remaining command after being truncated
	 * @return SuccessDisplay object
	 */

	protected SuccessDisplay parseDisplayCommand(String remainingCommand) {

		assert (remainingCommand != null);

		LOGGER.fine("Parsing display command");

		SuccessDisplay status = null;
		Scanner sc = new Scanner(remainingCommand);
		String displayType = KeywordConstant.KEYWORD_DATE;
		Date startDate = null;
		Date endDate = null;

		List<Date> dateList = DateFixer.parseStringToDate(remainingCommand);

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
			startDate = DateFixer.fixStartDateDisplay(unfixedStartDate,
					displayType);
			if (!dateList.isEmpty()) {
				Date unfixedEndDate = dateList.remove(0);
				endDate = DateFixer.fixEndDateDisplay(unfixedEndDate,
						displayType);
			} else {
				endDate = DateFixer.fixEndDateDisplay(unfixedStartDate,
						displayType);
			}

			try {
				status = engine
						.retrieveDisplay(startDate, endDate, displayType);

				status.setCalendar(startDate);

			} catch (IOException e) {
				LOGGER.warning(e.getMessage());
				status = new SuccessDisplay(false, Message.ERROR_GENERAL);
			}

		} else {
			LOGGER.warning(Message.FAIL_PARSE_COMMAND);
			status = new SuccessDisplay(false, Message.FAIL_PARSE_COMMAND);
		}
		sc.close();

		LOGGER.fine("Display command returns with Success value : "
				+ status.isSuccess());

		return status;
	}

}
