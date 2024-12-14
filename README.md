To implement this requirement, we will:

1. Create a DTO class to encapsulate the data.


2. Write a service method to fetch data from the database.


3. Write a controller endpoint to return the response in the required format.




---

1. DTO Class

Create a DTO class GenesysDetailsDTO to hold the response data.

public class GenesysDetailsDTO {
    private String isSoftPhoneDisplay;
    private String iFrameUrl;
    private String flowID;
    private String flowName;

    // Constructors
    public GenesysDetailsDTO(String isSoftPhoneDisplay, String iFrameUrl, String flowID, String flowName) {
        this.isSoftPhoneDisplay = isSoftPhoneDisplay;
        this.iFrameUrl = iFrameUrl;
        this.flowID = flowID;
        this.flowName = flowName;
    }

    // Getters and Setters
    public String getIsSoftPhoneDisplay() {
        return isSoftPhoneDisplay;
    }

    public void setIsSoftPhoneDisplay(String isSoftPhoneDisplay) {
        this.isSoftPhoneDisplay = isSoftPhoneDisplay;
    }

    public String getiFrameUrl() {
        return iFrameUrl;
    }

    public void setiFrameUrl(String iFrameUrl) {
        this.iFrameUrl = iFrameUrl;
    }

    public String getFlowID() {
        return flowID;
    }

    public void setFlowID(String flowID) {
        this.flowID = flowID;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}


---

2. Service Method

Write a service method to fetch the required data from the CEMS_PARAM_PICKLIST table.

@Service
public class GenesysDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GenesysDetailsDTO getGenesysDetails(String idParam) {
        String sql = "SELECT X_PARM_DATA_1 AS iFrameUrl, X_PARM_DATA_2 AS flowID, X_PARM_DATA_3 AS flowName " +
                     "FROM CEMS_PARAM_PICKLIST WHERE ID_PARAM = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{idParam}, (rs, rowNum) -> 
            new GenesysDetailsDTO(
                "Yes", // Hardcoded as "isSoftPhoneDisplay" is always "Yes"
                rs.getString("iFrameUrl"),
                rs.getString("flowID"),
                rs.getString("flowName")
            )
        );
    }
}


---

3. Controller Endpoint

Expose an API endpoint to fetch and return the data.

@RestController
@RequestMapping("/api/genesys")
public class GenesysDetailsController {
    @Autowired
    private GenesysDetailsService genesysDetailsService;

    @GetMapping("/details")
    public ResponseEntity<GenesysDetailsDTO> getGenesysDetails(@RequestParam String idParam) {
        try {
            GenesysDetailsDTO details = genesysDetailsService.getGenesysDetails(idParam);
            return ResponseEntity.ok(details);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null); // Return 404 if no data is found for the given ID_PARAM
        }
    }
}


---

4. Sample API Response

For the ID_PARAM = GEN01, the response will look like:

{
    "isSoftPhoneDisplay": "Yes",
    "iFrameUrl": "https://apps.aps1.pure.cloud/crm/index.html?enableFrameworkClientId=true&dedicatedLoginWindow=true&crm=embeddableframework",
    "flowID": "a8cb704f-df4b-44dd-9521-e659b4451947",
    "flowName": "MM_MY_v1"
}


---

5. Database Script

Insert the required data into the CEMS_PARAM_PICKLIST table:

INSERT INTO CEMS_PARAM_PICKLIST (
    ID_PARAM, X_ENTITY_ID, X_APP_GROUP, X_FUNCTION, X_OBJECT_TYP, X_CTRY_CD,
    X_PARM_KEY_1, X_PARM_KEY_2, X_PARM_KEY_3, X_PARM_DATA_1, X_PARM_DATA_2, X_PARM_DATA_3, D_CREAT, X_CREAT, D_UPD, X_UPD
) VALUES (
    'GEN01', '**', 'S2SBATCH', '**', 'BW', 'GLOBAL',
    'iFrameUrl', 'flowID', 'flowName',
    'https://apps.aps1.pure.cloud/crm/index.html?enableFrameworkClientId=true&dedicatedLoginWindow=true&crm=embeddableframework',
    'a8cb704f-df4b-44dd-9521-e659b4451947',
    'MM_MY_v1',
    TO_DATE('14-05-2019', 'DD-MM-YYYY'), 'admin', TO_DATE('14-05-2019', 'DD-MM-YYYY'), 'admin'
);


---

6. Frontend / UI Integration

The frontend can consume the endpoint `/api/genesys

