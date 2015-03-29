package web;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class HtmlBuilder {

	private PrintWriter writer;

	public HtmlBuilder() {
		try {
			init();
			createHtml();

			writer.close();
		} catch (IOException e) {
			System.out.println("error");
		}
	}

	private void init() throws UnsupportedEncodingException,
			FileNotFoundException {

		writer = new PrintWriter("month.html", "UTF-8");

	}

	private void createHtml() {

		writer.println("<head>");
		writer.println("<link rel='stylesheet' href='fullcalendar.css' />");
		writer.println("<script src='jquery-2.1.3.min.js'></script>");
		writer.println("<script src='moment.js'></script>");
		writer.println("<script src='fullcalendar.js'></script>");
		
		writer.println("<script>");
		writer.println("$(document).ready(function() {");
		writer.println("$('#calendar').fullCalendar({");
		//calendar body
		writer.println("})");
		writer.println("});");
		writer.println("</script>");

		writer.println("</head>");

		writer.println("<body>");
		writer.println("<div id='calendar'>");
		writer.println("</div>");
		writer.println("</body>");

	}
}
