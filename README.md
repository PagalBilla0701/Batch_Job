@PostMapping("/custIndicator")
public ResponseEntity<Object> fetchCustIndicators(
        @RequestParam("relld") String relid,
        @RequestHeader("UUID") String uuid,
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for fetching customer indicators. [UUID: {}, Country: {}]", uuid, country);
    
    CustIndicatorDto dto = selector.fetchCustIndicators(relid, country);
    
    if (Objects.nonNull(dto)) {
        return ResponseEntity.ok().body(dto);
    }
    
    return ResponseEntity.notFound().build();
}

@PostMapping("/getFeeWaiver")
public ResponseEntity<Object> getFeeWaiver(
        @RequestHeader("country") String country, 
        @RequestHeader("UUID") String uuid,
        @RequestBody FeeWaiverDto dto) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for fetching fee waiver details. [UUID: {}, Country: {}, Card Number: {}]", uuid, country, dto.getCardNum());
    
    FeewaiverDto res = feewaiverServiceSelector.findBynCardnum(dto, country);
    
    if (Objects.nonNull(res)) {
        log.info("Fee waiver details found. [UUID: {}, Card Number: {}]", uuid, dto.getCardNum());
        return ResponseEntity.ok().body(res);
    }
    
    log.warn("No fee waiver details found. [UUID: {}, Card Number: {}]", uuid, dto.getCardNum());
    return ResponseEntity.notFound().build();
}

@PatchMapping("/updateFeelWaiver")
public ResponseEntity<StatusDto> updateFeeWaiver(
        @RequestHeader("country") String country,
        @RequestHeader("UUID") String uuid, 
        @RequestBody FeeWaiverDto dto) {
    
    ValidateUUID.validateUUID(uuid);
    log.info("Request received for updating fee waiver. [UUID: {}, Card Number: {}]", uuid, dto.getCardNum());

    StatusDto statusDto = new StatusDto();
    Boolean updated = feeWaiverServiceSelector.updateFeeWaiver(dto, country);
    
    if (updated) {
        statusDto.setStatus("Success");
        return ResponseEntity.ok().body(statusDto);
    }
    
    statusDto.setStatus("Failure");
    return ResponseEntity.ok().body(statusDto);
}

@PatchMapping("/update")
public ResponseEntity<StatusDto> updateIVRCallActivity(
        @RequestParam("refNo") Long refNo,
        @RequestHeader("UUID") String uuid, 
        @RequestHeader("country") String country,
        @RequestBody SecondFactorAuthentication secondFactorAuthentication) {
    
    ValidateUUID.validateUUID(uuid);
    log.info("Request received for updating IVR call activity. [UUID: {}, RefNo: {}]", uuid, refNo);

    StatusDto statusDto = new StatusDto();
    boolean updated = selector.updateIVRCallActivity(refNo, country, secondFactorAuthentication);
    
    if (updated) {
        statusDto.setStatus("Success");
        return ResponseEntity.ok().body(statusDto);
    }
    
    statusDto.setStatus("Failure");
    return ResponseEntity.ok().body(statusDto);
}

@PostMapping("/ivrInsert")
public ResponseEntity<Object> insertIvrCallReport(
        @RequestBody IvrCallReportDto ivrCallReportDto, 
        @RequestHeader("UUID") String uuid,
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for inserting IVR call report. [UUID: {}, Country: {}]", uuid, country);

    StatusDto statusDto = new StatusDto();
    Boolean created = selector.insertIvrCallReport(country, ivrCallReportDto);
    
    if (created) {
        statusDto.setStatus("Success");
        return ResponseEntity.ok().body(statusDto);
    }
    
    statusDto.setStatus("Failure");
    return ResponseEntity.ok().body(statusDto);
}

@PostMapping("/acdInsert")
public ResponseEntity<Object> insertAcdCallReport(
        @RequestBody AcdCallReportDto acdCallReportDto, 
        @RequestHeader("UUID") String uuid,
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for inserting ACD call report. [UUID: {}, Country: {}]", uuid, country);

    StatusDto statusDto = new StatusDto();
    Boolean created = selector.insertAcdCallReport(country, acdCallReportDto);
    
    if (created) {
        statusDto.setStatus("Success");
        return ResponseEntity.ok().body(statusDto);
    }
    
    statusDto.setStatus("Failure");
    return ResponseEntity.ok().body(statusDto);
}

@PostMapping("/insertCallBack")
public ResponseEntity<Object> insertNtbCallBack(
        @RequestBody NtbCallBackDto ntbCallBackDto,
        @RequestHeader("UUID") String uuid, 
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for inserting NTB callback. [UUID: {}, Country: {}]", uuid, country);

    StatusDto statusDto = new StatusDto();
    Boolean created = ntbCallBackServiceSelector.insertNtbCallBack(ntbCallBackDto, country);
    
    if (created) {
        statusDto.setStatus("Success");
        return ResponseEntity.ok().body(statusDto);
    }
    
    statusDto.setStatus("Failure");
    return ResponseEntity.ok().body(statusDto);
}

@PostMapping("/getLanguage")
public ResponseEntity<Object> getPreferredLanguageByRelId(
        @RequestParam("relId") String relId,
        @RequestHeader("UUID") String uuid, 
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for fetching preferred language. [UUID: {}, Country: {}, RelId: {}]", uuid, country, relId);
    
    PreferredLanguageDto preferredLang = preferredLanguageServiceSelector.getPreferredLanguageByRelId(relId, country);
    
    if (Objects.nonNull(preferredLang)) {
        return ResponseEntity.ok().body(preferredLang);
    }
    
    return ResponseEntity.notFound().build();
}

@PostMapping("/saveLang")
public ResponseEntity<Object> insertPreferredLanguage(
        @RequestBody PreferredLanguageDto preferredLanguage,
        @RequestHeader("UUID") String uuid, 
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for inserting preferred language. [UUID: {}, Country: {}]", uuid, country);

    String response = preferredLanguageServiceSelector.insertPreferredLanguage(preferredLanguage, country);
    
    StatusDto dto = new StatusDto();
    dto.setStatus(response);
    
    return ResponseEntity.ok().body(dto);
}

@DeleteMapping("/delete")
public ResponseEntity<Object> deletePreferredLanguageByRelId(
        @RequestParam("relId") String relId,
        @RequestHeader("UUID") String uuid, 
        @RequestHeader("country") String country) {

    ValidateUUID.validateUUID(uuid);
    log.info("Request received for deleting preferred language. [UUID: {}, Country: {}, RelId: {}]", uuid, country, relId);

    String response = preferredLanguageServiceSelector.deletePreferredLanguageByRelId(relId, country);
    
    StatusDto dto = new StatusDto();
    dto.setStatus(response);
    
    if (response.contains("No")) {
        return ResponseEntity.badRequest().body(dto);
    } else {
        return ResponseEntity.ok().body(dto);
    }
}
