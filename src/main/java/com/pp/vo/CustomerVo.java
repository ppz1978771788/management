package com.pp.vo;

import com.pp.pojo.BusCustomer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerVo extends BusCustomer {
    private static final long serialVersionUID = 1L;
    private Integer limit=10;
    private Long count;
    private Integer page=1;
    private Integer[] ids;
}
