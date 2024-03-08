package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.service.registration.model.RegisteredUserRequest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String checkIn(@Valid @RequestBody final RegisteredUserRequest user) {
        final User savedUser = registrationService.checkIn(user);
        return ResponseEntity.ok(savedUser);
    }
}
