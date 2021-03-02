package pl.lorenc.dodohow.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lorenc.dodohow.validation.PasswordMatches;
import pl.lorenc.dodohow.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UserDto {

    private Long id;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Login musi mieć przynajmniej 5 znaków")
    private String login;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Hasło musi mieć przynajmniej 5 znaków")
    private String password;
    private String matchingPassword;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotEmpty(message = "*Pole musi być uzupełnione")
    @ValidEmail
    private String email;
    private Boolean active;
    private String roles;
    private Set<SectionDto> sections;

    public UserDto(Long id, @NotNull(message = "*Pole musi być uzupełnione") @NotEmpty(message = "*Pole musi być uzupełnione") @Size(min = 5, max = 100, message = "Login musi mieć przynajmniej 5 znaków") String login, @NotNull(message = "*Pole musi być uzupełnione") @NotEmpty(message = "*Pole musi być uzupełnione") @Size(min = 5, max = 100, message = "Hasło musi mieć przynajmniej 5 znaków") String password,  @NotNull(message = "*Pole musi być uzupełnione") @NotEmpty(message = "*Pole musi być uzupełnione") String email, Boolean active, String roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }
}
