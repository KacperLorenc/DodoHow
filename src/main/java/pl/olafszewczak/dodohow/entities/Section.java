package pl.olafszewczak.dodohow.entities;

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
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private Set<Exercise> exercises;
    private SectionType sectionType;

    @Override
    public String toString() {
        return "id:" + id + ", title:'" + title ;
    }
}
