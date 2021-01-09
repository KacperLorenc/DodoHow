package pl.olafszewczak.dodohow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.services.CredentialsService;

import java.util.UUID;

@Service
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private CredentialsService service;
    private final JavaMailSender emailSender;

    @Autowired
    public RegistrationListener(CredentialsService service, JavaMailSender emailSender) {
        this.service = service;
        this.emailSender = emailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Potwierdzenie rejestracji";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Kliknij w poniższy link aby dokończyć proces rejestracji:" + "\r\n" + "http://localhost:8080" + confirmationUrl);
        emailSender.send(email);
    }
}
