package com.project.ksiazeczkazdrowiadlazwierzat.controller.util;

import org.springframework.security.core.context.SecurityContextHolder;

import static com.project.ksiazeczkazdrowiadlazwierzat.service.UserService.VET;

public class UserUtils {

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static boolean isLoggedAsVet() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(VET));
    }
}
