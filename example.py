import os
import subprocess
import sys

import commands


def lambda_handler(event, context):

    tainted = event["exploit_code"]

    rand = os.urandom()

    # ok: tainted-os-command-stdlib-aws-lambda
    os.putenv("var", tainted)

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.startfile(r"root/path/" + tainted)

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.startfile(tainted, "print")

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.startfile(tainted, "compile")

    # ok: tainted-os-command-stdlib-aws-lambda
    os.startfile("filename", "compile", tainted)  # <- args can be tainted as well

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.system(tainted)

    if rand == 1:
        # ok: tainted-os-command-stdlib-aws-lambda
        os.execl(
            sys.executable,
            sys.executable,
            "-m",
            "promptflow._cli._pf.entry",
            *sys.argv[1:]
        )

    if rand == 1:
        # ruleid: tainted-os-command-stdlib-aws-lambda
        os.execl(tainted, tainted, "-m", "promptflow._cli._pf.entry", *sys.argv[1:])

    if rand == 1:
        # ok: tainted-os-command-stdlib-aws-lambda
        os.execl(
            sys.executable, sys.executable, "-m", "promptflow._cli._pf.entry", tainted
        )

    if rand == 1:
        # ruleid: tainted-os-command-stdlib-aws-lambda
        os.execv(tainted, ["arg1", "arg2"])

    if rand == 1:
        # ruleid: tainted-os-command-stdlib-aws-lambda
        os.execve(tainted, ["arg1", "arg2"], sys.env)

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.posix_spawn(tainted, ["arg1", "arg2"], sys.env, setsid=False)
    # ok: tainted-os-command-stdlib-aws-lambda
    os.posix_spawn("cmd", [tainted, "arg2"], sys.env, setsid=False)
    # ok: tainted-os-command-stdlib-aws-lambda
    os.posix_spawn("cmd", ["arg1", "arg2"], tainted, setsid=False)

    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.spawnle(os.P_NOWAITO, tainted, *sys.args, sys.env)
    # ok: tainted-os-command-stdlib-aws-lambda
    os.spawnle(os.P_NOWAITO, "myfile.py", *sys.args, tainted)
    # ok: tainted-os-command-stdlib-aws-lambda
    os.spawnle(os.P_NOWAITO, "myfile.py", [tainted, "arg2"], sys.env)
    # ruleid: tainted-os-command-stdlib-aws-lambda
    os.spawnl(os.P_NOWAIT, tainted)
    # ok: tainted-os-command-stdlib-aws-lambda
    os.spawnl(os.P_NOWAIT, "setup.exe")

    # ok: tainted-os-command-stdlib-aws-lambda
    subprocess.run(tainted, shell=True)
    # ok: tainted-os-command-stdlib-aws-lambda
    subprocess.run(["cmd", tainted], shell=False)
    # ruleid: tainted-os-command-stdlib-aws-lambda
    subprocess.run([tainted, "arg1"], shell=False)
    # ruleid: tainted-os-command-stdlib-aws-lambda
    subprocess.run("mycommand", shell=False, env=tainted)

    # ruleid: tainted-os-command-stdlib-aws-lambda
    commands.getoutput(tainted)
    # ruleid: tainted-os-command-stdlib-aws-lambda
    commands.getstatusoutput(tainted)
