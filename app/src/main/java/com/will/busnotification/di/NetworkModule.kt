package com.will.busnotification.di

import com.will.busnotification.data.network.GooglePlacesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGooglePlacesApiService(): GooglePlacesApiService {

        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://routes.googleapis.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApiService::class.java)
    }
}
