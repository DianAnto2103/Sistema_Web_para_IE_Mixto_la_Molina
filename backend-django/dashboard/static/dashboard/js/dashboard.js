// dashboard.js - MixtoTrack

document.addEventListener('DOMContentLoaded', function() {
    
// ========== GRÁFICO: PROMEDIO POR GRADO ==========
const promedioCanvas = document.getElementById('promedioGradoChart');
if (promedioCanvas) {
    const promediosData = promedioCanvas.getAttribute('data-promedios');
    if (promediosData && promediosData !== '[]') {
        try {
            const promedios = JSON.parse(promediosData);
            if (promedios && promedios.length > 0) {
                
                // Ordenar por grado (1ro, 2do, 3ro...)
                const ordenGrados = ['1ro', '2do', '3ro', '4to', '5to', '6to'];
                promedios.sort((a, b) => {
                    const numA = parseInt(a.grado) || 0;
                    const numB = parseInt(b.grado) || 0;
                    if (numA !== numB) return numA - numB;
                    // Si mismo número, ordenar por nivel (Primaria antes que Secundaria)
                    if (a.grado.includes('Primaria') && b.grado.includes('Secundaria')) return -1;
                    if (a.grado.includes('Secundaria') && b.grado.includes('Primaria')) return 1;
                    return 0;
                });
                
                new Chart(promedioCanvas, {
                    type: 'bar',
                    data: {
                        labels: promedios.map(item => item.grado || 'Sin grado'),
                        datasets: [{
                            label: 'Promedio Académico',
                            data: promedios.map(item => parseFloat(item.promedio) || 0),
                            backgroundColor: '#2E7D32',
                            borderRadius: 4,
                            barPercentage: 0.7,
                            categoryPercentage: 0.8
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true,
                        plugins: {
                            legend: { position: 'top', labels: { font: { size: 11 } } },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        return `Promedio: ${context.raw.toFixed(2)} puntos`;
                                    }
                                }
                            }
                        },
                        scales: {
                            y: { 
                                beginAtZero: true, 
                                max: 20,
                                title: { display: true, text: 'Promedio (0-20)', font: { size: 11 } },
                                grid: { color: '#E9ECEF' }
                            },
                            x: { 
                                title: { display: true, text: 'Grados', font: { size: 11 } },
                                ticks: { 
                                    maxRotation: 45, 
                                    minRotation: 45,
                                    font: { size: 10 }
                                }
                            }
                        }
                    }
                });
            }
        } catch(e) { console.error('Error en gráfico de promedios:', e); }
    }
}
    // ========== GRÁFICO: EVOLUCIÓN ==========
    const evolucionCanvas = document.getElementById('evolucionChart');
    if (evolucionCanvas) {
        const evolucionData = evolucionCanvas.getAttribute('data-evolucion');
        if (evolucionData && evolucionData !== '[]') {
            try {
                const data = JSON.parse(evolucionData);
                if (data && data.length > 0) {
                    new Chart(evolucionCanvas, {
                        type: 'line',
                        data: {
                            labels: data.map(item => item.bimestre || `Bimestre ${item.id}`),
                            datasets: [{
                                label: 'Promedio General',
                                data: data.map(item => parseFloat(item.promedio) || 0),
                                borderColor: '#2E7D32',
                                backgroundColor: 'rgba(46,125,50,0.05)',
                                borderWidth: 3,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: '#2E7D32',
                                pointBorderColor: '#FFFFFF',
                                pointRadius: 5,
                                pointHoverRadius: 7
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: true,
                            plugins: {
                                legend: { position: 'top' },
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            return `Promedio: ${context.raw.toFixed(2)} puntos`;
                                        }
                                    }
                                }
                            },
                            scales: {
                                y: { beginAtZero: true, max: 20, title: { display: true, text: 'Promedio (0-20)' } },
                                x: { title: { display: true, text: 'Periodo Académico' } }
                            }
                        }
                    });
                }
            } catch(e) { console.error('Error en gráfico de evolución:', e); }
        }
    }

    // ========== GRÁFICO: DISTRIBUCIÓN ==========
    const distribucionCanvas = document.getElementById('distribucionChart');
    if (distribucionCanvas) {
        const destacados = parseFloat(distribucionCanvas.getAttribute('data-destacados') || 0);
        const aprobados = parseFloat(distribucionCanvas.getAttribute('data-aprobados') || 0);
        const riesgo = parseFloat(distribucionCanvas.getAttribute('data-riesgo') || 0);
        
        if (destacados > 0 || aprobados > 0 || riesgo > 0) {
            new Chart(distribucionCanvas, {
                type: 'pie',
                data: {
                    labels: ['Destacados (≥16)', 'Aprobados (11-15)', 'En Riesgo (<11)'],
                    datasets: [{
                        data: [destacados, aprobados, riesgo],
                        backgroundColor: ['#2E7D32', '#FFC107', '#DC3545'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: { position: 'bottom', labels: { font: { size: 11 } } },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return `${context.label}: ${context.raw.toFixed(1)}%`;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    // ========== CALENDARIO DINÁMICO (definitivo) ==========
    function generarCalendario(year, month) {
        const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 
                    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
        const monthYearSpan = document.getElementById('calendarMonthYear');
        if (monthYearSpan) monthYearSpan.textContent = `${meses[month]} ${year}`;
        
        const primerDia = new Date(year, month, 1);
        let diaInicio = primerDia.getDay();
        // Ajuste para semana empezando en lunes (0 = domingo)
        let startOffset = diaInicio === 0 ? 6 : diaInicio - 1;
        const diasEnMes = new Date(year, month + 1, 0).getDate();
        
        const calendarDates = document.getElementById('calendarDates');
        if (!calendarDates) return;
        calendarDates.innerHTML = '';
        
        for (let i = 0; i < startOffset; i++) {
            const emptyDiv = document.createElement('div');
            emptyDiv.className = 'cal-date empty';
            calendarDates.appendChild(emptyDiv);
        }
        
        for (let i = 1; i <= diasEnMes; i++) {
            const dateDiv = document.createElement('div');
            dateDiv.className = 'cal-date';
            dateDiv.textContent = i;
    
            if (i === 4 && month === 5 && year === 2026) {
                dateDiv.classList.add('active');
            }
            calendarDates.appendChild(dateDiv);
        }
    }

    let fechaActual = new Date(2026, 5, 1);
    generarCalendario(2026, 5);

    document.getElementById('prevMonthBtn')?.addEventListener('click', () => {
        fechaActual.setMonth(fechaActual.getMonth() - 1);
        generarCalendario(fechaActual.getFullYear(), fechaActual.getMonth());
    });

    document.getElementById('nextMonthBtn')?.addEventListener('click', () => {
        fechaActual.setMonth(fechaActual.getMonth() + 1);
        generarCalendario(fechaActual.getFullYear(), fechaActual.getMonth());
    });
    
    // ========== FILTROS Y BÚSQUEDA (mensajes placeholder) ==========
    const filterBtn = document.querySelector('.filter-btn');
    if (filterBtn) {
        filterBtn.addEventListener('click', () => {
            alert('Funcionalidad de filtros en desarrollo. Próximamente podrás filtrar por grado, bimestre y más.');
        });
    }
    
    const searchInput = document.querySelector('.search-bar input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                alert(`Búsqueda de "${this.value}" - Funcionalidad en desarrollo.`);
            }
        });
    }
});