package pl.lorenc.dodohow.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ExerciseType {
    TRUTH_FALSE("truthFalse", "Prawda lub fałsz"),
    CHOOSE_ANSWER("chooseAnswer", "Wybierz odpowiedź"),
    TYPE_SENTENCE("typeSentence", "Przetłumacz zdanie"),
    FILL_THE_BLANK("fillTheBlank", "Uzupełnij lukę"),
    TRANSLATE_WORD("translateWord", "Przetłumacz słowo");

    private final String name;
    private final String label;

    public static Optional<ExerciseType> findByName(String name) {
        for (ExerciseType e : ExerciseType.values()) {
            if (e.getName().equals(name)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public static Optional<ExerciseType> findByLabel(String label) {
        for (ExerciseType e : ExerciseType.values()) {
            if (e.getLabel().equals(label)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public static List<String> getAllNames() {
        return Arrays.stream(values())
                .map(ExerciseType::getName)
                .collect(Collectors.toList());
    }

    public static List<String> getAllLabels() {
        return Arrays.stream(values())
                .map(ExerciseType::getLabel)
                .collect(Collectors.toList());
    }
}
