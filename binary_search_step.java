import java.io.*;
import java.util.*;

public class binary_search_step {
    static List<String> logSteps = new ArrayList<>();

    static class RowData {
        int number;
        String text;
        int index;

        RowData(int number, String text, int index) {
            this.number = number;
            this.text = text;
            this.index = index;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);

        System.out.print("Enter DataSet Filename: ");
        String fileName = scanner.nextLine();

        System.out.print("Enter Target Value: ");
        int targetValue = scanner.nextInt();

        String outputFile = "binary_search_step_" + targetValue + ".txt";

        List<RowData> list = readCSV(fileName);
        if (list == null) {
            System.err.println("Error: Unable to read dataset.");
            scanner.close();
            return;
        }

        boolean found = binarySearch(list, targetValue);
        if (found == false) {
            logSteps.add("-1");
        }

        writeStepsToFile(outputFile);
        System.out.println("Binary search steps written to " + outputFile);

        scanner.close();
    }

    public static List<RowData> readCSV(String fileName) {
        List<RowData> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            for (int index = 1; ((line = br.readLine()) != null); index++) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String text = parts[1];
                    list.add(new RowData(number, text, index));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return list;
    }

    public static boolean binarySearch(List<RowData> list, int targetValue) {
        int leftIndex = 0;
        int rightIndex = list.size() - 1;

        while (leftIndex <= rightIndex) {
            int midIndex = (leftIndex + rightIndex) / 2;
            RowData midData = list.get(midIndex);

            logSteps.add(midData.index + ":" + midData.number + "/" + midData.text);

            if (midData.number == targetValue) {
                return true;
            }
            else if (midData.number < targetValue) {
                leftIndex = midIndex + 1;
            }
            else if (midData.number > targetValue) {
                rightIndex = midIndex - 1;
            }
        }
        return false;
    }

    public static void writeStepsToFile(String outputFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < logSteps.size(); i++) {
                bw.write(logSteps.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}