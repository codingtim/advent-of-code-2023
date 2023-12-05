import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class Day05 {

    private final String example = """               
            seeds: 79 14 55 13
                           
            seed-to-soil map:
            50 98 2
            52 50 48
                           
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                           
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                           
            water-to-light map:
            88 18 7
            18 25 70
                           
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                           
            temperature-to-humidity map:
            0 69 1
            1 0 69
                           
            humidity-to-location map:
            60 56 37
            56 93 4
            """;

    @Test
    void partOneExample() {
        List<String> input = Arrays.stream(example.split("\n")).toList();
        Collection<Long> seeds = mapSeedNumbers(input);
        List<List<Mapping>> mappings = parseMappings(input);

        Optional<Long> min = seeds.stream()
                .map(seed -> toDestination(seed, mappings))
                .min(Long::compareTo);
        assertThat(min).contains(35L);
    }

    @Test
    void partOne() throws IOException {
        List<String> input = readToList();
        Collection<Long> seeds = mapSeedNumbers(input);
        List<List<Mapping>> mappings = parseMappings(input);

        Optional<Long> min = seeds.stream()
                .map(seed -> toDestination(seed, mappings))
                .min(Long::compareTo);
        System.out.println(min);
    }

    private List<Long> mapSeedNumbers(List<String> input) {
        return Arrays.stream(input.get(0).replace("seeds: ", "").split(" ")).map(Long::parseLong).toList();
    }

    @Test
    void partTwoExample() {
        List<String> input = Arrays.stream(example.split("\n")).toList();
        Collection<SeedRange> seedRanges = mapSeedRanges(input);
        List<List<Mapping>> mappings = parseMappings(input);

        long min = minOfSeedRanges(seedRanges, mappings);
        System.out.println(min);
    }

    @Test
    void partTwo() throws IOException {
        List<String> input = readToList();
        Collection<SeedRange> seedRanges = mapSeedRanges(input);
        List<List<Mapping>> mappings = parseMappings(input);

        long min = minOfSeedRanges(seedRanges, mappings);
        System.out.println(min);
    }

    private long minOfSeedRanges(Collection<SeedRange> seedRanges, List<List<Mapping>> mappings) {
        for (List<Mapping> mappingOfResource : mappings) {
            seedRanges = applyMapping(mappingOfResource, seedRanges);
        }
        return seedRanges.stream().findFirst().get().from;
    }

    private Collection<SeedRange> applyMapping(List<Mapping> mappingOfResource, Collection<SeedRange> seedRanges) {
        List<SeedRange> input = new ArrayList<>(seedRanges);
        List<SeedRange> output = new ArrayList<>();
        for (Mapping mapping : mappingOfResource) {
            List<SeedRange> newInput = new ArrayList<>();
            for (SeedRange seedRange : input) {
                if (seedRange.overlaps(mapping)) {
                    output.add(seedRange.overlap(mapping));
                    newInput.addAll(seedRange.inverse(mapping));
                } else {
                    newInput.add(seedRange);
                }
            }
            input = newInput;
        }
        output.addAll(input);
        output.sort(Comparator.comparingLong(o -> o.from));
        output = join(output);
        System.out.println(output);
        return output;
    }

    private List<SeedRange> join(List<SeedRange> output) {
        Stack<SeedRange> stack = new Stack<>();

        stack.push(output.get(0));
        for (int i = 1; i < output.size(); i++) {
            SeedRange peek = stack.peek();
            SeedRange range = output.get(i);
            if (peek.from + peek.range == range.from) {
                SeedRange overlap = stack.pop();
                stack.push(new SeedRange(overlap.from, overlap.range + range.range));
            } else {
                stack.push(range);
            }
        }
        return new ArrayList<>(stack);
    }

    private List<SeedRange> mapSeedRanges(List<String> input) {
        List<Long> seedInput = Arrays.stream(input.get(0).replace("seeds: ", "").split(" "))
                .map(Long::parseLong)
                .toList();
        List<SeedRange> seeds = new ArrayList<>();
        for (int i = 0; i < seedInput.size(); i = i + 2) {
            Long fromSeed = seedInput.get(i);
            Long seedRange = seedInput.get(i + 1);
            seeds.add(new SeedRange(fromSeed, seedRange));
        }
        return seeds;
    }

    private record SeedRange(Long from, Long range) {
        public boolean overlaps(Mapping mapping) {
            return from <= mapping.source + mapping.range - 1 && from + range - 1 >= mapping.source;
        }

        public SeedRange overlap(Mapping mapping) {
            long f = Long.max(from, mapping.source);
            long shift = mapping.destination - mapping.source;
            long max = Long.min(from + range - 1, mapping.source + mapping.range - 1);
            long newRange = max - f + 1;
            SeedRange seedRange = new SeedRange(f + shift, newRange);
            return seedRange;
        }

        public List<SeedRange> inverse(Mapping mapping) {
            if (mapping.source <= from && mapping.source + mapping.range >= from + range) {
                //complete overlap
                return List.of();
            } else if (from < mapping.source && from + range > mapping.source + mapping.range) {
                //inside
                return List.of(
                        new SeedRange(from, mapping.source - from),
                        new SeedRange(mapping.source + mapping.range, from + range - (mapping.source + mapping.range))
                );
            } else {
                if (from < mapping.source) {
                    return List.of(new SeedRange(from, mapping.source - from));
                } else {
                    long from = mapping.source + mapping.range;
                    return List.of(new SeedRange(from, this.from + range - from));
                }
            }
        }
    }

    public static void main(String[] args) {
        SeedRange seedRange = new SeedRange(70L, 16L);
        Mapping mapping = new Mapping(75L, 75L, 6L);
        System.out.println(seedRange.overlap(mapping));
        System.out.println(seedRange.inverse(mapping));
    }

    private Long toDestination(Long seed, List<List<Mapping>> mappings) {
        Long currentDestination = seed;
        for (List<Mapping> mapping : mappings) {
            for (Mapping currentMapping : mapping) {
                if (currentMapping.matches(currentDestination)) {
                    currentDestination = currentMapping.valueOf(currentDestination);
                    break;
                }
            }
        }
        return currentDestination;
    }

    private List<List<Mapping>> parseMappings(List<String> input) {
        List<List<Mapping>> mappings = new ArrayList<>();
        ArrayList<Mapping> currentMappingList = new ArrayList<>();
        mappings.add(currentMappingList);
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            if (line.contains("map")) {

            } else if (line.isEmpty()) {
                currentMappingList = new ArrayList<>();
                mappings.add(currentMappingList);
            } else {
                String[] mapping = line.split(" ");
                currentMappingList.add(new Mapping(Long.parseLong(mapping[0]), Long.parseLong(mapping[1]), Long.parseLong(mapping[2])));
            }
        }
        return mappings;
    }

    private record Mapping(Long destination, Long source, Long range) {
        public boolean matches(Long l) {
            return l >= source && l <= source + range;
        }

        public Long valueOf(Long l) {
            return destination + (l - source);
        }
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day05.txt")));
    }
}
