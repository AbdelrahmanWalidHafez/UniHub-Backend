package com.unihub.auth.accountmanagement.strategy.resolver;

import com.unihub.auth.accountmanagement.strategy.UserCreationStrategy;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StrategyResolver {

    private final Map<UserRoles, UserCreationStrategy> strategyMap;

    public StrategyResolver(List<UserCreationStrategy>strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        UserCreationStrategy::getSupportedRole,
                        s -> s
                ));
    }

    public UserCreationStrategy resolve(UserRoles role) {
        UserCreationStrategy strategy = strategyMap.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for role: " + role);
        }
        return strategy;
    }

}
