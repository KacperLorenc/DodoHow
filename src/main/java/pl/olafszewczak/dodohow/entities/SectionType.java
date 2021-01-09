package pl.olafszewczak.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SectionType {
    FIRST_SECTION("First section"),
    NORMAL_SECTION("Normal section");

    private String label;

    public static Optional<SectionType> findByLabel(String label){
        for(SectionType s : SectionType.values()){
            if(s.getLabel().equals(label)){
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
