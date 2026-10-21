package pe.idat.ferreteria.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * Protección contra CSRF sin Spring Security: rechaza con 403 las peticiones que modifican datos
 * (POST, PUT, DELETE...) cuando el navegador indica que vienen de otro sitio.
 * <p>
 * Los navegadores envían {@code Sec-Fetch-Site} y {@code Origin} en todo envío entre sitios; las
 * herramientas como curl, Postman o Swagger no los envían o los envían del mismo origen, así que siguen funcionando.
 */
@Component
public class OrigenMismoSitioFilter extends OncePerRequestFilter {

    private static final Set<String> METODOS_SEGUROS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (!METODOS_SEGUROS.contains(req.getMethod()) && vieneDeOtroSitio(req)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean vieneDeOtroSitio(HttpServletRequest req) {
        String sitio = req.getHeader("Sec-Fetch-Site");
        if (sitio != null && !sitio.equals("same-origin") && !sitio.equals("none")) {
            return true;
        }
        String origen = req.getHeader("Origin");
        String propio = req.getScheme() + "://" + req.getHeader("Host");
        return origen != null && !origen.equals(propio);
    }
}
