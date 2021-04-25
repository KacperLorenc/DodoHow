package pl.lorenc.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Quiz quiz;
    private Integer score;
    private LocalDateTime dateTime;
    private Boolean quizFinished;

    public Score(Long id, User user, Quiz quiz, Integer score, boolean quizFinished) {
        this.id = id;
        this.user = user;
        this.quiz = quiz;
        this.score = score;
        this.dateTime = LocalDateTime.now();
        this.quizFinished = quizFinished;
    }
}
