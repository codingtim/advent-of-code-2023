import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11 {

    @Test
    void partOneExample() {
        List<String> input = List.of(
                "...#......",
                ".......#..",
                "#.........",
                "..........",
                "......#...",
                ".#........",
                ".........#",
                "..........",
                ".......#..",
                "#...#....."
        );
        StarMap starMap = parseStarMap(input);
        long total = countTotalDistanceOfPairs(starMap, 2);
        assertThat(374).isEqualTo(total);
    }

    @Test
    void partOne() throws IOException {
        List<String> input = readToList();
        StarMap starMap = parseStarMap(input);
        long total = countTotalDistanceOfPairs(starMap, 2);
        System.out.println(total);
    }

    @Test
    void partTwoExample() {
        List<String> input = List.of(
                "...#......",
                ".......#..",
                "#.........",
                "..........",
                "......#...",
                ".#........",
                ".........#",
                "..........",
                ".......#..",
                "#...#....."
        );
        StarMap starMap = parseStarMap(input);
        long total = countTotalDistanceOfPairs(starMap, 10);
        assertThat(1030).isEqualTo(total);
        total = countTotalDistanceOfPairs(starMap, 100);
        assertThat(8410).isEqualTo(total);
    }

    @Test
    void partTwo() throws IOException {
        List<String> input = readToList();
        StarMap starMap = parseStarMap(input);
        long total = countTotalDistanceOfPairs(starMap, 1000000);
        System.out.println(total);
    }

    private long countTotalDistanceOfPairs(StarMap starMap, int emptyDistance) {
        long total = 0;
        for (Galaxy galaxy : starMap.galaxies) {
            total += starMap.galaxies.stream()
                    .filter(g -> !g.equals(galaxy))
                    .mapToInt(g -> distance(galaxy, g, starMap, emptyDistance))
                    .sum();
        }
        return total;
    }

    private int distance(Galaxy galaxy, Galaxy galaxy1, StarMap starMap, int emptyDistance) {
        int xs = IntStream.range(galaxy.x + 1, galaxy1.x + 1)
                .map(x -> starMap.emptyRows.contains(x) ? emptyDistance : 1)
                .sum();
        int ys = IntStream.range(galaxy.y + 1, galaxy1.y + 1)
                .map(y -> starMap.emptyColumns.contains(y) ? emptyDistance : 1)
                .sum();
        return xs + ys;
    }

    private StarMap parseStarMap(List<String> input) {
        Set<Integer> emptyRows = new HashSet<>();
        Set<Integer> emptyColumns = new HashSet<>();
        Set<Galaxy> galaxies = new HashSet<>();
        int id = 1;
        for (int i = 0; i < input.size(); i++) {
            String row = input.get(i);
            if (isEmpty(row)) {
                emptyRows.add(i + 1);
            } else {
                for (int j = 0; j < row.length(); j++) {
                    if (row.charAt(j) == '#') {
                        galaxies.add(new Galaxy(id++, i + 1, j + 1));
                    }
                }
            }
        }
        for (int j = 0; j < input.get(0).length(); j++) {
            int y = j + 1;
            if (galaxies.stream().noneMatch(galaxy -> galaxy.y == y)) {
                emptyColumns.add(y);
            }
        }
        return new StarMap(galaxies, emptyRows, emptyColumns);
    }

    private boolean isEmpty(String row) {
        return row.matches("\\.*");
    }

    record Galaxy(int id, int x, int y) {
    }

    private record StarMap(Set<Galaxy> galaxies, Set<Integer> emptyRows, Set<Integer> emptyColumns) {
    }

    private List<String> readToList() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = readFile()) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private BufferedReader readFile() {
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day11.txt")));
    }
}
