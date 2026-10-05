package com.springfw03_jpa_talktalk.todo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "todo")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private Long id;
    // 할일 번호
    private String title;
    // 할일
    private int done; // 완료 여부

    public void setDone(int done){
        this.done = done;
    }
}
