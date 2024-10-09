@Override
public CustIndicatorDto fetchCustIndicators(String relId) {

    log.info("Fetching CustIndicators for REL_ID: {}", relId);

    Optional<CustIndicatorMY> optionalCustIndicator = custIndicatorRepository.findById(relId);
    Optional<RepeatCallerMY> optionalRepeatCaller = repeatCallerRepository.findById(relId);

    log.info("Customer {} present in cust indicator repo? {}", relId, optionalCustIndicator.isPresent());
    log.info("Customer {} present in repeat caller repo? {}", relId, optionalRepeatCaller.isPresent());

    if (optionalCustIndicator.isPresent() && optionalRepeatCaller.isPresent()) {
        RepeatCallerMY repeatCaller = optionalRepeatCaller.get();
        CustIndicatorMY custIndicator = optionalCustIndicator.get();

        CustIndicatorDto dto = new CustIndicatorDto();
        dto.setKycDeficient(custIndicator.getfKycStatus().equalsIgnoreCase("Y") ? "Yes" : "No");
        dto.setSensitiveCustomer(custIndicator.getfSensitiveCust().equalsIgnoreCase("Y") ? "Yes" : "No");
        dto.setTransferExclusion(custIndicator.getfTransferExclusion().equalsIgnoreCase("Y") ? "Yes" : "No");
        dto.setRepeatCaller(repeatCaller.getfRepeat().equalsIgnoreCase("YES") ? "Yes" : "No");

        log.info("Successfully fetched CustIndicators for REL_ID: {}", relId);
        return dto;
    }

    log.warn("Customer {} not found in either cust indicator or repeat caller repositories", relId);
    return null;
}
