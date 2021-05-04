package pl.lorenc.dodohow.dtos;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDto {
    private Long id;
    @NotNull(message = "*Pole musi być uzupełnione")
    @Min(value = 1, message = "*Punkty muszą być większe o zera")
    @Max(value = 15, message = "*Maksymalna ilość punktów za zadanie to 15")
    private Integer maxScore;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 3, max = 100, message = "*Pole musi mieć od 3 do 100 znaków")
    private String question;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 3, max = 100, message = "*Pole musi mieć od 3 do 100 znaków")
    private String answer;
    private String wrongAnswers;
    private Long quizId;
    private String type;
    private String userAnswer;
    private Integer number;

    public List<String> getListOfWrongAnswers(){
        String[] answers = wrongAnswers.split(";");
        return Lists.newArrayList(answers);
    }

    public ExerciseDto(Long id, Integer maxScore, String question, String answer, String wrongAnswers, Long quizId, String type, Integer number) {
        this.id = id;
        this.maxScore = maxScore;
        this.question = question;
        this.answer = answer;
        this.wrongAnswers = wrongAnswers;
        this.quizId = quizId;
        this.type = type;
        this.userAnswer = null;
        this.number = number;
    }
}
