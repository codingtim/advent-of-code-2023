import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day04 {

    private final Pattern cardLineRegex = Pattern.compile("Card\\s+[0-9]*: (.*?) \\| (.*)");

    @Test
    void partOneExample() {
        assertThat(pointsOf("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53")).isEqualTo(8);
        assertThat(pointsOf("Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19")).isEqualTo(2);
        assertThat(pointsOf("Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1")).isEqualTo(2);
        assertThat(pointsOf("Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83")).isEqualTo(1);
        assertThat(pointsOf("Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36")).isEqualTo(0);
        assertThat(pointsOf("Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11")).isEqualTo(0);
    }

    private int pointsOf(String s) {
        Matcher matcher = cardLineRegex.matcher(s);
        boolean matches = matcher.matches();
        if (!matches) {
            System.out.println(s);
        }
        String winningNumbersString = matcher.group(1).trim();
        String myNumbersString = matcher.group(2).trim();

        Set<Integer> winningNumbers = Arrays.stream(winningNumbersString.split("\\s+")).mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());

        Set<Integer> myNumbers = Arrays.stream(myNumbersString.split("\\s+")).mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());

        int points = 0;

        for (Integer myNumber : myNumbers) {
            if (winningNumbers.contains(myNumber)) {
                points = points == 0 ? 1 : points * 2;
            }
        }

        return points;
    }

    @Test
    void partOne() throws IOException {
        int sum = readToList().stream()
                .map(this::pointsOf)
                .mapToInt(value -> value)
                .sum();
        System.out.println(sum);
    }

    @Test
    void partTwoExample() {
        List<String> input = List.of(
                "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
        );
        Map<Integer, Integer> copiesOfEachCard = count(input);
        assertThat(copiesOfEachCard.values().stream().mapToInt(value -> value).sum()).isEqualTo(30);
    }

    private Map<Integer, Integer> count(List<String> input) {
        Map<Integer, Integer> copiesOfEachCard = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            copiesOfEachCard.put(i, 1);
        }
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            count(i, s, copiesOfEachCard);
        }
        return copiesOfEachCard;
    }

    @Test
    void partTwo() throws IOException {
        Map<Integer, Integer> copiesOfEachCard = count(readToList());
        System.out.println(copiesOfEachCard.values().stream().mapToInt(value -> value).sum());
    }

    private void count(int currentCardNumber, String s, Map<Integer, Integer> copiesOfEachCard) {
        int numberOfMatches = numberOfMatches(s);
        Integer occurrencesOfThisCard = copiesOfEachCard.get(currentCardNumber);
        IntStream.range(currentCardNumber + 1, currentCardNumber + numberOfMatches + 1)
                .forEach(cardNumberToAddTo -> copiesOfEachCard.put(cardNumberToAddTo, copiesOfEachCard.get(cardNumberToAddTo) + occurrencesOfThisCard));

    }

    private int numberOfMatches(String s) {
        Matcher matcher = cardLineRegex.matcher(s);
        boolean matches = matcher.matches();
        if (!matches) {
            System.out.println(s);
        }
        String winningNumbersString = matcher.group(1).trim();
        String myNumbersString = matcher.group(2).trim();

        Set<Integer> winningNumbers = Arrays.stream(winningNumbersString.split("\\s+")).mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());

        Set<Integer> myNumbers = Arrays.stream(myNumbersString.split("\\s+")).mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());

        int numberOfMatches = 0;

        for (Integer myNumber : myNumbers) {
            if (winningNumbers.contains(myNumber)) {
                numberOfMatches++;
            }
        }

        return numberOfMatches;
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day04.txt")));
    }
}
