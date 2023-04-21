package com.kwangeonkim.thoth.data.remote.naver

import okhttp3.Interceptor
import okhttp3.Response


/**
 * Naver Authentication Header Interceptor
 *
 * This class is responsible for adding authentication headers to requests
 * before they are sent out.
 */
class NaverAuthInterceptor constructor(
    val naverClientId: String,
    val naverClientSecret: String
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("X-Naver-Client-Id", naverClientId)
            .addHeader("X-Naver-Client-Secret", naverClientSecret)
            .build()
        return chain.proceed(request)
    }
}