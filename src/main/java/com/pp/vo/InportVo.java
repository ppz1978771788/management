package com.pp.vo;

import com.pp.pojo.BusInport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class InportVo extends BusInport {
    private static final long serialVersionUID = 1L;
    private Integer limit=10;
    private Long count;
    private Integer page=1;
    private Integer[] ids;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date EndTime;
}
