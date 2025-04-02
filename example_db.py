from django.db import connection

DJANGO_ENABLE=False

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

