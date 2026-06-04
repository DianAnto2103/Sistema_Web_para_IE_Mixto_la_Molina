// estudiante_form.js - Validaciones adicionales
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const telefonoInput = document.querySelector('input[name="telefono"]');
    
    // Validar que el teléfono solo tenga números
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }
    
    // Validar correo antes de enviar
    if (form) {
        form.addEventListener('submit', function(e) {
            const email = document.querySelector('input[name="correo"]').value;
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (email && !emailPattern.test(email)) {
                e.preventDefault();
                alert('Por favor, ingrese un correo electrónico válido');
            }
        });
    }
});