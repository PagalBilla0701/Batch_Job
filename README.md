To add logging, error handling, and UUID to each request in your API, you can follow these steps:

1. Add Logging to the Controller and Service Classes

You can use Slf4j from Lombok for logging in each class. Add logging at the start and end of each method to trace the flow.

2. Generate a UUID for Each Request

Add a UUID to the request header, which will be used to trace logs for individual requests. This can be achieved by using an interceptor or handling it directly in the controller methods.

3. Error Handling

You should implement global exception handling using @ControllerAdvice to catch and handle exceptions globally, and log them along with the UUID for better traceability.

Here's how you can modify your classes:

Updated FeeWaiverController

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
public class FeeWaiverController {

    @Autowired
    private FeeWaiverServiceSelector feeWaiverServiceSelector;

    @PostMapping("/getFeeWaiver")
    public ResponseEntity<Object> getFeeWaiver(
            @RequestHeader("country") String country,
            @RequestHeader("uuid") String uuid,  // UUID passed in request header
            @RequestBody FeeWaiverDto dto) {

        log.info("Request UUID: {}, Country: {}, CardNum: {}", uuid, country, dto.getCardNum());

        FeeWaiverDto res = feeWaiverServiceSelector.findBynCardnum(dto, country);

        if (Objects.nonNull(res)) {
            log.info("Request UUID: {}, FeeWaiver found for CardNum: {}", uuid, dto.getCardNum());
            return ResponseEntity.ok().body(res);
        }

        log.warn("Request UUID: {}, No FeeWaiver found for CardNum: {}", uuid, dto.getCardNum());
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/updateFeeWaiver")
    public ResponseEntity<StatusDto> updateFeeWaiver(
            @RequestHeader("country") String country,
            @RequestHeader("uuid") String uuid,
            @RequestBody FeeWaiverDto dto) {

        log.info("Request UUID: {}, Update FeeWaiver for CardNum: {}", uuid, dto.getCardNum());

        StatusDto statusDto = new StatusDto();
        Boolean updated = feeWaiverServiceSelector.updateFeeWaiver(dto, country);

        if (updated) {
            statusDto.setStatus("Success");
            log.info("Request UUID: {}, FeeWaiver update successful for CardNum: {}", uuid, dto.getCardNum());
            return ResponseEntity.ok().body(statusDto);
        }

        statusDto.setStatus("Failure");
        log.warn("Request UUID: {}, FeeWaiver update failed for CardNum: {}", uuid, dto.getCardNum());
        return ResponseEntity.ok().body(statusDto);
    }
}

Updated FeeWaiverServiceSelector

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class FeeWaiverServiceSelector {

    private final Map<String, FeeWaiverService> feeWaiverServiceMap;

    @Autowired
    public FeeWaiverServiceSelector(List<FeeWaiverService> feeWaiverServiceList) {
        this.feeWaiverServiceMap = feeWaiverServiceList.stream()
                .collect(Collectors.toMap(FeeWaiverService::getCountryCode, Function.identity()));
    }

    public FeeWaiverDto findBynCardnum(FeeWaiverDto dto, String countryCode) {
        log.info("Finding FeeWaiver for CardNum: {}, Country: {}", dto.getCardNum(), countryCode);
        return feeWaiverServiceMap.get(countryCode).findBynCardnum(dto);
    }

    public Boolean updateFeeWaiver(FeeWaiverDto dto, String countryCode) {
        log.info("Updating FeeWaiver for CardNum: {}, Country: {}", dto.getCardNum(), countryCode);
        return feeWaiverServiceMap.get(countryCode).updateFeeWaiver(dto);
    }
}

Updated FeeWaiverServiceImpl

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class FeeWaiverServiceImpl implements FeeWaiverService {

    private final String countryCode = "SG";

    @Autowired
    private FeeWaiverRepository feeWaiverRepository;

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public FeeWaiverDto findBynCardnum(FeeWaiverDto reqDto) {
        String cardNum = reqDto.getCardNum();
        log.info("Finding FeeWaiver in repository for CardNum: {}", cardNum);

        Optional<FeeWaiver> feeWaiver = feeWaiverRepository.findById(cardNum);

        if (feeWaiver.isPresent()) {
            FeeWaiver dbFeeWaiver = feeWaiver.get();
            FeeWaiverDto dto = new FeeWaiverDto();
            dto.setAnnualFeeEligible(dbFeeWaiver.getNAnnualFeeEligible());
            dto.setCardNum(dbFeeWaiver.getNCardnum());
            dto.setAnnualFeeRequested(dbFeeWaiver.getNAnnualFeeRequested());
            dto.setLateFeeRequested(dbFeeWaiver.getFLateFeeRequested());
            dto.setLateFeeRequestedDate(dbFeeWaiver.getDLateFeeReqDate());
            dto.setLateFeeEligible(dbFeeWaiver.getFLateFeeEligible());
            dto.setAnnualFeeRequestedDate(dbFeeWaiver.getDAnnualFeeReqDate());

            log.info("FeeWaiver found for CardNum: {}", cardNum);
            return dto;
        }

        log.warn("No FeeWaiver found for CardNum: {}", cardNum);
        return null;
    }

    @Override
    @Transactional
    public Boolean updateFeeWaiver(FeeWaiverDto dto) {
        String cardNum = dto.getCardNum();
        log.info("Updating FeeWaiver in repository for CardNum: {}", cardNum);

        Optional<FeeWaiver> feeWaiver = feeWaiverRepository.findById(cardNum);
        if (feeWaiver.isPresent()) {
            FeeWaiver dbFeeWaiver = feeWaiver.get();
            dbFeeWaiver.setNAnnualFeeRequested(dto.getAnnualFeeRequested());
            dbFeeWaiver.setDAnnualFeeReqDate(dto.getAnnualFeeRequestedDate());
            dbFeeWaiver.setFLateFeeRequested(dto.getLateFeeRequested());
            dbFeeWaiver.setDLateFeeReqDate(dto.getLateFeeRequestedDate());

            FeeWaiver savedFeeWaiver = feeWaiverRepository.save(dbFeeWaiver);
            log.info("FeeWaiver updated successfully for CardNum: {}", cardNum);
            return Objects.nonNull(savedFeeWaiver);
        }

        log.warn("FeeWaiver update failed, no FeeWaiver found for CardNum: {}", cardNum);
        return false;
    }
}

Global Exception Handling (@ControllerAdvice)

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, @RequestHeader("uuid") String uuid) {
        // Log the error with UUID
        log.error("Request UUID: {}, Error: {}", uuid, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred, please check the logs with UUID: " + uuid);
    }
}

Summary of Changes:

1. Logging: Added logging using Slf4j in all methods for request, response, and exception handling.


2. UUID: A UUID is passed in each request's header for tracking and logged in each method.


3. Error Handling: A global exception handler logs any errors and sends a user-friendly response with the UUID.



This should help with tracing requests, debugging errors, and improving observability of your API.
