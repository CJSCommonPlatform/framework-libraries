package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("note-created")
public class NoteCreated {

    private final UUID noteId;

    @JsonCreator
    public NoteCreated(final UUID noteId) {
        this.noteId = noteId;
    }

    public UUID getNoteId() {
        return noteId;
    }
}
