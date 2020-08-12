package com.jm.api.manage.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class ZjyiotR implements Serializable {
    private static final long serialVersionUID = -7611550613176896983L;

    private String status;
    private String code;
    private String tip;
    private String data;
}
