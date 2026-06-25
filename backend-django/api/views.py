from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from django.shortcuts import get_object_or_404
from django.contrib.auth import authenticate
from rest_framework_simplejwt.tokens import RefreshToken
from django.db import models  # ✅ Para Avg
from .models import Alumno, Curso, Bimestre, Calificacion, Docente, Taller, SolicitudTaller, Horario
from .serializers import (
    AlumnoSerializer, CursoSerializer, BimestreSerializer,
    CalificacionSerializer, TallerSerializer, SolicitudTallerSerializer, HorarioSerializer
)


# ==================== LOGIN ====================
@api_view(['POST'])
def login(request):
    username = request.data.get('username')
    password = request.data.get('password')
    user = authenticate(username=username, password=password)
    
    if user:
        refresh = RefreshToken.for_user(user)
        
        # Determinar rol y user_id
        rol = "admin"
        user_id = user.id
        
        try:
            alumno = Alumno.objects.get(user=user)
            rol = "alumno"
            user_id = alumno.id
        except Alumno.DoesNotExist:
            try:
                docente = Docente.objects.get(user=user)
                rol = "docente"
                user_id = docente.id
            except Docente.DoesNotExist:
                pass
        
        return Response({
            'access': str(refresh.access_token),
            'refresh': str(refresh),
            'rol': rol,
            'user_id': user_id
        })
    
    return Response({'error': 'Credenciales inválidas'}, status=400)


# ==================== PERFIL ====================
class PerfilView(APIView):
    permission_classes = [IsAuthenticated]
    
    def get(self, request):
        user = request.user
        data = {
            'id': user.id,
            'email': user.email,
            'username': user.username
        }
        
        # Verificar si es Alumno
        if hasattr(user, 'alumno'):
            alumno = user.alumno
            data.update({
                'rol': 'ALUMNO',
                'nombre': alumno.nombres,
                'apellido': alumno.apellidos,
                'grado': alumno.grado.nombre if alumno.grado else None,
                'seccion': alumno.seccion,
                'promedio_general': calcular_promedio_general(alumno)
            })
        
        # Verificar si es Docente
        elif hasattr(user, 'docente'):
            docente = user.docente
            data.update({
                'rol': 'DOCENTE',
                'nombre': docente.nombres,
                'apellido': docente.apellidos,
                'departamento': docente.departamento if hasattr(docente, 'departamento') else None
            })
        
        return Response(data)


def calcular_promedio_general(alumno):
    calificaciones = Calificacion.objects.filter(alumno=alumno)
    if calificaciones.exists():
        total = sum(c.nota for c in calificaciones)
        return round(total / calificaciones.count(), 1)
    return None


# ==================== MIS CURSOS (Alumno) ====================
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def mis_cursos(request):
    user = request.user
    
    # Verificar si el usuario es alumno
    if not hasattr(user, 'alumno'):
        return Response({'error': 'Usuario no es alumno'}, status=400)
    
    alumno = user.alumno
    
    # Obtener calificaciones del alumno
    calificaciones = Calificacion.objects.filter(alumno=alumno)
    
    if not calificaciones.exists():
        return Response([])  # Retorna lista vacía si no hay calificaciones
    
    # Obtener cursos únicos
    cursos_ids = calificaciones.values_list('curso_id', flat=True).distinct()
    cursos = Curso.objects.filter(id__in=cursos_ids)
    
    # Construir respuesta
    data = []
    for curso in cursos:
        calificaciones_curso = calificaciones.filter(curso=curso)
        
        # Calcular promedio
        promedio = calificaciones_curso.aggregate(avg=models.Avg('nota'))['avg']
        
        # Obtener bimestres
        bimestres = []
        for cal in calificaciones_curso:
            bimestres.append({
                'bimestre': cal.bimestre.id if cal.bimestre else None,
                'nota': cal.nota,
                'estado': 'Aprobado' if cal.nota >= 13 else 'Desaprobado'
            })
        
        data.append({
            'id': curso.id,
            'nombre': curso.nombre,
            'docente': curso.docente.nombres if curso.docente else 'Sin docente',
            'promedio': promedio,
            'bimestres': bimestres
        })
    
    return Response(data)


