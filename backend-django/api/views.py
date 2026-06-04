from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from django.shortcuts import get_object_or_404
from django.contrib.auth import authenticate
from rest_framework_simplejwt.tokens import RefreshToken
from .models import Alumno, Curso, Bimestre, Calificacion, Docente, Taller, SolicitudTaller, Horario
from .serializers import (
    AlumnoSerializer, CursoSerializer, BimestreSerializer,
    CalificacionSerializer, TallerSerializer, SolicitudTallerSerializer, HorarioSerializer
)


# LOGIN (devuelve token JWT y rol)
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

#ALUMNOS (protegido)
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def alumnos_list(request):
    alumnos = Alumno.objects.all()
    serializer = AlumnoSerializer(alumnos, many=True)
    return Response(serializer.data)

#CURSOS (protegido)
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def cursos_list(request):
    cursos = Curso.objects.all()
    serializer = CursoSerializer(cursos, many=True)
    return Response(serializer.data)

#BIMESTRES (protegido)
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def bimestres_list(request):
    bimestres = Bimestre.objects.all()
    serializer = BimestreSerializer(bimestres, many=True)
    return Response(serializer.data)

#CALIFICACIONES (protegido)
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

#CALIFICACIÓN INDIVIDUAL (para editar/eliminar)
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

#TALLERES (protegido)
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def talleres_list(request):
    talleres = Taller.objects.all()
    serializer = TallerSerializer(talleres, many=True)
    return Response(serializer.data)

#SOLICITAR TALLER (protegido)
@api_view(['POST'])
@permission_classes([IsAuthenticated])
def solicitar_taller(request):
    serializer = SolicitudTallerSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#HORARIOS (protegido)
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