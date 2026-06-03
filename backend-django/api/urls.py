from django.urls import path
from . import views
from django.http import HttpResponse
from django.shortcuts import redirect


def home(request):
    return HttpResponse("Bienvenido a MixtoTrack API")

urlpatterns = [
    path('', home),
    path('alumnos/', views.alumnos_list),
    path('cursos/', views.cursos_list),
    path('bimestres/', views.bimestres_list),
    path('calificaciones/', views.calificaciones_list),
    path('calificaciones/<int:pk>/', views.calificacion_detail),
    path('talleres/', views.talleres_list),
    path('solicitar-taller/', views.solicitar_taller),
    path('horarios/', views.horarios_list),
]