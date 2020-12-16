package pl.olafszewczak.dodohow.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotBlank(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Login musi mieć przynajmniej 5 znaków")
    private String login;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotBlank(message = "*Pole musi być uzupełnione")
    @Size(min = 5, max = 100, message = "Hasło musi mieć przynajmniej 5 znaków")
    private String password;
    @NotNull(message = "*Pole musi być uzupełnione")
    @NotBlank(message = "*Pole musi być uzupełnione")
    @Size(min = 3, max = 100, message = "Email musi mieć przynajmniej 3 znaki")
    private String email;
}
