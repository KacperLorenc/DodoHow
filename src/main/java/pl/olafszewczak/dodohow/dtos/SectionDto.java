package pl.olafszewczak.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto {
    private Long id;
    private String title;
    private Set<ExerciseDto> exercises;
    private Integer maxScore;
    private Integer numberInClass;
}
