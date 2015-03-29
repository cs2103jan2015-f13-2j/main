package application;

import java.awt.SystemTray;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import data.InitFileIO;
import resource.Graphic;
import resource.KeywordConstant;
import resource.Message;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Success;
import entity.SuccessDisplay;
import entity.Task;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logic.Validator;

public class Main extends Application {

	// need a global variable for the input
	private static List<String> elementList = null;
	private static List<String> secondaryList = null;
	private static Validator commandValidator = null;
	private static Success successObj = null;

	// SCALE
	final static int TEXT_BOX_WIDTH = 700;

	@Override
	public void start(final Stage primaryStage) {

		try {

			Pane root = new Pane();
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			setProgramIconDesc(primaryStage);

			Scene scene = new Scene(root, TEXT_BOX_WIDTH, 550);
			root.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-background-radius: 10;");

			scene.getStylesheets().add(
					Main.class.getResource("../css/application.css")
							.toExternalForm());
			scene.setFill(null);

			// POP UP
			final Popup popup = new Popup();
			popup.setAutoFix(true);
			popup.setAutoHide(true);
			popup.setHideOnEscape(true);

			// UI DECLARE
			final TextField txtF = new TextField();
			final TextFieldListCell cellText = new TextFieldListCell();
			final ListView listView = new ListView();

			initializeGlobals();

			// UI - LIST VIEW
			listView.setId("listView");
			listView.setPrefSize(TEXT_BOX_WIDTH, 250);
			listView.setOpacity(0);
			listView.setEditable(true);
			listView.setLayoutY(50);
			listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					// addDataToListView();
					listHandler(event, primaryStage, txtF, listView);
					popup.hide();
				}
			});
			
			// UI - CALENDAR
			final WebView webview = new WebView();
			URL url = getClass().getResource("/web/calendar.html");
			webview.setVisible(true);
			WebEngine webengine = webview.getEngine();
			webengine.setJavaScriptEnabled(true);
			webengine.load(url.toString());
			final ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			scrollPane.setContent(webview);
			scrollPane.setPrefSize(TEXT_BOX_WIDTH, 400);
			scrollPane.setLayoutY(60);
		    scrollPane.setVisible(false);
			
			
			// UI - TEXT FIELD
			txtF.setId("textField");
			txtF.setLayoutX(0);
			txtF.setLayoutY(0);
			txtF.setPrefHeight(50);
			txtF.setPrefWidth(TEXT_BOX_WIDTH);
			txtF.setText(Message.UI_INPUT_HERE);
			txtF.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					
					System.out.println("textfield Text: " + txtF.getText());
					scrollPane.setVisible(false);
					wordHandler(txtF, txtF.getText());
					executeCommand(txtF, txtF.getText(), primaryStage, popup,
							listView);
					
						if (successObj != null) {
							if (successObj.isSuccess()) {
								Object returnObj = successObj.getObj();
								if(successObj instanceof SuccessDisplay){
									SuccessDisplay sdObj = (SuccessDisplay) successObj;
									if(sdObj.getDisplayType().equals("monthly")) {
										scrollPane.setVisible(true);
									} else if (sdObj.getDisplayType().equals("weekly")) {
										//show weekly view
										scrollPane.setVisible(true);
									} else {
										//daily view
										List<Task> taskList = (ArrayList<Task>) sdObj
												.getObj();
										if (taskList.isEmpty()) {
											listView.getItems().clear();
											listView.setOpacity(0);
											txtF.setText(Message.UI_NO_TASK_FOUND);
											txtF.selectAll();
										} else {
											addTaskToListView(listView, successObj);
											switchListView(listView, txtF);
										}
									}
									
								} else {
									if (returnObj instanceof ArrayList<?>) {
										List<Task> taskList = (ArrayList<Task>) successObj
												.getObj();
										if (taskList.isEmpty()) {
											listView.getItems().clear();
											listView.setOpacity(0);
											txtF.setText(Message.UI_NO_TASK_FOUND);
											txtF.selectAll();
										} else {
											addTaskToListView(listView, successObj);
											switchListView(listView, txtF);
										}
									}
								}
							}
						} else {
							showPopUp(null, false, primaryStage, popup);
						}
					
				}
			});

			// onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				public void handle(KeyEvent event) {
					// addDataToListView();
					commandHandler(event, txtF.getText(), primaryStage, txtF,
							listView);
					switchListView(listView, txtF);
					popup.hide();
				}
			});



			// UI - TEXT FIELD

			root.getChildren().add(txtF);
			root.getChildren().add(listView);
			root.getChildren().add(scrollPane);

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setProgramIconDesc(final Stage primaryStage) {
		Image imgProgramIcon16 = new Image(Graphic.UI_PROGRAM_ICON_16);
		Image imgProgramIcon24 = new Image(Graphic.UI_PROGRAM_ICON_24);
		Image imgProgramIcon32 = new Image(Graphic.UI_PROGRAM_ICON_32);
		Image imgProgramIcon64 = new Image(Graphic.UI_PROGRAM_ICON_64);
		Image imgProgramIcon128 = new Image(Graphic.UI_PROGRAM_ICON_128);
		Image imgProgramIcon256 = new Image(Graphic.UI_PROGRAM_ICON_256);
		Image imgProgramIcon512 = new Image(Graphic.UI_PROGRAM_ICON_512);

		primaryStage.getIcons().add(imgProgramIcon16);
		primaryStage.getIcons().add(imgProgramIcon24);
		primaryStage.getIcons().add(imgProgramIcon32);
		primaryStage.getIcons().add(imgProgramIcon64);
		primaryStage.getIcons().add(imgProgramIcon128);
		primaryStage.getIcons().add(imgProgramIcon256);
		primaryStage.getIcons().add(imgProgramIcon512);

		primaryStage.setTitle("Work On It");
	}

	public void initializeGlobals() {
		elementList = new ArrayList<String>();
		secondaryList = new ArrayList<String>();
		commandValidator = new Validator();
	}

	protected void executeCommand(TextField txtF, String commandString,
			Stage primaryStage, Popup popup, ListView listView) {

		Success status = null;
		
		
		if (commandValidator.validateKeywordSequence(secondaryList) == true) {
			System.out.println(commandString);
			status = commandValidator.parseCommand(commandString);

			if (status.isSuccess() == false) {
				System.out.println(status.getMessage());
			} else {
				if (status.getObj() instanceof String) {
					String updateCommand = (String) status.getObj();
					txtF.setText(updateCommand);
				} else {
					txtF.clear();
					listView.getItems().clear();
					listView.setOpacity(0);
				}

				System.out.println(Message.SUCCESS_COMMAND);
			}

			showPopUp(status.getMessage(), status.isSuccess(), primaryStage,
					popup);

		} else {
			showPopUp(null, false, primaryStage, popup);
			System.out.println(Message.FAIL_PARSE_COMMAND);
		}

		successObj = status;
	}

	public static void commandHandler(KeyEvent event, String textFieldText,
			final Stage stage, TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (event.getCode().equals(KeyCode.UP)
				|| event.getCode().equals(KeyCode.DOWN)) {
			listView.requestFocus();
		}

		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(txtF, textFieldText);
		}
	}

	public static void listHandler(KeyEvent event, final Stage stage,
			TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (event.getCode().equals(KeyCode.LEFT)
				|| event.getCode().equals(KeyCode.RIGHT)) {
			txtF.requestFocus();
		}
	}

	private static void wordHandler(TextField txtF, String textFieldText) {

		String[] stringArr = textFieldText.trim().split(" ");

		elementList.clear();
		secondaryList.clear();

		// iterate thru the input
		for (int i = 0; i < stringArr.length; i++) {
			elementList.add(stringArr[i]);

			// check if current word is a keyword
			if (commandValidator.validateKeyword(stringArr[i])) {
				txtF.setStyle("-fx-text-fill: red;");
				String currentKeyword = stringArr[i];

				secondaryList.add(currentKeyword);
				// this method is for fencing the keyword (will implement later)
				// handleMethod("Handling: " + stringArr[i]);

			} else {
				txtF.setStyle("-fx-text-fill: black;");
			}

		}
	}

	// just a proof of concept the thing works
	private static void handleMethod(String input) {
		System.out.println(input);
	}

	private static void hide(final Stage stage) {
		Platform.setImplicitExit(false);
		stage.hide();
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// if (SystemTray.isSupported()) {
		// //stage.setOnHiding(null);
		// stage.hide();
		// System.out.println("hide");
		// } else {
		// System.exit(0);
		// }
		// }
		// });
	}

	// for running via Main, bypassing hotkey
	public static void main(String[] args) {
		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		launch();
	}

	private static void switchListView(ListView listView, TextField textField) {
		if (listView.getItems().size() != 0) {
			listView.setOpacity(1);
		} else {
			listView.setOpacity(0);
		}
	}

	private static void addTaskToListView(ListView listView, Success successObj) {

		Image imgDoneTask = new Image(Graphic.UI_GREEN_TICK_PATH);
		Image imgHighPriority = new Image(Graphic.UI_URGENT_PATH);
		Image imgNormalPriority = new Image(Graphic.UI_NORMAL_PATH);
		Image imgLowPriority = new Image(Graphic.UI_LOW_PATH);

		if (successObj.getObj() == null) {
			listView.getItems().clear();
		} else {
			ObservableList task = FXCollections.observableArrayList();
			Object obj = successObj.getObj();

			String displayTitle = getAgendaTitle(successObj);

			if (!displayTitle.equalsIgnoreCase(KeywordConstant.KEYWORD_DEFAULT)) {
				Pane pane = new Pane();
				Label agendaTitle = new Label();
				agendaTitle.setText(displayTitle);
				agendaTitle.setLayoutX(40);
				pane.getChildren().add(agendaTitle);
				task.add(pane);
			}

			// if (successObj instanceof SuccessDisplay) {
			// if (((SuccessDisplay) successObj).getDisplayType().equals(
			// KeywordConstant.KEYWORD_MONTH)) {
			// System.out.println("web view");
			// WebView browser = new WebView();
			// WebEngine webEngine = browser.getEngine();
			// webEngine.load("http://www.google.com");
			// }
			// }

			if (obj instanceof ArrayList<?>) {
				ArrayList<Task> allTask = (ArrayList<Task>) obj;
				Collections.sort(allTask, Task.taskComparator);
				for (int i = 0; i < allTask.size(); i++) {
					String displayDate = getDateFromTask(allTask.get(i));

					Pane pane = new Pane();

					Label taskName = new Label();
					Label taskDate = new Label();
					pane.setMinWidth(TEXT_BOX_WIDTH);

					taskName.setText((i + 1) + ") "
							+ allTask.get(i).getTaskName());
					taskName.setAlignment(Pos.CENTER_RIGHT);
					taskName.setMaxWidth(TEXT_BOX_WIDTH / 2);
					taskName.setMaxHeight(200);
					taskName.setWrapText(true);
					taskName.setLayoutX(40);

					taskDate.setText(displayDate);
					taskDate.setAlignment(Pos.CENTER_RIGHT);
					taskDate.setLayoutX(TEXT_BOX_WIDTH / 2);

					if (allTask.get(i).isCompleted() == true) {
						pane.getChildren().add(new ImageView(imgDoneTask));
					} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_HIGH) {
						pane.getChildren().add(new ImageView(imgHighPriority));
					} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_MEDIUM) {
						pane.getChildren()
								.add(new ImageView(imgNormalPriority));
					} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_LOW) {
						pane.getChildren().add(new ImageView(imgLowPriority));
					}

					pane.getChildren().add(taskName);
					pane.getChildren().add(taskDate);

					task.add(pane);
				}

				listView.setItems(task);
			} else if (obj instanceof String) {
				listView.getItems().clear();
			}
		}
	}

	private static String getAgendaTitle(Success successObj) {

		String title = Message.UI_DISPLAY_TASK_FOR;
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String displayType = KeywordConstant.KEYWORD_DEFAULT;

		if (successObj instanceof SuccessDisplay) {
			Object obj = successObj.getObj();
			ArrayList<Task> allTask = (ArrayList<Task>) obj;

			displayType = ((SuccessDisplay) successObj).getDisplayType();

			if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
					|| displayType
							.equalsIgnoreCase(KeywordConstant.KEYWORD_DATE)) {

				// TODO
				Calendar currCalendar = Calendar.getInstance();

				int currDay = currCalendar.get(Calendar.DAY_OF_MONTH);
				int currMonth = currCalendar.get(Calendar.MONTH);
				int currYear = currCalendar.get(Calendar.YEAR);

				Calendar displayCalendar = ((SuccessDisplay) successObj)
						.getCalendar();

				int dispDay = displayCalendar.get(Calendar.DAY_OF_MONTH);
				int dispMonth = displayCalendar.get(Calendar.MONTH);
				int dispYear = displayCalendar.get(Calendar.YEAR);

				if (currDay == dispDay && currMonth == dispMonth
						&& currYear == dispYear) {
					title += Message.UI_TODAY;
				} else if (currDay + 1 == dispDay && currMonth == dispMonth
						&& currYear == dispYear) {
					title += Message.UI_TOMORROW;
				} else if (currDay - 1 == dispDay && currMonth == dispMonth
						&& currYear == dispYear) {
					title += Message.UI_YESTERDAY;
				}

				title += sdf.format(displayCalendar.getTime());

			} else if (displayType
					.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)) {
				// TO DO
			} else if (displayType
					.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)) {
				// TO DO
			} else if (displayType
					.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)) {
				// TO DO
			}

		} else {
			title = displayType;
		}

		return title;
	}

	public static String getDateFromTask(Task taskObj) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM h:mm a");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("dd MMM h:mm a");
		String displayText = null;
		if (taskObj instanceof NormalTask) {

			Date startDate = ((NormalTask) taskObj).getStartDateTime();
			Date endDate = ((NormalTask) taskObj).getEndDateTime();
			String startDateString = sdf.format(startDate);
			String endDateString = sdf.format(endDate);

			if (startDate.equals(endDate)) {
				displayText = startDateString;
			} else {
				displayText = startDateString + " to " + endDateString;
			}
		}

		if (taskObj instanceof FloatingTask) {
			displayText = "-";
		}

		if (taskObj instanceof RecurrenceTask) {

			Date startDate = ((RecurrenceTask) taskObj)
					.getStartRecurrenceDate();
			Date endDate = ((RecurrenceTask) taskObj).getEndRecurrenceDate();
			String occurenceType = ((RecurrenceTask) taskObj)
					.getOccurenceType();
			String startDateString = null;
			String endDateString = null;

			if (occurenceType.equals(KeywordConstant.KEYWORD_DAILY)) {
				sdf = new SimpleDateFormat("h:mm a");
				sdfEnd = new SimpleDateFormat("h:mm a");

			} else if (occurenceType.equals(KeywordConstant.KEYWORD_WEEKLY)) {
				sdf = new SimpleDateFormat("EEEE h:mm a");
				sdfEnd = new SimpleDateFormat("h:mm a");

			} else if (occurenceType.equals(KeywordConstant.KEYWORD_MONTHLY)) {
				sdf = new SimpleDateFormat("d");
				sdfEnd = new SimpleDateFormat("d");

			} else if (occurenceType.equals(KeywordConstant.KEYWORD_YEARLY)) {
				sdf = new SimpleDateFormat("d MMM");
				sdfEnd = new SimpleDateFormat("d MMM");
			}

			String[] ordinals = { "th", "st", "nd", "rd", "th", "th", "th",
					"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
					"th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th",
					"th", "th", "th", "th", "st" };

			if (startDate.equals(endDate)) {

				startDateString = sdf.format(startDate);

				if (occurenceType.equals(KeywordConstant.KEYWORD_MONTHLY)) {
					displayText = startDateString
							+ ordinals[startDate.getDate()];
				} else {
					displayText = startDateString;
				}
			} else {
				startDateString = sdf.format(startDate);
				endDateString = sdfEnd.format(endDate);

				if (occurenceType.equals(KeywordConstant.KEYWORD_MONTHLY)) {
					displayText = startDateString
							+ ordinals[startDate.getDate()];
					displayText += " to ";
					displayText += endDateString + ordinals[endDate.getDate()];
				} else {
					displayText = startDateString + " to " + endDateString;
				}
			}

			if (occurenceType.equals(KeywordConstant.KEYWORD_MONTHLY)) {
				displayText += " " + Message.UI_OF_THE_MONTH;
			} else {
				displayText += " " + occurenceType;
			}
		}

		if (taskObj instanceof DeadlineTask) {

			Date deadlineDate = ((DeadlineTask) taskObj).getDeadline();
			String deadlineDateString = sdf.format(deadlineDate);

			displayText = Message.UI_DUE_BY + " " + deadlineDateString;

		}

		return displayText;
	}

	private void showPopUp(String message, boolean isSuccess,
			final Stage primaryStage, final Popup popup) {

		Label label = new Label(message);

		label.getStyleClass().add("popup");

		Image img = null;
		ImageView imgView = null;

		if (isSuccess) {
			img = new Image(Graphic.UI_TICK_PATH);
			imgView = new ImageView(img);
		} else {
			img = new Image(Graphic.UI_CROSS_PATH);
			imgView = new ImageView(img);
		}

		popup.getContent().clear();
		popup.getContent().add(imgView);
		popup.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				// popup.setX(primaryStage.getX() + primaryStage.getWidth() /
				// 2);
				// popup.setY(primaryStage.getY() + primaryStage.getHeight() /
				// 2);
				popup.setX(primaryStage.getX() + primaryStage.getWidth()
						- popup.getWidth() - 5);
				popup.setY(primaryStage.getY() + 5);
			}
		});
		popup.show(primaryStage);
	}
}
