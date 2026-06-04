from django.db import models
from django.contrib.auth.models import User

class Alumno(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=True, blank=True)
    nombres = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    correo = models.EmailField(unique=True)
    fecha_nacimiento = models.DateField(blank=True, null=True)
    grado = models.ForeignKey('Grado', on_delete=models.SET_NULL, null=True, blank=True) 
    seccion = models.CharField(max_length=10, blank=True)  

    def __str__(self):
        return f"{self.nombres} {self.apellidos}"
    

class Grado(models.Model):
    nombre = models.CharField(max_length=50)
    nivel = models.CharField(max_length=50)

    def __str__(self):
        return self.nombre
    
class Docente(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=True, blank=True)
    nombres = models.CharField(max_length=100)
    apellidos = models.CharField(max_length=100)
    correo = models.EmailField(unique=True)

    def __str__(self):
        return f"{self.nombres} {self.apellidos}"

class Curso(models.Model):
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True)
    docente = models.ForeignKey('Docente', on_delete=models.SET_NULL, null=True, blank=True)

    def __str__(self):
        return self.nombre
    
class Calificacion(models.Model):
    alumno = models.ForeignKey('Alumno', on_delete=models.CASCADE)
    curso = models.ForeignKey('Curso', on_delete=models.CASCADE)
    nota = models.DecimalField(max_digits=5, decimal_places=2)
    bimestre = models.ForeignKey('Bimestre', on_delete=models.SET_NULL, null=True, blank=True)

    def __str__(self):
        return f"{self.alumno} - {self.curso}: {self.nota}"
    
class Bimestre(models.Model):
    nombre = models.CharField(max_length=50)
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()

    def __str__(self):
        return self.nombre

class Telefono(models.Model):
    numero = models.CharField(max_length=20)
    docente = models.ForeignKey(
        Docente,
        on_delete=models.CASCADE,
        related_name='telefonos'
    )

class Taller(models.Model):
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True)
    dia = models.CharField(max_length=20, blank=True)
    hora_inicio = models.TimeField(blank=True, null=True)
    hora_fin = models.TimeField(blank=True, null=True)
    cupos_totales = models.IntegerField(blank=True, null=True)
    docente = models.ForeignKey('Docente', on_delete=models.SET_NULL, null=True, blank=True)

    def __str__(self):
        return self.nombre
    
    @property
    def cupos_disponibles(self):
        aprobados = self.solicitudtaller_set.filter(estado='aprobado').count()
        return self.cupos_totales - aprobados
    
class SolicitudTaller(models.Model):
    alumno = models.ForeignKey('Alumno', on_delete=models.CASCADE)
    taller = models.ForeignKey('Taller', on_delete=models.CASCADE)
    fecha_solicitud = models.DateTimeField(auto_now_add=True)
    estado = models.CharField(max_length=20, default='pendiente')

    def __str__(self):
        return f"{self.alumno} - {self.taller}"
    
class Horario(models.Model):
    docente = models.ForeignKey('Docente', on_delete=models.CASCADE)
    dia = models.CharField(max_length=20)  # Lunes, Martes, etc.
    hora_inicio = models.TimeField()
    hora_fin = models.TimeField()
    curso = models.ForeignKey('Curso', on_delete=models.CASCADE, null=True, blank=True)

    def __str__(self):
        return f"{self.docente} - {self.dia} {self.hora_inicio}-{self.hora_fin}"