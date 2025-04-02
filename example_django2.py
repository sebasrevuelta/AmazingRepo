import base64
import mimetypes
import os

from django.core.urlresolvers import reverse
from django.http import HttpResponse
from django.shortcuts import redirect, render
from django.views.decorators.csrf import csrf_exempt
from django.db import connection

DJANGO_ENABLE=False

def xss_form(request):
    # ruleid: context-autoescape-off
    env = {'qs': request.GET.get('qs', 'hello'), 'autoescape': True}
    response = render(request, 'vulnerable/xss/form.html', env)
    response.set_cookie(key='monster', value='omnomnomnomnom!')
    return response

def sql_print(message: str) -> None:

    commentified_message = f"-- {message}"    
    print(commentified_message)
    if DJANGO_ENABLE == True:
        with connection.cursor() as cursor:
            cursor.execute(f'SELECT 1; {commentified_message}')

def main():
    sql_print("HELLO")
    #sql_print("HELLO -- \n DROP TABLE users; --")

if __name__ == "__main__":
    main()


