import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.Map;

@RestController
public class AccountController {

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final UrlValidator urlValidator = new UrlValidator();

    @PostMapping("/account/deactivate")
    public ResponseEntity<Map<String, String>> deactivateAccount(@RequestParam String deactivationUrl) throws Exception {
        String validUrl = urlValidator.validateAndCleanUrl(deactivationUrl);
        Request request = new Request.Builder().url(validUrl).build();

        okHttpClient.newCall(request).execute().close();

        return ResponseEntity.ok(Map.of("account", "deactivated"));
    }
}
