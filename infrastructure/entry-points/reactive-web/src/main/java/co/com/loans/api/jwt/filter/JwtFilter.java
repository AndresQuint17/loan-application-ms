package co.com.loans.api.jwt.filter;

import co.com.loans.api.exception.BadTokenError;
import co.com.loans.api.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    private final JwtTokenProvider tokenProvider;

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Permitir el acceso a la ruta de login sin token
        if (path.contains("login")) {
            return chain.filter(exchange);
        }

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Si no hay token, propagamos el error al manejador global
        if (auth == null) {
            return Mono.error(new BadTokenError("No token was found"));
        }

        if (!auth.startsWith("Bearer ")) {
            // Token mal formado, también propagamos el error
            return Mono.error(new BadTokenError("Invalid auth"));
        }
        String token = auth.replace("Bearer ", "");
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }
}
