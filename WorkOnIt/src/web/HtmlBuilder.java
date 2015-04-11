package web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import resource.FileName;
import resource.Graphic;
import resource.KeywordConstant;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;

public class HtmlBuilder {

	private PrintWriter writer = null;

	/**
	 * This constructor will take in the necessary information and create an
	 * HTML file for viewing of agenda.
	 * 
	 * @param displayType
	 *            the agenda type (DAY, WEEK, MONTH)
	 * @param displayCal
	 *            the starting date, depending on agenda type (DAY - a single
	 *            day, WEEK - first day of the week, MONTH - first day of the
	 *            month)
	 * @param taskList
	 *            List of Task(s) that will be shown in the agenda, according to
	 *            the display type and date range
	 */
	// @author A0111916M
	public HtmlBuilder(String displayType, Calendar displayCal,
			List<Task> taskList) {
		try {
			init(displayType);
			createHtml(taskList, displayCal, displayType);

			writer.close();
		} catch (IOException e) {
			// continue
		}
	}

	/**
	 * Create an HTML file at the specified save path.
	 * 
	 * @param displayType
	 *            the agenda type (DAY, WEEK, MONTH)
	 * @throws UnsupportedEncodingException
	 *             Will throw this exception if PrintWriter cannot write in
	 *             UTF-8
	 * @throws FileNotFoundException
	 *             Will throw this exception if the HTML file cannot be found
	 */
	// @author A0111916M
	private void init(String displayType) throws UnsupportedEncodingException,
			FileNotFoundException {

		String fileName = FileName.getFilenameCalendarUi();

		writer = new PrintWriter(fileName, "UTF-8");

	}

