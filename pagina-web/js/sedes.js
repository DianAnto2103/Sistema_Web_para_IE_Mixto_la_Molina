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

// ========== INICIALIZAR MAPA (Leaflet) ==========
function initMap() {
    // Coordenadas de Jirón Cuzco Zona 2 405, La Molina
    const colegioLat = -12.0792;
    const colegioLng = -76.9467;
    
    const map = L.map('mapa-colegio').setView([colegioLat, colegioLng], 16);
    
    // Cargar tiles de OpenStreetMap
    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        subdomains: 'abcd',
        maxZoom: 19,
        minZoom: 12
    }).addTo(map);
    
    // Marcador personalizado
    const markerIcon = L.divIcon({
        html: '<i class="fas fa-school" style="font-size: 2rem; color: #2E7D32; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.3));"></i>',
        className: 'custom-marker',
        iconSize: [40, 40],
        popupAnchor: [0, -20]
    });
    
    // Agregar marcador
    const marker = L.marker([colegioLat, colegioLng], { icon: markerIcon }).addTo(map);
    
    // Popup con información
    marker.bindPopup(`
        <div style="text-align: center;">
            <strong style="color: #2E7D32;">I.E. 1278 Mixto La Molina</strong><br>
            Jr. Cuzco Zona 2 405, Etapa II<br>
            La Molina, Lima
        </div>
    `).openPopup();
    
    // Agregar un círculo alrededor
    L.circle([colegioLat, colegioLng], {
        color: '#2E7D32',
        fillColor: '#4CAF50',
        fillOpacity: 0.15,
        radius: 100
    }).addTo(map);
}

// Esperar a que el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    if (typeof L !== 'undefined') {
        initMap();
    }
});

// ========== GALERÍA LIGHTBOX ==========
const galleryItems = document.querySelectorAll('.galeria-sede-item');
galleryItems.forEach(item => {
    item.addEventListener('click', function() {
        const imgSrc = this.querySelector('img').src;
        const imgAlt = this.querySelector('img').alt;
        
        const modal = document.createElement('div');
        modal.style.position = 'fixed';
        modal.style.top = '0';
        modal.style.left = '0';
        modal.style.width = '100%';
        modal.style.height = '100%';
        modal.style.backgroundColor = 'rgba(0,0,0,0.95)';
        modal.style.display = 'flex';
        modal.style.alignItems = 'center';
        modal.style.justifyContent = 'center';
        modal.style.zIndex = '9999';
        modal.style.cursor = 'pointer';
        
        const container = document.createElement('div');
        container.style.textAlign = 'center';
        
        const img = document.createElement('img');
        img.src = imgSrc;
        img.alt = imgAlt;
        img.style.maxWidth = '90%';
        img.style.maxHeight = '80vh';
        img.style.borderRadius = '16px';
        img.style.boxShadow = '0 0 30px rgba(0,0,0,0.5)';
        
        const caption = document.createElement('p');
        caption.innerText = imgAlt;
        caption.style.color = 'white';
        caption.style.marginTop = '1rem';
        caption.style.fontSize = '1rem';
        
        const closeBtn = document.createElement('button');
        closeBtn.innerHTML = '✕';
        closeBtn.style.position = 'absolute';
        closeBtn.style.top = '20px';
        closeBtn.style.right = '30px';
        closeBtn.style.fontSize = '2rem';
        closeBtn.style.background = 'none';
        closeBtn.style.border = 'none';
        closeBtn.style.color = 'white';
        closeBtn.style.cursor = 'pointer';
        closeBtn.style.zIndex = '10000';
        
        container.appendChild(img);
        container.appendChild(caption);
        modal.appendChild(container);
        modal.appendChild(closeBtn);
        document.body.appendChild(modal);
        
        modal.addEventListener('click', function(e) {
            if (e.target === modal || e.target === closeBtn) {
                modal.remove();
            }
        });
        
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && document.body.contains(modal)) {
                modal.remove();
            }
        });
    });
});

console.log('Página Sedes cargada exitosamente');

