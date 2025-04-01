import textwrap

from django.db import connection


def sql_print(message: str) -> None:
    """
    Send some arbitrary text as a commented query to the database.

    This is useful when debugging the sources of SQL statements, especially in
    tests which use `BaseStylemeTestCase.assertQuerySnapshot`.

    The passed message must be a string and may contain newlines (which will all
    be suitably commented).
    """
    commentified_message = textwrap.indent(message, '-- ')
    with connection.cursor() as cursor:
        cursor.execute(f'SELECT 1; {commentified_message}')
