import os
import subprocess
import sys
from http.server import BaseHTTPRequestHandler
from urllib.parse import urlparse

import commands


class GetHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        tainted = urlparse.urlparse(self.path).query

        rand = os.urandom()

        # ok: tainted-os-command-stdlib
        os.putenv("var", tainted)

        # ruleid: tainted-os-command-stdlib
        import re

        class GetHandler(BaseHTTPRequestHandler):

            def do_GET(self):
                tainted = urlparse.urlparse(self.path).query

                rand = os.urandom()

                # ok: tainted-os-command-stdlib
                os.putenv("var", tainted)

                # Validate and sanitize the input
                if not re.match(r'^[\w\-. ]+$', tainted):
                    self.send_error(400, "Invalid input")
                    return

                # Construct the file path safely
                file_path = os.path.join("root/path", tainted)

                # Use subprocess.run to safely open the file
                subprocess.run(["open", file_path])
