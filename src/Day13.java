import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 13: Point of Incidence ---
 *
 * With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely up to the edge of Lava Island.
 *
 * There's just one problem: you don't see any lava.
 *
 * You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around. After a while, you make your way to a nearby cluster of mountains only to discover that the valley between them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way; perhaps you should head in that direction?
 *
 * As you move through the valley of mirrors, you find that several of them have fallen from the large metal frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's safe to walk or where you're about to run into a mirror.
 *
 * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by carefully analyzing these patterns, you can figure out where the mirrors are!
 *
 * For example:
 *
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *
 * #...##..#
 * #....#..#
 * ..##..###
 * #####.##.
 * #####.##.
 * ..##..###
 * #....#..#
 * To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal line between two rows or across a vertical line between two columns.
 *
 * In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two columns point at the line between the columns:
 *
 * 123456789
 *     ><
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *     ><
 * 123456789
 * In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and can be ignored; every other column has a reflected column within the pattern and must match exactly: column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
 *
 * The second pattern reflects across a horizontal line instead:
 *
 * 1 #...##..# 1
 * 2 #....#..# 2
 * 3 ..##..### 3
 * 4v#####.##.v4
 * 5^#####.##.^5
 * 6 ..##..### 6
 * 7 #....#..# 7
 * This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8, but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7, row 3 matches row 6, and row 4 matches row 5.
 *
 * To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example, the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above it, a total of 405.
 *
 * Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of your notes?
 *
 * Your puzzle answer was 42974.
 *
 * --- Part Two ---
 *
 * You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully nobody was watching, because that must have been pretty embarrassing.
 *
 * Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one . or # should be the opposite type.
 *
 * In each pattern, you'll need to locate and fix the smudge that causes a different reflection line to be valid. (The old reflection line won't necessarily continue being valid after the smudge is fixed.)
 *
 * Here's the above example again:
 *
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *
 * #...##..#
 * #....#..#
 * ..##..###
 * #####.##.
 * #####.##.
 * ..##..###
 * #....#..#
 * The first pattern's smudge is in the top-left corner. If the top-left # were instead ., it would have a different, horizontal line of reflection:
 *
 * 1 ..##..##. 1
 * 2 ..#.##.#. 2
 * 3v##......#v3
 * 4^##......#^4
 * 5 ..#.##.#. 5
 * 6 ..##..##. 6
 * 7 #.#.##.#. 7
 * With the smudge in the top-left corner repaired, a new horizontal line of reflection between rows 3 and 4 now exists. Row 7 has no corresponding reflected row and can be ignored, but every other row matches exactly: row 1 matches row 6, row 2 matches row 5, and row 3 matches row 4.
 *
 * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
 *
 * 1v#...##..#v1
 * 2^#...##..#^2
 * 3 ..##..### 3
 * 4 #####.##. 4
 * 5 #####.##. 5
 * 6 ..##..### 6
 * 7 #....#..# 7
 * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
 *
 * Summarize your notes as before, but instead use the new different reflection lines. In this example, the first pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal line has 1 row above it, summarizing to the value 400.
 *
 * In each pattern, fix the smudge and find the different line of reflection. What number do you get after summarizing the new reflection line in each pattern in your notes?
 *
 * Your puzzle answer was 27587.
 */
public class Day13 {
  public static void main(String[] args) {
    Day13 day13 = new Day13();
    List<String> test = InputReader.readInput("resources/day13test.txt");
    List<String> real = InputReader.readInput("resources/day13.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day13.partOne(test, false));
    System.out.println("part one real: " + day13.partOne(real, false));
    System.out.println("____________");
    System.out.println("part two test: " + day13.partOne(test, true));
    System.out.println("part two real: " + day13.partOne(real, true));
    System.out.println("____________");
  }

  private int partOne(List<String> input, boolean countErrors) {
    List<List<String>> patterns = new ArrayList<>();
    List<String> pattern = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      if (line.isEmpty()) {
        patterns.add(new ArrayList<>(pattern));
        pattern.clear();
      } else {
        pattern.add(line);
      }
      if (i == input.size() - 1) {
        patterns.add(new ArrayList<>(pattern));
        pattern.clear();
      }
    }
    int result = 0;
    for (List<String> p : patterns) {
      result += calculatePattern(p, countErrors);
    }
    return result;
  }

  private int calculatePattern(List<String> pattern, boolean countMismatch) {
    List<String> cols = traverseColsToRows(pattern);
    if (countMismatch) {
      for (int i = 0; i < pattern.size(); i++) {
        for (int j = i + 1; j < pattern.size(); j++) {
          if (getErrorCount(pattern.get(i), pattern.get(j)) == 1) {
            return (j - i + 1) / 2 * 100;
          }
        }
      }
      for (int i = 0; i < cols.size(); i++) {
        for (int j = i + 1; j < cols.size(); j++) {
          if (getErrorCount(cols.get(i), cols.get(j)) == 1) {
            return (j - i + 1) / 2 * 100;
          }
        }
      }
    } else {
      for (int i = 0; i < pattern.size() - 1; i++) {
        String curr = pattern.get(i);
        String next = pattern.get(i + 1);
        if (curr.equals(next) && validateLine(pattern, i)) {
          return (i + 1) * 100;
        }
      }
      for (int i = 0; i < cols.size() - 1; i++) {
        String curr = cols.get(i);
        String next = cols.get(i + 1);
        if (curr.equals(next) && validateLine(cols, i)) {
          return (i + 1);
        }
      }
    }
    return 0;
  }

  private static int getErrorCount(String bottom, String top) {
    int errorCount = 0;
    for (int l = 0; l < bottom.length(); l++) {
      if (top.charAt(l) != bottom.charAt(l)) {
        errorCount++;
      }
    }
    return errorCount;
  }

  private boolean validateLine(List<String> cols, int i) {
    for (int j = i + 2, k = i - 1; j < cols.size() && k >= 0; j++, k--) {
      String bottom = cols.get(j);
      String top = cols.get(k);
      if (!bottom.equals(top)) {
        return false;
      }
    }
    return true;
  }

  private static List<String> traverseColsToRows(List<String> pattern) {
    List<String> cols = new ArrayList<>();
    for (int i = 0; i < pattern.getFirst().length(); i++) {
      StringBuilder curr = new StringBuilder();
      for (String s : pattern) {
        curr.append(s.charAt(i));
      }
      cols.add(curr.toString());
    }
    return cols;
  }
}
