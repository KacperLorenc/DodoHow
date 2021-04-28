package pl.lorenc.dodohow.dtos;

import lombok.*;
import pl.lorenc.dodohow.validation.CorrectNumberInClass;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CorrectNumberInClass
public class QuizDto {
    private Long id;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "*Pole musi mieć od 5 do 100 znaków")
    private String title;
    private Set<ExerciseDto> exercises;
    private Integer maxScore;
    private Integer numberInClass;
    private Long classId;
    private Boolean active;
    private Boolean repeatable;
}
