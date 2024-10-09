@Override
public boolean updateIVRCallActivity(Long refNo, String country, SecondFactorAuthentication secondFactorAuthentication) {

    log.info("Attempting to update IVRCallActivity for RefNo: {} and Country: {}", refNo, country);

    Optional<IVRCallActivity> optionalRecord = ivrCallActivityRepository.findByRefNoAndCountryCode(refNo, country);

    if (optionalRecord.isPresent()) {
        IVRCallActivity record = optionalRecord.get();
        log.info("Record found for RefNo: {}, updating second factor authentication.", refNo);

        record.setCustomField2(secondFactorAuthentication.getSecondFactorAuthentication());
        ivrCallActivityRepository.save(record);

        log.info("Successfully updated IVRCallActivity for RefNo: {}", refNo);
        return true;

    } else {
        log.warn("No record found for RefNo: {} and Country: {}", refNo, country);
        return false;
    }
}
