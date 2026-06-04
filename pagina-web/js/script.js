// ========== INIT AOS ANIMATIONS ==========
AOS.init({
    duration: 800,
    once: true,
    offset: 100
});

// ========== NAVBAR SCROLL EFFECT ==========
window.addEventListener('scroll', function() {
    const navbar = document.getElementById('mainNavbar');
    if (window.scrollY > 50) {
        navbar.classList.add('navbar-scrolled');
    } else {
        navbar.classList.remove('navbar-scrolled');
    }
});

// ========== COUNTER ANIMATION ==========
const counters = document.querySelectorAll('.counter');
const speed = 200;

const animateCounter = (counter) => {
    const target = parseInt(counter.getAttribute('data-target'));
    let count = 0;
    const increment = target / speed;
    
    const updateCount = () => {
        if (count < target) {
            count += increment;
            counter.innerText = Math.ceil(count);
            setTimeout(updateCount, 20);
        } else {
            counter.innerText = target;
        }
    };
    
    updateCount();
};

// Intersection Observer para activar contadores cuando sean visibles
const observerOptions = {
    threshold: 0.5,
    rootMargin: "0px"
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const counter = entry.target;
            animateCounter(counter);
            observer.unobserve(counter);
        }
    });
}, observerOptions);

counters.forEach(counter => {
    observer.observe(counter);
});

// ========== GALERÍA LIGHTBOX ==========
const galleryItems = document.querySelectorAll('.galeria-item');
galleryItems.forEach(item => {
    item.addEventListener('click', function() {
        const imgSrc = this.querySelector('img').src;
        const modal = document.createElement('div');
        modal.style.position = 'fixed';
        modal.style.top = '0';
        modal.style.left = '0';
        modal.style.width = '100%';
        modal.style.height = '100%';
        modal.style.backgroundColor = 'rgba(0,0,0,0.9)';
        modal.style.display = 'flex';
        modal.style.alignItems = 'center';
        modal.style.justifyContent = 'center';
        modal.style.zIndex = '9999';
        modal.style.cursor = 'pointer';
        
        const img = document.createElement('img');
        img.src = imgSrc;
        img.style.maxWidth = '90%';
        img.style.maxHeight = '90%';
        img.style.borderRadius = '16px';
        img.style.boxShadow = '0 0 30px rgba(0,0,0,0.5)';
        
        modal.appendChild(img);
        document.body.appendChild(modal);
        
        modal.addEventListener('click', function() {
            modal.remove();
        });
    });
});

// ========== SMOOTH SCROLL ==========
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// ========== EFECTO TIPO MÁQUINA DE ESCRIBIR (opcional) ==========
const typedText = document.querySelector('.hero-lema');
if (typedText) {
    const originalText = typedText.innerText;
    typedText.innerText = '';
    let i = 0;
    const typeWriter = () => {
        if (i < originalText.length) {
            typedText.innerText += originalText.charAt(i);
            i++;
            setTimeout(typeWriter, 50);
        }
    };
    // Descomentar si quieres efecto:
    // typeWriter();
}

// ========== PRELOADER (opcional) ==========
window.addEventListener('load', () => {
    document.body.style.visibility = 'visible';
});

// ========== FORMULARIO DE CONTACTO (si existe) ==========
const contactForm = document.getElementById('contactForm');
if (contactForm) {
    contactForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(contactForm);
        const response = await fetch(contactForm.action, {
            method: 'POST',
            body: formData
        });
        if (response.ok) {
            alert('¡Mensaje enviado con éxito!');
            contactForm.reset();
        } else {
            alert('Error al enviar el mensaje. Intente nuevamente.');
        }
    });
}

// ========== TOOLTIPS INICIALIZAR (Bootstrap) ==========
var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});

console.log('I.E. Mixto La Molina - Web cargada exitosamente');