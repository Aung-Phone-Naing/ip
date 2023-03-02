package command;

import exceptions.IncompleteInputException;
import parser.Parser;
import tasks.Deadline;
import tasks.Event;
import tasks.TaskList;
import tasks.ToDo;
import ui.Ui;

public class Command {
    private final CommandType type;
    private final String fullDescription;

    public Command(CommandType type, String fullDescription) {
        this.type = type;
        this.fullDescription = fullDescription;
    }
    public boolean isExit() {
        return this.type == CommandType.EXIT;
    }
    public void execute(TaskList taskList) {
        boolean isChangedMarking = false;
        String description;
        String[] parsedFullDescription;
        int taskListIndex;

        switch (type) {
        case LIST:
            Ui.showTaskList(taskList.getTasksList());
            break;
        case MARK:
            try {
                if (fullDescription.isEmpty()) {
                    throw new IncompleteInputException("The index of " + type + " cannot be empty.\n");
                }
                taskListIndex = Integer.parseInt(fullDescription);
                if (!taskList.checkTaskDone(taskListIndex)) {
                    taskList.setTaskAsDone(taskListIndex);
                    isChangedMarking = true;
                }
                Ui.showMarkTask(isChangedMarking, taskList.getTaskFromList(taskListIndex), type);
            } catch (Exception e) {
                Ui.showMarkingErrorMessage(e, type);
            }
            break;
        case UNMARK:
            try {
                if (fullDescription.isEmpty()) {
                    throw new IncompleteInputException("The index of " + type + " cannot be empty.\n");
                }
                taskListIndex = Integer.parseInt(fullDescription);
                if (taskList.checkTaskDone(taskListIndex)) {
                    taskList.setTaskAsUndone(taskListIndex);
                    isChangedMarking = true;
                }
                Ui.showMarkTask(isChangedMarking, taskList.getTaskFromList(taskListIndex), type);
            } catch (Exception e) {
                Ui.showMarkingErrorMessage(e, type);
            }
            break;
        case TODO:
            try {
                if (fullDescription.isEmpty()) {
                    throw new IncompleteInputException("The description of " + type + " cannot be empty.\n");
                }
                taskList.addNewTask(new ToDo(fullDescription));
                Ui.showTaskAdded(taskList.getNewestTask(), taskList.getCurrTaskNumber());
            } catch (Exception e) {
                Ui.showAddingTaskErrorMessage(e);
            }
            break;
        case DEADLINE:
            try {
                parsedFullDescription = Parser.parseDeadline(fullDescription);
                description = parsedFullDescription[0];
                String deadlineBy = parsedFullDescription[1];
                taskList.addNewTask(new Deadline(description, deadlineBy));
                Ui.showTaskAdded(taskList.getNewestTask(), taskList.getCurrTaskNumber());
            } catch (Exception e) {
                Ui.showAddingTaskErrorMessage(e);
            }
            break;
        case EVENT:
            try {
                parsedFullDescription = Parser.parseEvent(fullDescription);
                description = parsedFullDescription[0];
                String eventFrom = parsedFullDescription[1];
                String eventTo = parsedFullDescription[2];
                taskList.addNewTask(new Event(description, eventFrom, eventTo));
                Ui.showTaskAdded(taskList.getNewestTask(), taskList.getCurrTaskNumber());
            } catch (Exception e) {
                Ui.showAddingTaskErrorMessage(e);
            }
            break;
        case DELETE:
            try {
                if (fullDescription.isEmpty()) {
                    throw new IncompleteInputException("The index of " + type + " cannot be empty.\n");
                }
                taskListIndex = Integer.parseInt(fullDescription);
                Ui.showTaskDeleted(taskList.getTaskFromList(taskListIndex), taskList.getCurrTaskNumber() - 1);
                taskList.deleteTask(taskListIndex);
            } catch (Exception e) {
                Ui.showDeletingTaskErrorMessage(e, type);
            }
            break;
        case FIND:
            System.out.println("\nFeature to be implemented in next Level.\n");
            break;
        case HELP:
            Ui.showHelp();
            break;
        case EXIT:
            Ui.showBye();
            break;
        case UNKNOWN:
            Ui.showUnrecognizableErrorMessage();
            break;
        }
    }
}
