package pl.lorenc.dodohow.entities;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lorenc.dodohow.utility.ExerciseType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer maxScore;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String answer;
    @Column(nullable = false)
    private String wrongAnswers;
    @ManyToOne()
    private Quiz quiz;
    private ExerciseType type;
    private Integer number;

    public List<String> getListOfWrongAnswers(){
      String[] answers = wrongAnswers.split(";");
      return Lists.newArrayList(answers);
    }

    @Override
    public String toString() {
        return "id:" + id + ", maxScore:" + maxScore + ", question:'" + question;
    }
}