	/**
	 * This will generate the HTML file, based on the input parameters in the
	 * constructor. It will then create the HTML file, and display it in UI tier
	 * via JavaFX WebView.
	 * 
	 * @param taskList
	 *            List of Task(s), based on the display criteria
	 * @param displayCal
	 *            the starting date, depending on agenda type (DAY - a single
	 *            day, WEEK - first day of the week, MONTH - first day of the
	 *            month)
	 * @param displayType
	 *            the agenda type (DAY, WEEK, MONTH)
	 */
	// @author A0111916M
	private void createHtml(List<Task> taskList, Calendar displayCal,
			String displayType) {

		SimpleDateFormat sdfNormal = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		writer.println("<head>");
		writer.println("<link rel='stylesheet' href='css/fullcalendar.css' />");
		writer.println("<script src='js/jquery-2.1.3.min.js'></script>");
		writer.println("<script src='js/moment.js'></script>");
		writer.println("<script src='js/fullcalendar.js'></script>");

		writer.println("<script>");
		writer.println("$(document).ready(function() {");
		writer.println("$('#calendar').fullCalendar({");

		// calendar data(s)

		writer.println("header: {");
		writer.println("left: '',");
		writer.println("center: 'title',");
		writer.println("right: ''");
		writer.println("},");
		writer.println("editable: false,");
		writer.println("eventLimit: true,");

		if (displayType.equals(KeywordConstant.KEYWORD_WEEK)) {
			writer.println("defaultView: 'agendaWeek',");
		}

		String dateView = sdfNormal.format(displayCal.getTime());
		writer.println("defaultDate: '" + dateView + "',");

		writer.println("events: [");

		if (taskList != null) {

			for (int i = 0; i < taskList.size(); i++) {

				Task currTask = taskList.get(i);

				// if (!(currTask instanceof FloatingTask)) {
				String title = currTask.getTaskName();
				// System.out.println(title);

				writer.println("{");
				int indexOffset = i + 1;
				String fixedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "");
				writer.println("title: '" + indexOffset + ". " + fixedTitle
						+ "',");

				if (currTask instanceof NormalTask) {

					NormalTask normalTask = (NormalTask) currTask;
					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					startCal.setTime(normalTask.getStartDateTime());
					endCal.setTime(normalTask.getEndDateTime());

					if (startCal.equals(endCal)) {
						if (startCal.get(Calendar.HOUR_OF_DAY) == 0
								&& startCal.get(Calendar.MINUTE) == 0
								&& startCal.get(Calendar.SECOND) == 1) {
							writer.println("start: '"
									+ sdfNormal.format(startCal.getTime())
									+ "'");
						} else {
							writer.println("start: '"
									+ sdfTime.format(startCal.getTime()) + "'");
						}
					} else {
						if (startCal.get(Calendar.HOUR_OF_DAY) == 0
								&& startCal.get(Calendar.MINUTE) == 0
								&& startCal.get(Calendar.SECOND) == 1) {
							writer.println("start: '"
									+ sdfNormal.format(startCal.getTime())
									+ "',");
						} else {
							writer.println("start: '"
									+ sdfTime.format(startCal.getTime()) + "',");
						}

						if (endCal.get(Calendar.HOUR_OF_DAY) == 23
								&& endCal.get(Calendar.MINUTE) == 59
								&& endCal.get(Calendar.SECOND) == 59) {

							// offset current end date to the next day
							endCal.add(Calendar.DATE, 1);
							writer.println("end: '"
									+ sdfNormal.format(endCal.getTime()) + "'");
						} else {
							writer.println("end: '"
									+ sdfTime.format(endCal.getTime()) + "'");
						}
					}

				} else if (currTask instanceof DeadlineTask) {

					DeadlineTask deadlineTask = (DeadlineTask) currTask;
					Calendar deadlineCal = Calendar.getInstance();
					deadlineCal.setTime(deadlineTask.getDeadline());

					if (deadlineCal.get(Calendar.HOUR_OF_DAY) == 23
							&& deadlineCal.get(Calendar.MINUTE) == 59
							&& deadlineCal.get(Calendar.SECOND) == 59) {
						writer.println("start: '"
								+ sdfNormal.format(deadlineCal.getTime()) + "'");
					} else {
						writer.println("start: '"
								+ sdfTime.format(deadlineCal.getTime()) + "'");
					}
				} else if (currTask instanceof RecurrenceTask) {

					RecurrenceTask recurrenceTask = (RecurrenceTask) currTask;

					Calendar startCal = Calendar.getInstance();
					Calendar endCal = Calendar.getInstance();
					startCal.setTime(recurrenceTask.getStartRecurrenceDate());
					endCal.setTime(recurrenceTask.getEndRecurrenceDate());

					if (startCal.equals(endCal)) {
						if (startCal.get(Calendar.HOUR_OF_DAY) == 0
								&& startCal.get(Calendar.MINUTE) == 0
								&& startCal.get(Calendar.SECOND) == 1) {
							writer.println("start: '"
									+ sdfNormal.format(startCal.getTime())
									+ "'");
						} else {
							writer.println("start: '"
									+ sdfTime.format(startCal.getTime()) + "'");
						}
					} else {
						if (startCal.get(Calendar.HOUR_OF_DAY) == 0
								&& startCal.get(Calendar.MINUTE) == 0
								&& startCal.get(Calendar.SECOND) == 1) {
							writer.println("start: '"
									+ sdfNormal.format(startCal.getTime())
									+ "',");
						} else {
							writer.println("start: '"
									+ sdfTime.format(startCal.getTime()) + "',");
						}

						if (endCal.get(Calendar.HOUR_OF_DAY) == 23
								&& endCal.get(Calendar.MINUTE) == 59
								&& endCal.get(Calendar.SECOND) == 59) {
							writer.println("end: '"
									+ sdfNormal.format(endCal.getTime()) + "'");
						} else {
							writer.println("end: '"
									+ sdfTime.format(endCal.getTime()) + "'");
						}
					}

				} else if (currTask instanceof FloatingTask) {

					FloatingTask floatingTask = (FloatingTask) currTask;
					Date createdDate = new Date(floatingTask.getTaskId());
					Calendar createdCal = Calendar.getInstance();
					createdCal.setTime(createdDate);

					writer.println("start: '"
							+ sdfNormal.format(createdCal.getTime()) + "'");
				}

				if (currTask.isCompleted()) {
					writer.println(", color: '" + Graphic.WEBUI_DONE_COLOR_VAL
							+ "'");
				} else if (currTask.getPriority() == KeywordConstant.PRIORITY_HIGH) {
					writer.println(", color: '"
							+ Graphic.WEBUI_PRIORITY_HIGH_VAL + "'");
				} else if (currTask.getPriority() == KeywordConstant.PRIORITY_MEDIUM) {
					writer.println(", color: '"
							+ Graphic.WEBUI_PRIORITY_MEDIUM_VAL + "'");
				} else if (currTask.getPriority() == KeywordConstant.PRIORITY_LOW) {
					writer.println(", color: '"
							+ Graphic.WEBUI_PRIORITY_LOW_VAL + "'");
				}

				writer.println("}");
				if (i != taskList.size() - 1) {
					writer.println(",");
				}
				// }
			}
		}
		writer.println("]");

		writer.println("});");
		writer.println("});");
		writer.println("</script>");

		writer.println("</head>");

		writer.println("<body>");
		writer.println("<div id='calendar'>");
		writer.println("</div>");
		writer.println("</body>");

	}
}
