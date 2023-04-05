package edu.sustech.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课题组
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课题组id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课题组名称
     */
    private String groupName;

    /**
     * 课题组编码
     */
    private String groupCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    private Integer isDeleted;


}
