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

        # Define a whitelist pattern for allowed filenames
        allowed_pattern = re.compile(r'^[a-zA-Z0-9_\-\.]+$')

        # Validate the tainted input against the whitelist pattern
        if allowed_pattern.match(tainted):
            # Use subprocess.run to safely open the file
            subprocess.run(["open", os.path.join("root/path", tainted)], check=True)
        else:
            # Handle invalid input case
            self.send_error(400, "Invalid file name")
