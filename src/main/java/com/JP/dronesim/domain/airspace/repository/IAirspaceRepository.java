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
     * 保存唯一空域
     * @param airspace 空域实体
     * @return 保存后的空域实体
     */
    Airspace save(Airspace airspace);

    /**
     * 获取唯一空域
     * @return 空域实体（如不存在返回Optional.empty）
     */
    Optional<Airspace> find();
}