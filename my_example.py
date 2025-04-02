import textwrap
from django.db import connection

def sql_print(message: str) -> None:
    commentified_message = textwrap.indent(message, '-- ')
    print(commentified_message)
    with connection.cursor() as cursor:
        # Use parameterized query to prevent SQL injection
        cursor.execute('SELECT 1; %s', [commentified_message])

def main():
    sql_print("This is a test message.")

if __name__ == "__main__":
    main()
