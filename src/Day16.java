import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * --- Day 16: The Floor Will Be Lava ---
 *
 * With the beam of light completely focused somewhere, the reindeer leads you deeper still into the Lava Production Facility. At some point, you realize that the steel facility walls have been replaced with cave, and the doorways are just cave, and the floor is cave, and you're pretty sure this is actually just a giant cave.
 *
 * Finally, as you approach what must be the heart of the mountain, you see a bright light in a cavern up ahead. There, you discover that the beam of light you so carefully focused is emerging from the cavern wall closest to the facility and pouring all of its energy into a contraption on the opposite side.
 *
 * Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.), mirrors (/ and \), and splitters (| and -).
 *
 * The contraption is aligned so that most of the beam bounces around the grid, but each tile on the grid converts some of the beam's light into heat to melt the rock in the cavern.
 *
 * You note the layout of the contraption (your puzzle input). For example:
 *
 * .|...\....
 * |.-.\.....
 * .....|-...
 * ........|.
 * ..........
 * .........\
 * ..../.\\..
 * .-.-/..|..
 * .|....-|.\
 * ..//.|....
 * The beam enters in the top-left corner from the left and heading to the right. Then, its behavior depends on what it encounters as it moves:
 *
 * If the beam encounters empty space (.), it continues in the same direction.
 * If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror. For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column, while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
 * If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the splitter were empty space. For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
 * If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing. For instance, a rightward-moving beam that encounters a | splitter would split into two beams: one that continues upward from the splitter's column and one that continues downward from the splitter's column.
 * Beams do not interact with other beams; a tile can have many beams passing through it at the same time. A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.
 *
 * In the above example, here is how the beam of light bounces around the contraption:
 *
 * >|<<<\....
 * |v-.\^....
 * .v...|->>>
 * .v...v^.|.
 * .v...v^...
 * .v...v^..\
 * .v../2\\..
 * <->-/vv|..
 * .|<<<2-|.\
 * .v//.|.v..
 * Beams are only shown on empty tiles; arrows indicate the direction of the beams. If a tile contains beams moving in multiple directions, the number of distinct directions is shown instead. Here is the same diagram but instead only showing whether a tile is energized (#) or not (.):
 *
 * ######....
 * .#...#....
 * .#...#####
 * .#...##...
 * .#...##...
 * .#...##...
 * .#..####..
 * ########..
 * .#######..
 * .#...#.#..
 * Ultimately, in this example, 46 tiles become energized.
 *
 * The light isn't energizing enough tiles to produce lava; to debug the contraption, you need to start by analyzing the current situation. With the beam starting in the top-left heading right, how many tiles end up being energized?
 *
 * Your puzzle answer was 7860.
 *
 * --- Part Two ---
 *
 * As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel. There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading away from that edge. (You can choose either of two directions for the beam if it starts on a corner; for instance, if the beam starts in the bottom-right corner, it can start heading either left or upward.)
 *
 * So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward), any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left). To produce lava, you need to find the configuration that energizes as many tiles as possible.
 *
 * In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:
 *
 * .|<2<\....
 * |v-v\^....
 * .v.v.|->>>
 * .v.v.v^.|.
 * .v.v.v^...
 * .v.v.v^..\
 * .v.v/2\\..
 * <-2-/vv|..
 * .|<<<2-|.\
 * .v//.|.v..
 * Using this configuration, 51 tiles are energized:
 *
 * .#####....
 * .#.#.#....
 * .#.#.#####
 * .#.#.##...
 * .#.#.##...
 * .#.#.##...
 * .#.#####..
 * ########..
 * .#######..
 * .#...#.#..
 * Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that configuration?
 *
 * Your puzzle answer was 8331.
 */
