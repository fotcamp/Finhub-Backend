package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        this.createdTime = this.createdTime.withNano(0);
        this.modifiedTime = this.modifiedTime.withNano(0);
    }
}
