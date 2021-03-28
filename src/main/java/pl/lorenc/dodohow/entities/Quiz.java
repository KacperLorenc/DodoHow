package pl.lorenc.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "id:" + id + ", title:'" + title ;
    }
}
