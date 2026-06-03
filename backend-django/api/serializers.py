from rest_framework import serializers
from .models import Alumno, Grado, Docente, Curso, Calificacion, Bimestre, Horario, Taller, SolicitudTaller

class AlumnoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Alumno
        fields = ['id', 'nombres', 'apellidos', 'correo', 'fecha_nacimiento', 'grado', 'seccion']
        read_only_fields = ['id']

class GradoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Grado
        fields = ['id', 'nombre', 'nivel']
        read_only_fields = ['id']

class DocenteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Docente
        fields = ['id', 'nombres', 'apellidos', 'correo']
        read_only_fields = ['id']

class CursoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Curso
        fields = ['id', 'nombre', 'descripcion', 'docente']
        read_only_fields = ['id']

class CalificacionSerializer(serializers.ModelSerializer):
    # Muestra el nombre del alumno y curso en lugar de solo IDs
    alumno_nombre = serializers.CharField(source='alumno.nombres', read_only=True)
    curso_nombre = serializers.CharField(source='curso.nombre', read_only=True)
    
    class Meta:
        model = Calificacion
        fields = ['id', 'alumno', 'alumno_nombre', 'curso', 'curso_nombre', 'nota', 'bimestre']
        read_only_fields = ['id']

class BimestreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Bimestre
        fields = ['id', 'nombre', 'fecha_inicio', 'fecha_fin']
        read_only_fields = ['id']

class TallerSerializer(serializers.ModelSerializer):
    # Campo calculado (no está en la BD)
    cupos_disponibles = serializers.IntegerField(read_only=True)
    
    class Meta:
        model = Taller
        fields = ['id', 'nombre', 'descripcion', 'dia', 'hora_inicio', 'hora_fin', 
                  'cupos_totales', 'cupos_disponibles', 'docente']
        read_only_fields = ['id']

class SolicitudTallerSerializer(serializers.ModelSerializer):
    alumno_nombre = serializers.CharField(source='alumno.nombres', read_only=True)
    taller_nombre = serializers.CharField(source='taller.nombre', read_only=True)
    
    class Meta:
        model = SolicitudTaller
        fields = ['id', 'alumno', 'alumno_nombre', 'taller', 'taller_nombre', 
                  'fecha_solicitud', 'estado']
        read_only_fields = ['id', 'fecha_solicitud']

class HorarioSerializer(serializers.ModelSerializer):
    class Meta:
        model = Horario
        fields = '__all__'