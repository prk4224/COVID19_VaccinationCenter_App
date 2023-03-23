package com.jaehong.data.util

import com.jaehong.domain.model.result.ApiResult
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> {
    val response: Response<T>

    try {
        response = call()
    } catch (e: IOException) {
        return ApiResult.Error(IOException(e.message ?: "Internet error runs"))
    }

    val body = response.body()
    return if (body != null) {
        ApiResult.Success(body)
    } else {
        ApiResult.Error(IOException(""))
    }
}