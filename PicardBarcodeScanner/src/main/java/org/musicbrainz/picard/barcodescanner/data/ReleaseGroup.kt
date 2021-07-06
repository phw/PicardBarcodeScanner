package org.musicbrainz.picard.barcodescanner.data

import java.util.*

class ReleaseGroup : ReleaseGroupInfo() {
    var ratingCount = 0
    var rating = 0f
    private var tags = LinkedList<Tag>()
    private var links = ArrayList<WebLink>()
    var releases = ArrayList<ReleaseInfo>()
    fun getTags(): LinkedList<Tag> {
        Collections.sort(tags)
        return tags
    }

    fun setTags(tags: LinkedList<Tag>) {
        this.tags = tags
    }

    fun addTag(tag: Tag) {
        tags.add(tag)
    }

    fun getLinks(): ArrayList<WebLink> {
        Collections.sort(links)
        return links
    }

    fun addLink(link: WebLink) {
        links.add(link)
    }

    fun setLinks(links: ArrayList<WebLink>) {
        this.links = links
    }

    fun addRelease(release: ReleaseInfo) {
        releases.add(release)
    }
}