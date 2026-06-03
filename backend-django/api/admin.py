from django.contrib import admin
from .models import Alumno, Grado, Docente, Curso, Calificacion, Bimestre, Horario, Telefono, Taller, SolicitudTaller

class AlumnoAdmin(admin.ModelAdmin):
    list_display = ('apellidos', 'nombres', 'grado', 'seccion')
    search_fields = ('apellidos', 'nombres', 'correo')
    list_filter = ('grado', 'seccion')
admin.site.register(Alumno, AlumnoAdmin)

class GradoAdmin(admin.ModelAdmin):
    list_display = ('nombre', 'nivel')
    search_fields = ('nombre', 'nivel')
admin.site.register(Grado, GradoAdmin)

class TelefonoInline(admin.TabularInline):
    model = Telefono
    extra = 1

class DocenteAdmin(admin.ModelAdmin):
    list_display = ('apellidos', 'nombres', 'correo')
    search_fields = ('apellidos', 'nombres', 'correo')
    inlines = [TelefonoInline]  
admin.site.register(Docente, DocenteAdmin)

class CursoAdmin(admin.ModelAdmin):
    list_display = ('nombre', 'docente')
    search_fields = ('nombre',)
    list_filter = ('docente',)
admin.site.register(Curso, CursoAdmin)

class CalificacionAdmin(admin.ModelAdmin):
    list_display = ('alumno', 'curso', 'nota', 'bimestre')
    search_fields = ('alumno__apellidos', 'alumno__nombres', 'curso__nombre')
    list_filter = ('bimestre',)
admin.site.register(Calificacion, CalificacionAdmin)

class BimestreAdmin(admin.ModelAdmin):
    list_display = ('nombre', 'fecha_inicio', 'fecha_fin')
    search_fields = ('nombre',)
admin.site.register(Bimestre, BimestreAdmin)


class TelefonoAdmin(admin.ModelAdmin):
    list_display = ('numero', 'docente')
    search_fields = ('numero', 'docente__apellidos', 'docente__nombres')
    list_filter = ('docente',)
admin.site.register(Telefono, TelefonoAdmin)


class TallerAdmin(admin.ModelAdmin):
    list_display = ('nombre', 'descripcion', 'docente')
    search_fields = ('nombre', 'descripcion', 'docente__apellidos', 'docente__nombres')
    list_filter = ('docente',)

admin.site.register(Taller, TallerAdmin)

class SolicitudTallerAdmin(admin.ModelAdmin):
    list_display = ('alumno', 'taller', 'fecha_solicitud', 'estado')
    search_fields = ('alumno__apellidos', 'alumno__nombres', 'taller__nombre')
    list_filter = ('estado',)
admin.site.register(SolicitudTaller, SolicitudTallerAdmin)


class HorarioAdmin(admin.ModelAdmin):
    list_display = ('curso', 'dia', 'hora_inicio', 'hora_fin')
    search_fields = ('curso__nombre',)
    list_filter = ('dia',)
admin.site.register(Horario, HorarioAdmin)
