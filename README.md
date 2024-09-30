@Service
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("kycIndicatorsJob")
    private Job kycIndicatorsJob;

    @Autowired
    @Qualifier("transferExclusionJob")
    private Job transferExclusionJob;

    @Autowired
    @Qualifier("sensitiveCustomerJob")
    private Job sensitiveCustomerJob;

    @Autowired
    @Qualifier("rbsCustomerJob")
    private Job rbsCustomerJob;

    @Autowired
    private CemsAuditLogEnityRepository cemsAuditLogEnityRepository;

    // Scheduled to run every 1 minute for KYC Indicators processing
    @Scheduled(cron = "0 */1 * * * *")
    public void importKycIndicators() {
        processJob(kycIndicatorsJob, "KYC Indicators", "KYCInd");
    }

    // Scheduled to run every 1 minute for Transfer Exclusion processing
    @Scheduled(cron = "0 */1 * * * *")
    public void importTransferExclusion() {
        processJob(transferExclusionJob, "Transfer Exclusion", "TransEx");
    }

    // Scheduled to run every 1 minute for Sensitive Customer processing
    @Scheduled(cron = "0 */1 * * * *")
    public void importSensitiveCustomer() {
        processJob(sensitiveCustomerJob, "Sensitive Customer", "SensCust");
    }

    // Scheduled to run every 1 minute for RBS Customer processing
    @Scheduled(cron = "0 */1 * * * *")
    public void importRbsCustomer() {
        processJob(rbsCustomerJob, "RBS Customer", "RBSCust");
    }

    // Method to handle job execution
    private void processJob(Job job, String jobName, String fileType) {
        // Step 1: Fetch the submitted file
        CemsAuditLog submittedFile = fetchSubmittedFile(fileType);

        if (submittedFile != null) {
            try {
                // Step 2: Update the status to 'InProgress'
                updateFileStatus(submittedFile, "InProgress");

                // Prepare job parameters with the file ID and other details
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("startAt", System.currentTimeMillis())
                        .addString("fileId", String.valueOf(submittedFile.getFileId()))
                        .addString("jobName", jobName)
                        .toJobParameters();

                // Step 3: Trigger the Batch Job
                JobExecution jobExecution = jobLauncher.run(job, jobParameters);

                // Step 5: On successful completion, update the status to 'Completed'
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    updateFileStatus(submittedFile, "Completed");
                }

            } catch (JobExecutionAlreadyRunningException | JobRestartException
                    | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                // Step 6: Handle failures, update the status to 'Failed'
                updateFileStatus(submittedFile, "Failed");
                e.printStackTrace();
            }
        } else {
            System.out.println("No submitted file found for processing: " + jobName);
        }
    }

    // Fetch the latest submitted file based on file type
    private CemsAuditLog fetchSubmittedFile(String fileType) {
        return cemsAuditLogEnityRepository.findLatestSubmittedFileByType(fileType)
                .orElse(null);
    }

    // Update file status
    private void updateFileStatus(CemsAuditLog file, String status) {
        file.setStatus(status);
        cemsAuditLogEnityRepository.save(file);
    }
}
