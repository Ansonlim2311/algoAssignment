import java.io.*; //reading&writing java files
import java.util.*; //provides arrayList, Scanner....

public class quick_sort_step{
    //logSteps = list, it was needed to store strings
    static List<String> logSteps = new ArrayList<>(); 

    public static void main(String[] args) {

        //Scanner to read input from users
        Scanner scanner = new Scanner(System.in);  

        //User input
        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();

        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();
        
        //output filename
        String outputFile = "quick_sort_step_" + startRow + "_" + endRow + ".txt";

        //calls a helper method to read number range from file 
        int[] numbers = readCSVRange(inputFile, startRow, endRow);
        if (numbers == null) { //if reading fails, return 
            scanner.close();
            return;
        }

        //logSteps = logging the steps (like writing the messages about the steps...)
        logSteps.add("Before QuickSort: " + Arrays.toString(numbers)); //convert array of numbers into string
        quickSort(numbers, 0, numbers.length - 1); //call the function quickSort
        logSteps.add("After QuickSort:  " + Arrays.toString(numbers));

        writeStepsToFile(outputFile);
        System.out.println("Quick sort steps written to " + outputFile);

        scanner.close();
    }

    //read lines from CSV and collect integers
     public static int[] readCSVRange(String filename, int start, int end) { //read range 
        List<Integer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) { //try to read the file
            String line;
            for (int row = 0; (line = br.readLine()) != null; row++) {
                if (row < start) {
                    continue;
                }
                if (row > end) { 
                    break;
                }
                //if its within the user-specified range, split by commas
                list.add(Integer.valueOf(line.split(",", 2)[0])); //convert each walue to an integer and store it 
            }
        } catch (IOException e) {  //if cannot read the file, use catch to print error message 
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return list.stream().mapToInt(i -> i).toArray(); //convert string to integers and place in array, and put in an arrayList called list in order to compare number.
    }

    public static void quickSort(){

    }

    public static void writeStepsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < logSteps.size(); i++) {
                bw.write(logSteps.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}

