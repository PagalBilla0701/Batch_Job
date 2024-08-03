package com.alibou.batch.processor;

import com.alibou.batch.entity.CustIndicator;
import com.alibou.batch.repo.CustIndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KycIndicatorsProcessor implements ItemProcessor<CustIndicator, CustIndicator> {

    private final CustIndicatorRepository repository;

    @Override
    public CustIndicator process(CustIndicator item) throws Exception {
        Optional<CustIndicator> existing = repository.findByRelId(item.getRelId());

        if (existing.isPresent()) {
            CustIndicator existingIndicator = existing.get();
            existingIndicator.setFKycStatus(item.getFKycStatus());
            existingIndicator.setDUpd(new Date());
            existingIndicator.setXUpd("current_user");  // Replace with actual user info
            return existingIndicator;
        } else {
            item.setDCreat(new Date());
            item.setDUpd(new Date());
            item.setXCreat("current_user");  // Replace with actual user info
            item.setXUpd("current_user");    // Replace with actual user info
            item.setNKycStatusFileId(1);     // Static value for development
            return item;
        }
    }
}
