package soma.edupi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "Health Check API")
public class HealthCheckController {

    @GetMapping("/")
    @Operation(summary = "헬스 체크")
    public String healthCheck() {
        return "OK";
    }

}