# ==================== ALUMNOS ====================
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def alumnos_list(request):
    alumnos = Alumno.objects.all()
    serializer = AlumnoSerializer(alumnos, many=True)
    return Response(serializer.data)


# ==================== CURSOS ====================
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def cursos_list(request):
    cursos = Curso.objects.all()
    serializer = CursoSerializer(cursos, many=True)
    return Response(serializer.data)


# ==================== BIMESTRES ====================
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def bimestres_list(request):
    bimestres = Bimestre.objects.all()
    serializer = BimestreSerializer(bimestres, many=True)
    return Response(serializer.data)


# ==================== CALIFICACIONES ====================
@api_view(['GET', 'POST'])
@permission_classes([IsAuthenticated])
def calificaciones_list(request):
    if request.method == 'GET':
        calificaciones = Calificacion.objects.all()
        serializer = CalificacionSerializer(calificaciones, many=True)
        return Response(serializer.data)
    
    elif request.method == 'POST':
        serializer = CalificacionSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# ==================== CALIFICACIÓN INDIVIDUAL ====================
@api_view(['GET', 'PUT', 'DELETE'])
@permission_classes([IsAuthenticated])
def calificacion_detail(request, pk):
    calificacion = get_object_or_404(Calificacion, pk=pk)
    
    if request.method == 'GET':
        serializer = CalificacionSerializer(calificacion)
        return Response(serializer.data)
    
    elif request.method == 'PUT':
        serializer = CalificacionSerializer(calificacion, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    elif request.method == 'DELETE':
        calificacion.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


# ==================== TALLERES ====================
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def talleres_list(request):
    talleres = Taller.objects.all()
    serializer = TallerSerializer(talleres, many=True)
    return Response(serializer.data)


# ==================== SOLICITAR TALLER ====================
@api_view(['POST'])
@permission_classes([IsAuthenticated])
def solicitar_taller(request):
    serializer = SolicitudTallerSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# ==================== HORARIOS ====================
@api_view(['GET', 'POST'])
@permission_classes([IsAuthenticated])
def horarios_list(request):
    if request.method == 'GET':
        horarios = Horario.objects.all()
        serializer = HorarioSerializer(horarios, many=True)
        return Response(serializer.data)
    
    elif request.method == 'POST':
        serializer = HorarioSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

# ========== MIS CURSOS (Alumno) ==========
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def mis_cursos(request):
    user = request.user
    
    # Verificar si el usuario es alumno
    if not hasattr(user, 'alumno'):
        return Response({'error': 'Usuario no es alumno'}, status=400)
    
    alumno = user.alumno
    
    # Obtener calificaciones del alumno
    calificaciones = Calificacion.objects.filter(alumno=alumno)
    
    if not calificaciones.exists():
        return Response([])  # Retorna lista vacía si no hay calificaciones
    
    # Obtener cursos únicos
    cursos_ids = calificaciones.values_list('curso_id', flat=True).distinct()
    cursos = Curso.objects.filter(id__in=cursos_ids)
    
    # Construir respuesta
    data = []
    for curso in cursos:
        calificaciones_curso = calificaciones.filter(curso=curso)
        
        # Calcular promedio
        promedio = calificaciones_curso.aggregate(avg=models.Avg('nota'))['avg']
        
        # Obtener bimestres
        bimestres = []
        for cal in calificaciones_curso:
            bimestres.append({
                'bimestre': cal.bimestre.id if cal.bimestre else None,
                'nota': cal.nota,
                'estado': 'Aprobado' if cal.nota >= 13 else 'Desaprobado'
            })
        
        data.append({
            'id': curso.id,
            'nombre': curso.nombre,
            'docente': curso.docente.nombres if curso.docente else 'Sin docente',
            'promedio': promedio,
            'bimestres': bimestres
        })
    
    return Response(data)