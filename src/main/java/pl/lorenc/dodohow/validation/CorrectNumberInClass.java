package pl.lorenc.dodohow.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.dtos.QuizDto;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.services.ClassService;
import pl.lorenc.dodohow.services.QuizService;

import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CorrectNumberValidator.class)
@ReportAsSingleViolation
@Documented
public @interface CorrectNumberInClass {
    String message() default "*Numer musi być unikalny i większy od zera";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Service
class CorrectNumberValidator implements ConstraintValidator<CorrectNumberInClass, QuizDto> {

    private final QuizService quizService;
    private final ClassService classService;

    @Autowired
    public CorrectNumberValidator(QuizService quizService, ClassService classService) {
        this.quizService = quizService;
        this.classService = classService;
    }

    @Override
    public void initialize(CorrectNumberInClass constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(QuizDto quizDto, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid;

        Long classId = quizDto.getClassId();

        if (classId == null) {
            isValid = false;
        } else {
            isValid = classService.findById(classId)
                    .map(c -> {
                        Set<Quiz> quizzes = quizService.findAllByClassId(classId);
                        if (quizzes == null || quizzes.isEmpty())
                            return quizDto.getNumberInClass() > 0;
                        else if (quizzes.stream().noneMatch(q -> quizDto.getNumberInClass().equals(q.getNumberInClass())))
                            return quizDto.getNumberInClass() > 0;
                        return false;
                    }).orElse(false);
        }

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("numberInClass").addConstraintViolation();
        }

        return isValid;
    }
}