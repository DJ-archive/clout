package com.clout.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {

    }

    //login -> token -> users (with token) -> header(include token)
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        Jws<Claims> subject = null;

        try {
//            subject = Jwts.parser().setSigningKey(env.getProperty("jwt.secret"))
//                    .parseClaimsJws(jwt).getBody()
//                    .getSubject();
            subject = Jwts.parserBuilder()
                    .setSigningKey(env.getProperty("jwt.secret"))
                    .build()
                    .parseClaimsJws(jwt);
        } catch (Exception ex) {
            ex.printStackTrace();
            returnValue = false;
        }

        if (subject == null || subject.equals("")) {
            returnValue = false;
        }

        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        String responseBody = err;
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes());
        log.error(err);
        return response.writeWith(Mono.just(buffer));
    }
}
