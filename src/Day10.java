import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Day10 {
  public static void main(String[] args) {
    Day10 day10 = new Day10();
    List<String> sample = prettify(InputReader.readInput("resources/day10test.txt"));
    List<String> real = prettify(InputReader.readInput("resources/day10.txt"));
    List<String> sampleTwo = prettify(InputReader.readInput("resources/day10test_2.txt"));
    System.out.println("____________");
    System.out.println("part one test: " + day10.partOne(sample));
    System.out.println("part one real: " + day10.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day10.partTwo(sampleTwo));
    System.out.println("part two real: " + day10.partTwo(real));
    System.out.println("____________");
  }

  static List<String> prettify(List<String> input) {
      input.replaceAll(string -> string.replaceAll("" + Pipe.NS.symbol, "┃")
              .replaceAll("-", "━")
              .replaceAll("\\|", "┃")
              .replaceAll("L", "┗")
              .replaceAll("J", "┛")
              .replaceAll("7", "┓")
              .replaceAll("F", "┏"));
    return input;
  }

  private int partOne(List<String> input) {
    for (int j = 0; j < input.size(); j++) {
      String line = input.get(j);
      for (int i = 0; i < line.length(); i++) {
        if (line.charAt(i) == 'S') {
          return findConnectedNodes(j, i, input).size() / 2;
        }
      }
    }
    return 0;
  }

  private int partTwo(List<String> input) {
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      for (int j = 0; j < line.length(); j++) {
        if (line.charAt(j) == 'S') {
          return countInnerTiles(i, j, input);
        }
      }
    }
    return 0;
  }

  private int countInnerTiles(int i, int j, List<String> input) {
    Set<List<Integer>> nodes = findConnectedNodes(i, j, input);
    int result = 0;
    for (int k = 0; k < input.size(); k++) {
      for (int l = 0; l < input.get(0).length(); l++) {
        if (!nodes.contains(List.of(k, l)) && isPointInPolygon(k, l, nodes, input)) {
          System.out.print("\u001B[31m"); // RED
          result++;
        }
        if (nodes.contains(List.of(k, l))) {
          System.out.print("\u001B[36m"); // CYAN
        }
        System.out.print(input.get(k).charAt(l));
        System.out.print("\u001B[0m"); // WHITE
      }
      System.out.println();
    }
    return result;
  }

  boolean isPointInPolygon(int i, int j, Set<List<Integer>> circle, List<String> input) {
    var intersects = 0;
    for (int offset = 1; offset < input.get(0).length(); offset++) {
      if (circle.contains(List.of(i, j + offset))) {
        char symbol = input.get(i).charAt(j + offset);
        if (!Set.of('-', 'L', 'J', '━', '┗', '┛').contains(symbol)) {
          intersects++;
        }
      }
    }
    return intersects % 2 == 1;
  }

  private Set<List<Integer>> findConnectedNodes(int a, int b, List<String> input) {
    LinkedList<int[]> queue = new LinkedList<>();
    queue.offer(new int[] {a, b});
    Set<List<Integer>> visited = new HashSet<>();
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        int[] point = queue.poll();
        int i = point[0];
        int j = point[1];
        visited.add(List.of(i, j));
        Optional<Pipe> pipe = Pipe.from(input.get(i).charAt(j));
        //        markVisited(input, i, j);
        // check right
        if (!visited.contains(List.of(i, j + 1))
            && pipe.isPresent()
            && Set.of(Pipe.START, Pipe.EW, Pipe.NE, Pipe.SE).contains(pipe.orElseThrow())
            && j + 1 < input.get(i).length()) {
          char right = input.get(i).charAt(j + 1);
          if (right == Pipe.EW.symbol || right == Pipe.NW.symbol || right == Pipe.SW.symbol) {
            queue.add(new int[] {i, j + 1});
          }
        }
        // check left
        if (!visited.contains(List.of(i, j - 1))
            && pipe.isPresent()
            && Set.of(Pipe.START, Pipe.EW, Pipe.NW, Pipe.SW).contains(pipe.orElseThrow())
            && j > 0) {
          char left = input.get(i).charAt(j - 1);
          if (left == Pipe.EW.symbol || left == Pipe.NE.symbol || left == Pipe.SE.symbol) {
            queue.add(new int[] {i, j - 1});
          }
        }
        // check top
        if (!visited.contains(List.of(i - 1, j))
            && pipe.isPresent()
            && Set.of(Pipe.START, Pipe.NS, Pipe.NW, Pipe.NE).contains(pipe.orElseThrow())
            && i > 0) {
          char top = input.get(i - 1).charAt(j);
          if (top == Pipe.NS.symbol || top == Pipe.SW.symbol || top == Pipe.SE.symbol) {
            queue.add(new int[] {i - 1, j});
          }
        }
        // check bottom
        if (!visited.contains(List.of(i + 1, j))
            && pipe.isPresent()
            && Set.of(Pipe.START, Pipe.NS, Pipe.SW, Pipe.SE).contains(pipe.orElseThrow())
            && i + 1 < input.size()) {
          char bottom = input.get(i + 1).charAt(j);
          if (bottom == Pipe.NS.symbol || bottom == Pipe.NW.symbol || bottom == Pipe.NE.symbol) {
            queue.add(new int[] {i + 1, j});
          }
        }
      }
    }
    //    input.forEach(System.out::println);
    return visited;
  }

  private static void markVisited(List<String> input, int i, int j) {
    char[] lineChars = input.get(i).toCharArray();
    if (nice.containsKey(lineChars[j])) {
      lineChars[j] = nice.get(lineChars[j]);
    }
    input.set(i, new String(lineChars));
  }

  private static final Map<Character, Character> nice =
          Map.of(
                  Pipe.NS.symbol, '┃',
                  Pipe.EW.symbol, '━',
                  Pipe.NE.symbol, '┗',
                  Pipe.NW.symbol, '┛',
                  Pipe.SW.symbol, '┓',
                  Pipe.SE.symbol, '┏');

  private enum Pipe {
    GROUND('.'),
    START('S'),
    NS('┃'), // | is a vertical pipe connecting north and south.
    EW('━'), // - is a horizontal pipe connecting east and west.
    NE('┗'), // L is a 90-degree bend connecting north and east.
    NW('┛'), // J is a 90-degree bend connecting north and west.
    SW('┓'), // 7 is a 90-degree bend connecting south and west.
    SE('┏'); // F is a 90-degree bend connecting south and east.
    final Character symbol;

    Pipe(Character symbol) {
      this.symbol = symbol;
    }

    public static Optional<Pipe> from(char ch) {
      return Arrays.stream(Pipe.values()).filter(p -> p.symbol == ch).findFirst();
    }
  }
}
