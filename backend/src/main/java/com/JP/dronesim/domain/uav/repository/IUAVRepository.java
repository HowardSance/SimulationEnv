package com.JP.dronesim.domain.uav.repository;

import com.JP.dronesim.domain.uav.model.UAV;
import java.util.List;
import java.util.Optional;

/**
 * 无人机仓储接口
 * 定义无人机聚合的持久化契约
 *
 * @author JP Team
 * @version 1.0
 */
public interface IUAVRepository {

    /**
     * 保存无人机
     *
     * @param uav 无人机实体
     * @return 保存后的无人机实体
     */
    UAV save(UAV uav);

    /**
     * 根据ID查找无人机
     *
     * @param id 无人机ID
     * @return 无人机实体，如果不存在则返回空
     */
    Optional<UAV> findById(String id);

    /**
     * 查找所有无人机
     *
     * @return 所有无人机列表
     */
    List<UAV> findAll();

    /**
     * 删除无人机
     *
     * @param id 无人机ID
     * @return 是否删除成功
     */
    boolean deleteById(String id);

    /**
     * 检查无人机是否存在
     *
     * @param id 无人机ID
     * @return 是否存在
     */
    boolean existsById(String id);
}