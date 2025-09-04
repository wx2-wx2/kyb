package com.wx2.common.model.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollegePageQuery extends PageQuery {
    private String province;
    private String city;
    @Min(value = 1, message = "院校层次取值范围为1-4")
    @Max(value = 4, message = "院校层次取值范围为1-4")
    private Integer level;
    @Min(value = 1, message = "院校类型取值范围为1-6")
    @Max(value = 6, message = "院校类型取值范围为1-6")
    private Integer type;
    private String majorName;
    @Min(value = 1, message = "学位类型取值范围为1-2")
    @Max(value = 2, message = "学位类型取值范围为1-2")
    private Integer degreeType;
}
