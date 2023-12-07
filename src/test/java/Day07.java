import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day07 {

    @Test
    void partOneExample() {
        List<String> input = List.of(
                "32T3K 765",
                "T55J5 684",
                "KK677 28",
                "KTJJT 220",
                "QQQJA 483"
        );
        final int sum = getTotalWinnings(input);
        assertThat(sum).isEqualTo(6440);
    }


    @Test
    void partOne() throws IOException {
        List<String> input =readToList();
        final int sum = getTotalWinnings(input);
        System.out.println(sum);
    }

    private int getTotalWinnings(List<String> input) {
        List<Hand> handsSorted = input.stream().map(s -> s.split(" "))
                .map(parts -> new Hand(parts[0], Integer.parseInt(parts[1])))
                .sorted(Hand::compareTo)
                .toList();

        return calculateTotalWinnings(handsSorted);
    }

    private int calculateTotalWinnings(List<Hand> sorted) {
        int numberOfHands = sorted.size();
        int sum = 0;
        for (int i = 0; i < sorted.size(); i++) {
            Hand hand = sorted.get(i);
            int handValue = hand.bet * (numberOfHands - i);
            sum += handValue;
        }
        return sum;
    }

    public class Hand implements Comparable<Hand> {

        private final List<CardType> cards;
        private final int bet;
        private final HandType handType;

        public Hand(String cards, int bet) {
            Map<Character, Integer> countByCard = new HashMap<>();
            List<CardType> cardTypes = new ArrayList<>();
            for (char c : cards.toCharArray()) {
                countByCard.put(c, countByCard.getOrDefault(c, 0) + 1);
                cardTypes.add(CardType.fromLetter(c));
            }
            List<Integer> list = countByCard.values().stream().sorted((o1, o2) -> Integer.compare(o2, o1)).toList();
            this.handType = switch (list.get(0)) {
                case 5 -> HandType.FIVE_OF_A_KIND;
                case 4 -> HandType.FOUR_OF_A_KIND;
                case 3 -> list.get(1) == 2 ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
                case 2 -> list.get(1) == 2 ? HandType.TWO_PAIR : HandType.ONE_PAIR;
                default -> HandType.HIGH_CARD;
            };
            this.cards = cardTypes;
            this.bet = bet;
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "cards='" + cards + '\'' +
                    ", bet=" + bet +
                    ", handType=" + handType +
                    '}';
        }

        @Override
        public int compareTo(Hand other) {
            int typeCompare = handType.compareTo(other.handType);
            if (typeCompare == 0) {
                for (int i = 0; i < cards.size(); i++) {
                    CardType card = cards.get(i);
                    CardType cardOther = other.cards.get(i);
                    int cardCompare = card.compareTo(cardOther);
                    if (cardCompare == 0) {
                        //compare next card
                    } else {
                        return cardCompare;
                    }
                }
                return 0;
            } else {
                return typeCompare;
            }
        }
    }

    enum CardType {
        ACE('A'),
        KING('K'),
        QUEEN('Q'),
        JACK('J'),
        TEN('T'),
        NINE('9'),
        EIGHT('8'),
        SEVEN('7'),
        SIX('6'),
        FIVE('5'),
        FOUR('4'),
        THREE('3'),
        TWO('2');
        private final char letter;

        CardType(char letter) {
            this.letter = letter;
        }

        public static CardType fromLetter(char c) {
            for (CardType cardType : CardType.values()) {
                if (cardType.letter == c) {
                    return cardType;
                }
            }
            throw new IllegalStateException("Invalid card type " + c);
        }
    }

    enum HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
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
        return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("Day07.txt")));
    }
}
