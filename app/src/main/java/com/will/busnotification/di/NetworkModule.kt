package com.will.busnotification.di

import android.util.Log
import com.will.busnotification.data.network.GooglePlacesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
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
            .addInterceptor { chain ->
                val request = chain.request()
                try {
                    val requestBody = request.body
                    val buffer = okio.Buffer()
                    requestBody?.writeTo(buffer)
                    val requestBodyString = if (requestBody != null) buffer.readUtf8() else ""
                    Log.d("NetworkModule", "REQUEST ${request.method} ${request.url}\nHeaders: ${request.headers}\nBody: $requestBodyString")
                } catch (t: Throwable) {
                    Log.d("NetworkModule", "Error logging request body", t)
                }

                val response = chain.proceed(request)
                val responseBody = response.body
                val responseBodyString = try {
                    responseBody?.string() ?: ""
                } catch (t: Throwable) {
                    "<unreadable>"
                }

                Log.d("NetworkModule", "RESPONSE ${response.code} ${response.message}\nBody: $responseBodyString")

                // Recreate response body because .string() consumes it
                val newBody = responseBodyString.toResponseBody(responseBody?.contentType())
                return@addInterceptor response.newBuilder().body(newBody).build()
            }
            .build()

        return Retrofit.Builder()
            // Routes API uses routes.googleapis.com
            .baseUrl("https://routes.googleapis.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApiService::class.java)
    }
}
