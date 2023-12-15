import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * --- Day 14: Parabolic Reflector Dish ---
 *
 * You reach the place where all of the mirrors were pointing: a massive parabolic reflector dish attached to the side of another large mountain.
 *
 * The dish is made up of many small mirrors, but while the mirrors themselves are roughly in the shape of a parabolic reflector dish, each individual mirror seems to be pointing in slightly the wrong direction. If the dish is meant to focus light, all it's doing right now is sending it in a vague direction.
 *
 * This system must be what provides the energy for the lava! If you focus the reflector dish, maybe you can go where it's pointing and use the light to fix the lava production.
 *
 * Upon closer inspection, the individual mirrors each appear to be connected via an elaborate system of ropes and pulleys to a large metal platform below the dish. The platform is covered in large rocks of various shapes. Depending on their position, the weight of the rocks deforms the platform, and the shape of the platform controls which ropes move and ultimately the focus of the dish.
 *
 * In short: if you move the rocks, you can focus the dish. The platform even has a control panel on the side that lets you tilt it in one of four directions! The rounded rocks (O) will roll when the platform is tilted, while the cube-shaped rocks (#) will stay in place. You note the positions of all of the empty spaces (.) and rocks (your puzzle input). For example:
 *
 * O....#....
 * O.OO#....#
 * .....##...
 * OO.#O....O
 * .O.....O#.
 * O.#..O.#.#
 * ..O..#O..O
 * .......O..
 * #....###..
 * #OO..#....
 * Start by tilting the lever so all of the rocks will slide north as far as they will go:
 *
 * OOOO.#.O..
 * OO..#....#
 * OO..O##..O
 * O..#.OO...
 * ........#.
 * ..#....#.#
 * ..O..#.O.O
 * ..O.......
 * #....###..
 * #....#....
 * You notice that the support beams along the north side of the platform are damaged; to ensure the platform doesn't collapse, you should calculate the total load on the north support beams.
 *
 * The amount of load caused by a single rounded rock (O) is equal to the number of rows from the rock to the south edge of the platform, including the row the rock is on. (Cube-shaped rocks (#) don't contribute to load.) So, the amount of load caused by each rock in each row is as follows:
 *
 * OOOO.#.O.. 10
 * OO..#....#  9
 * OO..O##..O  8
 * O..#.OO...  7
 * ........#.  6
 * ..#....#.#  5
 * ..O..#.O.O  4
 * ..O.......  3
 * #....###..  2
 * #....#....  1
 * The total load is the sum of the load caused by all of the rounded rocks. In this example, the total load is 136.
 *
 * Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on the north support beams?
 *
 * Your puzzle answer was 110274.
 *
 * --- Part Two ---
 *
 * The parabolic reflector dish deforms, but not in a way that focuses the beam. To do that, you'll need to move the rocks to the edges of the platform. Fortunately, a button on the side of the control panel labeled "spin cycle" attempts to do just that!
 *
 * Each cycle tilts the platform four times so that the rounded rocks roll north, then west, then south, then east. After each tilt, the rounded rocks roll as far as they can before the platform tilts in the next direction. After one cycle, the platform will have finished rolling the rounded rocks in those four directions in that order.
 *
 * Here's what happens in the example above after each of the first few cycles:
 *
 * After 1 cycle:
 * .....#....
 * ....#...O#
 * ...OO##...
 * .OO#......
 * .....OOO#.
 * .O#...O#.#
 * ....O#....
 * ......OOOO
 * #...O###..
 * #..OO#....
 *
 * After 2 cycles:
 * .....#....
 * ....#...O#
 * .....##...
 * ..O#......
 * .....OOO#.
 * .O#...O#.#
 * ....O#...O
 * .......OOO
 * #..OO###..
 * #.OOO#...O
 *
 * After 3 cycles:
 * .....#....
 * ....#...O#
 * .....##...
 * ..O#......
 * .....OOO#.
 * .O#...O#.#
 * ....O#...O
 * .......OOO
 * #...O###.O
 * #.OOO#...O
 * This process should work if you leave it running long enough, but you're still worried about the north support beams. To make sure they'll survive for a while, you need to calculate the total load on the north support beams after 1000000000 cycles.
 *
 * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
 *
 * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north support beams?
 *
 * Your puzzle answer was 90982.
 */
public class Day14 {
  public static void main(String[] args) {
    Day14 day14 = new Day14();
    List<String> test = InputReader.readInput("resources/day14test.txt");
    List<String> real = InputReader.readInput("resources/day14.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day14.partOne(test));
    System.out.println("part one real: " + day14.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day14.partTwo(test));
    System.out.println("part two real: " + day14.partTwo(real));
    System.out.println("____________");
  }

  private int partOne(List<String> input) {
    return calculateResult(tiltNorth(toArray(input)));
  }

  private int partTwo(List<String> input) {
    List<String> states = new ArrayList<>();
    char[][] before = toArray(input);
    int cycleStart = -1;
    for (int i = 0; i < 1_000_000_000; i++) {
      char[][] north = tiltNorth(before);
      char[][] west = tiltWest(north);
      char[][] south = tiltSouth(west);
      char[][] east = tiltEast(south);
      String stateStr = Arrays.deepToString(east);
      if (states.contains(stateStr)) {
        cycleStart = states.indexOf(stateStr);
        break;
      }
      states.add(stateStr);
      before = east;
    }
    int result = 0;
    if (cycleStart != -1) {
      int cycleLength = states.size() - cycleStart;
      int remainingIterations = 1_000_000_000 - cycleStart;
      for (int i = cycleStart; i < cycleStart + remainingIterations % cycleLength; i++) {
        char[][] north = tiltNorth(before);
        char[][] west = tiltWest(north);
        char[][] south = tiltSouth(west);
        before = tiltEast(south);
      }
      result = calculateResult(before);
    }
    return result;
  }

  private int calculateResult(char[][] state) {
    int result = 0;
    for (int r = 0; r < state.length; r++) {
      int multiplier = state[0].length - r;
      for (int c = 0; c < state[r].length; c++) {
        if (state[r][c] == 'O') {
          result += multiplier;
        }
      }
    }
    return result;
  }

  private static void log(char[][] after) {
    for (char[] arr : after) {
      System.out.println(new String(arr));
    }
    System.out.println("____________");
  }

  private char[][] tiltNorth(char[][] input) {
    char[][] chars = traverse(input);
    for (int r = 0; r < chars.length; r++) {
      char[] row = chars[r];
      StringBuilder sb = new StringBuilder();
      String[] parts = new String(row).split("#", -1);
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        char[] partArray = part.toCharArray();
        Arrays.sort(partArray);
        char[] reversed = reverse(partArray);
        sb.append(reversed);
        if (i < parts.length - 1) {
          sb.append("#");
        }
      }
      chars[r] = sb.toString().toCharArray();
    }
    return traverse(chars);
  }

  private char[][] tiltSouth(char[][] input) {
    char[][] chars = traverse(input);
    for (int r = 0; r < chars.length; r++) {
      char[] row = chars[r];
      StringBuilder sb = new StringBuilder();
      String[] parts = new String(row).split("#", -1);
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        char[] partArray = part.toCharArray();
        Arrays.sort(partArray);
        sb.append(partArray);
        if (i < parts.length - 1) {
          sb.append("#");
        }
      }
      chars[r] = sb.toString().toCharArray();
    }
    return traverse(chars);
  }

  private char[][] tiltWest(char[][] chars) {
    for (int r = 0; r < chars.length; r++) {
      char[] row = chars[r];
      StringBuilder sb = new StringBuilder();
      String[] parts = new String(row).split("#", -1);
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        char[] partArray = part.toCharArray();
        Arrays.sort(partArray);
        char[] reversed = reverse(partArray);
        sb.append(reversed);
        if (i < parts.length - 1) {
          sb.append("#");
        }
      }
      chars[r] = sb.toString().toCharArray();
    }
    return chars;
  }

  private char[][] tiltEast(char[][] chars) {
    for (int r = 0; r < chars.length; r++) {
      char[] row = chars[r];
      StringBuilder sb = new StringBuilder();
      String[] parts = new String(row).split("#", -1);
      for (int i = 0; i < parts.length; i++) {
        String part = parts[i];
        char[] partArray = part.toCharArray();
        Arrays.sort(partArray);
        sb.append(partArray);
        if (i < parts.length - 1) {
          sb.append("#");
        }
      }
      chars[r] = sb.toString().toCharArray();
    }
    return chars;
  }

  private char[][] toArray(List<String> input) {
    char[][] chars = new char[input.size()][input.getFirst().length()];
    for (int i = 0; i < input.size(); i++) {
      for (int j = 0; j < input.getFirst().length(); j++) {
        chars[i][j] = input.get(i).charAt(j);
      }
    }
    return chars;
  }

  private char[] reverse(char[] arr) {
    int l = 0;
    int r = arr.length - 1;
    while (l < r) {
      char tmp = arr[l];
      arr[l++] = arr[r];
      arr[r--] = tmp;
    }
    return arr;
  }

  private static char[][] traverse(char[][] input) {
    char[][] chars = new char[input[0].length][input.length];
    for (int i = 0; i < input.length; i++) {
      for (int j = 0; j < input[i].length; j++) {
        chars[j][i] = input[i][j];
      }
    }
    return chars;
  }
}
