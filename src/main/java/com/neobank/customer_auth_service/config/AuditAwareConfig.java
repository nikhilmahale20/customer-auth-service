//package com.neobank.customer_auth_service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.auditing.DateTimeProvider;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.time.OffsetDateTime;
//import java.util.Optional;
//
//@Configuration
//public class AuditAwareConfig {
//
//    @Bean
//    public AuditorAware<String> auditorAware() {
//
//        return () -> {
//
//            Authentication authentication =
//                    SecurityContextHolder
//                            .getContext()
//                            .getAuthentication();
//
//            if (authentication != null
//                    && authentication.isAuthenticated()) {
//
//                return Optional.of(
//                        authentication.getName()
//                );
//            }
//
//            return Optional.of("SYSTEM");
//        };
//    }
//
//    @Bean
//    public DateTimeProvider dateTimeProvider() {
//
//        return () ->
//                Optional.of(
//                        OffsetDateTime.now()
//                );
//    }
//}