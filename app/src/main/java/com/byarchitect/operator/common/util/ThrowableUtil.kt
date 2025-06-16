package com.byarchitect.operator.common.util


open class NotSudoException(message: String) : Exception(message)

open class SystemNotRootedException(message: String) : NotSudoException(message)

open class SuperPermissionNotAllowedException(message: String) : NotSudoException(message)

