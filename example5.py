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
        os.startfile(r"root/path/" + tainted)
