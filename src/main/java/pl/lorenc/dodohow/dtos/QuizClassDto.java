package pl.lorenc.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizClassDto {
    private Long id;
    private Long teacherId;
    private Set<Long> students;
    private Set<Long> quizList;
    private String title;
    private String description;
}
