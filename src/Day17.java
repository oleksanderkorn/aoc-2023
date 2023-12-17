import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * --- Day 17: Clumsy Crucible ---
 *
 * The lava starts flowing rapidly once the Lava Production Facility is operational. As you leave, the reindeer offers you a parachute, allowing you to quickly reach Gear Island.
 *
 * As you descend, your bird's-eye view of Gear Island reveals why you had trouble finding anyone on your way up: half of Gear Island is empty, but the half below you is a giant factory city!
 *
 * You land near the gradually-filling pool of lava at the base of your new lavafall. Lavaducts will eventually carry the lava throughout the city, but to make use of it immediately, Elves are loading it into large crucibles on wheels.
 *
 * The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very difficult to steer at high speeds, and so it can be hard to go in a straight line for very long.
 *
 * To get Desert Island the machine parts it needs as soon as possible, you'll need to find the best way to get the crucible from the lava pool to the machine parts factory. To do this, you need to minimize heat loss while choosing a route that doesn't require the crucible to go in a straight line for too long.
 *
 * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient temperature, and hundreds of other parameters to calculate exactly how much heat loss can be expected for a crucible entering any particular city block.
 *
 * For example:
 *
 * 2413432311323
 * 3215453535623
 * 3255245654254
 * 3446585845452
 * 4546657867536
 * 1438598798454
 * 4457876987766
 * 3637877979653
 * 4654967986887
 * 4564679986453
 * 1224686865563
 * 2546548887735
 * 4322674655533
 * Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that block. The starting point, the lava pool, is the top-left city block; the destination, the machine parts factory, is the bottom-right city block. (Because you already start in the top-left block, you don't incur that block's heat loss unless you leave that block and then return to it.)
 *
 * Because it is difficult to keep the top-heavy crucible going in a straight line for very long, it can move at most three blocks in a single direction before it must turn 90 degrees left or right. The crucible also can't reverse direction; after entering each city block, it may only turn left, continue straight, or turn right.
 *
 * One way to minimize heat loss is this path:
 *
 * 2>>34^>>>1323
 * 32v>>>35v5623
 * 32552456v>>54
 * 3446585845v52
 * 4546657867v>6
 * 14385987984v4
 * 44578769877v6
 * 36378779796v>
 * 465496798688v
 * 456467998645v
 * 12246868655<v
 * 25465488877v5
 * 43226746555v>
 * This path never moves more than three consecutive blocks in the same direction and incurs a heat loss of only 102.
 *
 * Directing the crucible from the lava pool to the machine parts factory, but not moving more than three consecutive blocks in the same direction, what is the least heat loss it can incur?
 *
 * Your puzzle answer was 1039.
 *
 * --- Part Two ---
 *
 * The crucibles of lava simply aren't large enough to provide an adequate supply of lava to the machine parts factory. Instead, the Elves are going to upgrade to ultra crucibles.
 *
 * Ultra crucibles are even more difficult to steer than normal crucibles. Not only do they have trouble going in a straight line, but they also have trouble turning!
 *
 * Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks in that direction before it can turn (or even before it can stop at the end). However, it will eventually start to get wobbly: an ultra crucible can move a maximum of ten consecutive blocks without turning.
 *
 * In the above example, an ultra crucible could follow this path to minimize heat loss:
 *
 * 2>>>>>>>>1323
 * 32154535v5623
 * 32552456v4254
 * 34465858v5452
 * 45466578v>>>>
 * 143859879845v
 * 445787698776v
 * 363787797965v
 * 465496798688v
 * 456467998645v
 * 122468686556v
 * 254654888773v
 * 432267465553v
 * In the above example, an ultra crucible would incur the minimum possible heat loss of 94.
 *
 * Here's another example:
 *
 * 111111111111
 * 999999999991
 * 999999999991
 * 999999999991
 * 999999999991
 * Sadly, an ultra crucible would need to take an unfortunate path like this one:
 *
 * 1>>>>>>>1111
 * 9999999v9991
 * 9999999v9991
 * 9999999v9991
 * 9999999v>>>>
 * This route causes the ultra crucible to incur the minimum possible heat loss of 71.
 *
 * Directing the ultra crucible from the lava pool to the machine parts factory, what is the least heat loss it can incur?
 *
 * Your puzzle answer was 1201.
 */
