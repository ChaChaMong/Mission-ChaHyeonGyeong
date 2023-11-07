package com.ll.domain;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Quotation {
    @Expose
    private long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    @Expose
    private String content;
    @Expose
    private String authorName;

    public Quotation(String content, String authorName) {
        this.content = content;
        this.authorName = authorName;
    }
}
