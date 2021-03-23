package pl.lorenc.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointsDto {
    private Long id;
    private Long exerciseId;
    private Integer exerciseNumber;
    private Long userId;
    private Integer userScore;
    private Integer maxScore;
    private String question;
    private boolean skipped = false;

    public PointsDto(Long id, Long exerciseId, Integer exerciseNumber, Long userId, Integer userScore, Integer maxScore, String question) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.userId = userId;
        this.userScore = userScore;
        this.maxScore = maxScore;
        this.question = question;
        this.exerciseNumber = exerciseNumber;
    }
}
