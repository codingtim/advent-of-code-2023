import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class Day09 {

    @Test
    void partOneExample() {
        List<String> input = List.of(
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45"
        );
        assertThat(nextValueFor(toIntegerList(input.get(0)))).isEqualTo(18);
        assertThat(nextValueFor(toIntegerList(input.get(1)))).isEqualTo(28);
        assertThat(nextValueFor(toIntegerList(input.get(2)))).isEqualTo(68);
    }

    @Test
    void partOne() throws IOException {
        List<String> input = readToList();
        int sum = input.stream()
                .mapToInt(line -> nextValueFor(toIntegerList(line)))
                .sum();
        System.out.println(sum);
    }

    @Test
    void partTwoExample() {
        List<String> input = List.of(
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45"
        );
        assertThat(previousValueFor(toIntegerList(input.get(0)))).isEqualTo(-3);
        assertThat(previousValueFor(toIntegerList(input.get(1)))).isEqualTo(0);
        assertThat(previousValueFor(toIntegerList(input.get(2)))).isEqualTo(5);
    }

    @Test
    void partTwo() throws IOException {
        List<String> input = readToList();
        int sum = input.stream()
                .mapToInt(line -> previousValueFor(toIntegerList(line)))
                .sum();
        System.out.println(sum);
    }

    private List<Integer> toIntegerList(String s) {
        return Arrays.stream(s.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private int nextValueFor(List<Integer> input) {
        int nextValue = 0;
        while(!input.stream().allMatch(i -> i == 0)) {
            nextValue += input.get(input.size() - 1);
            List<Integer> mapped = new ArrayList<>();
            for (int i = 1; i < input.size(); i++) {
                mapped.add(input.get(i) - input.get(i - 1));
            }
            input = mapped;
        }
        return nextValue;
    }

    private int previousValueFor(List<Integer> input) {
        List<Integer> previousValues = new ArrayList<>();
        while(!input.stream().allMatch(i -> i == 0)) {
            previousValues.add(0, input.get(0));
            List<Integer> mapped = new ArrayList<>();
            for (int i = 1; i < input.size(); i++) {
                mapped.add(input.get(i) - input.get(i - 1));
            }
            input = mapped;
        }
        return previousValues.stream().reduce(0, (integer, integer2) -> integer2 - integer);
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day09.txt")));
    }
}
