package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import parser.CommandParser;
import resource.FileName;
import resource.Graphic;
import resource.KeywordConstant;
import resource.Message;
import validator.Validator;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Main extends Application {

	// need a global variable for the input
	private List<String> elementList = null;
	private List<String> secondaryList = null;
	private static CommandParser commandValidator = null;
	private static Validator keywordValidator = null;
	private Success successObj = null;
	private ArrayList<Integer> indexArray = null;
	private int indexCounter = 0;
	private boolean isHistory = false;

	// SCALE
	final static int TEXT_BOX_HEIGHT = 50;
	final static int TEXT_BOX_WIDTH = 750;
	final static int SCENE_HEIGHT = 600;
	final static int LIST_VIEW_HEIGHT = 350;
	final static int SCROLL_PANE_HEIGHT = 610;
	final static int SCROLL_PANE_WIDTH = TEXT_BOX_WIDTH + 20;

	// POSITION ADJUSTMENTS
	final static int POSITION_LIST_VIEW_Y = 50;
	final static int POSITION_TEXT_BOX_X = 0;
	final static int POSITION_TEXT_BOX_Y = 0;
	final static int POSITION_CALENDAR_SCROLLPANE_Y = 60;

	/**
	 * This method initiates and sets up the User interface for the application
	 *
	 * @param primaryStage
	 *            the main stage of the UI
	 */
	//@author A0111837J
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
			final TextFlow tFlow = new TextFlow();

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
			webview.setStyle("overflow-x: hidden;");
			webview.setStyle("overflow-y: hidden;");
			scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
			scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
			scrollPane.setContent(webview);
			scrollPane.setPrefWidth(SCROLL_PANE_WIDTH);
			scrollPane.setFitToWidth(true);
			scrollPane.setVisible(false);

			// UI - TEXT FIELD SETTINGS
			txtF.setId("textField");
			txtF.setPrefHeight(TEXT_BOX_HEIGHT);
			txtF.setPrefWidth(TEXT_BOX_WIDTH);
			txtF.setText(Message.UI_INPUT_HERE);
			txtF.setFont(Font.font("Arial", 28));

			// UI - TEXT FLOW SETTINGS
			tFlow.setLayoutX(10);
			tFlow.setLayoutY(10);

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

					scrollPane.setVisible(false);

					wordHandler(txtF.getText(), listView, tFlow);
					executeCommand(txtF, txtF.getText(), primaryStage, popup,
							listView, tFlow);
					handleCommandResponse(primaryStage, txtF, listView,
							scrollPane, popup, webengine);
					wordHandler(txtF.getText(), listView, tFlow);
					indexCounter = 0;
				}

			});

			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					keypressHandler(event, txtF.getText(), primaryStage, txtF,
							listView, tFlow, true);
					switchListView(listView, txtF, event);
					popup.hide();
				}
			});

			txtF.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					keypressHandler(event, txtF.getText(), primaryStage, txtF,
							listView, tFlow, false);
					switchListView(listView, txtF, event);
				}
			});

			/***********************
			 * UI DISPLAY OPTIONS
			 ***********************/

			root.getChildren().add(txtF);
			root.getChildren().add(listView);
			root.getChildren().add(scrollPane);
			root.getChildren().add(tFlow);

			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the response of the executed command, it shows the user whether
	 * the command is accepted or not, and calls for an appropriate response
	 * when the command is executed correctly.
	 *
	 * @param primaryStage
	 *            The stage of this application
	 * @param txtF
	 *            The main input textbox
	 * @param listView
	 *            The listview that contain tasks to be displayed
	 * @param scrollPane
	 *            The scrollpane containing the calendar
	 * @param popup
	 *            The popup to show whether command is accepted
	 * @param webengine
	 *            The WebEngine for creating the calendar
	 */
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

	/**
	 * This method handles the list that will be shown after a successfully
	 * executed command
	 *
	 * @param txtF
	 *            The main input textbox
	 * @param listView
	 *            The listview that contain tasks to be displayed
	 * @param taskList
	 *            The list that contains task items
	 * 
	 */
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

	/**
	 * This method handles the displays of various objects being returned from
	 * the appropriate commands
	 * 
	 * @param txtF
	 *            The main input textbox
	 * @param listView
	 *            The listview that contain tasks to be displayed
	 * @param scrollPane
	 *            The scrollpane to contain the listview
	 * @param webengine
	 *            The webengine which creates the calendar
	 * @param returnObj
	 *            The embedded object of the Success object returned
	 */
	private void handleDisplayCommand(final TextField txtF,
			final ListView listView, final ScrollPane scrollPane,
			final WebEngine webengine, Object returnObj) {
		if (successObj instanceof SuccessDisplay) {

			SuccessDisplay sdObj = (SuccessDisplay) successObj;
			displayCalendar(txtF, listView, scrollPane, webengine, sdObj);

		} else {
			if (returnObj instanceof ArrayList<?>) {

				List<Task> taskList = (ArrayList<Task>) successObj.getObj();
				handleDisplayList(txtF, listView, taskList);

			} else if (returnObj instanceof SuccessDisplay) {

				SuccessDisplay sdObj = (SuccessDisplay) returnObj;
				displayCalendar(txtF, listView, scrollPane, webengine, sdObj);
			}
		}
	}

	/**
	 * This method handles the displays of various objects being returned from
	 * the appropriate commands
	 * 
	 * @param txtF
	 *            The main input textbox
	 * @param listView
	 *            The listview that contain tasks to be displayed
	 * @param scrollPane
	 *            The scrollpane to contain the listview
	 * @param webengine
	 *            The webengine which creates the calendar
	 * @param returnObj
	 *            The embedded object of the Success object returned
	 */
	private void displayCalendar(final TextField txtF, final ListView listView,
			final ScrollPane scrollPane, final WebEngine webengine,
			SuccessDisplay sdObj) {
		String displayType = sdObj.getDisplayType();
		if (displayType.equals(KeywordConstant.KEYWORD_MONTH)
				|| displayType.equals(KeywordConstant.KEYWORD_WEEK)) {
			@SuppressWarnings("unchecked")
			List<Task> taskList = (ArrayList<Task>) sdObj.getObj();
			Calendar displayCal = sdObj.getCalendar();

			HtmlBuilder htmlBuilder = new HtmlBuilder(displayType, displayCal,
					taskList);
			webengine.setJavaScriptEnabled(true);
			webengine.load(FileName.getFilenameCalendarUiUrl());

			scrollPane.setVisible(true);

		} else if (sdObj.getDisplayType().equals(KeywordConstant.KEYWORD_DAY)
				|| sdObj.getDisplayType().equals(KeywordConstant.KEYWORD_DATE)) {
			// daily view
			List<Task> taskList = (ArrayList<Task>) sdObj.getObj();
			handleDisplayList(txtF, listView, taskList);
		}
	}

	/**
	 * This method sets the appropriate icons of different sizes and the title
	 * to the main stage of the application
	 *
	 * @param primaryStage
	 *            The main stage of the application
	 * 
	 */
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

		primaryStage.setTitle(Message.UI_TITLE);
	}

	/**
	 * This method initializes the global objects that is required by the
	 * program
	 * 
	 */
	public void initializeGlobals() {
		elementList = new ArrayList<String>();
		secondaryList = new ArrayList<String>();
		commandValidator = new CommandParser();
		keywordValidator = new Validator();
		indexArray = new ArrayList<Integer>();
	}

	/**
	 * This method shows the history of task names that the user has entered.
	 * 
	 * @param listView
	 *            This listview is the container of the list items
	 */
	//@author A0111916M
	private void history(ListView listView) {

		Success status = commandValidator.getHistory();

		if (listView.getItems().isEmpty() && status.isSuccess()) {
			addTaskToListView(listView, status);
			switchListView(listView);
			isHistory = true;
		}
	}

	/**
	 * This method executes the command from the textfield and and displays
	 * appropriate elements
	 * 
	 * @param txtF
	 *            The main textfield of the application
	 * @param commandString
	 *            The command that the user entered
	 * @param primaryStage
	 *            The main stage of the application
	 * @param popup
	 *            The popup to show whether command is accepted
	 * @param listView
	 *            This listview is the container of the list items
	 */
	//@author A0111837J
	private void executeCommand(TextField txtF, String commandString,
			Stage primaryStage, Popup popup, ListView listView, TextFlow tFlow) {

		Success status = null;

		if (keywordValidator.validateKeywordSequence(secondaryList) == true) {

			status = commandValidator.parseCommand(commandString);

			if (status.isSuccess() == false) {
				
			} else {
				if (status.getObj() instanceof String) {
					String updateCommand = (String) status.getObj();
					txtF.setText(updateCommand);
				} else {
					txtF.clear();
					listView.getItems().clear();
					listView.setOpacity(0);
				}
			}

			showPopUp(status.getMessage(), status.isSuccess(), primaryStage,
					popup);
			tFlow.getChildren().clear();

		} else {
			showPopUp(null, false, primaryStage, popup);
		}

		successObj = status;
	}

	/**
	 * This method handles the keypresses of the user
	 * 
	 * @param event
	 *            This is the keypress event of the user
	 * @param textFieldText
	 *            This is the text from the textfield
	 * @param stage
	 *            This is the main stage of the application
	 * @param txtF
	 *            This is the text field from the application
	 * @param listView
	 *            This is the container containing the list of items
	 */
	public void keypressHandler(KeyEvent event, String textFieldText,
			final Stage stage, TextField txtF, ListView listView,
			TextFlow tFlow, boolean isOnKeyPressed) {

		int maxCharacter = 43; // optimal value for the textbox dimension

		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		} else if (event.getCode().equals(KeyCode.TAB) && isOnKeyPressed) {
			int startPosition = 0;
			if (indexArray.size() > 0) {
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
			txtF.positionCaret(endPosition);
			txtF.selectRange(endPosition, startPosition);

			// increment tab counter
			indexCounter++;

			if (indexCounter >= indexArray.size()) {
				indexCounter = 0;
			}
		} else if (event.getCode().equals(KeyCode.DOWN)) {
			listView.requestFocus();
		}
		handleEachKey(txtF, tFlow);
		if (txtF.getText().length() > maxCharacter) {
			tFlow.getChildren().clear();
		}
		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(textFieldText, listView, tFlow);
		}
	}

	/**
	 * This method handles each character of user input for textFlow
	 * 
	 * @param txtF
	 *            This is the textfield of
	 * @param tf
	 *            This is the textflow of the text nodes
	 */
	public void handleEachKey(TextField txtF, TextFlow tf) {
		String[] stringArr = txtF.getText().trim().split(" ");
		tf.getChildren().clear();
		for (int i = 0; i < stringArr.length; i++) {
			Text currText;

			currText = new Text(" " + stringArr[i]);

			currText.setFont(Font.font("Arial", 28));
			if (keywordValidator.validateKeyword(stringArr[i])) {
				currText.setFill(Color.RED);

			} else {
				currText.setVisible(false);
			}
			tf.getChildren().add(currText);
		}
	}

	/**
	 * This method handles keypresses from within the list view
	 *
	 * @param event
	 *            The key event by the user
	 * @param stage
	 *            The main stage of the application
	 * @param txtF
	 *            The text field of the application
	 * @param listView
	 *            This is the container containing the list of items
	 */
	//@author A0112694E
	public static void listHandler(KeyEvent event, final Stage stage,
			TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (listView.getFocusModel().getFocusedIndex() - 1 == -1
				&& event.getCode().equals(KeyCode.UP)) {
			txtF.requestFocus();
		}

		if (event.getCode().equals(KeyCode.LEFT)
				|| event.getCode().equals(KeyCode.RIGHT)) {
			txtF.requestFocus();
		}
	}

	/**
	 * This method handles the words of user input
	 * 
	 * @param textFieldText
	 *            This is the text of the textfield
	 * @param listView
	 *            This is the container containing the list of items
	 */
	//@author A0111837J
	private void wordHandler(String textFieldText, ListView listView,
			TextFlow tFlow) {

		if (textFieldText == "") {
			tFlow.getChildren().clear();
		}

		String[] stringArr = textFieldText.trim().split(" ");

		resetDependentLists();
		int startIndex = 0;
		startIndex = loopTextNodes(listView, stringArr, startIndex);
	}

	/**
	 * This method loops through the text nodes input by the user and processes
	 * the keywords
	 * 
	 * @param listView
	 *            This is the container containing the list of items
	 * @param stringArr
	 *            This is the string array of the text nodes
	 * @param startIndex
	 *            This is the start index
	 * @return int startIndex
	 */
	private int loopTextNodes(ListView listView, String[] stringArr,
			int startIndex) {
		for (int i = 0; i < stringArr.length; i++) {
			elementList.add(stringArr[i]);
			
			int length = startIndex + stringArr[i].length();
			if (i > 0) {
				length++;
			}

			stringArr[i] = stringArr[i].toLowerCase();

			// check if current word is a keyword
			if (keywordValidator.validateKeyword(stringArr[i])) {
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

	/**
	 * This method resets and clears relevant lists after an operation
	 */
	private void resetDependentLists() {
		elementList.clear();
		secondaryList.clear();
		// tf.getChildren().clear();
		indexArray.clear();
	}

	/**
	 * This method hides the main stage of the application
	 * 
	 * @param stage
	 *            This is the main stage of the application
	 */
	private static void hide(final Stage stage) {
		Platform.setImplicitExit(false);
		stage.hide();
	}

	/**
	 * This method alternates between hiding and showing the list view
	 * 
	 * @param listView
	 *            This is the container of the list of tasks
	 * @param textField
	 *            This is the main text field of the application
	 */
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

	/**
	 * This method alternates between hiding and showing the list view
	 *
	 * @param listView
	 *            This is the container for the list of items
	 */
	// FOR KEY ON ACTION
	private static void switchListView(ListView listView) {
		if (listView.getItems().size() != 0) {
			listView.setOpacity(1);
		} else {
			listView.setOpacity(0);
		}
	}

	/**
	 * This method adds the task from the success object into the list view
	 * 
	 * @param listView
	 *            This is the container containing the list of tasks
	 * @param successObj
	 *            This is the object containing the required data to add into
	 *            the list
	 */
	//@author A0112694E
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

	/**
	 * This method formats the agenda view's title from data retrieved from the
	 * success object.
	 * 
	 * @param successObj
	 *            This object contains the relevant data to be displayed
	 * @return String containing the formatted title
	 */
	//@author A0111916M
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

	/**
	 * This method gets the date from the task and formats it to be shown on the
	 * list view
	 * 
	 * @param taskObj
	 *            This is the object containing the date
	 * 
	 * @return String containing the formatted date
	 */
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

	/**
	 * This method shows the popup on the UI
	 * 
	 * @param message
	 *            This is the message to be shown
	 * @param isSuccess
	 *            This determines the type of graphic the popup will use
	 * @param primaryStage
	 *            This is the main stage of the application
	 * @param popup
	 *            This is the popup of the application
	 */
	private void showPopUp(String message, boolean isSuccess,
			final Stage primaryStage, final Popup popup) {

		Label label = new Label(message);

		label.getStyleClass().add(Message.UI_POP_UP);

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