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
    private Section section;
    private Integer score;
    private LocalDateTime dateTime;

    public Score(Long id, User user, Section section, Integer score) {
        this.id = id;
        this.user = user;
        this.section = section;
        this.score = score;
        this.dateTime = LocalDateTime.now();
    }
}
