package com.capstone.project.Bean.Holders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnData {
    private Integer code;
    private String message;
    private Object object;
}
