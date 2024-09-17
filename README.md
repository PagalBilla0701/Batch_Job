# Batch_Job

Here's the revised implementation based on your provided code sample, integrated with the task of reading a CSV file stored in the database as a Blob and writing it to the local file system before processing it in a Spring Batch job.

The main flow:

A scheduled method (importKycIndicators) runs every minute.
It fetches the latest submitted file from the database using the CemsAuditLog entity.
If a valid file is found, it converts the Blob from the database into a local CSV file.
The batch job (runJob) is executed with the path to the temporary file.
Revised Code:
java
Copy code
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KycIndicatorsImportService {

    private static final Logger log = LoggerFactory.getLogger(KycIndicatorsImportService.class);

    @Autowired
    private CemsAuditLogRepository cemsAuditLogRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job kycIndicatorsJob;

    // Scheduled method to run every minute
    @Scheduled(cron = "0 0/1 * * * ?")  // Runs every minute
    public void importKycIndicators() throws IOException, SQLException {
        CemsAuditLog cemsAuditLog = getLatestSubmittedFiles();

        if (cemsAuditLog == null) {
            log.error("No valid file found in the database");
            throw new IllegalArgumentException("No valid file found in the database");
        }

        if (cemsAuditLog.getInputfileContent() == null) {
            log.error("No input file found for the selected record");
            throw new IllegalArgumentException("No input file found for the selected record");
        }

        log.info("Converting Blob to file for the reader...");
        File tempFile = File.createTempFile("kycData", ".csv");
        tempFile.deleteOnExit(); // Ensures the temp file is deleted after the JVM exits

        try (InputStream inputStream = cemsAuditLog.getInputfileContent().getBinaryStream();
             OutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        log.info("File successfully written to temp directory: {}", tempFile.getAbsolutePath());

        // Now trigger the batch job to process the file
        runJob(kycIndicatorsJob, "KYC Indicators", tempFile.getAbsolutePath());
    }

    // Fetches the latest submitted file from the database
    private CemsAuditLog getLatestSubmittedFiles() {
        log.info("Fetching the latest submitted file from the database");

        // Assuming "CustInd" and "KYCInd" are the objectType and fileType
        List<CemsAuditLog> cemsAuditLogList = cemsAuditLogRepository.findLatestKycIndSubmittedFiles("CustInd", "KYCInd");

        if (cemsAuditLogList.isEmpty()) {
            log.warn("No submitted files found for objectType: CustInd and fileType: KYCInd");
            return null;
        }

        log.info("Found submitted file: {}", cemsAuditLogList.get(0).getFileId());
        return cemsAuditLogList.get(0); // Return the latest file
    }

    // Trigger the batch job
    private void runJob(Job job, String jobName, String filePath) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", filePath)
                .addLong("time", System.currentTimeMillis())  // Unique parameter to rerun the job
                .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.info("Job {} started with status: {}", jobName, jobExecution.getStatus());
        } catch (Exception e) {
            log.error("Failed to start job {} due to error: {}", jobName, e.getMessage());
        }
    }
}
Breakdown of Key Components:
importKycIndicators:

This is the scheduled method that runs every minute (cron expression: 0 0/1 * * * ?).
It fetches the latest file from the database, converts the Blob content to a file, and triggers the Spring Batch job to process the file.
getLatestSubmittedFiles:

This method queries the database to fetch the latest submitted file that matches the required objectType (CustInd) and fileType (KYCInd).
If no file is found, it logs a warning and returns null.
runJob:

It triggers the Spring Batch job (kycIndicatorsJob), passing the file path as a job parameter.
Assumptions:
The file content is stored in a Blob in the CemsAuditLogEntity table.
findLatestKycIndSubmittedFiles(String objectType, String fileType) is a method in your repository that fetches the latest file records based on object type and file type.
You’ve configured the Spring Batch job (kycIndicatorsJob) to accept a file path and process the file from that location.
Example Repository Query:
Here’s how you might implement the repository method to fetch the latest file.

java
Copy code
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CemsAuditLogRepository extends CrudRepository<CemsAuditLogEntity, Long> {

    @Query("SELECT c FROM CemsAuditLogEntity c WHERE c.objectType = ?1 AND c.fileType = ?2 ORDER BY c.fileId DESC")
    List<CemsAuditLogEntity> findLatestKycIndSubmittedFiles(String objectType, String fileType);
}
Entity Class (CemsAuditLogEntity):
java
Copy code
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;

@Entity
@Table(name = "CEMS_LMS_FILE_AUDIT")
public class CemsAuditLogEntity implements Serializable {

    @Id
    @Column(name = "ID_FILE")
    private BigDecimal fileId;

    @Column(name = "X_OBJECT_TYPE")
    private String objectType;

    @Column(name = "X_FILE_TYPE")
    private String fileType;

    @Column(name = "X_FILE_NAME")
    private String fileName;

    @Lob
    @Column(name = "B_DATA_FILE_CONTENT")
    private Blob inputfileContent;  // Blob to hold the file content

    // Getters and Setters
    // ...
}
Conclusion:
This code:

Retrieves the file content from the database.
Writes it to the file system as a CSV file.
Schedules the import process to run every minute, triggering a batch job that processes the newly written CSV file.
