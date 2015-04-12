package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import resource.FileName;
import resource.KeywordConstant;
import resource.Message;

import com.google.gson.JsonSyntaxException;

import entity.FloatingTask;
import entity.NormalTask;
import entity.DeadlineTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;

public class WriteFileIO {

	private String filename_floating = null;
	private String filename_normal = null;
	private String filename_recur = null;
	private String filename_deadline = null;
	private String filename_history = null;

	private static String file_type = null;

	private static final Logger LOGGER = Logger.getLogger(WriteFileIO.class
			.getName());

	public WriteFileIO() {

		filename_floating = FileName.getFilenameFloating();
		filename_normal = FileName.getFilenameNormal();
		filename_recur = FileName.getFilenameRecur();
		filename_deadline = FileName.getFilenameDeadline();
		filename_history = FileName.getFilenameHistory();

		LOGGER.fine("FileIO instantiated");
	}

	/**
	 * save Task into into data file base on it Class Type.
	 *
	 * @param Task
	 *            created by user
	 * @return Success Object which contain the success/failure message
	 */
	// @author A0112694E
	public Success saveIntoFile(Task task) {

		LOGGER.fine("Saving into file: " + task.toDisplay());

		Success status = null;

		assert (task != null);

		addHistory(task.getTaskName());

		file_type = getFileType(task);

		String gsonSerial = Serializer.serializeToJson(task);
		PrintWriter filewrite = null;

		try {
			filewrite = new PrintWriter(new BufferedWriter(new FileWriter(
					file_type, true)));
			filewrite.println(gsonSerial);

			status = new Success(true, Message.SUCCESS_ADDED);
			filewrite.close();

		} catch (IOException e) {
			System.err.println(Message.ERROR_SAVE_INTO_FILE);
			status = new Success(false, Message.ERROR_SAVE_INTO_FILE);
		}

		return status;
	}

	/**
	 * get name of file base on the task class.
	 *
	 * @param Task
	 *            Type of task which parse in.
	 * @return String Name of file for the task parse in.
	 */

	private String getFileType(Task task) {

		LOGGER.fine("Identifying the file type of this task: "
				+ task.toDisplay());

		String type = null;

		if (task instanceof FloatingTask) {
			type = filename_floating;
		} else if (task instanceof NormalTask) {
			type = filename_normal;
		} else if (task instanceof RecurrenceTask) {
			type = filename_recur;
		} else if (task instanceof DeadlineTask) {
			type = filename_deadline;
		}
		return type;
	}

	/**
	 * Delete task from the data file
	 *
	 * @param Task
	 *            the specific task that need to be deleted from the data file
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */

	public Success deleteFromFile(Task taskObj) {

		LOGGER.fine("delete task from file: " + taskObj.toDisplay());

		Success successObj = null;

		BufferedReader reader = null;

		getFileType(taskObj);

		try {

			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			if (taskObj instanceof NormalTask) {

				reader = new BufferedReader(new FileReader(filename_normal));
				while ((printLine = reader.readLine()) != null) {
					try {
						NormalTask task = (NormalTask) Serializer
								.deserializeFromJson(printLine,
										NormalTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_DELETE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_normal);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);

				}

				filewriteIntoFile.close();

			}

			if (taskObj instanceof FloatingTask) {

				reader = new BufferedReader(new FileReader(filename_floating));
				while ((printLine = reader.readLine()) != null) {
					try {
						FloatingTask task = (FloatingTask) Serializer
								.deserializeFromJson(printLine,
										FloatingTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_DELETE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_floating);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));

					filewriteIntoFile.println(gsonSerial);

				}

				filewriteIntoFile.close();
			}

			if (taskObj instanceof DeadlineTask) {

				reader = new BufferedReader(new FileReader(filename_deadline));
				while ((printLine = reader.readLine()) != null) {
					try {
						DeadlineTask task = (DeadlineTask) Serializer
								.deserializeFromJson(printLine,
										DeadlineTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_DELETE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_deadline);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);

				}

				filewriteIntoFile.close();
			}

			if (taskObj instanceof RecurrenceTask) {

				reader = new BufferedReader(new FileReader(filename_recur));
				while ((printLine = reader.readLine()) != null) {
					try {
						RecurrenceTask task = (RecurrenceTask) Serializer
								.deserializeFromJson(printLine,
										RecurrenceTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_DELETE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_recur);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);
				}

				filewriteIntoFile.close();
			}

			reader.close();
		}

