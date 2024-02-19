package com.kit.profile.controller;

import com.kit.profile.model.request.profile.PasswordResetRequest;
import com.kit.profile.model.request.profile.ProfileRequest;
import com.kit.profile.model.request.profile.SignInRequest;
import com.kit.profile.model.response.CommonResponse;
import com.kit.profile.model.response.profile.Profile;
import com.kit.profile.model.response.profile.ProfileResponse;
import com.kit.profile.service.ProfileService;
import com.kit.profile.utils.UserValidation;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Profile Access Management", value = "Profile Access Management")
@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private final UserValidation userValidation;

    @PostMapping("/signup")
    public ProfileResponse signup(@RequestHeader String uniqueInteractionId,
                                  @RequestBody ProfileRequest request,
                                  @RequestHeader(name = "user-name") String userName,
                                  @RequestHeader(name = "password") String password) {
        log.info("interactionId :: [{}] request : {} sign up ", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return profileService.signUp(request, uniqueInteractionId);
    }

    @Deprecated
    @PostMapping("/send-otp")
    public ProfileResponse sendOtp(@RequestHeader String uniqueInteractionId,
                                   @RequestHeader String email,
                                   @RequestHeader(name = "user-name") String userName,
                                   @RequestHeader(name = "password") String password) {
        log.info("interactionId :: [{}] request : {} email id  ", uniqueInteractionId, email);
        userValidation.validate(userName, password);
        return profileService.sendOtp(email, uniqueInteractionId);
    }

    @Deprecated
    @PostMapping("/validate-otp")
    public ProfileResponse validateOtp(@RequestHeader String uniqueInteractionId,
                                       @RequestBody ProfileRequest request,
                                       @RequestHeader(name = "user-name") String userName,
                                       @RequestHeader(name = "password") String password) {
        log.info("interactionId :: [{}] request : {} sign up ", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return profileService.validateOtp(request, uniqueInteractionId);
    }

    @PostMapping("/signin")
    public ProfileResponse signIn(@RequestHeader String uniqueInteractionId,
                                  @RequestBody SignInRequest request,
                                  @RequestHeader(name = "user-name") String userName,
                                  @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} sign in ", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return profileService.signIn(request);
    }

    @Deprecated
    @PutMapping("/password/reset")
    public CommonResponse passwordUpdate(@RequestHeader String uniqueInteractionId,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password,
                                         @RequestBody PasswordResetRequest request) throws Exception {
        log.info("interactionId :: [{}] request : {} password reset", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return profileService.passwordReset(request);
    }

    @Deprecated
    @PutMapping("/password/send-key")
    public CommonResponse passwordResetKeyGenerateAndSend(@RequestHeader String uniqueInteractionId,
                                                          @RequestHeader(name = "user-name") String userName,
                                                          @RequestHeader(name = "password") String password,
                                                          @RequestParam String email) throws Exception {
        log.info("interactionId :: [{}] request : {} password reset key generate", uniqueInteractionId, email);
        userValidation.validate(userName, password);
        return profileService.passwordResetKeyGenerateAndSend(email);
    }

    @GetMapping
    public Page<Profile> getUsers(@RequestHeader String uniqueInteractionId,
                                  @RequestHeader(name = "user-name") String userName,
                                  @RequestHeader(name = "password") String password,
                                  @RequestParam(name = "email", required = false) String email,
                                  @RequestParam(name = "phone-no", required = false) String phoneNo,
                                  @RequestParam(name = "first-name", required = false) String firstName,
                                  @RequestParam(name = "last-name", required = false) String lastName,
                                  @RequestParam(name = "page") int page,
                                  @RequestParam(name = "size") int size
    ) throws Exception {

        log.info("interactionId :: [{}] request for get profile details , email {} , phoneNo : {} firstName : {} lastName : {} page : {} ,size : {} ", uniqueInteractionId, email, phoneNo, firstName, lastName, page, size);
        userValidation.validate(userName, password);
        return profileService.getUsers(email, phoneNo, firstName, lastName, page, size);
    }
}
