package pl.olafszewczak.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SectionType {
    FIRST_SECTION("First section", 1),
    JEDZENIE("Jedzenie", 2),
    PRACA("Praca", 3);

    private final String label;
    private final Integer number;

    public static Optional<SectionType> findByLabel(String label) {
        for (SectionType s : SectionType.values()) {
            if (s.getLabel().equals(label)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
    public static Optional<SectionType> findByNumber(Integer number) {
        for (SectionType s : SectionType.values()) {
            if (s.getNumber().equals(number)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
