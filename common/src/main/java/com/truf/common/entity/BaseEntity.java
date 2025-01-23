package com.truf.common.entity;

import com.truf.common.entity.listener.CommonEntityListener;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@lombok.Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({CommonEntityListener.class})
@SuperBuilder
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false)
    protected String uuid;

    @CreatedDate
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, insertable = true, updatable = true, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
