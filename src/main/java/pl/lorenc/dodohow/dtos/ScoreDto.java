package pl.lorenc.dodohow.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDto {
    private Long id;
    private Long userId;
    private Long sectionId;
    private Integer score;
}
