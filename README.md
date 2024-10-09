@Override
public Boolean insertNtbCallBack(NtbCallBackDto dto) {
    log.info("Entering insertNtbCallBack method in SG callback class");

    Date currentDate = Calendar.getInstance().getTime();
    NtbCallback dbEntity = new NtbCallback();

    log.debug("Setting values from NtbCallBackDto to NtbCallback entity");
    dbEntity.setXCli(dto.getCli());
    dbEntity.setXNtbMenu(dto.getNtbMenu());
    dbEntity.setXCallbackNum(dto.getCallBackNumber());
    dbEntity.setFSmsTriggered(dto.getSmsTriggered());
    dbEntity.setXInteractionId(dto.getInteractionId());
    dbEntity.setXDnis(dto.getDnis());
    dbEntity.setDCallDtTime(dto.getCallDateTime());
    dbEntity.setDCreat(currentDate);
    dbEntity.setDUpd(currentDate);
    dbEntity.setXCreat("IVR_USER");
    dbEntity.setXUpd("IVR_USER");

    log.debug("Saving the NtbCallback entity to the database");
    NtbCallback saved = ntbCallbackRepository.save(dbEntity);

    boolean isSaved = Objects.nonNull(saved);
    log.info("Insert operation was {}", isSaved ? "successful" : "unsuccessful");

    return isSaved;
}
