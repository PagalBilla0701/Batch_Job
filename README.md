@Override
public Object getPreferredLanguageByRelId(String relId) {
    log.info("Entering getPreferredLanguageByRelId method with relId: {}", relId);

    PreferredLanguageDto dto = new PreferredLanguageDto();
    String latestFlag = "Y";

    log.debug("Fetching preferred language with relId: {} and latestFlag: {}", relId, latestFlag);
    PreferredLanguage result = preferredLanguageRepository.findByXRelIdAndFLatest(relId, latestFlag);

    if (Objects.nonNull(result)) {
        log.debug("Preferred language found for relId: {}", relId);
        dto.setRelId(result.getxRelId());
        dto.setLangCd(result.getxLangCd());
        return dto;
    } else {
        log.warn("No preferred language found for relId: {}", relId);
        return null;
    }
}

@Override
public Optional<PreferredLanguage> getPreferredLanguage(Long id, String relId) {
    log.info("Entering getPreferredLanguage method with id: {} and relId: {}", id, relId);

    PreferredLanguageId preferredLanguageId = new PreferredLanguageId(id, relId);
    log.debug("Fetching preferred language by id and relId");
    return preferredLanguageRepository.findById(preferredLanguageId);
}

@Override
@Transactional
public String insertPreferredLanguage(PreferredLanguageDto preferredLanguageDto) {
    log.info("Entering insertPreferredLanguage method with PreferredLanguageDto: {}", preferredLanguageDto);

    updateNonLatest(preferredLanguageDto);
    String response = "Failure";
    Date currentDate = Calendar.getInstance().getTime();
    
    PreferredLanguage dbEntity = new PreferredLanguage();
    log.debug("Setting values from PreferredLanguageDto to PreferredLanguage entity");
    dbEntity.setxRelId(preferredLanguageDto.getRelId());
    dbEntity.setxLangCd(preferredLanguageDto.getLangCd());
    dbEntity.setdCreat(currentDate);
    dbEntity.setdUpd(currentDate);
    dbEntity.setxCreat("IVR USER");
    dbEntity.setxUpd("IVR USER");
    dbEntity.setfLatest("Y");

    log.debug("Saving the PreferredLanguage entity to the database");
    PreferredLanguage dbPreferredLanguage = preferredLanguageRepository.save(dbEntity);

    if (Objects.nonNull(dbPreferredLanguage)) {
        log.info("Preferred language inserted successfully for relId: {}", preferredLanguageDto.getRelId());
        response = "Success";
    } else {
        log.error("Failed to insert preferred language for relId: {}", preferredLanguageDto.getRelId());
    }

    return response;
}

private void updateNonLatest(PreferredLanguageDto preferredLanguageDto) {
    String relId = preferredLanguageDto.getRelId();
    log.info("Updating non-latest preferred languages for relId: {}", relId);

    preferredLanguageRepository.updateNonLatest(relId);
}

@Override
public String deletePreferredLanguageByRelId(String relId) {
    log.info("Entering deletePreferredLanguageByRelId method with relId: {}", relId);

    Object relIdPreferredLanguage = preferredLanguageRepository.findByRelId(relId);
    
    if (Objects.nonNull(relIdPreferredLanguage)) {
        log.debug("Preferred language found for relId: {}, proceeding with deletion", relId);
        preferredLanguageRepository.deletePreferredLanguageByRelId(relId);

        Object byRelId = preferredLanguageRepository.findByRelId(relId);
        if (Objects.nonNull(byRelId)) {
            log.error("Failed to delete preferred language for relId: {}", relId);
            return "Failure";
        }
        log.info("Preferred language deleted successfully for relId: {}", relId);
        return "Success";
    } else {
        log.warn("No preferred language exists for relId: {}", relId);
        return "No preferred Language exists for Customer";
    }
}
