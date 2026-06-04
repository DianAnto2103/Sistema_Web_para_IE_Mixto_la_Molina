// ========== INICIALIZAR AOS ==========
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

// ========== CONTADORES ANIMADOS (para logros) ==========
const counterElements = document.querySelectorAll('.logro-number');
const speed = 200;

const animateNumber = (element) => {
    const text = element.innerText;
    const target = parseInt(text.replace(/[^0-9]/g, ''));
    const hasPlus = text.includes('+');
    const hasPercent = text.includes('%');
    
    if (isNaN(target)) return;
    
    let count = 0;
    const increment = target / speed;
    
    const updateCount = () => {
        if (count < target) {
            count += increment;
            let display = Math.floor(count);
            if (hasPercent) display += '%';
            if (hasPlus && !hasPercent) display += '+';
            element.innerText = display;
            setTimeout(updateCount, 20);
        } else {
            let final = target;
            if (hasPercent) final += '%';
            if (hasPlus && !hasPercent) final += '+';
            element.innerText = final;
        }
    };
    
    updateCount();
};

// Observer para contadores
const observerOptions = { threshold: 0.5 };
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            animateNumber(entry.target);
            observer.unobserve(entry.target);
        }
    });
}, observerOptions);

counterElements.forEach(counter => {
    observer.observe(counter);
});

// ========== ANIMACIÓN DE ENTRADA PARA TARJETAS ==========
const cards = document.querySelectorAll('.valor-card, .logro-card, .mv-card');
cards.forEach((card, index) => {
    card.style.animationDelay = `${index * 0.1}s`;
});

console.log('🚀 Página Nosotros cargada exitosamente');