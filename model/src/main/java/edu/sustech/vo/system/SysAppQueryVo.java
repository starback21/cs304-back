package edu.sustech.vo.system;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysAppQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fundName;
    private Long fundId;
    private String group;
    private String startDate;
    private String endDate;
    private Long startValue;
    private Long endValue;
    private String category;
    private String ascend;
    private String sortBy;
}
