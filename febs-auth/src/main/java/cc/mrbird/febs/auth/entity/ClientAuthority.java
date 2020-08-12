package cc.mrbird.febs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@TableName("ei_client_authority")
@Data
public class ClientAuthority implements Serializable {
    private static final long serialVersionUID = 5951614708637854731L;

    private String clientId;

    private String authorityId;
}