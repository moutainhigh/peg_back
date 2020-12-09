package com.kbopark.operation.apidto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReviewLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("状态")
	private String statusName;

	@ApiModelProperty("时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm"
	)
	private LocalDateTime time;

	@ApiModelProperty("操作人")
	private String operatorName;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("是不是应该展示备注，如果为true则展示备注，否则不展示备注")
	private Boolean showRemark;

	@ApiModelProperty("审核是否被驳回，如果审核驳回，备注的颜色是红色的，否则就是灰色的")
	private Boolean isFail;
}
