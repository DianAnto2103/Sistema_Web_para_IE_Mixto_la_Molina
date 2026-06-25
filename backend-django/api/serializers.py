from rest_framework import serializers
from .models import Alumno, Grado, Docente, Curso, Calificacion, Bimestre, Horario, Taller, SolicitudTaller

class GradoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Grado
        fields = ['id', 'nombre', 'nivel']
        read_only_fields = ['id']

class AlumnoSerializer(serializers.ModelSerializer):
    nombre = serializers.SerializerMethodField()
    apellido = serializers.SerializerMethodField()
    email = serializers.EmailField(source='correo')
    grado = serializers.SerializerMethodField()
    
    class Meta:
        model = Alumno
        fields = ['id', 'nombre', 'apellido', 'email', 'fecha_nacimiento', 'grado', 'seccion']
        read_only_fields = ['id']
    
    def get_nombre(self, obj):
        return obj.nombres
    
    def get_apellido(self, obj):
        return obj.apellidos
    
    def get_grado(self, obj):
        return obj.grado.nombre if obj.grado else None

class DocenteSerializer(serializers.ModelSerializer):
    nombre = serializers.SerializerMethodField()
    apellido = serializers.SerializerMethodField()
    email = serializers.EmailField(source='correo')
    departamento = serializers.CharField(default='Sin asignar')
    
    class Meta:
        model = Docente
        fields = ['id', 'nombre', 'apellido', 'email', 'departamento']
        read_only_fields = ['id']
    
    def get_nombre(self, obj):
        return obj.nombres
    
    def get_apellido(self, obj):
        return obj.apellidos

class CursoSerializer(serializers.ModelSerializer):
    docente_nombre = serializers.SerializerMethodField()
    
    class Meta:
        model = Curso
        fields = ['id', 'nombre', 'descripcion', 'docente', 'docente_nombre']
        read_only_fields = ['id']
    
    def get_docente_nombre(self, obj):
        if obj.docente:
            return f"{obj.docente.nombres} {obj.docente.apellidos}"
        return None

class CalificacionSerializer(serializers.ModelSerializer):
    bimestre = serializers.SerializerMethodField()
    alumno_nombre = serializers.SerializerMethodField()
    curso_nombre = serializers.SerializerMethodField()
    
    class Meta:
        model = Calificacion
        fields = ['id', 'alumno', 'alumno_nombre', 'curso', 'curso_nombre', 'nota', 'bimestre']
        read_only_fields = ['id']
    
    def get_bimestre(self, obj):
        return obj.bimestre.id if obj.bimestre else None
    
    def get_alumno_nombre(self, obj):
        return f"{obj.alumno.nombres} {obj.alumno.apellidos}"
    
    def get_curso_nombre(self, obj):
        return obj.curso.nombre

class BimestreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Bimestre
        fields = ['id', 'nombre', 'fecha_inicio', 'fecha_fin']
        read_only_fields = ['id']

class TallerSerializer(serializers.ModelSerializer):
    cupos_disponibles = serializers.SerializerMethodField()
    docente_nombre = serializers.SerializerMethodField()
    
    class Meta:
        model = Taller
        fields = ['id', 'nombre', 'descripcion', 'dia', 'hora_inicio', 'hora_fin', 
                  'cupos_totales', 'cupos_disponibles', 'docente', 'docente_nombre']
        read_only_fields = ['id']
    
    def get_cupos_disponibles(self, obj):
        return obj.cupos_disponibles
    
    def get_docente_nombre(self, obj):
        if obj.docente:
            return f"{obj.docente.nombres} {obj.docente.apellidos}"
        return None

class SolicitudTallerSerializer(serializers.ModelSerializer):
    taller_nombre = serializers.SerializerMethodField()
    docente = serializers.SerializerMethodField()
    horario = serializers.SerializerMethodField()
    
    class Meta:
        model = SolicitudTaller
        fields = ['id', 'taller', 'taller_nombre', 'docente', 'horario', 
                  'fecha_solicitud', 'estado']
        read_only_fields = ['id', 'fecha_solicitud']
    
    def get_taller_nombre(self, obj):
        return obj.taller.nombre if obj.taller else None
    
    def get_docente(self, obj):
        if obj.taller and obj.taller.docente:
            return f"{obj.taller.docente.nombres} {obj.taller.docente.apellidos}"
        return None
    
    def get_horario(self, obj):
        if obj.taller:
            return f"{obj.taller.dia} {obj.taller.hora_inicio}-{obj.taller.hora_fin}"
        return None

class HorarioSerializer(serializers.ModelSerializer):
    docente = serializers.SerializerMethodField()
    docente_id = serializers.IntegerField(source='docente.id')
    disponible = serializers.BooleanField(default=True)
    
    class Meta:
        model = Horario
        fields = ['id', 'docente', 'docente_id', 'dia', 'hora_inicio', 'hora_fin', 
                  'curso', 'disponible']
        read_only_fields = ['id']
    
    def get_docente(self, obj):
        if obj.docente:
            return f"{obj.docente.nombres} {obj.docente.apellidos}"
        return None