package com.example.springfw01;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {  // 다른 곳에서 getter setter 가능
    private String id;
    private String parentId;
    private String name;
    private Long depthLevel;
    private Long seq;
    private String userYn;
}
