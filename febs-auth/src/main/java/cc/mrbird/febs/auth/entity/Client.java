package cc.mrbird.febs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author duzou
 */
@TableName("iot_client")
@Data
public class Client implements Serializable {

    private static final long serialVersionUID = -7642811803741165785L;

    /**
     * 禁用
     */
    public static final String NO = "0";

    /**
     * 启用
     */
    public static final String YES = "1";

    /** 用户 ID */
    private String clientId;

    /** 用户名 */
    private String clientName;

    /**
     * 账号
     */
    private String clientAccount;

    /**
     * security 密码
     */
    private String securityPassword;

    /**
     * 状态 0锁定 1有效
     */
    private String clientStatus;

}
