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

public class FileIO {

	private String filename_floating = null;
	private String filename_normal = null;
	private String filename_recur = null;
	private String filename_deadline = null;
	private String filename_history = null;

	private static String file_type = null;

	public FileIO() {

		filename_floating = FileName.getFilenameFloating();
		filename_normal = FileName.getFilenameNormal();
		filename_recur = FileName.getFilenameRecur();
		filename_deadline = FileName.getFilenameDeadline();
		filename_history = FileName.getFilenameHistory();
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

		Success status = null;

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
	 * 			Type of task which parse in.
	 * @return String	
	 * 			Name of file for the task parse in.
	 */
	// @author A0112694E
	private String getFileType(Task task) {

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
	 * load task from data file base the on keyword for the class.
	 *
	 * @param String
	 *            keyword which specific which class type to retrieve
	 * @return Success Object which contain the success/failure message
	 */
	// @author A0112694E
	public Success loadFromFileTask(String file_keyword) {

		Success successObj;
		BufferedReader reader = null;

		file_type = getFileTypeWithKeyword(file_keyword);

		try {
			List<Task> taskList = new ArrayList<Task>();

			reader = new BufferedReader(new FileReader(file_type));
			String printLine;

			if (file_keyword
					.equalsIgnoreCase(KeywordConstant.KEYWORD_FLOATING_TASK)) {
				while ((printLine = reader.readLine()) != null) {
					try {
						Task task = (FloatingTask) Serializer
								.deserializeFromJson(printLine,
										FloatingTask.class);
						taskList.add(task);
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}
			} else if (file_keyword
					.equalsIgnoreCase(KeywordConstant.KEYWORD_NORMAL_TASK)) {
				while ((printLine = reader.readLine()) != null) {
					try {
						Task task = (NormalTask) Serializer
								.deserializeFromJson(printLine,
										NormalTask.class);
						taskList.add(task);
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}
			} else if (file_keyword
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DEADLINE_TASK)) {
				while ((printLine = reader.readLine()) != null) {
					try {
						Task task = (DeadlineTask) Serializer
								.deserializeFromJson(printLine,
										DeadlineTask.class);
						taskList.add(task);
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}
			} else if (file_keyword
					.equalsIgnoreCase(KeywordConstant.KEYWORD_RECUR_TASK)) {
				while ((printLine = reader.readLine()) != null) {
					try {
						Task task = (RecurrenceTask) Serializer
								.deserializeFromJson(printLine,
										RecurrenceTask.class);
						taskList.add(task);
					} catch (JsonSyntaxException e) {
						// skip error
					}
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);
			reader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}
	
	/**
	 * get the file name the on keyword for the file.
	 *
	 * @param String
	 *            keyword which specific which file name to be return
	 * @return String 
	 *			  the name of file from the keyword.
	 */
	// @author A0112694E
	private String getFileTypeWithKeyword(String file_keyword) {

		String type = "";

		if (file_keyword
				.equalsIgnoreCase(KeywordConstant.KEYWORD_FLOATING_TASK)) {
			type = filename_floating;
		} else if (file_keyword
				.equalsIgnoreCase(KeywordConstant.KEYWORD_NORMAL_TASK)) {
			type = filename_normal;
		} else if (file_keyword
				.equalsIgnoreCase(KeywordConstant.KEYWORD_RECUR_TASK)) {
			type = filename_recur;
		} else if (file_keyword
				.equalsIgnoreCase(KeywordConstant.KEYWORD_DEADLINE_TASK)) {
			type = filename_deadline;
		}

		return type;
	}

	/**
	 * load task from data file base on the input date
	 *
	 * @param date
	 *            which specific which task to retrieve
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadFromStartDate(Date date) {

		Success successObj;
		String printLine;
		BufferedReader recurReader = null;
		BufferedReader deadlineReader = null;
		BufferedReader normalReader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();

			recurReader = new BufferedReader(new FileReader(filename_recur));
			deadlineReader = new BufferedReader(new FileReader(
					filename_deadline));
			normalReader = new BufferedReader(new FileReader(filename_normal));

			while ((printLine = normalReader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);

					if (checkNormalTaskDate(task, date)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getDeadline().compareTo(date) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getStartRecurrenceDate().getDate() == date
							.getDate()
							&& task.getStartRecurrenceDate().getMonth() == date
									.getMonth()
							&& task.getStartRecurrenceDate().getYear() == date
									.getYear()) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			recurReader.close();
			deadlineReader.close();
			normalReader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * load task from data file in between the start date and end date.
	 *
	 * @param Date
	 *            startDate which specific the start date
	 * @param Date
	 *            endDate which specific the end date
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadFromBetweenDate(Date startDate, Date endDate) {

		Success successObj;
		BufferedReader recurReader = null;
		BufferedReader deadlineReader = null;
		BufferedReader normalReader = null;
		try {
			List<Task> taskList = new ArrayList<Task>();

			recurReader = new BufferedReader(new FileReader(filename_recur));
			deadlineReader = new BufferedReader(new FileReader(
					filename_deadline));
			normalReader = new BufferedReader(new FileReader(filename_normal));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (checkNormalTaskBetweenDate(task, startDate, endDate)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.

					if (task.getDeadline().compareTo(startDate) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getStartRecurrenceDate().compareTo(startDate) > 0
							&& task.getStartRecurrenceDate().compareTo(endDate) <= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			recurReader.close();
			deadlineReader.close();
			normalReader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * load task from data file base on task priority
	 *
	 * @param int specific task priority for the task
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadFromPriority(int priority) {

		Success successObj;
		BufferedReader recurReader = null;
		BufferedReader deadlineReader = null;
		BufferedReader normalReader = null;
		BufferedReader floatReader = null;
		try {
			List<Task> taskList = new ArrayList<Task>();

			recurReader = new BufferedReader(new FileReader(filename_recur));
			deadlineReader = new BufferedReader(new FileReader(
					filename_deadline));
			normalReader = new BufferedReader(new FileReader(filename_normal));
			floatReader = new BufferedReader(new FileReader(filename_floating));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getPriority() == priority) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getPriority() == priority) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getPriority() == priority) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = floatReader.readLine()) != null) {
				try {
					FloatingTask task = (FloatingTask) Serializer
							.deserializeFromJson(printLine, FloatingTask.class);
					if (task.getPriority() == priority) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			recurReader.close();
			deadlineReader.close();
			normalReader.close();
			floatReader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * load task from data file base on its priority and its date
	 *
	 * @param int specific task priority for the task
	 * @param Date
	 *            date which specific the end date
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadFromPriorityAndDate(int priority, Date date) {

		Success successObj;
		BufferedReader recurReader = null;
		BufferedReader deadlineReader = null;
		BufferedReader normalReader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();

			recurReader = new BufferedReader(new FileReader(filename_recur));
			deadlineReader = new BufferedReader(new FileReader(
					filename_deadline));
			normalReader = new BufferedReader(new FileReader(filename_normal));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getPriority() == priority
							&& checkNormalTaskDate(task, date)) {

						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getPriority() == priority
							&& task.getDeadline().compareTo(date) > 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getPriority() == priority
							&& task.getStartRecurrenceDate().getDate() == date
									.getDate()
							&& task.getStartRecurrenceDate().getMonth() == date
									.getMonth()
							&& task.getStartRecurrenceDate().getYear() == date
									.getYear()) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			recurReader.close();
			deadlineReader.close();
			normalReader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * load task from data file in between the start date,end date and priority.
	 *
	 * @param int specific task priority for the task
	 * @param Date
	 *            startDate which specific the start date
	 * @param Date
	 *            endDate which specific the end date
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadFromPriorityBetweenDate(int priority, Date startDate,
			Date endDate) {

		Success successObj;
		BufferedReader recurReader = null;
		BufferedReader deadlineReader = null;
		BufferedReader normalReader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();

			recurReader = new BufferedReader(new FileReader(filename_recur));
			deadlineReader = new BufferedReader(new FileReader(
					filename_deadline));
			normalReader = new BufferedReader(new FileReader(filename_normal));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getPriority() == priority
							&& checkNormalTaskBetweenDate(task, startDate,
									endDate)) {

						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getPriority() == priority
							&& task.getDeadline().compareTo(startDate) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getPriority() == priority
							&& task.getStartRecurrenceDate().compareTo(
									startDate) > 0
							&& task.getStartRecurrenceDate().compareTo(endDate) <= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			recurReader.close();
			deadlineReader.close();
			normalReader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	/**
	 * load task from data file which already marked as done
	 *
	 * @param boolean the specific boolean which indicate wether task is already
	 *        completed
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success getCompletedTask(boolean isCompleted) {
		Success successObj;
		BufferedReader reader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.isCompleted() == isCompleted) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));
			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.isCompleted() == isCompleted) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}

			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_floating));
			while ((printLine = reader.readLine()) != null) {
				try {
					FloatingTask task = (FloatingTask) Serializer
							.deserializeFromJson(printLine, FloatingTask.class);
					if (task.isCompleted() == isCompleted) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			reader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * load task from data file which already marked as done on a date.
	 *
	 * @param boolean the specific boolean which indicate wether task is already
	 *        completed
	 * @param Date
	 *            date which specific the task date
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadCompletedTaskWithDate(boolean isCompleted, Date date) {
		Success successObj;
		BufferedReader reader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.isCompleted() == isCompleted
							&& checkNormalTaskDate(task, date)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));

			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.isCompleted() == isCompleted
							&& task.getDeadline().compareTo(date) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}

			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_floating));

			while ((printLine = reader.readLine()) != null) {
				try {
					FloatingTask task = (FloatingTask) Serializer
							.deserializeFromJson(printLine, FloatingTask.class);
					if (task.isCompleted() == isCompleted) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}

			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			reader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * load task from data file which already marked as done from a start date
	 * to an end date.
	 *
	 * @param boolean The specific boolean which indicate wether task is already
	 *        completed
	 * @param Date
	 *            start date which specific the task date
	 * @param Date
	 *            end date which specific the task date
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success loadCompletedTaskBetweenDate(boolean isCompleted,
			Date startDate, Date endDate) {
		Success successObj;
		BufferedReader reader = null;

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.isCompleted() == isCompleted
							&& checkNormalTaskBetweenDate(task, startDate,
									endDate)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));

			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.isCompleted() == isCompleted
							&& task.getDeadline().compareTo(startDate) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}

			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_floating));

			while ((printLine = reader.readLine()) != null) {
				try {
					FloatingTask task = (FloatingTask) Serializer
							.deserializeFromJson(printLine, FloatingTask.class);
					if (task.isCompleted() == isCompleted) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}

			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			reader.close();

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * search for a list of task base on the keyword entered
	 *
	 * @param String
	 *            specific keyword which user enter to search
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success searchFromFile(String keyword) {
		Success successObj;
		BufferedReader reader = null;

		keyword = keyword.trim();

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));

			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_recur));

			while ((printLine = reader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_floating));

			while ((printLine = reader.readLine()) != null) {
				try {
					FloatingTask task = (FloatingTask) Serializer
							.deserializeFromJson(printLine, FloatingTask.class);

					if (task.getTaskName().toLowerCase().contains(keyword)) {

						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

			reader.close();
		}

		catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * search for a list of task base on the keyword entered on a date
	 *
	 * @param String
	 *            specific keyword which user enter to search
	 * @param Date
	 *            specific date which user enter to search
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success searchFromFileWithDate(String keyword, Date date) {
		Success successObj;
		BufferedReader reader = null;

		keyword = keyword.trim();

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& checkNormalTaskDate(task, date)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));

			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& task.getDeadline().compareTo(date) > 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_recur));

			while ((printLine = reader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& task.getStartRecurrenceDate().getDate() == date
									.getDate()
							&& task.getStartRecurrenceDate().getMonth() == date
									.getMonth()
							&& task.getStartRecurrenceDate().getYear() == date
									.getYear()) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}
			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);
			reader.close();
		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * search for a list of task base on the keyword entered from a start date
	 * to an end date
	 *
	 * @param String
	 *            specific keyword which user enter to search
	 * @param Date
	 *            specific start date which user enter to search
	 * @param Date
	 *            specific end date which user enter to search
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success searchFromFileBetweenDate(String keyword, Date startDate,
			Date endDate) {
		Success successObj;
		BufferedReader reader = null;

		keyword = keyword.trim();

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			reader = new BufferedReader(new FileReader(filename_normal));
			while ((printLine = reader.readLine()) != null) {
				try {
					NormalTask task = (NormalTask) Serializer
							.deserializeFromJson(printLine, NormalTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& checkNormalTaskBetweenDate(task, startDate,
									endDate)) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_deadline));

			while ((printLine = reader.readLine()) != null) {
				try {
					DeadlineTask task = (DeadlineTask) Serializer
							.deserializeFromJson(printLine, DeadlineTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& task.getDeadline().compareTo(startDate) >= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}

			reader.close();

			reader = new BufferedReader(new FileReader(filename_recur));

			while ((printLine = reader.readLine()) != null) {
				try {
					RecurrenceTask task = (RecurrenceTask) Serializer
							.deserializeFromJson(printLine,
									RecurrenceTask.class);
					if (task.getTaskName().toLowerCase().contains(keyword)
							&& task.getStartRecurrenceDate().compareTo(
									startDate) > 0
							&& task.getStartRecurrenceDate().compareTo(endDate) <= 0) {
						taskList.add(task);
					}
				} catch (JsonSyntaxException e) {
					// skip error
				}
			}
			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);
			reader.close();
		}

		catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	/**
	 * Delete task from the data file
	 *
	 * @param Task
	 *            the specific task that need to be deleted from the data file
	 * @return Success Object which contain message and task ArrayList from data
	 *         file
	 */
	// @author A0112694E
	public Success deleteFromFile(Task taskObj) {
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
	// @author A0112694E
	public Success updateFromFile(Task taskUpdate, Task taskObj) {
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
	 *
	 * @param
	 * @return
	 */
	// @author
	public Success addHistory(String toAdd) {

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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public Success getHistory() {

		Success status = null;

		File file = new File(filename_history);

		if (file.exists()) {

			ArrayList<String> historyList = new ArrayList<String>();

			try {
				BufferedReader historyReader = new BufferedReader(
						new FileReader(filename_history));

				String printLine;

				while ((printLine = historyReader.readLine()) != null) {
					historyList.add(printLine);
				}

				status = new Success(historyList, true,
						Message.SUCCESS_GET_HISTORY);

				historyReader.close();

			} catch (FileNotFoundException e) {
				status = new Success(false, Message.FAIL_GET_HISTORY);
			} catch (Exception e) {
				status = new Success(false, Message.FAIL_GET_HISTORY);
			}
		} else {
			status = new Success(false, Message.FAIL_GET_HISTORY);
		}

		return status;
	}

	/**
	 * Check wether the task date and the date entered are the same
	 *
	 * @param Task
	 *            the task which have the task date to be compared
	 * @param Date
	 *            the date that need to be compared
	 * @return boolean true if the both date are the same
	 */
	// @author A0112694E
	public static boolean checkNormalTaskDate(NormalTask task, Date date) {
		boolean matchDate = false;

		if (task.getStartDateTime().equals(task.getEndDateTime())) {
			if (task.getStartDateTime().getDate() == date.getDate()
					&& task.getStartDateTime().getMonth() == date.getMonth()
					&& task.getStartDateTime().getYear() == date.getYear()) {
				matchDate = true;
			}
		} else {
			if (task.getStartDateTime().compareTo(date) <= 0
					&& task.getEndDateTime().compareTo(date) >= 0) {
				matchDate = true;
			}

		}
		return matchDate;
	}

	/**
	 * Check wether the task date is the between the start and end date entered.
	 *
	 * @param Task
	 *            the task which have the task date to be compared
	 * @param Date
	 *            the start date that need to be compared
	 * @param Date
	 *            the end date that need to be compared
	 * @return boolean true if the both date are the same
	 */
	// @author A0112694E
	public static boolean checkNormalTaskBetweenDate(NormalTask task,
			Date startDate, Date endDate) {
		boolean matchDate = false;

		if ((task.getEndDateTime().compareTo(endDate) >= 0 && task
				.getStartDateTime().compareTo(endDate) <= 0)
				|| (task.getEndDateTime().compareTo(startDate) >= 0 && task
						.getStartDateTime().compareTo(startDate) <= 0)
				|| (task.getEndDateTime().compareTo(endDate) <= 0 && task
						.getStartDateTime().compareTo(startDate) >= 0)) {
			matchDate = true;
		}

		return matchDate;
	}
}