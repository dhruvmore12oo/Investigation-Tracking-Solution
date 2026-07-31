package com.example.Investigation_Tracking_Solution.listener;

import com.example.Investigation_Tracking_Solution.dto.notification.NotificationRequest;
import com.example.Investigation_Tracking_Solution.event.*;
import com.example.Investigation_Tracking_Solution.model.NotificationPriority;
import com.example.Investigation_Tracking_Solution.model.NotificationType;
import com.example.Investigation_Tracking_Solution.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFirCreated(FirCreatedEvent event) {
        if (event.getAssignedOfficerUserId() != null) {
            sendNotification(
                    "New FIR Assigned",
                    "FIR " + event.getFirNumber() + " (" + event.getTitle() + ") has been created and assigned to you.",
                    NotificationType.FIR_CREATED,
                    NotificationPriority.MEDIUM,
                    event.getAssignedOfficerUserId(),
                    event.getFirId(),
                    "FIR"
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCaseAssigned(CaseAssignedEvent event) {
        if (event.getAssignedOfficerUserId() != null) {
            sendNotification(
                    "New Case Assigned",
                    "Case " + event.getCaseNumber() + " has been assigned to you as Officer.",
                    NotificationType.CASE_ASSIGNED,
                    NotificationPriority.HIGH,
                    event.getAssignedOfficerUserId(),
                    event.getCaseId(),
                    "CASE"
            );
        }
        if (event.getAssignedInvestigatorUserId() != null) {
            sendNotification(
                    "New Case Assigned",
                    "Case " + event.getCaseNumber() + " has been assigned to you as Lead Investigator.",
                    NotificationType.CASE_ASSIGNED,
                    NotificationPriority.HIGH,
                    event.getAssignedInvestigatorUserId(),
                    event.getCaseId(),
                    "CASE"
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCaseStatusChanged(CaseStatusChangedEvent event) {
        String msg = "Case " + event.getCaseNumber() + " status changed from " + event.getOldStatus() + " to " + event.getNewStatus() + ".";
        NotificationType type = "CLOSED".equalsIgnoreCase(event.getNewStatus()) ? NotificationType.CASE_CLOSED : NotificationType.SYSTEM;
        
        if (event.getAssignedOfficerUserId() != null) {
            sendNotification("Case Status Updated", msg, type, NotificationPriority.MEDIUM, event.getAssignedOfficerUserId(), event.getCaseId(), "CASE");
        }
        if (event.getAssignedInvestigatorUserId() != null) {
            sendNotification("Case Status Updated", msg, type, NotificationPriority.MEDIUM, event.getAssignedInvestigatorUserId(), event.getCaseId(), "CASE");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInvestigationAssigned(InvestigationAssignedEvent event) {
        if (event.getAssignedInvestigatorUserId() != null) {
            sendNotification(
                    "New Investigation Assigned",
                    "Investigation " + event.getInvestigationNumber() + " has been assigned to you.",
                    NotificationType.INVESTIGATION_ASSIGNED,
                    NotificationPriority.HIGH,
                    event.getAssignedInvestigatorUserId(),
                    event.getInvestigationId(),
                    "INVESTIGATION"
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInvestigationStatusChanged(InvestigationStatusChangedEvent event) {
        if (event.getAssignedInvestigatorUserId() != null) {
            String msg = "Investigation " + event.getInvestigationNumber() + " status updated from " + event.getOldStatus() + " to " + event.getNewStatus() + ".";
            NotificationType type = "COMPLETED".equalsIgnoreCase(event.getNewStatus()) ? NotificationType.INVESTIGATION_COMPLETED : NotificationType.SYSTEM;
            sendNotification("Investigation Status Updated", msg, type, NotificationPriority.MEDIUM, event.getAssignedInvestigatorUserId(), event.getInvestigationId(), "INVESTIGATION");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvidenceAdded(EvidenceAddedEvent event) {
        String msg = "New evidence '" + event.getTitle() + "' (" + event.getEvidenceNumber() + ") added to investigation " + event.getInvestigationNumber() + ".";
        if (event.getAssignedInvestigatorUserId() != null) {
            sendNotification("New Evidence Added", msg, NotificationType.EVIDENCE_ADDED, NotificationPriority.MEDIUM, event.getAssignedInvestigatorUserId(), event.getEvidenceId(), "EVIDENCE");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWitnessAdded(WitnessAddedEvent event) {
        String msg = event.isProtected()
                ? "A protected witness (" + event.getWitnessNumber() + ") has been added to case " + event.getCaseNumber() + "."
                : "New witness (" + event.getWitnessNumber() + ") registered for case " + event.getCaseNumber() + ".";

        if (event.getAssignedInvestigatorUserId() != null) {
            sendNotification("Witness Registered", msg, NotificationType.WITNESS_ADDED, NotificationPriority.MEDIUM, event.getAssignedInvestigatorUserId(), event.getWitnessId(), "WITNESS");
        }
        if (event.getAssignedOfficerUserId() != null) {
            sendNotification("Witness Registered", msg, NotificationType.WITNESS_ADDED, NotificationPriority.MEDIUM, event.getAssignedOfficerUserId(), event.getWitnessId(), "WITNESS");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInvestigationNoteCreated(InvestigationNoteCreatedEvent event) {
        if (event.getAssignedInvestigatorUserId() != null && !event.getAssignedInvestigatorUserId().equals(event.getTriggeredByUserId())) {
            String msg = "New note '" + event.getTitle() + "' created for investigation " + event.getInvestigationNumber() + ".";
            sendNotification("New Investigation Note", msg, NotificationType.NOTE_ADDED, NotificationPriority.LOW, event.getAssignedInvestigatorUserId(), event.getInvestigationId(), "INVESTIGATION_NOTE");
        }
    }

    private void sendNotification(String title, String message, NotificationType type, NotificationPriority priority, Long recipientId, Long relatedEntityId, String relatedEntityType) {
        try {
            NotificationRequest request = new NotificationRequest();
            request.setTitle(title);
            request.setMessage(message);
            request.setNotificationType(type);
            request.setNotificationPriority(priority);
            request.setRecipientId(recipientId);
            request.setRelatedEntityId(relatedEntityId);
            request.setRelatedEntityType(relatedEntityType);
            notificationService.createNotification(request);
        } catch (Exception ex) {
            log.error("Failed to generate automatic notification for recipient ID {}: {}", recipientId, ex.getMessage());
        }
    }
}
