package pl.lorenc.dodohow.dtos;

import lombok.*;
import pl.lorenc.dodohow.utility.ExerciseType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExerciseTypeDto {

    private Long exerciseId;
    private String type;
    private Long quizId;
    @Setter(AccessLevel.NONE)
    private List<String> types = ExerciseType.getAllLabels();

}
