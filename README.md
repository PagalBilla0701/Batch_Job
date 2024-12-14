import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GenesysController {

    @Autowired
    private GenesysService genesysService;

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/get-softphone-details.do", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> getGenesysDetails() throws Exception {
        // Fetch details from DB using the service
        Map<String, String> details = genesysService.getGenesysDetailsById("GEN01"); // Replace "GEN01" with the appropriate ID if dynamic

        // Ensure the required fields are not null
        if (details == null || 
            details.get("iFrameUrl") == null || 
            details.get("flowID") == null || 
            details.get("flowName") == null) {
            throw new Exception("Required Genesys details are missing or null");
        }

        // If all required fields are non-null, return the response with isSoftPhoneDisplay = "Yes"
        Map<String, Object> response = new HashMap<>();
        response.put("isSoftPhoneDisplay", "Yes");
        response.put("Frameurl", details.get("iFrameUrl"));
        response.put("flowID", details.get("flowID"));
        response.put("flowName", details.get("flowName"));
        return response;
    }
}
