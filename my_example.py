import textwrap
from django.db import connection

def sql_print(message: str) -> None:
    commentified_message = textwrap.indent(message, '-- ')
    print(commentified_message)
    with connection.cursor() as cursor:
        cursor.execute(f'SELECT 1; {commentified_message}')

def main():
    sql_print("This is a test message.")

if __name__ == "__main__":
    main()
