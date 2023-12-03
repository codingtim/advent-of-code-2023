import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day03 {

    @Test
    void partOneExamples() {
        List<String> input = List.of(
                "467..114..",
                "...*......",
                "..35..633.",
                "......#...",
                "617*......",
                ".....+.58.",
                "..592.....",
                "......755.",
                "...$.*....",
                ".664.598.."
        );
        assertThat(sum(input)).isEqualTo(4361);
    }

    private int sum(List<String> input) {
        Set<SymbolCoor> symbolCoors = new HashSet<>();
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.' && !number(c)) {
                    SymbolCoor e = new SymbolCoor(i, j);
                    symbolCoors.add(e);
                }
            }
        }
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (number(c)) {
                    int next = j + 1;
                    while (next < input.size() && number(line.charAt(next))) {
                        next = next + 1;
                    }
                    if (hasSymbolNearby(symbolCoors, i, j, next - 1)) {
                        int sum1 = Integer.parseInt(line.substring(j, next));
                        sum += sum1;
                    }
                    j = next;
                }
            }
        }
        return sum;
    }

    private boolean hasSymbolNearby(Set<SymbolCoor> symbolCoors, int row, int columnFrom, int columnTo) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = columnFrom - 1; c <= columnTo + 1; c++) {
                if (symbolCoors.contains(new SymbolCoor(r, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    record SymbolCoor(int row, int column) {
    }

    private boolean number(char c) {
        if (c >= 48 && c <= 57) {
            return true;
        }
        return false;
    }


    @Test
    void partOne() throws IOException {
        List<String> strings = readToList();
        System.out.println(sum(strings));
    }

    @Test
    void partTwoExample() {
        List<String> input = List.of(
                "467..114..",
                "...*......",
                "..35..633.",
                "......#...",
                "617*......",
                ".....+.58.",
                "..592.....",
                "......755.",
                "...$.*....",
                ".664.598.."
        );
        assertThat(gears(input)).isEqualTo(467835);
    }

    @Test
    void partTwo() throws IOException {
        List<String> strings = readToList();
        System.out.println(gears(strings));
    }

    record Part(int number, Set<Integer> coors) {
    }

    private long gears(List<String> input) {
        long result = 0;

        Map<Integer, List<Part>> partsPerRow = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (number(c)) {
                    int next = j + 1;
                    while (next < input.size() && number(line.charAt(next))) {
                        next = next + 1;
                    }
                    int part = Integer.parseInt(line.substring(j, next));
                    partsPerRow.computeIfAbsent(i, q -> new ArrayList<>()).add(new Part(part,
                            IntStream.range(j, next).boxed().collect(Collectors.toSet())));
                    j = next;
                }
            }
        }

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.' && !number(c)) {
                    List<Integer> parts = getNumbersNextTo(partsPerRow, i, j);
                    if (parts.size() == 2) {
                        result += (long) parts.get(0) * parts.get(1);
                    }
                }
            }
        }
        return result;
    }

    private List<Integer> getNumbersNextTo(Map<Integer, List<Part>> partsPerRow, int r, int c) {
        List<Integer> previousRowMatches = partsPerRow.getOrDefault(r - 1, new ArrayList<>()).stream()
                .filter(part -> part.coors.contains(c - 1) || part.coors.contains(c) || part.coors.contains(c + 1))
                .map(part -> part.number)
                .toList();
        List<Integer> rowMatches = partsPerRow.getOrDefault(r, new ArrayList<>()).stream()
                .filter(part -> part.coors.contains(c - 1) || part.coors.contains(c) || part.coors.contains(c + 1))
                .map(part -> part.number)
                .toList();
        List<Integer> nextRowMatches = partsPerRow.getOrDefault(r + 1, new ArrayList<>()).stream()
                .filter(part -> part.coors.contains(c - 1) || part.coors.contains(c) || part.coors.contains(c + 1))
                .map(part -> part.number)
                .toList();
        ArrayList<Integer> integers = new ArrayList<>();
        integers.addAll(previousRowMatches);
        integers.addAll(rowMatches);
        integers.addAll(nextRowMatches);
        return integers;
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day03.txt")));
    }
}
