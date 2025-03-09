package devblackholemax.easychattingroom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @JsonFormat(pattern = "yyyy/M/d HH:mm:ss")
    @Column(updatable = false) // 禁止更新
    private LocalDateTime createTime;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now(); // 自动设置当前时间
    }
}