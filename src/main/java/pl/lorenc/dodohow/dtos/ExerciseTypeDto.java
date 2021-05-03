package pl.lorenc.dodohow.dtos;

import lombok.*;
import pl.lorenc.dodohow.entities.ExerciseType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExerciseTypeDto {

    private String type;
    private Long quizId;
    @Setter(AccessLevel.NONE)
    private List<String> types = ExerciseType.getAllLabels();

}
