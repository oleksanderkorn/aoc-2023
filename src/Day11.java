import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day11 {
  public static void main(String[] args) {
    Day11 day11 = new Day11();
    Space sample = expand(InputReader.readInput("resources/day11test.txt"));
    Space real = expand(InputReader.readInput("resources/day11.txt"));
    System.out.println("____________");
    System.out.println("part one test: " + day11.partOne(sample));
    System.out.println("part one real: " + day11.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day11.partTwo(sample));
    System.out.println("part two real: " + day11.partTwo(real));
    System.out.println("____________");
  }

  private int partOne(Space grid) {
    Map<Integer, Galaxy> galaxies = grid.galaxies;
    List<Integer> keys = galaxies.keySet().stream().toList();
    Map<Set<Integer>, Integer> distances = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      for (int j = i + 1; j < keys.size(); j++) {
        Galaxy left = galaxies.get(keys.get(i));
        Galaxy right = galaxies.get(keys.get(j));
        int distance = Math.abs(left.c - right.c) + Math.abs(left.r - right.r);
        distances.put(Set.of(left.index, right.index), distance);
      }
    }
    // visualize(grid, distances);
    return distances.values().stream().reduce(Integer::sum).orElseThrow();
  }

  private void visualize(Space grid, Map<Set<Integer>, Integer> distances) {
    for (int i = 0; i < grid.h; i++) {
      for (int j = 0; j < grid.w; j++) {
        int finalI = i;
        int finalJ = j;
        var galaxy =
            grid.galaxies.values().stream().filter(g -> g.r == finalI && g.c == finalJ).findFirst();
        if (galaxy.isPresent()) {
          System.out.print(galaxy.orElseThrow().index);
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
    System.out.println(distances);
  }

  private int partTwo(Space grid) {
    return 0;
  }

  private static Space expand(List<String> input) {
    List<String> expanded = new ArrayList<>();
    // expand rows
    List<Integer> emptyRows = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      expanded.add(line);
      if (!line.contains("#")) {
        emptyRows.add(i);
        expanded.add(line);
      }
    }
    // expand columns
    List<Integer> emptyColumns = new ArrayList<>();
    int cols = expanded.getFirst().length();
    for (int c = 0; c < cols; c++) {
      int count = 0;
      for (String string : expanded) {
        if (string.charAt(c) == '.') count++;
      }
      if (count == expanded.size()) emptyColumns.add(c);
    }
    for (int i = 0; i < expanded.size(); i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < expanded.get(i).length(); j++) {
        sb.append(expanded.get(i).charAt(j));
        if (emptyColumns.contains(j)) {
          sb.append(".");
        }
      }
      expanded.set(i, sb.toString());
    }
    Map<Integer, Galaxy> galaxies = new HashMap<>();
    int galaxy = 1;
    for (int i = 0; i < expanded.size(); i++) {
      for (int j = 0; j < expanded.get(i).length(); j++) {
        if (expanded.get(i).charAt(j) == '#') {
          galaxies.put(galaxy, new Galaxy(galaxy++, i, j));
        }
      }
    }
    return new Space(
        expanded.size(), expanded.getFirst().length(), galaxies, emptyRows, emptyColumns);
  }

  record Space(
      int w,
      int h,
      Map<Integer, Galaxy> galaxies,
      List<Integer> emptyRows,
      List<Integer> emptyColumns) {}

  record Galaxy(int index, int r, int c) {}
}
