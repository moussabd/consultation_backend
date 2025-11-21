package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.DisponibiliteDTO;
import sn.project.consultation.data.entities.Disponibilite;
import sn.project.consultation.data.repositories.DisponibiliteRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.services.DisponibiliteService;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class DisponibiliteServiceImpl implements DisponibiliteService {
    @Autowired
    DisponibiliteRepository dispoRepo;
    @Autowired
    ProSanteRepository proRepo;

    public List<DisponibiliteDTO> getDisponibilites(Long professionnelId, LocalDate date) {
        return dispoRepo.findByProfessionnelIdAndDate(professionnelId, date)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void majDisponibilite(Long id, boolean disponible) {
        Disponibilite dispo = dispoRepo.findById(id).orElseThrow();
        dispo.setDisponible(disponible);
        dispoRepo.save(dispo);
    }

    private DisponibiliteDTO mapToDTO(Disponibilite d) {
        DisponibiliteDTO dto = new DisponibiliteDTO();
        dto.setId(d.getId());
        dto.setDate(d.getDate());
        dto.setHeureDebut(d.getHeureDebut());
        dto.setHeureFin(d.getHeureFin());
        dto.setDisponible(d.isDisponible());
        dto.setProfessionnelId(d.getProfessionnel().getId());
        return dto;
    }
}
