package com.JP.dronesim.domain.airspace.repository;

import com.JP.dronesim.domain.airspace.model.Airspace;
import java.util.List;
import java.util.Optional;

/**
 * 空域仓储接口
 * 定义空域聚合的持久化契约
 *
 * @author JP Team
 * @version 1.0
 */
public interface IAirspaceRepository {
    /**
     * 保存空域
     *
     * @param airspace 空域实体
     * @return 保存后的空域实体
     */
    Airspace save(Airspace airspace);

    /**
     * 根据ID查找空域
     *
     * @param id 空域ID
     * @return 空域实体，如果不存在则返回空
     */
    Optional<Airspace> findById(String id);

    /**
     * 查找所有空域
     *
     * @return 所有空域列表
     */
    List<Airspace> findAll();

    /**
     * 删除空域
     *
     * @param id 空域ID
     * @return 是否删除成功
     */
    boolean deleteById(String id);

    /**
     * 检查空域是否存在
     *
     * @param id 空域ID
     * @return 是否存在
     */
    boolean existsById(String id);
}