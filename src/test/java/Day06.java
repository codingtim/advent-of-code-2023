import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class Day06 {

    @Test
    void partOneExample() {
        List<TimeWithDistance> timeWithDistances = List.of(
                new TimeWithDistance(7, 9),
                new TimeWithDistance(15, 40),
                new TimeWithDistance(30, 200)
        );

        int result = timeWithDistances
                .stream()
                .map(this::waysToWin)
                .mapToInt(v -> v)
                .reduce((left, right) -> left * right)
                .getAsInt();
        assertThat(result).isEqualTo(288);
    }

    record TimeWithDistance(int time, long distance){}

    @Test
    void partOne() {
        List<TimeWithDistance> timeWithDistances = List.of(
                new TimeWithDistance(49, 263),
                new TimeWithDistance(97, 1532),
                new TimeWithDistance(94, 1378),
                new TimeWithDistance(94, 1851)
        );

        int result = timeWithDistances
                .stream()
                .map(this::waysToWin)
                .mapToInt(v -> v)
                .reduce((left, right) -> left * right)
                .getAsInt();
        System.out.println(result);
    }

    @Test
    void partTwoExample() {
        List<TimeWithDistance> timeWithDistances = List.of(
                new TimeWithDistance(71530, 940200)
        );

        int result = timeWithDistances
                .stream()
                .map(this::waysToWin)
                .mapToInt(v -> v)
                .reduce((left, right) -> left * right)
                .getAsInt();
        assertThat(result).isEqualTo(71503);
    }


    @Test
    void partTwo() {
        List<TimeWithDistance> timeWithDistances = List.of(
                new TimeWithDistance(49979494, 263153213781851L)
        );

        int result = timeWithDistances
                .stream()
                .map(this::waysToWin)
                .mapToInt(v -> v)
                .reduce((left, right) -> left * right)
                .getAsInt();
        System.out.println(result);
    }

    private int waysToWin(TimeWithDistance timeWithDistance) {
        int waysToWin = 0;
        for (int t = 1; t < timeWithDistance.time; t++) {
            int timeToRace = timeWithDistance.time - t;
            long v = t;
            long d = v * timeToRace;
            if (d > timeWithDistance.distance) {
                waysToWin++;
            } else if (d < timeWithDistance.distance && waysToWin > 0) {
                break;
            }
        }
        return waysToWin;
    }
}
