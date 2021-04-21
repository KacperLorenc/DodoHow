package pl.lorenc.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizWithScoreDto {
    private Long id;
    private String title;
    private Integer maxScore;
    private Integer userScore;
    private String username;
    private boolean repeatable;
    private boolean finished;
}
