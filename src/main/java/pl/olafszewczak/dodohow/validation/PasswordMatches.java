package pl.olafszewczak.dodohow.validation;

import pl.olafszewczak.dodohow.dtos.UserDto;

import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@ReportAsSingleViolation
@Documented
public @interface PasswordMatches {
    String message() default "*Hasła muszą być takie same";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserDto user = (UserDto) obj;

        boolean isValid = user.getPassword().equals(user.getMatchingPassword());

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "matchingPassword" ).addConstraintViolation();
        }
        return isValid;
    }
}