public class Day17 {
  public static void main(String[] args) {
    Day17 day17 = new Day17();
    List<String> test = InputReader.readInput("resources/day17test.txt");
    List<String> real = InputReader.readInput("resources/day17.txt");
    System.out.println("____________");
    System.out.println("part one test: " + day17.partOne(test));
    System.out.println("part one real: " + day17.partOne(real));
    System.out.println("____________");
    System.out.println("part two test: " + day17.partTwo(test));
    System.out.println("part two real: " + day17.partTwo(real));
    System.out.println("____________");
  }

  private int partOne(List<String> lines) {
    return dijkstra(parse(lines), Part.ONE);
  }

  private int partTwo(List<String> lines) {
    int[][] cave = parse(lines);
    return dijkstra(cave, Part.TWO);
  }

  private static int[][] parse(List<String> lines) {
    int[][] cave = new int[lines.size()][lines.getFirst().length()];
    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);
      for (int c = 0; c < lines.getFirst().length(); c++) {
        cave[r][c] = Integer.parseInt(line.substring(c, c + 1));
      }
    }
    return cave;
  }

  private static int dijkstra(int[][] cave, Part part) {
    int nRows = cave.length;
    int nCols = cave[0].length;

    HashSet<Node> visited = new HashSet<>();
    PriorityQueue<State> queue = new PriorityQueue<>();
    queue.add(new State(new Node(0, 1, Direction.EAST, 1), cave[0][1], null));
    queue.add(new State(new Node(1, 0, Direction.SOUTH, 1), cave[1][0], null));
    while (!queue.isEmpty()) {
      State cur = queue.remove();
      if (visited.contains(cur.node)) {
        continue;
      }

      visited.add(cur.node);
      if (cur.node.row == nRows - 1
          && cur.node.col == nCols - 1
          && (part == Part.ONE || cur.node.steps > 2)) {
        return cur.cost;
      }
      if (part == Part.ONE) {
        cur.addNext(queue, cave);
      } else {
        cur.addNextUltra(queue, cave);
      }
    }
    return -1;
  }

  enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    final int rowOffset;
    final int colOffset;

    Direction(int rowOffset, int colOffset) {
      this.rowOffset = rowOffset;
      this.colOffset = colOffset;
    }

    public Direction left() {
      return switch (this) {
        case NORTH -> WEST;
        case WEST -> SOUTH;
        case SOUTH -> EAST;
        case EAST -> NORTH;
      };
    }

    public Direction right() {
      return switch (this) {
        case NORTH -> EAST;
        case EAST -> SOUTH;
        case SOUTH -> WEST;
        case WEST -> NORTH;
      };
    }
  }

  record Node(int row, int col, Direction dir, int steps) {}

  static class State implements Comparable<State> {
    Node node;
    int cost;
    State prev;

    public State(Node node, int cost, State prev) {
      this.node = node;
      this.cost = cost;
      this.prev = prev;
    }

    @Override
    public int compareTo(State o) {
      int ret = cost - o.cost;
      if (ret == 0 && node.dir == o.node.dir) {
        ret = node.steps - o.node.steps;
      }
      if (ret == 0) {
        ret = o.node.row + o.node.col - node.row - node.col;
      }
      return ret;
    }

    private void addNextWhenInBounds(Collection<State> out, Direction d, int[][] costs) {
      int nr = node.row + d.rowOffset;
      int nc = node.col + d.colOffset;
      if (nr >= 0 && nr < costs.length && nc >= 0 && nc < costs[0].length) {
        int nSteps = node.dir == d ? node.steps + 1 : 0;
        State nxt = new State(new Node(nr, nc, d, nSteps), cost + costs[nr][nc], this);
        out.add(nxt);
      }
    }

    public void addNext(Collection<State> out, int[][] costs) {
      if (node.steps < 2) {
        addNextWhenInBounds(out, node.dir, costs);
      }
      addNextWhenInBounds(out, node.dir.left(), costs);
      addNextWhenInBounds(out, node.dir.right(), costs);
    }

    public void addNextUltra(Collection<State> out, int[][] costs) {
      if (node.steps < 9) {
        addNextWhenInBounds(out, node.dir, costs);
      }
      if (node.steps > 2) {
        addNextWhenInBounds(out, node.dir.left(), costs);
        addNextWhenInBounds(out, node.dir.right(), costs);
      }
    }

    @Override
    public String toString() {
      return node.row + "," + node.col + " " + node.steps + " " + node.dir + " | " + cost;
    }
  }

  enum Part {
    ONE,
    TWO
  }
}
