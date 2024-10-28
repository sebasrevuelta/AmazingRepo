from flask import Flask

app = Flask(__name__)


app = flask.Flask(__name__)


# ruleid: debug-flask-passthrough-errors
app.run(passthrough_errors=True)

# ok: debug-flask-passthrough-errors
app.run()
