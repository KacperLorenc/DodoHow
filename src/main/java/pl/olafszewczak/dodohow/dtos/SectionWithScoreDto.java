package pl.olafszewczak.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionWithScoreDto {
    private Long id;
    private String title;
    private Integer maxScore;
    private Integer userScore;
}
