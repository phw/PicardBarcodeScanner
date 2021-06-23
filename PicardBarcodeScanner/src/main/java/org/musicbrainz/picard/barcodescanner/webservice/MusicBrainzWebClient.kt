package org.musicbrainz.picard.barcodescanner.webservice

import org.apache.http.HttpEntity
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.HttpResponseException
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.AbstractHttpClient
import org.apache.http.impl.client.BasicResponseHandler
import org.musicbrainz.picard.barcodescanner.MusicBrainz
import org.musicbrainz.picard.barcodescanner.User
import org.musicbrainz.picard.barcodescanner.data.*
import java.io.IOException
import java.util.*

/**
 * Makes the web service available for Activity classes. Calls are blocking and
 * should be made asynchronously. The XML returned is parsed into pojos with
 * SAX handlers.
 */
class MusicBrainzWebClient : MusicBrainz {
    private var httpClient: AbstractHttpClient?
    private var responseParser: ResponseParser
    private var clientId: String? = null

    constructor(userAgent: String) {
        httpClient = HttpClient.getClient(userAgent)
        responseParser = ResponseParser()
    }

    constructor(user: User, userAgent: String, clientId: String?) {
        httpClient = HttpClient.getClient(userAgent)
        responseParser = ResponseParser()
        setCredentials(user.username, user.password)
        this.clientId = clientId
    }

    override fun setCredentials(username: String?, password: String?) {
        val authScope = AuthScope(AUTH_SCOPE, AUTH_PORT, AUTH_REALM, AUTH_TYPE)
        val credentials = UsernamePasswordCredentials(username, password)
        httpClient!!.credentialsProvider.setCredentials(authScope, credentials)
    }

    @Throws(IOException::class)
    override fun searchRelease(searchTerm: String?): LinkedList<ReleaseInfo>{
        val entity = get(QueryBuilder.releaseSearch(searchTerm))
        val releases = responseParser.parseReleaseSearch(entity.content)
        entity.consumeContent()
        return releases
    }

    @Throws(IOException::class)
    override fun autenticateCredentials(): Boolean {
        val authenticationTest = HttpGet(QueryBuilder.authenticationCheck())
        authenticationTest.setHeader("Accept", "application/xml")
        try {
            httpClient!!.execute(authenticationTest, BasicResponseHandler())
        } catch (e: HttpResponseException) {
            return false
        }
        return true
    }

    @Throws(IOException::class)
    private operator fun get(url: String?): HttpEntity {
        val get = HttpGet(url)
        get.setHeader("Accept", "application/xml")
        val response = httpClient!!.execute(get)
        return response.entity
    }

    @Throws(IOException::class)
    private fun post(url: String?, content: String?) {
        val post = HttpPost(url)
        post.addHeader("Content-Type", "application/xml; charset=UTF-8")
        val xml = StringEntity(content, "UTF-8")
        post.entity = xml
        val response = httpClient!!.execute(post)
        response.entity.consumeContent()
    }

    @Throws(IOException::class)
    private fun delete(url: String?) {
        val delete = HttpDelete(url)
        val response = httpClient!!.execute(delete)
        response.entity.consumeContent()
    }

    @Throws(IOException::class)
    private fun put(url: String?) {
        val put = HttpPut(url)
        val response = httpClient!!.execute(put)
        response.entity.consumeContent()
    }

    companion object {
        private const val AUTH_REALM = "musicbrainz.org"
        private const val AUTH_SCOPE = "musicbrainz.org"
        private const val AUTH_PORT = 80
        private const val AUTH_TYPE = "Digest"
    }
}