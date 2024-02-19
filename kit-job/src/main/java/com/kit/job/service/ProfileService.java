package com.kit.job.service;

import com.kit.job.model.entity.ProfileEntity;
import com.kit.job.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileEntity getProfileDetails(String email) {
        Optional<ProfileEntity> entityOptional = profileRepository.findByEmail(email);
        return entityOptional.orElse(null);
    }
}

