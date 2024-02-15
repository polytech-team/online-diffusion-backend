package team.polytech.online.diffusion.controller;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import team.polytech.online.diffusion.api.ExceptionController;
import java.util.HashSet;
import java.util.Set;

public class ExceptionApiTests {
    private ExceptionController exceptionController = new ExceptionController();

    @Test
    public void UserApiController_getProfile_OK() {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        ConstraintViolationException exception = new ConstraintViolationException("Validation failed", constraintViolations);

        Assertions.assertThat(exceptionController.handleConstraintViolation(exception).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
