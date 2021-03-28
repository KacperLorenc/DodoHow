package pl.lorenc.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum QuizType {
    FIRST_QUIZ("First quiz", 1),
    FOOD("Jedzenie", 2),
    WORK("Praca", 3);

    private final String label;
    private final Integer number;

    public static Optional<QuizType> findByLabel(String label) {
        for (QuizType s : QuizType.values()) {
            if (s.getLabel().equals(label)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
    public static Optional<QuizType> findByNumber(Integer number) {
        for (QuizType s : QuizType.values()) {
            if (s.getNumber().equals(number)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
