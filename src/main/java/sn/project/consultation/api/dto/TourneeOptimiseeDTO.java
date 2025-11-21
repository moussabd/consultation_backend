package sn.project.consultation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourneeOptimiseeDTO {
    private LocalDateTime date;
    private List<RendezVousDTO> ordreVisites;
}
