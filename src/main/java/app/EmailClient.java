package app;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "emailClient", url = "https://external-email-api.com")
public interface EmailClient {
    @PostMapping("/send")
    String sendEmail(@RequestBody Map<String, String> emailPayload);
}
