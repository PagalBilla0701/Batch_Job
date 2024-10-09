@Override
public Boolean insertIvrCallReport(String country, IvrCallReportDto dto) {

    log.info("Inserting IVR Call Report for country: {}, UCID: {}", country, dto.getUcid());

    Boolean inserted = false;
    Date currentDate = Calendar.getInstance().getTime();

    IvrCallReportMY report = ivrCallReportMapper.mapToIvrCallReportTable(dto);
    report.setXCreat("IVR");
    report.setDCreat(currentDate);
    report.setDUpd(currentDate);
    report.setXUpd("IVR");
    report.setFullMenuTraversal(null); // Explicitly set

    log.info("Saving IVR Call Report for UCID: {}", dto.getUcid());
    IvrCallReportMY saveReport = ivrRepo.save(report);

    FullMenuTraversalMY menuTraversal = new FullMenuTraversalMY();
    menuTraversal.setUcid(dto.getUcid());
    menuTraversal.setXCreat("IVR");
    menuTraversal.setDCreat(currentDate);
    menuTraversal.setDUpd(currentDate);
    menuTraversal.setXUpd("IVR");

    if (dto.getFullMenuTraversal() != null && !dto.getFullMenuTraversal().isEmpty()) {
        Map<String, String> menus = dto.getFullMenuTraversal();
        setMenusValue(menuTraversal, menus);
    }

    log.info("Saving Full Menu Traversal for UCID: {}", dto.getUcid());
    FullMenuTraversalMY save = fullMenuTraversalRepo.save(menuTraversal);

    if (saveReport != null && save != null) {
        inserted = true;
        log.info("IVR Call Report and Full Menu Traversal saved successfully for UCID: {}", dto.getUcid());
    } else {
        log.warn("Failed to save IVR Call Report or Full Menu Traversal for UCID: {}", dto.getUcid());
    }

    return inserted;
}

@Override
public Boolean insertAcdCallReport(String country, AcdCallReportDto dto) {

    log.info("Inserting ACD Call Report for country: {}, UCID: {}", country, dto.getUcid());

    Boolean inserted = false;
    Date currentDate = Calendar.getInstance().getTime();

    AcdCallReportMY report = acdCallReportMapper.mapToAcdCallReport(dto);
    report.setXCreat("IVR");
    report.setDCreat(currentDate);
    report.setDUpd(currentDate);
    report.setXUpd("IVR");

    log.info("Saving ACD Call Report for UCID: {}", dto.getUcid());
    AcdCallReportMY save = acdRepo.save(report);

    if (Objects.nonNull(save)) {
        inserted = true;
        log.info("ACD Call Report saved successfully for UCID: {}", dto.getUcid());
    } else {
        log.warn("Failed to save ACD Call Report for UCID: {}", dto.getUcid());
    }

    return inserted;
}
