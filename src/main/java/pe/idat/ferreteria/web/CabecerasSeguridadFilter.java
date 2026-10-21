package pe.idat.ferreteria.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Cabeceras de seguridad básicas para las pantallas: evita que se incrusten en un iframe (clickjacking)
 * y, con la CSP, que se ejecute JavaScript inyectado si un texto llegara a pintarse sin escapar.
 * Swagger UI queda fuera de la CSP porque usa scripts en línea.
 */
@Component
public class CabecerasSeguridadFilter extends OncePerRequestFilter {

    static final String CSP = "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; "
            + "img-src 'self' data:; object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("Referrer-Policy", "same-origin");
        String uri = req.getRequestURI();
        if (!uri.startsWith("/swagger-ui") && !uri.startsWith("/v3/api-docs")) {
            res.setHeader("Content-Security-Policy", CSP);
        }
        chain.doFilter(req, res);
    }
}
