package com.mmm.clout.advertisementservice.apply.domain.repository;

import com.mmm.clout.advertisementservice.advertisements.domain.Campaign;
import com.mmm.clout.advertisementservice.apply.domain.Apply;
import com.mmm.clout.advertisementservice.apply.domain.Apply.ApplyStatus;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyRepository {

    Apply save(Apply apply);

    Optional<Apply> findById(Long applyId);

    boolean checkApplyExists(Campaign campaign, Long clouterId);

    List<Apply> getAllByStatus(Pageable pageable, Long applicantId, ApplyStatus applyStatus);

    List<Apply> findApplicantList(Long advertisementId);

    List<Apply> findByCampaign(Campaign campaign);

    JPAQuery<Long> countByStatus(Long applicantId, ApplyStatus applyStatus);
}
