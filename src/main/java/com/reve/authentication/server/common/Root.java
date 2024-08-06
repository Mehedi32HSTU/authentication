package com.reve.authentication.server.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Root {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    private Long createdBy;

    @CreatedDate
    private Long creationDate;

    @LastModifiedBy
    private Long lastModifiedBy;

    @LastModifiedDate
    private Long lastModificationDate;

    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return Objects.isNull(createdBy) ? 0L : createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = Objects.isNull(createdBy) ? 0L : createdBy;
    }

    public Long getCreationDate() {
        return Objects.isNull(creationDate) ? 0L : creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = Objects.isNull(creationDate) ? 0L : creationDate;
    }

    public Long getLastModifiedBy() {
        return Objects.isNull(lastModifiedBy) ? 0L : lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = Objects.isNull(lastModifiedBy) ? 0L : lastModifiedBy;
    }

    public Long getLastModificationDate() {
        return Objects.isNull(lastModificationDate) ? 0L : lastModificationDate;
    }

    public void setLastModificationDate(Long lastModifiedDate) {
        this.lastModificationDate = Objects.isNull(lastModifiedDate) ? 0L : lastModifiedDate;
    }

    public Boolean getIsDeleted() {
        return Objects.isNull(isDeleted) ? false : isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = Objects.isNull(deleted) ? false : deleted;
    }
}
