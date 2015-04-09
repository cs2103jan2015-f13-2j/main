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
import resource.FileName;
import resource.Graphic;
import resource.KeywordConstant;
import resource.Message;
import web.HtmlBuilder;
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
import javafx.scene.Node;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logic.Validator;

public class Main extends Application {

	// need a global variable for the input
	private List<String> elementList = null;
	private List<String> secondaryList = null;
	private static Validator commandValidator = null;
	private Success successObj = null;
	private ArrayList<Integer> indexArray = null;
	private int indexCounter = 0;
	private boolean isHistory = false;

	// SCALE
	final static int TEXT_BOX_HEIGHT = 50;
	final static int TEXT_BOX_WIDTH = 700;
	final static int SCENE_HEIGHT = 550;
	final static int LIST_VIEW_HEIGHT = 250;
	final static int SCROLL_PANE_HEIGHT = 800;

	// POSITION ADJUSTMENTS
	final static int POSITION_LIST_VIEW_Y = 50;
	final static int POSITION_TEXT_BOX_X = 0;
	final static int POSITION_TEXT_BOX_Y = 0;
	final static int POSITION_CALENDAR_SCROLLPANE_Y = 60;

	@Override
	public void start(final Stage primaryStage) {

		try {
			/***********************
			 * UI DECLARATION
			 ***********************/

			// UI DECLARE
			final TextField txtF = new TextField();
			final TextFieldListCell cellText = new TextFieldListCell();
			final ListView listView = new ListView();
			final WebView webview = new WebView();
			final ScrollPane scrollPane = new ScrollPane();
			final Popup popup = new Popup();
			final WebEngine webengine = webview.getEngine();

			Pane root = new Pane();
			Scene scene = new Scene(root, TEXT_BOX_WIDTH, SCENE_HEIGHT);

			// TEXTFLOW
			// final TextFlow tFlow = new TextFlow();

			initializeGlobals();

			/***********************
			 * UI POSITIONING
			 ***********************/

			// UI - LIST VIEW POSITION
			listView.setLayoutY(POSITION_LIST_VIEW_Y);

			// UI - CALENDAR POSITION
			scrollPane.setLayoutY(POSITION_CALENDAR_SCROLLPANE_Y);

			// UI - TEXT FIELD POSITION
			txtF.setLayoutX(POSITION_TEXT_BOX_X);
			txtF.setLayoutY(POSITION_TEXT_BOX_Y);

			/***********************
			 * UI SETTINGS
			 ***********************/

			// UI - ROOT SETTINGS
			root.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"
					+ "-fx-background-radius: 10;");

			// UI - PRIMARY STAGE SETTINGS
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			setProgramIconDesc(primaryStage);

			// UI - SCENE SETTINGS
			//scene.getStylesheets().add(this.getClass().getResource("../css/application.css").toExternalForm());
			scene.setFill(null);

			// UI - POP UP SETTINGS
			popup.setAutoFix(true);
			popup.setAutoHide(true);
			popup.setHideOnEscape(true);

			// UI - LIST VIEW SETTINGS
			listView.setId("listView");
			listView.setPrefSize(TEXT_BOX_WIDTH, LIST_VIEW_HEIGHT);
			listView.setStyle("-fx-font-size: 15pt;");
					
			listView.setOpacity(0);
			listView.setEditable(true);
			listView.setFocusTraversable(false);

			// UI - CALENDAR SETTINGS
			webview.setVisible(true);
			scrollPane.setContent(webview);
			scrollPane.setFitToWidth(true);
			scrollPane.setPrefSize(TEXT_BOX_WIDTH, SCROLL_PANE_HEIGHT);
			scrollPane.setVisible(false);

			// UI - TEXT FIELD SETTINGS
			txtF.setId("textField");
			txtF.setPrefHeight(TEXT_BOX_HEIGHT);
			txtF.setPrefWidth(TEXT_BOX_WIDTH);
			txtF.setText(Message.UI_INPUT_HERE);
			txtF.setFont(Font.font("Arial", 28));

			// UI - TEXT FLOW SETTINGS
			// tFlow.setLayoutX(10);
			// tFlow.setLayoutY(10);

			/***********************
			 * UI ACTIONS
			 ***********************/

			// UI - LIST VIEW ACTIONS
			listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					// addDataToListView();
					listHandler(event, primaryStage, txtF, listView);
					popup.hide();
				}
			});

			// UI - TEXT FIELD ACTIONS
			txtF.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {

					System.out.println("textfield Text: " + txtF.getText());
					scrollPane.setVisible(false);
					
					wordHandler(txtF, txtF.getText(), listView);
					executeCommand(txtF, txtF.getText(), primaryStage, popup,
							listView);
					handleCommandResponse(primaryStage, txtF, listView,
							scrollPane, popup, webengine);
					wordHandler(txtF, txtF.getText(), listView);
					indexCounter = 0;
				}

			});

			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					keypressHandler(event, txtF.getText(), primaryStage, txtF,
							listView);
					switchListView(listView, txtF, event);
					popup.hide();
				}
			});

			/***********************
			 * UI DISPLAY OPTIONS
			 ***********************/

			root.getChildren().add(txtF);
			root.getChildren().add(listView);
			root.getChildren().add(scrollPane);
			// root.getChildren().add(tFlow);

			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleCommandResponse(final Stage primaryStage,
			final TextField txtF, final ListView listView,
			final ScrollPane scrollPane, final Popup popup,
			final WebEngine webengine) {

		if (successObj != null) {
			if (successObj.isSuccess()) {
				Object returnObj = successObj.getObj();

				handleDisplayCommand(txtF, listView, scrollPane, webengine,
						returnObj);
			}
		} else {
			showPopUp(null, false, primaryStage, popup);
		}
	}

	private void handleDisplayList(final TextField txtF,
			final ListView listView, List<Task> taskList) {
		if (taskList.isEmpty()) {
			listView.getItems().clear();
			listView.setOpacity(0);
			txtF.setText(Message.UI_NO_TASK_FOUND);
			txtF.selectAll();
		} else {
			addTaskToListView(listView, successObj);
			switchListView(listView);
		}
	}

	private void handleDisplayCommand(final TextField txtF,
			final ListView listView, final ScrollPane scrollPane,
			final WebEngine webengine, Object returnObj) {
		if (successObj instanceof SuccessDisplay) {
			SuccessDisplay sdObj = (SuccessDisplay) successObj;

			String displayType = sdObj.getDisplayType();
			if (displayType.equals(KeywordConstant.KEYWORD_MONTH)
					|| displayType.equals(KeywordConstant.KEYWORD_WEEK)) {
				@SuppressWarnings("unchecked")
				List<Task> taskList = (ArrayList<Task>) returnObj;
				Calendar displayCal = sdObj.getCalendar();

				HtmlBuilder htmlBuilder = new HtmlBuilder(displayType,
						displayCal, taskList);
				webengine.setJavaScriptEnabled(true);
				webengine.load(FileName.getFilenameCalendarUiUrl());

				scrollPane.setVisible(true);

			} else if (sdObj.getDisplayType().equals(
					KeywordConstant.KEYWORD_DAY)
					|| sdObj.getDisplayType().equals(
							KeywordConstant.KEYWORD_DATE)) {
				// daily view
				List<Task> taskList = (ArrayList<Task>) sdObj.getObj();
				handleDisplayList(txtF, listView, taskList);
			}

		} else {
			if (returnObj instanceof ArrayList<?>) {
				List<Task> taskList = (ArrayList<Task>) successObj.getObj();
				handleDisplayList(txtF, listView, taskList);
			}
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
		indexArray = new ArrayList<Integer>();
	}

	private void history(ListView listView) {

		Success status = commandValidator.getHistory();

		if (listView.getItems().isEmpty() && status.isSuccess()) {
			addTaskToListView(listView, status);
			switchListView(listView);
			isHistory = true;
		}
	}

	private void executeCommand(TextField txtF, String commandString,
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

	public void keypressHandler(KeyEvent event, String textFieldText,
			final Stage stage, TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		} else if (event.getCode().equals(KeyCode.TAB)) {
			int startPosition = 0;
			if(indexArray.size()>0){
				startPosition = indexArray.get(indexCounter);
			}
			int endPosition = 0;
			int arrayOffset = 1;
			if (indexCounter >= indexArray.size() - arrayOffset) {
				endPosition = txtF.getLength();
			} else {
				endPosition = indexArray.get(indexCounter + arrayOffset);
				endPosition -= secondaryList.get(indexCounter + 1).length();
			}
			// selecting in reverse
			txtF.selectRange(endPosition, startPosition);

			// increment tab counter
			indexCounter++;

			if (indexCounter >= indexArray.size()) {
				indexCounter = 0;
			}
		} else if (event.getCode().equals(KeyCode.DOWN)) {
			listView.requestFocus();
		}
		// handleEachKey(txtF, tFlow);
		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(txtF, textFieldText, listView);
		}
	}

	public void handleEachKey(TextField txtF, TextFlow tf) {
		String[] stringArr = txtF.getText().trim().split(" ");
		tf.getChildren().clear();
		for (int i = 0; i < stringArr.length; i++) {
			Text currText;

			currText = new Text(" " + stringArr[i]);

			currText.setFont(Font.font("Arial", 28));
			if (commandValidator.validateKeyword(stringArr[i])) {
				currText.setFill(Color.RED);

			} else {
				currText.setVisible(false);
			}
			tf.getChildren().add(currText);
		}
	}

	public static void listHandler(KeyEvent event, final Stage stage,
			TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (listView.getFocusModel().getFocusedIndex() - 1 == -1
				&& event.getCode().equals(KeyCode.UP)) {
			txtF.requestFocus();
			// listView.setOpacity(0);
		}

		if (event.getCode().equals(KeyCode.LEFT)
				|| event.getCode().equals(KeyCode.RIGHT)) {
			txtF.requestFocus();
		}
	}

	private void wordHandler(TextField txtF, String textFieldText,
			ListView listView) {

		String[] stringArr = textFieldText.trim().split(" ");

		resetDependentLists();
		int startIndex = 0;
		startIndex = loopTextNodes(listView, stringArr, startIndex);
	}

	private int loopTextNodes(ListView listView, String[] stringArr,
			int startIndex) {
		for (int i = 0; i < stringArr.length; i++) {
			elementList.add(stringArr[i]);
			// Text currText;

			// currText = new Text(" " + stringArr[i]);

			// currText.setFont(Font.font("Arial", 28));
			int length = startIndex + stringArr[i].length();
			if (i > 0) {
				length++;
			}
			
			stringArr[i] = stringArr[i].toLowerCase();
			
			// check if current word is a keyword
			if (commandValidator.validateKeyword(stringArr[i])) {
				// txtF.setStyle("-fx-text-fill: red;");

				String currentKeyword = stringArr[i];

				indexArray.add(length);
				// currText.setFill(Color.RED);
				secondaryList.add(currentKeyword);

				if (stringArr[i].equals(KeywordConstant.KEYWORD_ADD)) {
					history(listView);

				} else if (isHistory) {
					listView.getItems().clear();
					listView.setOpacity(0);
					isHistory = false;
				}

			} else {
				// currText.setVisible(false);
			}
			startIndex = length;
			// tf.getChildren().add(currText);
		}
		return startIndex;
	}

	private void resetDependentLists() {
		elementList.clear();
		secondaryList.clear();
		// tf.getChildren().clear();
		indexArray.clear();
	}

	private static void hide(final Stage stage) {
		Platform.setImplicitExit(false);
		stage.hide();
	}

	// for running via Main, bypassing hotkey
	// public static void main(String[] args) {
	// InitFileIO initFile = new InitFileIO();
	// initFile.checkAndProcessFile();
	// launch();
	// }

	// FOR KEY PRESS
	private static void switchListView(ListView listView, TextField textField,
			KeyEvent event) {

		if (event.getCode().equals(KeyCode.DOWN) && listView.getOpacity() == 0) {
			Success successObj = null;
			successObj = commandValidator.parseCommand("display today");

			addTaskToListView(listView, successObj);
			if (listView.getItems().size() != 0) {
				listView.setOpacity(1);
			}
		}
		if (event.getCode().equals(KeyCode.UP)) {
			listView.setOpacity(0);
		}
	}

	// FOR KEY ON ACTION
	private static void switchListView(ListView listView) {
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
				agendaTitle
						.setStyle("-fx-font-weight: bold ; -fx-underline: true;");
				agendaTitle.getStyleClass().add("title");
				agendaTitle.setLayoutX(100);

				pane.getChildren().add(agendaTitle);
				task.add(pane);
			}

			if (obj instanceof ArrayList<?>) {

				ArrayList<?> objectList = (ArrayList<?>) obj;

				if (!objectList.isEmpty()) {
					Object firstObj = ((ArrayList) obj).get(0);

					if (firstObj instanceof Task) {

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
								pane.getChildren().add(
										new ImageView(imgDoneTask));
							} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_HIGH) {
								pane.getChildren().add(
										new ImageView(imgHighPriority));
							} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_MEDIUM) {
								pane.getChildren().add(
										new ImageView(imgNormalPriority));
							} else if (allTask.get(i).getPriority() == KeywordConstant.PRIORITY_LOW) {
								pane.getChildren().add(
										new ImageView(imgLowPriority));
							}

							pane.getChildren().add(taskName);
							pane.getChildren().add(taskDate);
							pane.getStyleClass().add("title");

							task.add(pane);
						}

					} else if (firstObj instanceof String) {

						ArrayList<String> historyList = (ArrayList<String>) obj;

						for (int i = 0; i < historyList.size(); i++) {

							String currHistory = historyList.get(i);
							task.add(currHistory);
						}

					}

					listView.setItems(task);
				}

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
