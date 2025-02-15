package by.vladzuev.locationreceiver.controller.registration;

import by.vladzuev.locationreceiver.service.registration.RegistrationService;
import by.vladzuev.locationreceiver.service.registration.model.RegisteredUserRequest;
import by.vladzuev.locationreceiver.service.registration.model.RegisteredUserResponse;
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
    public ResponseEntity<RegisteredUserResponse> checkIn(@Valid @RequestBody final RegisteredUserRequest request) {
        final RegisteredUserResponse response = registrationService.checkIn(request);
        return ResponseEntity.ok(response);
    }
}
