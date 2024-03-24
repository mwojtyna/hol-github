package com.mw.hol_github_frontend.api

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.mw.hol_github_frontend.proto.PersistentCookie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.io.OutputStream
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI

class RetrofitClient(context: Context) {
    val retrofit: Retrofit by lazy {
        val cookieManager = CookieManager(
            PersistentCookieStore(context), CookiePolicy.ACCEPT_ORIGINAL_SERVER
        )
        val client = OkHttpClient.Builder().cookieJar(JavaNetCookieJar(cookieManager)).build()

        Retrofit.Builder().baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }
}

class ApiClient(
    context: Context,
) {
    private val retrofitClient: RetrofitClient = RetrofitClient(context)

    val user: UserApi by lazy {
        retrofitClient.retrofit.create(UserApi::class.java)
    }
}

object PersistentCookieSerializer : Serializer<PersistentCookie?> {
    override val defaultValue: PersistentCookie? = null
    override suspend fun readFrom(input: InputStream): PersistentCookie {
        try {
            return PersistentCookie.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: PersistentCookie?, output: OutputStream) {
        t?.writeTo(output)
    }
}

class PersistentCookieStore(private val context: Context) : CookieStore {
    private val Context.cookieDataStore: DataStore<PersistentCookie?> by dataStore(
        fileName = "cookies.pb", serializer = PersistentCookieSerializer
    )

    override fun add(uri: URI?, cookie: HttpCookie?) {
        if (uri == null || cookie == null) {
            return
        }

        runBlocking {
            context.cookieDataStore.updateData {
                if (!cookie.hasExpired()) {
                    return@updateData PersistentCookie.newBuilder()
                        .apply {
                            name = cookie.name
                            value = cookie.value
                            domain = uri.host
                            path = cookie.path
                            httpOnly = cookie.isHttpOnly
                            secure = cookie.secure
                            maxAge = cookie.maxAge
                        }.build()
                } else {
                    return@updateData null
                }
            }
        }
    }

    override fun get(uri: URI?): MutableList<HttpCookie> {
        if (uri == null) {
            return mutableListOf()
        }

        return runBlocking {
            val persistentCookie = context.cookieDataStore.data.first()
            val httpCookie = try {
                persistentCookie?.toHttpCookie()
            } catch (e: Exception) {
                null
            }

            return@runBlocking if (httpCookie == null) mutableListOf() else mutableListOf(httpCookie)
        }
    }

    override fun getCookies(): MutableList<HttpCookie> {
        return runBlocking {
            val persistentCookie = context.cookieDataStore.data.first()
            return@runBlocking if (persistentCookie == null) mutableListOf() else mutableListOf(
                persistentCookie.toHttpCookie()
            )
        }
    }

    override fun getURIs(): MutableList<URI> {
        throw NotImplementedError()
    }

    override fun remove(uri: URI?, cookie: HttpCookie?): Boolean {
        runBlocking {
            context.cookieDataStore.updateData { null }
        }
        return true
    }

    override fun removeAll(): Boolean {
        runBlocking {
            context.cookieDataStore.updateData { null }
        }
        return true
    }

    private fun PersistentCookie.toHttpCookie(): HttpCookie {
        val cookie = HttpCookie(name, value)
        cookie.domain = domain
        cookie.path = path
        cookie.isHttpOnly = httpOnly
        cookie.secure = secure
        cookie.maxAge = maxAge
        return cookie
    }
}

