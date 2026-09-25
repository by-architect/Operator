// Runs inside a process owned by the ADB shell user, started by Shizuku.
package com.byarchitect.operator;

interface IUserService {
    // Shizuku calls this to stop the service. The transaction id is fixed by Shizuku
    // (IBinder.LAST_CALL_TRANSACTION) and must not be changed.
    void destroy() = 16777114;

    // Runs a shell command. Element 0 of the result is the exit code as a string,
    // the remaining elements are the lines of stdout.
    String[] execute(String command) = 1;
}
