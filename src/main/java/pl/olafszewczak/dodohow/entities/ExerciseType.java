package pl.olafszewczak.dodohow.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ExerciseType {
    CHOOSE_ANSWER("Choose answer"),
    SELECT_WORDS("Select words"),
    TYPE_SENTENCE("Type a sentence");

    private String label;

    public static Optional<ExerciseType> findByLabel(String label){
        for(ExerciseType e : ExerciseType.values()){
            if(e.getLabel().equals(label)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
