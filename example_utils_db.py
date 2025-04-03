import textwrap

from django.db import connection

DJANGO_CONNECTION=False
WRAP_TEXT=False

def sql_print(message: str) -> None:
    """
    Send some arbitrary text as a commented query to the database.
    This is useful when debugging the sources of SQL statements, especially in
    tests which use `BaseStylemeTestCase.assertQuerySnapshot`.
    The passed message must be a string and may contain newlines (which will all
    be suitably commented).
    """
    
    if WRAP_TEXT == True:
        commentified_message = textwrap.indent(message, '-- ')
    else:
        commentified_message = '-- ' + message

    print(commentified_message);
    if DJANGO_CONNECTION == True:
        with connection.cursor() as cursor:
            cursor.execute(f'SELECT 1; {commentified_message}')

def main():
    sql_print("This is a comment message.")
    #sql_print("HELLO -- \n DROP TABLE users; --")


if __name__ == "__main__":
    main()

