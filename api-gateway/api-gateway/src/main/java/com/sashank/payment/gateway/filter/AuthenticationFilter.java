package com.sashank.payment.gateway.filter;

import com.sashank.payment.gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // PUBLIC ROUTES
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            return chain.filter(exchange);
        }

        // BLOCK DIRECT ACCESS TO NOTIFICATION SERVICE
        if (path.startsWith("/notify")) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        // ADMIN ONLY ROUTES
        if (path.startsWith("/wallet/admin") || path.startsWith("/auth/admin")) {

            if (!role.equals("Admin")) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        // USER OR ADMIN ROUTES
        if (path.startsWith("/wallet") || path.startsWith("/payment")) {

            if (!(role.equals("USER") || role.equals("Admin"))) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        // Forward headers to microservices
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("X-User-Email", email)
                .header("X-User-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}