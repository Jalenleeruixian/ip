import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class NewBuddyBot {
    private final FileStorage storage;
    private TaskList taskList;
    private final Ui ui;

    public NewBuddyBot(String filePath) {
        this.storage = new FileStorage(filePath);
        this.ui = new Ui();
        try {
            this.taskList = new TaskList(this.storage.readFileContents());
        } catch (BuddyBotException e) {
            this.ui.showBuddyBotException(e);
        }
    }


    private void run() {
        this.ui.welcomeMsg();
        running:
        while (true) {
            try {
                String userInput = this.ui.readInput();
                switch (Parser.parseCmd(userInput)) {
                    case BYE:
                        this.exit();
                        break running;
                    case LIST:
                        this.ui.showList(this.taskList);
                        break;
                    case TODO:
                        this.addTodo(Parser.parseArgs(userInput));
                        break;
                    case EVENT:
                        this.addEvent(Parser.parseArgs(userInput));
                        break;
                    case DEADLINE:
                        this.addDeadline(Parser.parseArgs(userInput));
                        break;
                    case DONE:
                        this.markAsDone(Parser.parseArgs(userInput));
                        break;
                    case DELETE:
                        this.delete(Parser.parseArgs(userInput));
                        break;
                }
            } catch (BuddyBotException e) {
                this.ui.showBuddyBotException(e);
            }
        }
        this.ui.goodbyeMsg();
    }


    private void exit() {
        this.ui.closeInput();
        this.storage.writeToTxt(this.taskList.toFile());
    }


    private void addTodo(String description) throws BuddyBotException {
        try {
            Task todo = new Todo(description);
            this.taskList.add(todo);
            this.ui.addTask(todo, this.taskList.size());
        } catch (IndexOutOfBoundsException e) {
            throw new BuddyBotException("Todo error");
        }
    }


    private void addEvent(String args) throws BuddyBotException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String[] splits = args.split(" /from ", 2);
            String[] timestamps = splits[1].split(" /to ", 2);
            String start = timestamps[0];
            String end = timestamps[1];
            LocalDate startTime = LocalDate.parse(start, formatter);
            LocalDate endTime = LocalDate.parse(end, formatter);
            if (startTime.isAfter(endTime)) {
                this.ui.showInvalidDateRange();
                return;
            }

            Task event = new Event(splits[0], startTime, endTime);
            this.taskList.add(event);
            this.ui.addTask(event, this.taskList.size());
        } catch (IndexOutOfBoundsException e) {
            throw new BuddyBotException("Event error");
        } catch (DateTimeParseException e) {
            this.ui.showInvalidDateFormat();
        }
    }


    private void addDeadline(String args) throws BuddyBotException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String[] splits = args.split(" /by ", 2);
            LocalDate time = LocalDate.parse(splits[1], formatter);

            Task deadline = new Deadline(splits[0], time);
            this.taskList.add(deadline);
            this.ui.addTask(deadline, this.taskList.size());
        } catch (IndexOutOfBoundsException e) {
            throw new BuddyBotException("Deadline error");
        } catch (DateTimeParseException e) {
            this.ui.showInvalidDateFormat();
        }
    }


    private void markAsDone(String args) throws BuddyBotException {
        try {
            int taskNum = Integer.parseInt(args);
            if (taskNum > this.taskList.size()) {
                throw new BuddyBotException("test");
            }
            this.taskList.get(taskNum).mark();
            this.ui.showDone(this.taskList.get(taskNum));
        } catch (NumberFormatException e) {
            throw new BuddyBotException("This is not a number");
        } catch (IndexOutOfBoundsException e) {
            throw new BuddyBotException("This task doesn't exist");
        }
    }


    private void delete(String args) throws BuddyBotException {
        try {
            int taskNum = Integer.parseInt(args);
            if (taskNum > this.taskList.size()) {
                throw new BuddyBotException("test");
            }
            Task taskToDelete = this.taskList.get(taskNum);
            this.ui.showDelete(taskToDelete, this.taskList.size());
            this.taskList.delete(taskNum);
        } catch (NumberFormatException e) {
            throw new BuddyBotException("This is not a number");
        } catch (IndexOutOfBoundsException e) {
            throw new BuddyBotException("This task doesn't exist.");
        }
    }


    public static void main(String[] args) {
        new NewBuddyBot("./data/BuddyBot.txt").run();
    }
}
