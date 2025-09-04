package com.wx2.user.model.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UserProfileQuery {
    @NotBlank(message = "昵称不能为空")
    @Length(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    private String avatar;
    @Min(value = 0, message = "性别参数无效")
    @Max(value = 2, message = "性别参数无效")
    private Integer gender;
    private LocalDate birthday;
    private String targetSchool;
    private String targetMajor;
    private Integer examYear;
    @Min(value = 0, message = "备考阶段参数无效")
    @Max(value = 2, message = "备考阶段参数无效")
    private String studyStage;
    private String signature;
}
