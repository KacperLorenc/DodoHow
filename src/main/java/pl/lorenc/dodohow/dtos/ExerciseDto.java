package pl.lorenc.dodohow.dtos;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDto {
    private Long id;
    private Integer maxScore;
    private String question;
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
