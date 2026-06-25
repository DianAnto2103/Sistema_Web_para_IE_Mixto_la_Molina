import json
import random

ALUMNOS = [
    (1, "Flores Torres", "Valentina", "1ro", "Primaria", "A"),
    (2, "Ramírez Silva", "Mateo", "1ro", "Primaria", "C"),
    (3, "Reyes Vargas", "Camila", "1ro", "Primaria", "A"),
    (4, "Ortiz Morales", "Javier", "1ro", "Primaria", "C"),
    (5, "Cruz Herrera", "Lucía", "1ro", "Primaria", "B"),
    (6, "Mendoza Chávez", "Andrés", "1ro", "Primaria", "A"),
    (7, "Rojas Jiménez", "Isabella", "2do", "Primaria", "A"),
    (8, "Díaz Ramírez", "Sebastián", "2do", "Primaria", "B"),
    (9, "Torres Flores", "Daniela", "2do", "Primaria", "A"),
    (10, "Rivera Castro", "Nicolás", "2do", "Primaria", "C"),
    (11, "González López", "Fernanda", "2do", "Primaria", "B"),
    (12, "Sánchez Rojas", "Alejandro", "2do", "Primaria", "A"),
    (13, "Ramírez Torres", "Antonella", "2do", "Primaria", "C"),
    (14, "Silva Mendoza", "Adrián", "2do", "Primaria", "B"),
    (15, "Vargas Cruz", "Gabriela", "2do", "Primaria", "A"),
    (16, "Flores Rojas", "Samuel", "2do", "Primaria", "C"),
    (17, "Pérez García", "Victoria", "3ro", "Primaria", "A"),
    (18, "Gómez Rojas", "Benjamín", "3ro", "Primaria", "B"),
    (19, "Martínez López", "Renata", "3ro", "Primaria", "A"),
    (20, "Rodríguez Castro", "Emiliano", "3ro", "Primaria", "C"),
    (21, "Flores Torres", "Julieta", "3ro", "Primaria", "B"),
    (22, "Ramírez Silva", "Matías", "3ro", "Primaria", "A"),
    (23, "Reyes Vargas", "Luciana", "3ro", "Primaria", "C"),
    (24, "Ortiz Morales", "Thiago", "3ro", "Primaria", "B"),
    (25, "Cruz Herrera", "Jimena", "3ro", "Primaria", "A"),
    (26, "Mendoza Chávez", "Dylan", "3ro", "Primaria", "C"),
    (27, "Pérez García", "Mía", "4to", "Primaria", "A"),
    (28, "Gómez Rojas", "Ian", "4to", "Primaria", "B"),
    (29, "Martínez López", "Catalina", "4to", "Primaria", "A"),
    (30, "Rodríguez Castro", "Gael", "4to", "Primaria", "C"),
    (31, "Flores Torres", "Aitana", "4to", "Primaria", "B"),
    (32, "Ramírez Silva", "Luca", "4to", "Primaria", "A"),
    (33, "Reyes Vargas", "Alma", "4to", "Primaria", "C"),
    (34, "Ortiz Morales", "Simón", "4to", "Primaria", "B"),
    (35, "Cruz Herrera", "Valentina", "4to", "Primaria", "A"),
    (36, "Mendoza Chávez", "Maximiliano", "4to", "Primaria", "C"),
    (37, "Pérez García", "Lara", "5to", "Primaria", "A"),
    (38, "Gómez Rojas", "Lorenzo", "5to", "Primaria", "B"),
    (39, "Martínez López", "Martina", "5to", "Primaria", "A"),
    (40, "Rodríguez Castro", "Joaquín", "5to", "Primaria", "C"),
    (41, "Flores Torres", "Emma", "5to", "Primaria", "B"),
    (42, "Ramírez Silva", "Leonardo", "5to", "Primaria", "A"),
    (43, "Reyes Vargas", "Olivia", "5to", "Primaria", "C"),
    (44, "Ortiz Morales", "Tomás", "5to", "Primaria", "B"),
    (45, "Cruz Herrera", "Julia", "5to", "Primaria", "A"),
    (46, "Mendoza Chávez", "Felipe", "5to", "Primaria", "C"),
    (47, "Paredes Soto", "Regina", "6to", "Primaria", "A"),
    (48, "Luna Torres", "Emilio", "6to", "Primaria", "B"),
    (49, "Ríos Castro", "Paloma", "6to", "Primaria", "A"),
    (50, "Fuentes Rojas", "Ignacio", "6to", "Primaria", "C"),
    (51, "Salas Vargas", "Aurora", "6to", "Primaria", "B"),
    (52, "Molina Soto", "Bruno", "6to", "Primaria", "A"),
    (53, "Ortega Silva", "Clara", "1ro", "Secundaria", "A"),
    (54, "Navarro Cruz", "Damián", "1ro", "Secundaria", "B"),
    (55, "Méndez Rojas", "Elisa", "1ro", "Secundaria", "A"),
    (56, "Aguirre López", "Facundo", "1ro", "Secundaria", "C"),
    (57, "Ponce García", "Noelia", "1ro", "Secundaria", "B"),
    (58, "Vera Torres", "Rafael", "1ro", "Secundaria", "A"),
    (59, "Iglesias Ríos", "Teresa", "2do", "Secundaria", "A"),
    (60, "Acosta Silva", "Vicente", "2do", "Secundaria", "B"),
    (61, "Luna Flores", "Natalia", "2do", "Secundaria", "A"),
    (62, "Sosa Martínez", "Hugo", "2do", "Secundaria", "C"),
    (63, "Bianchi Rojas", "Florencia", "2do", "Secundaria", "B"),
    (64, "Ferreyra López", "Santino", "2do", "Secundaria", "A"),
    (65, "Godoy Pérez", "Micaela", "3ro", "Secundaria", "A"),
    (66, "Toledo García", "Lautaro", "3ro", "Secundaria", "B"),
    (67, "Núñez Ríos", "Candela", "3ro", "Secundaria", "A"),
    (68, "Peralta Silva", "Thiago", "3ro", "Secundaria", "C"),
    (69, "Cabrera Torres", "Luz", "3ro", "Secundaria", "B"),
    (70, "Roldán Flores", "Brian", "3ro", "Secundaria", "A"),
    (71, "Giménez Rojas", "Agustina", "4to", "Secundaria", "A"),
    (72, "Vidal López", "Julián", "4to", "Secundaria", "B"),
    (73, "Domínguez Martínez", "Pilar", "4to", "Secundaria", "A"),
    (74, "Suárez Ríos", "Gonzalo", "4to", "Secundaria", "C"),
    (75, "Escobar García", "Rocío", "4to", "Secundaria", "B"),
    (76, "Bustos Pérez", "Máximo", "4to", "Secundaria", "A"),
    (77, "Moyano Rojas", "Celeste", "5to", "Secundaria", "A"),
    (78, "Paz Silva", "Alan", "5to", "Secundaria", "B"),
    (79, "Ledesma Torres", "Abril", "5to", "Secundaria", "A"),
    (80, "Ojeda Flores", "Kevin", "5to", "Secundaria", "C"),
    (81, "Vázquez Ríos", "Aldana", "5to", "Secundaria", "B"),
    (82, "Quiroga López", "Facundo", "5to", "Secundaria", "A"),
    (83, "Arias Martínez", "Guadalupe", "6to", "Secundaria", "A"),
    (84, "Cáceres Pérez", "Franco", "6to", "Secundaria", "B"),
    (85, "Tapia García", "Mora", "6to", "Secundaria", "A"),
    (86, "Soria Rojas", "Iván", "6to", "Secundaria", "C"),
    (87, "Carranza Silva", "Zoe", "6to", "Secundaria", "B"),
    (88, "Maldonado Torres", "Santino", "6to", "Secundaria", "A"),
    (89, "Figueroa Ríos", "Renata", "6to", "Secundaria", "A"),
    (90, "Correa López", "Thiago", "6to", "Secundaria", "B"),
    (91, "Lucero Flores", "Morena", "6to", "Secundaria", "A"),
    (92, "Ibarra Martínez", "Bautista", "6to", "Secundaria", "C"),
    (93, "Toledo Rojas", "Lola", "6to", "Secundaria", "B"),
    (94, "Vega García", "Joaquín", "6to", "Secundaria", "A"),
    (95, "Cuenca Pérez", "Malena", "6to", "Secundaria", "A"),
    (96, "Palacios Silva", "Federico", "6to", "Secundaria", "B")
]

