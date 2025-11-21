package sn.project.consultation.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sn.project.consultation.data.entities.Paiement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaytechService {

    @Value("${paytech.api.key}")
    private String apiKey;

    @Value("${paytech.api.secret}")
    private String apiSecret;

    @Value("${paytech.api.url}")
    private String apiUrl;

    @Value("${paytech.callback.url}")
    private String callbackUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String initierPaiement(Double montant, String methode, Long paiementId, Long patientId) {
        String refCommand = "CMD-" + paiementId + "-" + UUID.randomUUID().toString().substring(0,6);

        Map<String, Object> payload = new HashMap<>();
        payload.put("item_price", montant);
        payload.put("ref_command", refCommand);
        payload.put("command_name", "Paiement consultation Patient " + patientId);
        payload.put("env", "test");
        payload.put("currency", "XOF");
        payload.put("target_payment", methode);
        payload.put("transactionId", paiementId.toString());
        payload.put("ipn_url", callbackUrl);
        payload.put("success_url", "https://paytech.sn/mobile/success");
        payload.put("cancel_url", "https://paytech.sn/mobile/cancel");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API_KEY", apiKey);
        headers.set("API_SECRET", apiSecret);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, new HttpEntity<>(payload, headers), String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> mapBody = new ObjectMapper().readValue(response.getBody(), Map.class);
                if (mapBody.containsKey("redirect_url")) {
                    return (String) mapBody.get("redirect_url");
                }
                throw new RuntimeException("Réponse PayTech invalide : " + mapBody);
            } else {
                throw new RuntimeException("Erreur PayTech : " + response.getStatusCode());
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Erreur 401 Unauthorized : vérifie ton apiKey/apiSecret", e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à PayTech : " + e.getMessage(), e);
        }
    }
}
