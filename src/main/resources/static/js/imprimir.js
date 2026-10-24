// Botón "Imprimir / guardar PDF" de la pantalla de reportes. Va en un archivo aparte (y no en un onclick)
// porque la política de seguridad de contenido (CSP) no permite JavaScript dentro del HTML.
document.addEventListener('DOMContentLoaded', function () {
    var boton = document.getElementById('boton-imprimir');
    if (boton) {
        boton.addEventListener('click', function () {
            window.print();
        });
    }
});
