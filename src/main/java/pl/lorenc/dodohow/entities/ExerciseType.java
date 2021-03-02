package pl.lorenc.dodohow.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ExerciseType {
    CHOOSE_ANSWER("chooseAnswer"),
    TRUTH_FALSE("truthFalse"),
    TYPE_SENTENCE("typeSentence"),
    FILL_THE_BLANK("fillTheBlank"),
    TRANSLATE_WORD("translateWord");

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
