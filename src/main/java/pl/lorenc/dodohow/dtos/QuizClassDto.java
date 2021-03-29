package pl.lorenc.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizClassDto {
    private Long id;
    private Long teacherId;
    private Set<Long> students;
    private Set<Long> quizList;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Pole musi mieć od 5 do 100 znaków")
    private String title;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Pole musi mieć od 5 do 100 znaków")
    private String description;
}
