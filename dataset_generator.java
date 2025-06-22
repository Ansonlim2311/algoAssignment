import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class dataset_generator {
    static final int maxValue = 1000000000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements to generate: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        String filename = "dataset_" + n + ".csv";

        generateDataset(filename, n);

        scanner.close();
    }

    public static void generateDataset(String filename, int n) {
        Random rand = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();
        List<String> data = new ArrayList<>();

        while (uniqueNumbers.size() < n) {
            int num = rand.nextInt(maxValue) + 1;
            if (uniqueNumbers.add(num)) {
                String randomString = generateRandomString(rand);
                data.add(num + "," + randomString);
            }
        }

        Collections.shuffle(data);

        try {
            FileWriter writer = new FileWriter(filename);
            for (String line : data) {
                writer.write(line + "\n");
            }
            writer.close();
            System.out.println("Dataset written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String generateRandomString(Random rand) {
        int length = 4 + rand.nextInt(9);
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String result = "";

        for (int i = 0; i < length; i++) {
            result = result + letters.charAt(rand.nextInt(letters.length()));
        }
        return result;
    }
}