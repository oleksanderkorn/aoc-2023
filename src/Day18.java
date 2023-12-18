import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 */
public class Day18 {

  public static void main(String[] args) {
    Day18 day18 = new Day18();
    List<String> test = InputReader.readInput("resources/day18test.txt");
    List<String> real = InputReader.readInput("resources/day18.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day18.partOne(test));
    System.out.println("part one real: " + day18.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day18.partTwo(test));
    System.out.println("part two real: " + day18.partTwo(real));
    System.out.println("____________");
  }

  enum Direction {
    R,
    D,
    L,
    U
  }

  record Plan(Direction direction, int steps, String color) {}

  private int partTwo(List<String> input) {
    return solve(mapFromColors(input));
  }

  private int partOne(List<String> input) {
    return solve(input.stream().map(Day18::mapLine).toList());
  }

  private int solve(List<Plan> plans) {
    char[][] terrain = new char[calculateRows(plans) * 2][calculateColumns(plans) * 2];
    for (char[] line : terrain) {
      Arrays.fill(line, '.');
    }
    int r = terrain.length / 2;
    int c = terrain[0].length / 2;
    for (Plan plan : plans) {
      if (plan.direction == Direction.R) {
        for (int i = c; i < c + plan.steps; i++) {
          terrain[r][i] = '#';
        }
        c += plan.steps;
      } else if (plan.direction == Direction.L) {
        for (int i = c; i >= c - plan.steps; i--) {
          terrain[r][i] = '#';
        }
        c -= plan.steps;
      } else if (plan.direction == Direction.D) {
        for (int i = r; i < r + plan.steps + 1; i++) {
          terrain[i][c] = '#';
        }
        r += plan.steps;
      } else if (plan.direction == Direction.U) {
        for (int i = r; i >= r - plan.steps + 1; i--) {
          terrain[i][c] = '#';
        }
        r -= plan.steps;
      }
    }
    return new FigureSizeCounter(terrain).calculate();
  }

  private static List<Plan> mapFromColors(List<String> input) {
    return input.stream()
        .map(Day18::mapLine)
        .map(
            plan -> {
              String color = plan.color;
              Integer length = Integer.valueOf(color.substring(2, color.length() - 2), 16);
              char dir = color.charAt(color.length() - 2);
              Direction direction;
              if (dir == '0') {
                direction = Direction.R;
              } else if (dir == '1') {
                direction = Direction.D;
              } else if (dir == '2') {
                direction = Direction.L;
              } else {
                direction = Direction.U;
              }
              return new Plan(direction, length, color);
            })
        .toList();
  }

  private int calculateColumns(List<Plan> plans) {
    int maxW = 0;
    int w = 0;
    for (Plan plan : plans) {
      if (plan.direction == Direction.R) {
        w += plan.steps;
      } else if (plan.direction == Direction.L) {
        w -= plan.steps;
      }
      maxW = Math.max(w, maxW);
    }
    return maxW + 1;
  }

  private int calculateRows(List<Plan> plans) {
    int maxH = 0;
    int h = 0;
    for (Plan plan : plans) {
      if (plan.direction == Direction.D) {
        h += plan.steps;
      } else if (plan.direction == Direction.U) {
        h -= plan.steps;
      }
      maxH = Math.max(h, maxH);
    }
    return maxH + 1;
  }

  private static Plan mapLine(String line) {
    String[] split = line.split(" ");
    return new Plan(Direction.valueOf(split[0]), Integer.parseInt(split[1]), split[2]);
  }

  class FigureSizeCounter {
    char[][] grid;

    public FigureSizeCounter(char[][] grid) {
      this.grid = grid;
    }

    static boolean[][] enclosed;
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};

    boolean isEnclosed(int startX, int startY) {
      if (grid[startX][startY] == '#') return true;
      if (enclosed[startX][startY]) return true; // Already checked

      Queue<int[]> queue = new LinkedList<>();
      queue.offer(new int[] {startX, startY});
      boolean isEnclosed = true;

      while (!queue.isEmpty()) {
        int[] point = queue.poll();
        int x = point[0], y = point[1];

        for (int i = 0; i < 4; i++) {
          int newX = x + dx[i], newY = y + dy[i];

          if (newX < 0 || newY < 0 || newX >= grid.length || newY >= grid[newX].length) {
            isEnclosed = false; // Can reach boundary
            break;
          }

          if (grid[newX][newY] == '.' && !enclosed[newX][newY]) {
            enclosed[newX][newY] = true; // Mark as visited
            queue.offer(new int[] {newX, newY});
          }
        }

        if (!isEnclosed) {
          break;
        }
      }

      // Mark all connected '.' as not enclosed if any can reach boundary
      if (!isEnclosed) {
        queue.offer(new int[] {startX, startY});
        while (!queue.isEmpty()) {
          int[] point = queue.poll();
          int x = point[0], y = point[1];

          for (int i = 0; i < 4; i++) {
            int newX = x + dx[i], newY = y + dy[i];

            if (newX >= 0 && newY >= 0 && newX < grid.length && newY < grid[newX].length) {
              if (grid[newX][newY] == '.' && enclosed[newX][newY]) {
                enclosed[newX][newY] = false;
                queue.offer(new int[] {newX, newY});
              }
            }
          }
        }
      }

      return isEnclosed;
    }

    public int calculate() {
      int count = 0;
      enclosed = new boolean[grid.length][];
      for (int i = 0; i < grid.length; i++) {
        enclosed[i] = new boolean[grid[i].length];
      }

      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[i].length; j++) {
          if (grid[i][j] == '#' || (grid[i][j] == '.' && isEnclosed(i, j))) {
            count++;
          }
        }
      }

      return count;
    }
  }
}
