package org.skyfaced.wopi.utils.result

/*
    MIT License

    Copyright (c) 2020 Android Broadcast

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val rawReturnType: Class<*> = getRawType(returnType)
        if (rawReturnType == Call::class.java) {
            if (returnType is ParameterizedType) {
                val callInnerType: Type = getParameterUpperBound(0, returnType)
                if (getRawType(callInnerType) == Result::class.java) {
                    // resultType is Call<Result<*>> | callInnerType is Result<*>
                    if (callInnerType is ParameterizedType) {
                        val resultInnerType = getParameterUpperBound(0, callInnerType)
                        return ResultCallAdapter<Any?>(resultInnerType)
                    }
                    return ResultCallAdapter<Nothing>(Nothing::class.java)
                }
            }
        }

        return null
    }
}

private class ResultCallAdapter<R>(private val type: Type) : CallAdapter<R, Call<Result<R>>> {
    override fun responseType() = type

    override fun adapt(call: Call<R>): Call<Result<R>> = ResultCall(call)
}