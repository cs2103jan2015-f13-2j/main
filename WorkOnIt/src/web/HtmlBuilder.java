package web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import resource.FileName;
import resource.KeywordConstant;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;

public class HtmlBuilder {

	private PrintWriter writer;

	public HtmlBuilder(String displayType, List<Task> taskList) {
		try {
			init(displayType);
			createHtml(taskList);

			writer.close();
		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	private void init(String displayType) throws UnsupportedEncodingException,
			FileNotFoundException {

		String fileName = "";

		if (displayType.equals(KeywordConstant.KEYWORD_DAY)) {

		} else if (displayType.equals(KeywordConstant.KEYWORD_WEEK)) {

		} else if (displayType.equals(KeywordConstant.KEYWORD_MONTH)) {
			fileName = FileName.getFilenameMonthUi();
		} else if (displayType.equals(KeywordConstant.KEYWORD_YEAR)) {

		}

		writer = new PrintWriter(fileName, "UTF-8");

	}

	private void createHtml(List<Task> taskList) {

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
		writer.println("left: 'prev,next today',");
		writer.println("center: 'title',");
		 writer.println("right: 'month,basicWeek,basicDay'");
		writer.println("},");
		writer.println("editable: false,");
		writer.println("eventLimit: true,");
		writer.println("events: [");

		if (taskList != null) {

			for (int i = 0; i < taskList.size(); i++) {

				Task currTask = taskList.get(i);
				
				if (!(currTask instanceof FloatingTask)) {
					String title = currTask.getTaskName();

					writer.println("{");
					writer.println("title: '" + title + "',");

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
										+ sdfTime.format(startCal.getTime())
										+ "'");
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
										+ sdfTime.format(startCal.getTime())
										+ "',");
							}

							if (endCal.get(Calendar.HOUR_OF_DAY) == 23
									&& endCal.get(Calendar.MINUTE) == 59
									&& endCal.get(Calendar.SECOND) == 59) {
								writer.println("end: '"
										+ sdfNormal.format(endCal.getTime())
										+ "'");
							} else {
								writer.println("end: '"
										+ sdfTime.format(endCal.getTime())
										+ "'");
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
									+ sdfNormal.format(deadlineCal.getTime())
									+ "'");
						} else {
							writer.println("start: '"
									+ sdfTime.format(deadlineCal.getTime())
									+ "'");
						}
					} else if (currTask instanceof RecurrenceTask) {

						RecurrenceTask recurrenceTask = (RecurrenceTask) currTask;

						Calendar startCal = Calendar.getInstance();
						Calendar endCal = Calendar.getInstance();
						startCal.setTime(recurrenceTask
								.getStartRecurrenceDate());
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
										+ sdfTime.format(startCal.getTime())
										+ "'");
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
										+ sdfTime.format(startCal.getTime())
										+ "',");
							}

							if (endCal.get(Calendar.HOUR_OF_DAY) == 23
									&& endCal.get(Calendar.MINUTE) == 59
									&& endCal.get(Calendar.SECOND) == 59) {
								writer.println("end: '"
										+ sdfNormal.format(endCal.getTime())
										+ "'");
							} else {
								writer.println("end: '"
										+ sdfTime.format(endCal.getTime())
										+ "'");
							}
						}

						// NEVER RECUR EVERY WEEKLY

					}

					writer.println("}");
					if (i != taskList.size() - 1) {
						writer.println(",");
					}
				}
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
