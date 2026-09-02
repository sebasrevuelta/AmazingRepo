Security validation requirement

When working in Agent mode and generating or modifying code, you must perform a Semgrep validation step before finalizing the task.

After code changes:

Identify the files you changed.
Invoke the available semgrep_scan tool to scan the changed files or the relevant project area. If you change any lockfile, you MUST run the semgrep_supply_chain tool.
Fix high-confidence security findings before completing the task.
If the Semgrep MCP tool is unavailable, explicitly say that the scan could not be run.
Do not claim the code is secure unless Semgrep was run successfully.
