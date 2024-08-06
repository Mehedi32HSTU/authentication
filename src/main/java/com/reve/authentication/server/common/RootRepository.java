package com.reve.authentication.server.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface RootRepository<T extends Root, E extends Serializable> extends JpaRepository<T, E>, JpaSpecificationExecutor<T> {
    public Optional<T> findByIdAndIsDeleted(Long id, Boolean isDeleted);
    public List<T> findAllByIsDeleted(Boolean isDeleted);
    public List<T> findAllByIdInAndIsDeleted(Collection<Long> allIdList, Boolean isDeleted);
    boolean existsBy();
}
