package pe.idat.ferreteria.reportes.composite;

/**
 * Escapa texto antes de meterlo en HTML. Así un nombre de producto como {@code <script>} se muestra como
 * texto y no se ejecuta en el navegador (los reportes se muestran con {@code th:utext}).
 */
public final class Html {

    private Html() {
    }

    public static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder resultado = new StringBuilder(texto.length());
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '&' -> resultado.append("&amp;");
                case '<' -> resultado.append("&lt;");
                case '>' -> resultado.append("&gt;");
                case '"' -> resultado.append("&quot;");
                case '\'' -> resultado.append("&#39;");
                default -> resultado.append(c);
            }
        }
        return resultado.toString();
    }
}
