package com.kwangeonkim.thoth.di

import com.google.gson.GsonBuilder
import com.kwangeonkim.thoth.BuildConfig
import com.kwangeonkim.thoth.data.remote.naver.NaverAuthInterceptor
import com.kwangeonkim.thoth.data.remote.naver.NaverBookService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NaverBookSearchModule {

    @Provides
    @Singleton
    @Named("naverClientId")
    fun provideNaverClientId() = "12pjxsKc7fmBzfNSenDa"

    @Provides
    @Singleton
    @Named("naverClientSecret")
    fun provideNaverClientSecret() = BuildConfig.NAVER_CLIENT_SECRET


    @Provides
    @Singleton
    @Named("naverBaseUrl")
    fun provideNaverBaseUrl() = "https://openapi.naver.com"

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    @Provides
    @Singleton
    fun provideNaverAuthInterceptor(
        @Named("naverClientId") naverClientId: String,
        @Named("naverClientSecret") naverClientSecret: String
    ): NaverAuthInterceptor {
        return NaverAuthInterceptor(naverClientId, naverClientSecret)
    }

    @Provides
    @Singleton
    fun provideNaverOkHttpClient(naverAuthInterceptor: NaverAuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(naverAuthInterceptor).build()
    }

    @Provides
    @Singleton
    @Named("naverRetrofit")
    fun provideNaverAPIHandler(
        @Named("naverBaseUrl") baseUrl: String,
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(gsonConverterFactory)
            .client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideNaverBookService(@Named("naverRetrofit") retrofit: Retrofit): NaverBookService {
        return retrofit.create(NaverBookService::class.java);
    }
}