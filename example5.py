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

        # ruleid: tainted-os-command-stdlib
        os.startfile(tainted, "print")

        # ruleid: tainted-os-command-stdlib
        os.startfile(tainted, "compile")

        # ok: tainted-os-command-stdlib
        os.startfile("filename", "compile", tainted)

        # ruleid: tainted-os-command-stdlib
        os.system(tainted)

        if rand == 1:
            # ok: tainted-os-command-stdlib
            os.execl(
                sys.executable,
                sys.executable,
                "-m",
                "promptflow._cli._pf.entry",
                *sys.argv[1:]
            )

        if rand == 1:
            # ruleid: tainted-os-command-stdlib
            os.execl(tainted, tainted, "-m", "promptflow._cli._pf.entry", *sys.argv[1:])

        if rand == 1:
            # ok: tainted-os-command-stdlib
            os.execl(
                sys.executable,
                sys.executable,
                "-m",
                "promptflow._cli._pf.entry",
                tainted,
            )

        if rand == 1:
            # ruleid: tainted-os-command-stdlib
            os.execv(tainted, ["arg1", "arg2"])

        if rand == 1:
            # ruleid: tainted-os-command-stdlib
            os.execve(tainted, ["arg1", "arg2"], sys.env)

        # ruleid: tainted-os-command-stdlib
        os.posix_spawn(tainted, ["arg1", "arg2"], sys.env, setsid=False)
        # ok: tainted-os-command-stdlib
        os.posix_spawn("cmd", [tainted, "arg2"], sys.env, setsid=False)
        # ok: tainted-os-command-stdlib
        os.posix_spawn("cmd", ["arg1", "arg2"], tainted, setsid=False)

        # ruleid: tainted-os-command-stdlib
        os.spawnle(os.P_NOWAITO, tainted, *sys.args, sys.env)
        # ok: tainted-os-command-stdlib
        os.spawnle(os.P_NOWAITO, "myfile.py", *sys.args, tainted)
        # ok: tainted-os-command-stdlib
        os.spawnle(os.P_NOWAITO, "myfile.py", [tainted, "arg2"], sys.env)
        # ruleid: tainted-os-command-stdlib
        os.spawnl(os.P_NOWAIT, tainted)
        # ok: tainted-os-command-stdlib
        os.spawnl(os.P_NOWAIT, "setup.exe")

        # ok: tainted-os-command-stdlib
        subprocess.run(["cmd", tainted], shell=False)
        # ruleid: tainted-os-command-stdlib
        subprocess.run([tainted, "arg1"], shell=False)
        # ruleid: tainted-os-command-stdlib
        subprocess.run("mycommand", shell=False, env=tainted)

        # ruleid: tainted-os-command-stdlib
        commands.getoutput(tainted)
        # ruleid: tainted-os-command-stdlib
        commands.getstatusoutput(tainted)
