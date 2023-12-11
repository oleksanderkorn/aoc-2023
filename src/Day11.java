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
    System.out.println("part one test: " + day11.partOne(sample, 1L));
    System.out.println("part one real: " + day11.partOne(real, 1L));
    System.out.println("____________");
    System.out.println("part two test 10: " + day11.partOne(sample, 10L));
    System.out.println("part two test 100: " + day11.partOne(sample, 100));
    System.out.println("part two test 1M: " + day11.partOne(sample, 1_000_000L));
    System.out.println("part two real 1M: " + day11.partOne(real, 1_000_000L));
    System.out.println("____________");
  }

  private long partOne(Space grid, long multiplier) {
    Map<Integer, Galaxy> galaxies = grid.galaxies;
    List<Integer> keys = galaxies.keySet().stream().toList();
    Map<Set<Integer>, Long> distances = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      for (int j = i + 1; j < keys.size(); j++) {
        Galaxy left = galaxies.get(keys.get(i));
        Galaxy right = galaxies.get(keys.get(j));
        var emptyRowsBetween =
            grid.emptyRows.stream()
                .filter(row -> row > Math.min(left.r, right.r) && row < Math.max(left.r, right.r))
                .count();
        var emptyColsBetween =
            grid.emptyColumns.stream()
                .filter(col -> col > Math.min(left.c, right.c) && col < Math.max(left.c, right.c))
                .count();
        long empty = Math.abs(emptyColsBetween * multiplier) + (emptyRowsBetween * multiplier) - emptyColsBetween - emptyRowsBetween;
        long distance = Math.abs(left.c-right.c) + Math.abs(left.r-right.r) + empty;
        distances.put(Set.of(left.index, right.index),distance);
      }
    }
    // visualize(grid, distances);
    return distances.values().stream().reduce(Long::sum).orElseThrow();
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

  private static Space expand(List<String> input) {
    // expand rows
    List<Integer> emptyRows = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      if (!line.contains("#")) {
        emptyRows.add(i);
      }
    }
    // expand columns
    List<Integer> emptyColumns = new ArrayList<>();
    int cols = input.getFirst().length();
    for (int c = 0; c < cols; c++) {
      int count = 0;
      for (String string : input) {
        if (string.charAt(c) == '.') count++;
      }
      if (count == input.size()) emptyColumns.add(c);
    }
    Map<Integer, Galaxy> galaxies = new HashMap<>();
    int galaxy = 1;
    for (int i = 0; i < input.size(); i++) {
      for (int j = 0; j < input.get(i).length(); j++) {
        if (input.get(i).charAt(j) == '#') {
          galaxies.put(galaxy, new Galaxy(galaxy++, i, j));
        }
      }
    }
    return new Space(input.size(), input.getFirst().length(), galaxies, emptyRows, emptyColumns);
  }

  record Space(
      int w,
      int h,
      Map<Integer, Galaxy> galaxies,
      List<Integer> emptyRows,
      List<Integer> emptyColumns) {}

  record Galaxy(int index, int r, int c) {}
}
