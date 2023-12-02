import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day01 {

    @Test
    void partOneExamples() {
        assertThat(extractNumber("1abc2")).isEqualTo(12);
        assertThat(extractNumber("pqr3stu8vwx")).isEqualTo(38);
        assertThat(extractNumber("a1b2c3d4e5f")).isEqualTo(15);
        assertThat(extractNumber("treb7uchet")).isEqualTo(77);
    }

    @Test
    void partOne() throws IOException {
        List<String> strings = readToList();
        int sum = strings.stream()
                .mapToInt(this::extractNumber)
                .sum();
        System.out.println(sum);
    }

    @Test
    void partTwoExamples() {
        assertThat(extractNumber(convertSpelledLetters("two1nine"))).isEqualTo(29);
        assertThat(extractNumber(convertSpelledLetters("eightwothree"))).isEqualTo(83);
        assertThat(extractNumber(convertSpelledLetters("abcone2threexyz"))).isEqualTo(13);
        assertThat(extractNumber(convertSpelledLetters("xtwone3four"))).isEqualTo(24);
        assertThat(extractNumber(convertSpelledLetters("4nineeightseven2"))).isEqualTo(42);
        assertThat(extractNumber(convertSpelledLetters("zoneight234"))).isEqualTo(14);
        assertThat(extractNumber(convertSpelledLetters("7pqrstsixteen"))).isEqualTo(76);
    }

@Test
void partTwo() throws IOException {
    List<String> strings = readToList();
    int sum = strings.stream()
            .map(this::convertSpelledLetters)
            .mapToInt(this::extractNumber)
            .sum();
    System.out.println(sum);
}

private String convertSpelledLetters(String input) {

    return input
            .replace("one", "o1e")
            .replace("two", "t2o")
            .replace("three", "t3e")
            .replace("four", "4")
            .replace("five", "5e")
            .replace("six", "6")
            .replace("seven", "7n")
            .replace("eight", "e8t")
            .replace("nine", "n9e");
}

    private int extractNumber(String input) {
        String result = "";
        for (char c : input.toCharArray()) {
            if (c >= 48 && c <= 57) {
                result += c;
                break;
            }
        }
        for (int i = input.length() - 1; i >= 0; i--) {
            char c = input.toCharArray()[i];
            if (c >= 48 && c <= 57) {
                result += c;
                break;
            }
        }
        return Integer.parseInt(result);
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day01.txt")));
    }
}
