package pl.lorencszewczak.dodohow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Credentials")
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

//    public String encryptPassword(String password) throws Exception {
//        return AESCrypt.encrypt(password);
//    }
//
//    public String decryptPassword(String password) throws Exception {
//        return AESCrypt.decrypt(password);
//    }


}
