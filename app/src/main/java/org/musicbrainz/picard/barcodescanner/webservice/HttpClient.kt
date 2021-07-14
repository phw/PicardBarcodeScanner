package org.musicbrainz.picard.barcodescanner.webservice

import org.apache.http.*
import org.apache.http.conn.params.ConnManagerParams
import org.apache.http.conn.params.ConnPerRouteBean
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.entity.HttpEntityWrapper
import org.apache.http.impl.client.AbstractHttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.HttpConnectionParams
import org.apache.http.params.HttpParams
import org.apache.http.params.HttpProtocolParams
import org.apache.http.protocol.HttpContext
import java.io.IOException
import java.io.InputStream
import java.util.zip.GZIPInputStream

/**
 * Configures the static http client. Gzip code is based on BetterHttp in
 * droid-fu.
 */
object HttpClient {
    private const val TIMEOUT = 20000
    private const val MAX_CONNECTIONS = 4
    private var client: DefaultHttpClient? = null
    fun getClient(userAgent: String): AbstractHttpClient? {
        setUserAgent(userAgent)
        return client
    }

    private fun setupParams(): HttpParams {
        val params: HttpParams = BasicHttpParams()
        ConnManagerParams.setTimeout(params, TIMEOUT.toLong())
        ConnManagerParams.setMaxConnectionsPerRoute(params, ConnPerRouteBean(MAX_CONNECTIONS))
        ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS)
        HttpConnectionParams.setSoTimeout(params, TIMEOUT)
        HttpConnectionParams.setTcpNoDelay(params, true)
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1)
        return params
    }

    private fun setUserAgent(userAgent: String) {
        client!!.params.setParameter(HttpProtocolParams.USER_AGENT, userAgent)
    }

    private fun setupSchemeRegistry(): SchemeRegistry {
        val schemeRegistry = SchemeRegistry()
        schemeRegistry.register(Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
        return schemeRegistry
    }

    fun clearCredentials() {
        client!!.credentialsProvider.clear()
    }

    private fun enableGzipEncoding() {
        client!!.addRequestInterceptor(GzipHttpRequestInterceptor())
        client!!.addResponseInterceptor(GzipHttpResponseInterceptor())
    }

    private class GzipHttpRequestInterceptor : HttpRequestInterceptor {
        override fun process(request: HttpRequest, context: HttpContext) {
            if (!request.containsHeader("Accept-Encoding")) {
                request.addHeader("Accept-Encoding", "gzip")
            }
        }
    }

    private class GzipHttpResponseInterceptor : HttpResponseInterceptor {
        override fun process(response: HttpResponse, context: HttpContext) {
            val entity = response.entity
            val encoding = entity.contentEncoding
            encoding?.let { inflateGzip(response, it) }
        }

        private fun inflateGzip(response: HttpResponse, encoding: Header) {
            for (element in encoding.elements) {
                if (element.name.equals("gzip", ignoreCase = true)) {
                    response.entity = GzipInflatingEntity(response.entity)
                    break
                }
            }
        }
    }

    private class GzipInflatingEntity(wrapped: HttpEntity?) : HttpEntityWrapper(wrapped) {
        @Throws(IOException::class)
        override fun getContent(): InputStream {
            return GZIPInputStream(wrappedEntity.content)
        }

        override fun getContentLength(): Long {
            return -1
        }
    }

    init {
        val params = setupParams()
        val schemeRegistry = setupSchemeRegistry()
        val threadSafeManager = ThreadSafeClientConnManager(params, schemeRegistry)
        client = DefaultHttpClient(threadSafeManager, params)
        enableGzipEncoding()
    }
}