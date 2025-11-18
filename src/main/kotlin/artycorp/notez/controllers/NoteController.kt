package artycorp.notez.controllers

import artycorp.notez.controllers.NoteController.NoteResponse
import artycorp.notez.database.model.Note
import artycorp.notez.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(private val noteRepository: NoteRepository)
{
    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        var title: String,
        var content: String,
        var color: Long,
        var createdAt: Instant
    )

    @PostMapping
    fun save(@RequestBody body: NoteRequest): NoteResponse
    {
        val note = noteRepository.save(
            Note(
                id = body.id?.let {ObjectId(it)}?:ObjectId.get(), //What? This somehow does "if exists, use, else generate"
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId() //NOT SECURE. TODO Add auth.
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(@RequestParam(required = true) ownerId: String): List<NoteResponse>
    {
        return noteRepository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }
}

private fun Note.toResponse(): NoteController.NoteResponse
{
    return NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt
    )
}