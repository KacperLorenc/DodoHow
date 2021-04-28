package pl.lorenc.dodohow.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<Exercise> exercises;
    private Integer maxScore;
    private Integer numberInClass;
    @ManyToOne
    private QuizClass quizClass;
    private Boolean active;
    private Boolean repeatable;

    @Override
    public String toString() {
        return "id:" + id + ", title:'" + title ;
    }
}