		catch (IOException e) {
			successObj = new Success(false, e.getMessage());

		}
		return successObj;
	}

	/**
	 * update task from the data file by overwriting the old task.
	 *
	 * @param Task
	 *            the specific task that need to be updated from the data file
	 * @param Task
	 *            the specific task that need to be deleted from the data file
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */

	public Success updateFromFile(Task taskUpdate, Task taskObj) {

		LOGGER.fine("update task from file: " + taskUpdate.toDisplay());

		Success successObj = null;
		BufferedReader reader = null;
		getFileType(taskObj);

		try {

			List<Task> taskList = new ArrayList<Task>();

			String printLine;

			if (taskObj instanceof NormalTask) {

				reader = new BufferedReader(new FileReader(filename_normal));
				while ((printLine = reader.readLine()) != null) {
					try {
						NormalTask task = (NormalTask) Serializer
								.deserializeFromJson(printLine,
										NormalTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_UPDATE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_normal);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);

				}
				filewriteIntoFile.close();
				saveIntoFile(taskUpdate);
			}

			if (taskObj instanceof FloatingTask) {

				reader = new BufferedReader(new FileReader(filename_floating));
				while ((printLine = reader.readLine()) != null) {
					try {
						FloatingTask task = (FloatingTask) Serializer
								.deserializeFromJson(printLine,
										FloatingTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_UPDATE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_floating);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);

				}

				filewriteIntoFile.close();
				saveIntoFile(taskUpdate);
			}

			if (taskObj instanceof DeadlineTask) {

				reader = new BufferedReader(new FileReader(filename_deadline));
				while ((printLine = reader.readLine()) != null) {
					try {
						DeadlineTask task = (DeadlineTask) Serializer
								.deserializeFromJson(printLine,
										DeadlineTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_UPDATE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_deadline);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);
				}

				filewriteIntoFile.close();
				saveIntoFile(taskUpdate);
			}

			if (taskObj instanceof RecurrenceTask) {

				reader = new BufferedReader(new FileReader(filename_recur));
				while ((printLine = reader.readLine()) != null) {
					try {
						RecurrenceTask task = (RecurrenceTask) Serializer
								.deserializeFromJson(printLine,
										RecurrenceTask.class);
						if (task.getTaskId() != taskObj.getTaskId()) {
							taskList.add(task);
						} else {
							successObj = new Success(null, true,
									Message.SUCCESS_UPDATE);
						}
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}

				File newFile = new File(filename_recur);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);

				for (int i = 0; i < taskList.size(); i++) {

					String gsonSerial = Serializer.serializeToJson(taskList
							.get(i));
					filewriteIntoFile.println(gsonSerial);
				}

				filewriteIntoFile.close();
				saveIntoFile(taskUpdate);
			}

			reader.close();
		}

		catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * Insert the history of added task into file.
	 * 
	 * @param the
	 *            task name that is to be inserted into history
	 * @return Success object
	 */

	public Success addHistory(String toAdd) {

		LOGGER.fine("adding history: " + toAdd);

		Success status = null;
		PrintWriter filewrite = null;

		File file = new File(filename_history);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			filewrite = new PrintWriter(new BufferedWriter(new FileWriter(
					filename_history, true)));
			filewrite.println(toAdd);

			status = new Success(true, Message.SUCCESS_ADD_HISTORY);
			filewrite.close();

		} catch (IOException e) {
			status = new Success(false, Message.FAIL_ADD_HISTORY);
		} catch (Exception e) {
			status = new Success(false, Message.FAIL_ADD_HISTORY);
		}

		return status;
	}

}