public class Day16 {
  public static void main(String[] args) {
    Day16 day16 = new Day16();
    List<String> test = InputReader.readInput("resources/day16test.txt");
    List<String> real = InputReader.readInput("resources/day16.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day16.partOne(test));
    System.out.println("part one real: " + day16.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day16.partTwo(test));
    System.out.println("part two real: " + day16.partTwo(real));
    System.out.println("____________");
  }

  enum Direction {
    R,
    D,
    L,
    U;

    static int[] point(int ord) {
      if (ord == R.ordinal()) {
        return new int[] {0, 1};
      } else if (ord == L.ordinal()) {
        return new int[] {0, -1};
      } else if (ord == U.ordinal()) {
        return new int[] {-1, 0};
      }
      return new int[] {1, 0};
    }
  }

  private int partOne(List<String> input) {
    char[][] chars = toArray(input);
    int[] start = {0, 0, Direction.R.ordinal()};
    return solve(start, chars);
  }

  private int partTwo(List<String> input) {
    char[][] chars = toArray(input);
    int result = 0;
    for (int row = 0; row < chars.length; row++) {
      int goRight = solve(new int[] {row, 0, Direction.R.ordinal()}, chars);
      //      System.out.println("R:" + row + " C:0 RIGHT: " + goRight);
      result = Math.max(result, goRight);
      int goLeft = solve(new int[] {row, chars[0].length - 1, Direction.L.ordinal()}, chars);
      //      System.out.println("R:" + row + " C:" + (chars[0].length - 1) + " LEFT: " + goLeft);
      result = Math.max(result, goLeft);
    }
    for (int col = 0; col < chars[0].length; col++) {
      int goDown = solve(new int[] {0, col, Direction.D.ordinal()}, chars);
      //      System.out.println("R:" + 0 + " C:" + col + " DOWN: " + goDown);
      result = Math.max(result, goDown);
      int goUp = solve(new int[] {chars.length - 1, col, Direction.U.ordinal()}, chars);
      //      System.out.println("R:" + (chars.length - 1) + " C:" + col + " UP: " + goUp);
      result = Math.max(result, goUp);
    }
    return result;
  }

  private static int solve(int[] start, char[][] chars) {
    LinkedList<int[]> queue = new LinkedList<>();
    queue.add(start);
    Set<List<Integer>> visited = new HashSet<>();
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int direction = curr[2];
      char ch = chars[curr[0]][curr[1]];
      if (ch == '.') {
        moveTo(direction, curr, chars, queue, visited);
      } else if (ch == '|') {
        if (Direction.R.ordinal() == direction || Direction.L.ordinal() == direction) {
          moveTo(Direction.U.ordinal(), curr, chars, queue, visited);
          moveTo(Direction.D.ordinal(), curr, chars, queue, visited);
        } else if (Direction.U.ordinal() == direction || Direction.D.ordinal() == direction) {
          moveTo(direction, curr, chars, queue, visited);
        }
      } else if (ch == '-') {
        if (Direction.R.ordinal() == direction || Direction.L.ordinal() == direction) {
          moveTo(direction, curr, chars, queue, visited);
        } else if (Direction.U.ordinal() == direction || Direction.D.ordinal() == direction) {
          moveTo(Direction.R.ordinal(), curr, chars, queue, visited);
          moveTo(Direction.L.ordinal(), curr, chars, queue, visited);
        }
      } else if (ch == '/') {
        if (Direction.R.ordinal() == direction) {
          moveTo(Direction.U.ordinal(), curr, chars, queue, visited);
        } else if (Direction.L.ordinal() == direction) {
          moveTo(Direction.D.ordinal(), curr, chars, queue, visited);
        } else if (Direction.U.ordinal() == direction) {
          moveTo(Direction.R.ordinal(), curr, chars, queue, visited);
        } else if (Direction.D.ordinal() == direction) {
          moveTo(Direction.L.ordinal(), curr, chars, queue, visited);
        }
      } else if (ch == '\\') {
        if (Direction.R.ordinal() == direction) {
          moveTo(Direction.D.ordinal(), curr, chars, queue, visited);
        } else if (Direction.L.ordinal() == direction) {
          moveTo(Direction.U.ordinal(), curr, chars, queue, visited);
        } else if (Direction.U.ordinal() == direction) {
          moveTo(Direction.L.ordinal(), curr, chars, queue, visited);
        } else if (Direction.D.ordinal() == direction) {
          moveTo(Direction.R.ordinal(), curr, chars, queue, visited);
        }
      }
      visited.add(List.of(curr[0], curr[1], direction));
    }
    // log(chars, visited);
    return visited.stream()
        .map(l -> List.of(l.getFirst(), l.get(1)))
        .collect(Collectors.toSet())
        .size();
  }

  private static void log(char[][] chars, Set<List<Integer>> visited) {
    for (List<Integer> point : visited) {
      chars[point.getFirst()][point.get(1)] = '#';
    }
    for (char[] aChar : chars) {
      System.out.println(
          new String(aChar)
              .replace("/", ".")
              .replace("\\", ".")
              .replace("-", ".")
              .replace("|", "."));
    }
  }

  private static void moveTo(
      int direction,
      int[] curr,
      char[][] chars,
      LinkedList<int[]> queue,
      Set<List<Integer>> visited) {
    int[] dir = Direction.point(direction);
    int row = curr[0] + dir[0];
    int col = curr[1] + dir[1];
    if (!visited.contains(List.of(row, col, direction)) && isInBounds(row, col, chars)) {
      queue.add(new int[] {row, col, direction});
    }
  }

  private static boolean isInBounds(int r, int c, char[][] chars) {
    return r >= 0 && r < chars.length && c >= 0 && c < chars[0].length;
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
}
