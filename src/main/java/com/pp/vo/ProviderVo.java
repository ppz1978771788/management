package com.pp.vo;

import com.pp.pojo.BusProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderVo extends BusProvider {
    private static final long serialVersionUID = 1L;
    private Integer limit=10;
    private Long count;
    private Integer page=1;
    private Integer[] ids;


}
