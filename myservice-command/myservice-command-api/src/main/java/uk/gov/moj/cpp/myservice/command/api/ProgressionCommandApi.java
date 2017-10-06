package uk.gov.moj.cpp.myservice.command.api;

import static uk.gov.justice.services.core.annotation.Component.COMMAND_API;

import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.annotation.ServiceComponent;
import uk.gov.justice.services.core.json.JsonSchemaValidator;
import uk.gov.justice.services.core.sender.Sender;
import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.inject.Inject;

import org.slf4j.Logger;

@ServiceComponent(COMMAND_API)
public class ProgressionCommandApi {

    @Inject
    private Sender sender;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ProgressionCommandApi.class);

@Inject
private JsonSchemaValidator jsonSchemaValidator;


    @Handles("progression.command.add-case-to-crown-court")
    public void addCaseToCrownCourt(final JsonEnvelope envelope) {

        LOG.info("Got to this point! Hurray!");

        LOG.info("progression.command.add-case-to-crown-court: "+ envelope.toString());


    }

    @Handles("progression.command.sending-committal-hearing-information")
    public void sendCommittalHearingInformation(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }
    
    @Handles("progression.command.sentence-hearing-date")
    public void addSentenceHearingDate(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }

    @Handles("progression.command.case-to-be-assigned")
    public void updateCaseToBeAssigned(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }

    @Handles("progression.command.case-assigned-for-review")
    public void updateCaseAssignedForReview(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }

    @Handles("progression.command.prepare-for-sentence-hearing")
    public void prepareForSentenceHearing(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }

    @Handles("progression.command.add-defendant-additional-information")
    public void addAdditionalInformationForDefendant(final JsonEnvelope envelope) {
       // sender.send(envelope);
    }

    @Handles("progression.command.no-more-information-required")
    public void noMoreInformationRequired(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }
    
    @Handles("progression.command.request-psr-for-defendants")
    public void requestPSRForDefendants(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }

    @Handles("progression.command.add-sentence-hearing")
    public void addSentenceHearing(final JsonEnvelope envelope) {
        //sender.send(envelope);
    }
}
