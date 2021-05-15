package com.mq.common.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MQObj<T> implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * data
     */
    private T data;

    /**
     * create time
     */
    private LocalDateTime createTime;

}
