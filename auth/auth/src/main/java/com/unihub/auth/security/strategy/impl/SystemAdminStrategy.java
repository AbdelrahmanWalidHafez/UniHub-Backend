package com.unihub.auth.security.strategy.impl;

import com.unihub.auth.common.exception.model.MaxUserAmountExceededException;
import com.unihub.auth.common.exception.model.SubscriptionException;
import com.unihub.auth.internal.client.SubscriptionFeignClient;
import com.unihub.auth.internal.client.UniversityFeignClient;
import com.unihub.auth.internal.dto.response.SubscriptionPlanResponseDto;
import com.unihub.auth.internal.dto.response.UniversityResponse;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.repository.UserRepository;
import com.unihub.auth.security.strategy.JwtGenerationStrategy;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SystemAdminStrategy implements JwtGenerationStrategy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityFeignClient universityFeignClient;

    @Autowired
    private SubscriptionFeignClient subscriptionFeignClient;

    @Override
    public String generateJwt(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key, long expiration) {
        JwtBuilder jwt= Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("uniHub")
                .setSubject("access-token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .claim("add_users",true)
                .claim("active",true)
                .claim("email", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .claim("university_id",
                        (universityMetadata != null ?
                                universityMetadata.getTid() : "N/A"))
                .signWith(key);
        try{
          handleRequest(universityMetadata);
        }catch (SubscriptionException e){
            jwt.claim("add_users",false);
            jwt.claim("active",false);
        }catch (MaxUserAmountExceededException e){
            jwt.claim("active",false);
        }
        return jwt.compact();
    }

    private void handleRequest(UniversityMetadata universityMetadata) {
        UniversityResponse university = fetchUniversityResponse(universityMetadata)
                .orElseThrow(() -> new SubscriptionException("University has no subscription"));

        if (university.getSubscriptionPlan() == null) {
            throw new SubscriptionException("No subscription plan assigned");
        }
        if (!university.getSubscriptionPlan().getEndDate().isAfter(LocalDate.now())) {
                throw new SubscriptionException("Subscription Date Exceeded or there is no current Subscription Plan");
        }
        SubscriptionPlanResponseDto subscriptionPlanResponseDto=fetchSubscriptionPlan(university.getSubscriptionPlan().getPid());
        long currentCount=userRepository.countByUniversityTid(universityMetadata.getTid());
        if(subscriptionPlanResponseDto.getMaxUserAmount()<=currentCount){
            throw new MaxUserAmountExceededException("Maximum User Amount Exceeded");
        }
    }

    private Optional<UniversityResponse> fetchUniversityResponse(UniversityMetadata universityMetadata) {
        return Optional.ofNullable(universityFeignClient.fetchUniversity(universityMetadata.getTid()).getBody());
    }

    private SubscriptionPlanResponseDto fetchSubscriptionPlan(UUID pid) {
        SubscriptionPlanResponseDto response= subscriptionFeignClient.getSubscriptionPlan(pid).getBody();
        if(response==null){
            throw new RuntimeException("Service might not be available right now,We will resolve the error soon try again later");
        }
        return response;
    }









}
