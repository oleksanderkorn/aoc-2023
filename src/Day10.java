import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day10 {
  public static void main(String[] args) {
    Day10 day10 = new Day10();
    List<String> sample = InputReader.readInput("resources/day10test.txt");
    List<String> real = InputReader.readInput("resources/day10.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day10.partOne(beautifyList(new ArrayList<>(sample))));
    System.out.println("part one real: " + day10.partOne(beautifyList(new ArrayList<>(real))));
    System.out.println("____________");
    System.out.println("part two test: " + day10.partTwo(sample));
    System.out.println("part two real: " + day10.partTwo(real));
    System.out.println("____________");
  }

  private int partOne(List<String> input) {
    for (int j = 0; j < input.size(); j++) {
      String line = input.get(j);
      for (int i = 0; i < line.length(); i++) {
        if (line.charAt(i) == 'S') {
          return findMaxDistance(j, i, input);
        }
      }
    }
    return 0;
  }

  private enum Pipe {
    NS('|'), // | is a vertical pipe connecting north and south.
    EW('-'), // - is a horizontal pipe connecting east and west.
    NE('ᒪ'), // L is a 90-degree bend connecting north and east.
    NW('ᒧ'), // J is a 90-degree bend connecting north and west.
    SW('ᒣ'), // 7 is a 90-degree bend connecting south and west.
    SE('ᒥ'); // F is a 90-degree bend connecting south and east.
    final Character symbol;

    Pipe(Character symbol) {
      this.symbol = symbol;
    }
  }

  private int findMaxDistance(int a, int b, List<String> input) {
    LinkedList<int[]> queue = new LinkedList<>();
    queue.offer(new int[] {a, b});

    int position = -1;
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        int[] point = queue.poll();
        int i = point[0];
        int j = point[1];
        markVisited(input, i, j);
        // check right
        if (j + 1 < input.get(i).length()) {
          char right = input.get(i).charAt(j + 1);
          if (right == Pipe.EW.symbol || right == Pipe.NW.symbol || right == Pipe.SW.symbol) {
            queue.add(new int[] {i, j + 1});
          }
        }
        // check left
        if (j > 0) {
          char left = input.get(i).charAt(j - 1);
          if (left == Pipe.EW.symbol || left == Pipe.NE.symbol || left == Pipe.SE.symbol) {
            queue.add(new int[] {i, j - 1});
          }
        }
        // check top
        if (i > 0) {
          char top = input.get(i - 1).charAt(j);
          if (top == Pipe.NS.symbol || top == Pipe.SW.symbol || top == Pipe.SE.symbol) {
            queue.add(new int[] {i - 1, j});
          }
        }
        // check bottom
        if (i + 1 < input.size()) {
          char bottom = input.get(i + 1).charAt(j);
          if (bottom == Pipe.NS.symbol || bottom == Pipe.NW.symbol || bottom == Pipe.NE.symbol) {
            queue.add(new int[] {i + 1, j});
          }
        }
      }
      position++;
    }
    return position;
  }

  private static void markVisited(List<String> input, int i, int j) {
    char[] lineChars = input.get(i).toCharArray();
    lineChars[j] = '*';
    input.set(i, new String(lineChars));
  }

  private String partTwo(List<String> input) {
    return null;
  }

  private static List<String> beautifyList(List<String> input) {
    for (int i = 0; i < input.size(); i++) {
      input.set(i, beautify(input.get(i)));
    }
    return input;
  }

  private static String beautify(String s) {
    return s.replaceAll("F", "ᒥ").replaceAll("L", "ᒪ").replaceAll("J", "ᒧ").replaceAll("7", "ᒣ");
  }
}
