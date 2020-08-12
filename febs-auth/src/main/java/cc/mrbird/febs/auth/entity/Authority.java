package cc.mrbird.febs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@TableName("ei_authority")
@Data
public class Authority implements Serializable {
    private static final long serialVersionUID = 47549918344528116L;


    @TableId(value = "authority_id",type = IdType.AUTO)
    private String authorityId;

    @Size(max = 20)
    @NotNull
    private String name;

    @Size(max = 30)
    @NotNull
    private String authority;

}
