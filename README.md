@Configuration
public class KycIndicatorsReader {

    @Autowired
    private CemsAuditLogEnityRepository cemsAuditLogEnityRepository;

    @Autowired
    private CemsAuditLog cemsAuditLog;

    @Bean
    public FlatFileItemReader<KycIndicatorsDto> kycFileReader() throws SQLException, IOException {
        FlatFileItemReader<KycIndicatorsDto> itemReader = new FlatFileItemReader<>();
        
        // Fetch latest file from DB
        File latestFile = getLatestFileFromDB();
        
        if (latestFile != null) {
            itemReader.setResource(new FileSystemResource(latestFile));
        } else {
            throw new FileNotFoundException("No valid KYC file found in the database");
        }
        
        itemReader.setName("kycFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        
        return itemReader;
    }

    public LineMapper<KycIndicatorsDto> lineMapper() {
        DefaultLineMapper<KycIndicatorsDto> lineMapper = new DefaultLineMapper<>();
        
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("relId", "fKycStatus");
        lineTokenizer.setStrict(false);
        
        BeanWrapperFieldSetMapper<KycIndicatorsDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(KycIndicatorsDto.class);
        
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        return lineMapper;
    }

    // Method to fetch the latest file from DB, convert the Blob, and return the file.
    public File getLatestFileFromDB() throws IOException, SQLException {
        CemsAuditLog latestLogFile = getLatestSubmittedFileFromDB();
        
        if (latestLogFile == null || latestLogFile.getInputfileContent() == null) {
            log.info("No KYC file found in the database");
            return null;
        }

        log.info("Converting Blob to file for the reader...");
        return convertBlobToFile(latestLogFile.getInputfileContent(), "kycData", ".csv");
    }

    // Convert Blob content to a File object.
    private File convertBlobToFile(Blob blob, String prefix, String suffix) throws SQLException, IOException {
        // Use the temporary directory for storing the converted file
        File file = File.createTempFile(prefix, suffix);
        
        try (InputStream inputStream = blob.getBinaryStream();
             OutputStream outputStream = new FileOutputStream(file)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            log.info("File created at: {}", file.getAbsolutePath());
        }
        
        return file;
    }

    // Fetch the latest submitted file record from the database
    public CemsAuditLog getLatestSubmittedFileFromDB() {
        log.info("Fetching the latest submitted KYC file from the database...");
        
        List<CemsAuditLog> cemsAuditLogList = cemsAuditLogEnityRepository.findLatestKycIndSubmittedFiles("CustInd", "KYCInd");
        if (cemsAuditLogList.isEmpty()) {
            log.warn("No submitted files found for object type: CustInd and fileType: KYCInd");
            return null;
        }
        
        log.info("Found submitted file: {}", cemsAuditLogList.get(0).getFileId());
        return cemsAuditLogList.get(0);
    }
}
