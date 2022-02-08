package entity;

import db.entity.H2Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memo")
public class MemoEntity extends H2Entity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String contents;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(referencedColumnName = "userId")
    private UserEntity userEntity;

    public MemoEntity() {
    }

    public MemoEntity(String name, String contents, LocalDateTime createdAt, UserEntity userEntity) {
        this.name = name;
        this.contents = contents;
        this.createdAt = createdAt;
        this.userEntity = userEntity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    @Override
    public String toString() {
        return "MemoEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", userEntity=" + userEntity +
                '}';
    }
}
