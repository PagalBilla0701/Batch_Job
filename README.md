import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Test
public void testButtonClickInfo_Success() throws Exception {
    // Prepare mock data
    String callRefNo = "12345";
    String type = "type";
    
    LoginBean login = new LoginBean();
    // Assume login is set up with the required fields, like setting the user bean and country code

    // Mock expected response
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");

    // Perform the POST request
    mockMvc.perform(post("/genesys-button-click.do")
            .param("type", type)
            .param("callActivityNo", callRefNo)
            .sessionAttr("login", login) // Setting up session attribute for login
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"));
}
