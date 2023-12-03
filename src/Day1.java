import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * --- Day 1: Trebuchet?! ---
 *
 * <p>Something is wrong with global snow production, and you've been selected to take a look. The
 * Elves have even given you a map; on it, they've used stars to mark the top fifty locations that
 * are likely to be having problems.
 *
 * <p>You've been doing this long enough to know that to restore snow operations, you need to check
 * all fifty stars by December 25th.
 *
 * <p>Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent
 * calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star.
 * Good luck!
 *
 * <p>You try to ask why they can't just use a weather machine ("not powerful enough") and where
 * they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of
 * questions") and hang on did you just say the sky ("of course, where do you think snow comes
 * from") when you realize that the Elves are already loading you into a trebuchet ("please hold
 * still, we need to strap you in").
 *
 * <p>As they're making the final adjustments, they discover that their calibration document (your
 * puzzle input) has been amended by a very young Elf who was apparently just excited to show off
 * her art skills. Consequently, the Elves are having trouble reading the values on the document.
 *
 * <p>The newly-improved calibration document consists of lines of text; each line originally
 * contained a specific calibration value that the Elves now need to recover. On each line, the
 * calibration value can be found by combining the first digit and the last digit (in that order) to
 * form a single two-digit number.
 *
 * <p>For example:
 *
 * <p>1abc2 pqr3stu8vwx a1b2c3d4e5f treb7uchet In this example, the calibration values of these four
 * lines are 12, 38, 15, and 77. Adding these together produces 142.
 *
 * <p>Consider your entire calibration document. What is the sum of all of the calibration values?
 *
 * <p>Your puzzle answer was 53194.
 *
 * <p>--- Part Two ---
 *
 * <p>Your calculation isn't quite right. It looks like some of the digits are actually spelled out
 * with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid
 * "digits".
 *
 * <p>Equipped with this new information, you now need to find the real first and last digit on each
 * line. For example:
 *
 * <p>two1nine eightwothree abcone2threexyz xtwone3four 4nineeightseven2 zoneight234 7pqrstsixteen
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together
 * produces 281.
 *
 * <p>What is the sum of all of the calibration values?
 *
 * <p>Your puzzle answer was 54249.
 */
public class Day1 {
    public static void main(String[] args) {
        System.out.println(solve(numbersOne()));
        System.out.println(solve(numbersTwo()));
    }

    private static int solve(Map<String, String> numbers) {
        List<String> input = InputReader.readInput("resources/day1.txt");
        int result = 0;
        for (String str : input) {
            int firstIndex = Integer.MAX_VALUE;
            String first = "";
            String last = "";
            int lastIndex = Integer.MIN_VALUE;
            for (String number : numbers.keySet()) {
                if (str.contains(number)) {
                    if (str.indexOf(number) < firstIndex) {
                        firstIndex = str.indexOf(number);
                        first = numbers.get(number);
                    }
                    if (str.lastIndexOf(number) > lastIndex) {
                        lastIndex = str.lastIndexOf(number);
                        last = numbers.get(number);
                    }
                }
            }
            result += Integer.parseInt(first + last);
        }
        return result;
    }

    private static Map<String, String> numbersOne() {
        Map<String, String> numbers = new HashMap<>();
        numbers.put("1", "1");
        numbers.put("2", "2");
        numbers.put("3", "3");
        numbers.put("4", "4");
        numbers.put("5", "5");
        numbers.put("6", "6");
        numbers.put("7", "7");
        numbers.put("8", "8");
        numbers.put("9", "9");
        return numbers;
    }

    private static Map<String, String> numbersTwo() {
        Map<String, String> numbers = new HashMap<>();
        numbers.put("1", "1");
        numbers.put("2", "2");
        numbers.put("3", "3");
        numbers.put("4", "4");
        numbers.put("5", "5");
        numbers.put("6", "6");
        numbers.put("7", "7");
        numbers.put("8", "8");
        numbers.put("9", "9");
        numbers.put("one", "1");
        numbers.put("two", "2");
        numbers.put("three", "3");
        numbers.put("four", "4");
        numbers.put("five", "5");
        numbers.put("six", "6");
        numbers.put("seven", "7");
        numbers.put("eight", "8");
        numbers.put("nine", "9");
        return numbers;
    }

    private static int partOneFirstTry() {
        List<String> input = InputReader.readInput("resources/day1.txt");
        int result = 0;
        int first = 0;
        for (String str : input) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {
                    first = i;
                    break;
                }
            }
            int last = str.length() - 1;
            for (int i = str.length() - 1; i >= first; i--) {
                if (Character.isDigit(str.charAt(i))) {
                    last = i;
                    break;
                }
            }

            result += Integer.parseInt("" + str.charAt(first) + str.charAt(last));
        }
        return result;
    }
}
