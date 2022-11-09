package ajou.paran.entrip.model

import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment

data class Comment(
    val comment: ResponseComment,
    val listNestedComment: MutableList<ResponseNestedComment>
) {
    constructor(comment: ResponseComment) : this(
        comment = comment,
        listNestedComment = mutableListOf()
    )
}