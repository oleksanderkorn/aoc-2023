import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputReader {
    public static List<String> readInput(String path) {
        List<String> input = new ArrayList<>();
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            reader.close();
        } catch (IOException ignored) {
        }
        return input;
    }
}
