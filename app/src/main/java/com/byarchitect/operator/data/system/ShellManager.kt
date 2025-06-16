package com.byarchitect.operator.data.system

import com.byarchitect.operator.MainActivity
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.internal.MainShell

object ShellManager {

    fun initializeShell() {
        if (!Shell.getShell().isAlive) {
            Shell.setDefaultBuilder(
                Shell.Builder.create().setFlags(Shell.FLAG_MOUNT_MASTER).setTimeout(100)
            )
        }
    }


    fun closeShell() {
        Shell.getShell().close()
    }
}
