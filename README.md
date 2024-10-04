To enhance your FeeWaiverController with logging, error handling, and UUID tracking, I'll show you how to integrate UUID into the request headers, add logging using Slf4j, and update the error handling using a GlobalExceptionHandler. Additionally, I'll add validation for requestId and throw custom exceptions where appropriate.

Here’s how you can implement this:

1. FeeWaiverController with UUID Logging and Error Handling

package com.sc.rdc.cops.ivrapl.controller;

import com.sc.rdc.cops.ivrapl.dto.FeeWaiverDto;
import com.sc.rdc.cops.ivrapl.dto.StatusDto;
import com.sc.rdc.cops.ivrapl.exception.FeePayerControlServiceException;
import com.sc.rdc.cops.ivrapl.exception.MissingRequestIdException;
import com.sc.rdc.cops.ivrapl.service.FeeWaiverServiceSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/feewaiver")
public class FeeWaiverController {

    @Autowired
    private FeeWaiverServiceSelector feeWaiverServiceSelector;

    @PostMapping("/getFeeWaiver")
    public ResponseEntity<Object> getFeeWaiver(@RequestHeader("country") String country,
                                               @RequestHeader("requestId") String requestId,
                                               @RequestBody FeeWaiverDto dto) {
        validateRequestId(requestId);

        log.info("Request ID: {} - Retrieving fee waiver details for card number: {}", requestId, dto.getCardNum());

        FeeWaiverDto res = feeWaiverServiceSelector.findBynCardnum(dto, country);
        
        if (Objects.nonNull(res)) {
            log.info("Request ID: {} - Fee waiver details found for card number: {}", requestId, dto.getCardNum());
            return ResponseEntity.ok().body(res);
        }

        log.warn("Request ID: {} - No fee waiver details found for card number: {}", requestId, dto.getCardNum());
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/updateFeeWaiver")
    public ResponseEntity<StatusDto> updateFeeWaiver(@RequestHeader("country") String country,
                                                     @RequestHeader("requestId") String requestId,
                                                     @RequestBody FeeWaiverDto dto) {
        validateRequestId(requestId);

        log.info("Request ID: {} - Updating fee waiver for card number: {}", requestId, dto.getCardNum());

        StatusDto statusDto = new StatusDto();
        Boolean updated = feeWaiverServiceSelector.updateFeeWaiver(dto, country);

        if (updated) {
            statusDto.setStatus("Success");
            log.info("Request ID: {} - Fee waiver successfully updated for card number: {}", requestId, dto.getCardNum());
            return ResponseEntity.ok().body(statusDto);
        }

        statusDto.setStatus("Failure");
        log.warn("Request ID: {} - Failed to update fee waiver for card number: {}", requestId, dto.getCardNum());
        return ResponseEntity.ok().body(statusDto);
    }

    // Validate if requestId is present
    private void validateRequestId(String requestId) {
        if (Objects.isNull(requestId) || requestId.isEmpty()) {
            log.error("Request is missing requestId header.");
            throw new MissingRequestIdException("Missing requestId in request headers.");
        }
    }
}

2. GlobalExceptionHandler

package com.sc.rdc.cops.ivrapl.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, @RequestHeader("requestId") String requestId) {
        logger.error("Error occurred: [requestId: {}] - {}", requestId, ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("An error occurred while processing the request. Please try again later.");
    }

    @ExceptionHandler(FeePayerControlServiceException.class)
    public ResponseEntity<String> handleFeePayerControlServiceException(FeePayerControlServiceException ex, 
                                                                        @RequestHeader("requestId") String requestId) {
        logger.warn("FeePayerControlServiceException: [requestId: {}] - {}", requestId, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("An error occurred in FeePayerControlService: " + ex.getMessage());
    }

    @ExceptionHandler(MissingRequestIdException.class)
    public ResponseEntity<String> handleMissingRequestIdException(MissingRequestIdException ex) {
        logger.error("Missing requestId in request headers: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Missing requestId in the request headers.");
    }
}

3. Custom Exceptions

a. FeePayerControlServiceException

package com.sc.rdc.cops.ivrapl.exception;

public class FeePayerControlServiceException extends RuntimeException {
    
    public FeePayerControlServiceException(String message) {
        super(message);
    }
    
    public FeePayerControlServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

b. MissingRequestIdException

package com.sc.rdc.cops.ivrapl.exception;

public class MissingRequestIdException extends RuntimeException {

    public MissingRequestIdException(String message) {
        super(message);
    }

    public MissingRequestIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

Explanation:

1. UUID in Request Header: Each request now requires a requestId header, which is validated to ensure every request can be traced.


2. Logging: Logging is done using Slf4j with info, warn, and error levels for different scenarios.


3. Global Exception Handling: The GlobalExceptionHandler captures general exceptions and custom exceptions, ensuring consistent error responses.


4. Custom Exceptions: FeePayerControlServiceException is used for specific service-level errors, while MissingRequestIdException ensures the requestId is always present.



You can now track each request using the UUID and effectively handle exceptions in the system.

