package sn.project.consultation.api.dto;

import lombok.Data;

@Data
public class EventLogDTO {
    private String roomId;
    private String event;
    private String user;
    private String role;
    private String content;
    private String timestamp;
}
