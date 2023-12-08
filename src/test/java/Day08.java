import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class Day08 {

    @Test
    void partOneExample() {
        List<String> input = List.of(
                "RL",
                "",
                "AAA = (BBB, CCC)",
                "BBB = (DDD, EEE)",
                "CCC = (ZZZ, GGG)",
                "DDD = (DDD, DDD)",
                "EEE = (EEE, EEE)",
                "GGG = (GGG, GGG)",
                "ZZZ = (ZZZ, ZZZ)"
        );
        String route = input.get(0);
        int steps = numberOfStepsToReachZZZ(input, route);
        assertThat(steps).isEqualTo(2);
    }

    @Test
    void partOne() throws IOException {
        List<String> input = readToList();
        String route = input.get(0);
        int steps = numberOfStepsToReachZZZ(input, route);
        assertThat(steps).isEqualTo(20093);
    }

    private int numberOfStepsToReachZZZ(List<String> input, String route) {
        Map<String, Junction> junctions = parseInputToMap(input);
        int steps = 0;
        Junction j = junctions.get("AAA");
        while (!j.label.equals("ZZZ")) {
            char c = route.charAt(steps % route.length());
            if (c == 'L') {
                j = junctions.get(j.left);
            } else {
                j = junctions.get(j.right);
            }
            steps++;
        }
        return steps;
    }

    @Test
    void partTwoExample() {
        List<String> input = List.of(
                "LR",
                "",
                "11A = (11B, XXX)",
                "11B = (XXX, 11Z)",
                "11Z = (11B, XXX)",
                "22A = (22B, XXX)",
                "22B = (22C, 22C)",
                "22C = (22Z, 22Z)",
                "22Z = (22B, 22B)",
                "XXX = (XXX, XXX)"
        );
        String route = input.get(0);
        long steps = numberOfStepsToReachZ(input, route);
        assertThat(steps).isEqualTo(6);
    }

    @Test
    void partTwo() throws IOException {
        List<String> input = readToList();
        String route = input.get(0);
        long steps = numberOfStepsToReachZ(input, route);
        System.out.println(steps);
    }

    private long numberOfStepsToReachZ(List<String> input, String route) {
        Map<String, Junction> junctions = parseInputToMap(input);
        Junction[] js = junctions.values().stream().filter(s -> s.label.endsWith("A")).toList().toArray(new Junction[]{});
        for (Junction j : js) {
            System.out.println(j);
            int steps = 0;
            while (!j.label.endsWith("Z")) {
                char c = route.charAt(steps % route.length());
                if (c == 'L') {
                    j = junctions.get(j.left);
                } else {
                    j = junctions.get(j.right);
                }
                steps++;
            }
            System.out.println(steps);
        }

//        while (notAllArrived(js)) {
//            long l = steps % route.length();
//            char c = route.charAt((int)l);
//            if (c == 'L') {
//                for (int i = 0; i < js.length; i++) {
//                    Junction j = js[i];
//                    js[i] = junctions.get(j.left);
//                }
//            } else {
//                for (int i = 0; i < js.length; i++) {
//                    Junction j = js[i];
//                    js[i] = junctions.get(j.right);
//                }
//            }
//            steps++;
//        }
        return -1;
    }

    private boolean notAllArrived(Junction[] js) {
        for (Junction j : js) {
            if (!j.label.endsWith("Z")) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Junction> parseInputToMap(List<String> input) {
        Pattern pattern = Pattern.compile("([1-9A-Z]*) = \\(([1-9A-Z]*), ([1-9A-Z]*)\\)");
        Map<String, Junction> junctions = input.subList(2, input.size())
                .stream()
                .map(s -> {
                    Matcher matcher = pattern.matcher(s);
                    matcher.matches();
                    return new Junction(matcher.group(1), matcher.group(2), matcher.group(3));
                })
                .collect(Collectors.toMap(junction -> junction.label, junction -> junction));
        return junctions;
    }

    record Junction(String label, String left, String right) {
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day08.txt")));
    }
}
