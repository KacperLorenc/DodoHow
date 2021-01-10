package pl.olafszewczak.dodohow.dtos;

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
    private Long sectionId;
    private String type;

    public List<String> getListOfWrongAnswers(){
        String[] answers = wrongAnswers.split(";");
        return Lists.newArrayList(answers);
    }
}
