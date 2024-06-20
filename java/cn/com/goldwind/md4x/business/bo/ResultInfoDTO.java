package cn.com.goldwind.md4x.business.bo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @ClassName: ResultInfoDTO
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 2, 2020 7:59:26 PM
 *
 * @param <T>
 */
@ApiModel("返回结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
@Scope("prototype")
@Data
public class ResultInfoDTO<T> {

	public static final String OK = "0";
	public static final String FAILED = "-1";
	public static final String LOGIN_SUCCESS = "1";
	public static final String ERROR = "999";

	@ApiModelProperty(value = "返回code，正常为了0，异常为异常code编码")
	private String code;

	@ApiModelProperty(value = "返回消息，正常为空，异常为异常信息")
	private String message;

	@ApiModelProperty(value = "数据结果")
	private T data;

}
