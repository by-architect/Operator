package com.byarchitect.operator.common.model


sealed class Resource<T>(val data: T? = null, val error: com.byarchitect.operator.common.model.Error? = null) {
    class Success<T>(data: T) : Resource<T>(data){}
    class Error<T>(error: com.byarchitect.operator.common.model.Error) : Resource<T>(null, error)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

/*
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
*/
