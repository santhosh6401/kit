package com.kit.productpurchase.service;

import com.kit.productpurchase.model.entity.ProfileEntity;
import com.kit.productpurchase.repository.ProfileRepository;
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

