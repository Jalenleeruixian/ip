package BuddyBot;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FileStorage {

    public String filePath;

    public FileStorage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> readFileContents() throws BuddyBotException {
        ArrayList<Task> contents = new ArrayList<>();
        try {
            File f = new File(filePath); // create a File for the given file path
            Scanner s = new Scanner(f); // create a Scanner using the File as the source
            while (s.hasNextLine()) {
                contents.add(readEntry(s.nextLine()));
            }
            s.close();
        } catch (FileNotFoundException e) {
            throw new BuddyBotException("File not found!t");
        }
        return contents;
    }
    private Task readEntry(String entry) {
        String[] fields = entry.split("\\|");
        //  System.out.println(Arrays.toString(fields));  // debug
        Task taskToAdd;
//        for (String field: fields) {
//            System.out.println(field);
//        }
        switch (fields[0]) {
            case "E":
                taskToAdd = new Event(fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]));
                break;
            case "D":
                taskToAdd = new Deadline(fields[2], LocalDate.parse(fields[3]));
                break;
            default: // case "T":
                System.out.println(fields[2]);
                taskToAdd = new Todo(fields[2]);
                break;
        }
        if ((fields[1]).equals("X")) {
            taskToAdd.mark();
        }
        return taskToAdd;
    }

    public void writeToTxt(String myTasks) { //Using this method
        try  {
            File file = new File(this.filePath);
            File dir = new File(file.getParent());
            boolean dirCreated = dir.mkdirs();
            FileWriter fw = new FileWriter(this.filePath, false);
            BufferedWriter bw = new BufferedWriter(fw);
           bw.write(myTasks);
           bw.newLine();
           bw.close();
           fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