CURSOS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
BIMESTRES = [1, 2, 3, 4]

# Definir rangos de notas según el rendimiento del alumno
# Alumnos destacados: IDs 1-20, 53-72, 83-96 (aproximadamente 50 alumnos destacados)
# Alumnos regulares: IDs 21-40, 73-82
# Alumnos rezagados: IDs 41-52

def generar_nota_por_alumno(alumno_id):
    # Alumnos destacados (mejores promedios)
    if alumno_id <= 20 or (53 <= alumno_id <= 72) or (83 <= alumno_id <= 96):
        return round(random.uniform(14, 19), 1)
    # Alumnos regulares
    elif 21 <= alumno_id <= 40 or (73 <= alumno_id <= 82):
        return round(random.uniform(11, 15), 1)
    # Alumnos rezagados
    else:
        return round(random.uniform(5, 10), 1)

calificaciones = []
pk = 1

for alumno_id, _, _, _, _, _ in ALUMNOS:
    for curso_id in CURSOS:
        for bimestre_id in BIMESTRES:
            nota = generar_nota_por_alumno(alumno_id)
            calificaciones.append({
                "model": "api.calificacion",
                "pk": pk,
                "fields": {
                    "alumno": alumno_id,
                    "curso": curso_id,
                    "nota": nota,
                    "bimestre": bimestre_id
                }
            })
            pk += 1

with open('api/fixtures/calificaciones_realistas.json', 'w', encoding='utf-8') as f:
    json.dump(calificaciones, f, indent=2, ensure_ascii=False)

print(f"Generadas {len(calificaciones)} calificaciones")