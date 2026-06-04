from django.urls import path
from . import views

urlpatterns = [
    path('login/', views.login_view, name='login'),
    path('logout/', views.logout_view, name='logout'),
    path('', views.home, name='home'),
    path('estudiantes/', views.estudiantes_list, name='estudiantes_list'),
    path('estudiante/crear/', views.estudiante_create, name='estudiante_create'),
    path('rendimiento/', views.rendimiento_estudiante, name='rendimiento_estudiante'),
    path('ranking/', views.ranking, name='ranking'),  # ← COMENTA ESTA (no existe)
    path('evolucion/', views.evolucion, name='evolucion'),  # ← ESTÁ BIEN (existe)
    path('reportes/', views.reportes, name='reportes'),
    path('exportar/pdf/', views.exportar_pdf, name='exportar_pdf'),
    path('exportar/excel/', views.exportar_excel, name='exportar_excel'),
    path('exportar/txt/', views.exportar_txt, name='exportar_txt'),
    path('exportar/pdf/estudiante/', views.exportar_pdf_estudiante, name='exportar_pdf_estudiante'),
]