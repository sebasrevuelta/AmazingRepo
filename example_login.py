from django.http import HttpResponse
from django.db import connection

def unsafe_login(request):
    username = request.GET.get("username")
    password = request.GET.get("password")

    query = f"SELECT * FROM auth_user WHERE username = '{username}' AND password = '{password}'"
    
    with connection.cursor() as cursor:
        cursor.execute(query)  # 🚨 Vulnerable to SQL Injection
        user = cursor.fetchone()

    if user:
        return HttpResponse("Login successful!")
    return HttpResponse("Invalid credentials")

