package com.mmm.clout.userservice.clouter.infrastructure.persistence;

import com.mmm.clout.userservice.clouter.domain.Clouter;
import com.mmm.clout.userservice.clouter.domain.exceptuion.NotFoundClouter;
import com.mmm.clout.userservice.clouter.domain.repository.ClouterRepository;
import com.mmm.clout.userservice.clouter.infrastructure.persistence.jpa.JpaClouterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClouterRepositoryAdapter implements ClouterRepository {

    private final JpaClouterRepository jpaClouterRepository;

    @Override
    public Clouter save(Clouter clouter) {
        return jpaClouterRepository.save(clouter);
    }

    @Override
    public Clouter findById(Long userId) {
        return jpaClouterRepository.findById(userId).orElseThrow(
            () -> new NotFoundClouter()
        );
    }
}