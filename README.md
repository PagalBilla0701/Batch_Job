import java.util.ArrayList;
import java.util.List;

@Before
public void setUp() {
    MockitoAnnotations.initMocks(this);
    callActivity = new CallActivity();
    loginBean = new LoginBean();

    // Mocking loginBean setup
    loginBean.setUserBean(new UserBean());
    loginBean.getUserBean().setCountryCode("US");
    loginBean.getUserBean().setUserLanguage("EN");
    loginBean.getUserBean().setUserId(12345L);
    loginBean.getUserBean().setPeoplewiseId("PW123");
    loginBean.getUserBean().setCountryShortDesc("United States");
    loginBean.getUserBean().setInstanceCode("INST1");
    loginBean.getUserBean().setSalesObjectAccess(true);
    loginBean.getUserBean().setUserRole("Admin");
    loginBean.getUserBean().setUserRoleId("ROLE123");

    // Mocking response values
    responseValues = new HashMap<>();
    responseValues.put("ibankStatus", "Active");
    responseValues.put("lastLogin", "2024-12-16");
    responseValues.put("creditLimit", "5000");
    responseValues.put("riskCode", "Low");
    responseValues.put("kycStatus", "Verified");
    responseValues.put("outstandingBalance", "10000");
    responseValues.put("currentDueDate", "2024-12-31");
    responseValues.put("approvedAmount", "15000");
    responseValues.put("currentInstallment", "2000");
    responseValues.put("tenure", "12");
    responseValues.put("accountType", "Savings");
    responseValues.put("openComplaintsCount", 2);
    responseValues.put("openSRCount", 1);
    responseValues.put("sensitiveCust", "Yes");

    SectionDataResponse sectionDataResponse = mock(SectionDataResponse.class);

    // Create the list manually
    List<SectionData> sectionDataList = new ArrayList<>();
    SectionData sectionData = new SectionData();
    sectionData.setKeyValGridDataMap(responseValues);
    sectionDataList.add(sectionData);

    when(sectionDataResponse.getSections()).thenReturn(sectionDataList);

    when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(loginBean)))
            .thenReturn(sectionDataResponse);
}
