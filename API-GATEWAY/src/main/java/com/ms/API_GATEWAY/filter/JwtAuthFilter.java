package com.ms.API_GATEWAY.filter;

import com.ms.API_GATEWAY.dto.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class JwtAuthFilter implements GlobalFilter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest(); // extract request
        String path = exchange.getRequest().getURI().getPath(); // extract path for making login , register public

        // 💡 Skip token validation for public routes
        // coz gateway apply filter on all routes  that's why we need to make it public routes
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            return chain.filter(exchange);  // pass to next filter
        }

        String rawAuth = request.getHeaders().getFirst("Authorization"); // check for token

       // if the no token get or token doesn't start with "Bearer " return Unauthorized
        if(rawAuth == null || !rawAuth.startsWith("Bearer "))
        {
            return onError(exchange, "Authorization header missing", HttpStatus.UNAUTHORIZED);
        }

       // if the token is valid
        String token = rawAuth.substring(7); // strip Bearer from token

       return webClientBuilder.build()
               .get()
               .uri("http://AUTH-SERVICE/auth/validate")
               .header("Authorization", "Bearer " + token)
               .retrieve()
               .bodyToMono(AuthResponseDTO.class)

               .flatMap(authResponse -> {
                   ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                           .header("X-User-Id" , String.valueOf(authResponse.getId()))
                           .header("X-User-Email" , authResponse.getEmail())
                           .header("X-User-Role" , authResponse.getRoles().getFirst())
                           .build();
                   // forward this modified request to downstream filter
                 return chain.filter(exchange.mutate().request(modifiedRequest).build());
               })
               .onErrorResume(e -> {
                   return onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
               });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
