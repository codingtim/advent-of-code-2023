import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class Day02 {

    private final Pattern lineRegex = Pattern.compile("Game ([0-9]*): (.*)");

    @Test
    void partOneExamples() {
        assertThat(isPossible("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")).isEqualTo(1);
        assertThat(isPossible("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue")).isEqualTo(2);
        assertThat(isPossible("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red")).isEqualTo(0);
        assertThat(isPossible("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red")).isEqualTo(0);
        assertThat(isPossible("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")).isEqualTo(5);
    }

    private int isPossible(String s) {
        Map<String, Integer> colorsPresent = Map.of("red", 12, "green", 13, "blue", 14);
        Matcher matcher = lineRegex.matcher(s);
        matcher.matches();
        int gameNumber = Integer.parseInt(matcher.group(1));
        String shows = matcher.group(2);
        boolean possible = Arrays.stream(shows.split("; "))
                .flatMap(showings -> Arrays.stream(showings.split(", ")))
                .map(show -> show.split(" "))
                .allMatch(showing -> colorsPresent.get(showing[1]) >= Integer.parseInt(showing[0]));
        if (possible) {
            return gameNumber;
        } else {
            return 0;
        }
    }

    @Test
    void partOne() throws IOException {
        List<String> strings = readToList();
        int sum = strings.stream()
                .mapToInt(this::isPossible)
                .sum();
        System.out.println(sum);
    }

    @Test
    void partTwoExamples() {
        assertThat(powerOfLine("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")).isEqualTo(48);
        assertThat(powerOfLine("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue")).isEqualTo(12);
        assertThat(powerOfLine("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red")).isEqualTo(1560);
        assertThat(powerOfLine("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red")).isEqualTo(630);
        assertThat(powerOfLine("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")).isEqualTo(36);
    }

    private int powerOfLine(String s) {
        Matcher matcher = lineRegex.matcher(s);
        matcher.matches();
        String shows = matcher.group(2);
        return Arrays.stream(shows.split("; "))
                .flatMap(showings -> Arrays.stream(showings.split(", ")))
                .map(show -> show.split(" "))
                .collect(() -> new HashMap<String, Integer>(),
                        (hashMap, show) -> hashMap.compute(show[1], (o, numberPresent) -> {
                            int newValue = Integer.parseInt(show[0]);
                            return numberPresent == null || newValue > numberPresent ? newValue : numberPresent;
                        }),
                        (objectObjectHashMap, objectObjectHashMap2) -> {
                        })
                .values()
                .stream()
                .reduce(1, (integer, integer2) -> integer * integer2, (integer, integer2) -> integer * integer2);
    }


    @Test
    void partTwo() throws IOException {
        List<String> strings = readToList();
        int sum = strings.stream()
                .map(this::powerOfLine)
                .mapToInt(value -> value)
                .sum();
        System.out.println(sum);
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day02.txt")));
    }
}
