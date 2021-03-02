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
    private Long userId;
    private Integer userScore;
    private Integer maxScore;
}
