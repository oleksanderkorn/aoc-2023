import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 3: Gear Ratios ---
 *
 * <p>You and the Elf eventually reach a gondola lift station; he says the gondola lift will take
 * you up to the water source, but this is as far as he can bring you. You go inside.
 *
 * <p>It doesn't take long to find the gondolas, but there seems to be a problem: they're not
 * moving.
 *
 * <p>"Aaah!"
 *
 * <p>You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I
 * wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before
 * I can fix it." You offer to help.
 *
 * <p>The engineer explains that an engine part seems to be missing from the engine, but nobody can
 * figure out which one. If you can add up all the part numbers in the engine schematic, it should
 * be easy to work out which part is missing.
 *
 * <p>The engine schematic (your puzzle input) consists of a visual representation of the engine.
 * There are lots of numbers and symbols you don't really understand, but apparently any number
 * adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum.
 * (Periods (.) do not count as a symbol.)
 *
 * <p>Here is an example engine schematic:
 *
 * <p>467..114.. ...*...... ..35..633. ......#... 617*...... .....+.58. ..592..... ......755.
 * ...$.*.... .664.598.. In this schematic, two numbers are not part numbers because they are not
 * adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a
 * symbol and so is a part number; their sum is 4361.
 *
 * <p>Of course, the actual engine schematic is much larger. What is the sum of all of the part
 * numbers in the engine schematic?
 *
 * <p>Your puzzle answer was 527369.
 *
 * <p>--- Part Two ---
 *
 * <p>The engineer finds the missing part and installs it in the engine! As the engine springs to
 * life, you jump in the closest gondola, finally ready to ascend to the water source.
 *
 * <p>You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the
 * gondola has a phone labeled "help", so you pick it up and the engineer answers.
 *
 * <p>Before you can explain the situation, she suggests that you look out the window. There stands
 * the engineer, holding a phone in one hand and waving with the other. You're going so slowly that
 * you haven't even left the station. You exit the gondola.
 *
 * <p>The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is
 * any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of
 * multiplying those two numbers together.
 *
 * <p>This time, you need to find the gear ratio of every gear and add them all up so that the
 * engineer can figure out which gear needs to be replaced.
 *
 * <p>Consider the same engine schematic again:
 *
 * <p>467..114.. ...*...... ..35..633. ......#... 617*...... .....+.58. ..592..... ......755.
 * ...$.*.... .664.598.. In this schematic, there are two gears. The first is in the top left; it
 * has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right;
 * its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one
 * part number.) Adding up all of the gear ratios produces 467835.
 *
 * <p>What is the sum of all of the gear ratios in your engine schematic?
 *
 * <p>Your puzzle answer was 73074886.
 */
public class Day3 {
  public static void main(String[] args) {
    Day3 day3 = new Day3();
    System.out.println("________________________");
    String realPath = "resources/day3.txt";
    String testPath = "resources/day3test.txt";
    System.out.println("part one test: " + day3.partOne(testPath));
    System.out.println("part one real: " + day3.partOne(realPath));
    System.out.println("________________________");
    System.out.println("part two test: " + day3.partTwo(testPath));
    System.out.println("part two real: " + day3.partTwo(realPath));
    System.out.println("________________________");
  }

  private int partTwo(String path) {
    List<String> strings = InputReader.readInput(path);
    int result = 0;
    for (int i = 0; i < strings.size(); i++) {
      String line = strings.get(i);
      for (int j = 0; j < line.length(); j++) {
        if (line.charAt(j) == '*') {
          List<Integer> numbers = findNumbers(i, strings, j);
          if (numbers.size() == 2) {
            Integer first = numbers.get(0);
            Integer second = numbers.get(1);
            result += first * second;
          }
        }
      }
    }
    return result;
  }

  private List<Integer> findNumbers(int l, List<String> strings, int gear) {
    List<Integer> numbers = new ArrayList<>();
    for (int i = Math.max(0, l - 1); i <= Math.min(l + 1, strings.size() - 1); i++) {
      String line = strings.get(i);
      if (i == l) {
        if (Character.isDigit(line.charAt(gear - 1))) {
          int end = gear - 1;
          int start = gear - 1;
          while (start >= 0 && Character.isDigit(line.charAt(start))) {
            start--;
          }
          String substring = line.substring(start + 1, end + 1);
          numbers.add(Integer.parseInt(substring));
        }
        if (Character.isDigit(line.charAt(gear + 1))) {
          int start = gear + 1;
          int end = gear + 1;
          while (end < line.length() && Character.isDigit(line.charAt(end))) {
            end++;
          }
          if (start == end) continue;
          String substring = line.substring(start, end);
          numbers.add(Integer.parseInt(substring));
        }
      } else {
        for (int r = 0; r < line.length(); r++) {
          int start = r;
          while (r < line.length() && Character.isDigit(line.charAt(r))) {
            r++;
          }
          String substring = line.substring(start, r);
          if (start == r) continue;
          if (gear >= start - 1 && gear <= r) {
            numbers.add(Integer.parseInt(substring));
          }
        }
      }
    }
    return numbers;
  }

  private int partOne(String path) {
    int result = 0;
    List<String> strings = InputReader.readInput(path);
    for (int i = 0; i < strings.size(); i++) {
      String line = strings.get(i);
      for (int r = 0; r < line.length(); r++) {
        int l = r;
        while (r < line.length() && Character.isDigit(line.charAt(r))) {
          r++;
        }
        String substring = line.substring(l, r);
        if (l == r) continue;
        int num = Integer.parseInt(substring);
        boolean prevLineAdj = i > 0 && isValid(l - 1, r, i - 1, strings, false);
        boolean currLineAdj = isValid(l - 1, r, i, strings, true);
        boolean nextLineAdj = i < line.length() - 2 && isValid(l - 1, r, i + 1, strings, false);
        if (prevLineAdj || currLineAdj || nextLineAdj) {
          result += num;
        }
      }
    }
    return result;
  }

  boolean isValid(int l, int r, int line, List<String> strings, boolean sameLine) {
    String string = strings.get(line);
    if (sameLine) {
      return (l >= 0 && string.charAt(l) != '.')
          || (r < string.length() - 2
              && string.charAt(r) != '.'
              && !Character.isDigit(string.charAt(r)));
    } else {
      for (int i = l; i <= r; i++) {
        if (i < 0 || i > string.length() - 2) continue;
        if (string.charAt(i) != '.' && !Character.isDigit(string.charAt(i))) return true;
      }
    }
    return false;
  }
}
