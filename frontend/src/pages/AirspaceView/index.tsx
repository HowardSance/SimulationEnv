/**
 * 空域视图页面
 * 整合3D场景、控制面板和部署功能
 */

import React, { useEffect, useState } from 'react';
import { Layout, Card, Button, Space, Drawer, message, Spin, Alert } from 'antd';
import { PlusOutlined, ReloadOutlined, SettingOutlined } from '@ant-design/icons';
import { useAppSelector, useAppDispatch } from '../../hooks/reduxHooks';
import { 
  fetchAirspace, 
  selectAirspace, 
  selectEntities,
  selectSelectedEntity,
  selectDeploymentMode,
  setDeploymentMode,
  resetAirspace
} from '../../store/slices/airspaceSlice';
import ThreeScene from './components/ThreeScene';
import DeploymentForm from './components/DeploymentForm';
import EntityList from './components/EntityList';
import EnvironmentPanel from './components/EnvironmentPanel';
import ControlPanel from './components/ControlPanel';
import styles from './index.module.css';
import RadarWindow from './components/RadarWindow';

const { Content, Sider } = Layout;

const AirspaceView: React.FC = () => {
  const dispatch = useAppDispatch();
  const airspace = useAppSelector(selectAirspace);
  const entities = useAppSelector(selectEntities);
  const selectedEntity = useAppSelector(selectSelectedEntity);
  const deploymentMode = useAppSelector(selectDeploymentMode);
  const loading = useAppSelector(state => state.airspace.loading);
  const error = useAppSelector(state => state.airspace.error);

  const [deploymentDrawerVisible, setDeploymentDrawerVisible] = useState(false);
  const [environmentDrawerVisible, setEnvironmentDrawerVisible] = useState(false);
  const [selectedDeviceType, setSelectedDeviceType] = useState<string>('RADAR');

  // 初始加载空域数据
  useEffect(() => {
    dispatch(fetchAirspace());
  }, [dispatch]);

  // 处理部署模式
  const handleStartDeployment = (deviceType: string) => {
    setSelectedDeviceType(deviceType);
    dispatch(setDeploymentMode({ 
      enabled: true, 
      deviceType: deviceType as any 
    }));
    setDeploymentDrawerVisible(false);
    message.info('请在3D场景中点击选择部署位置');
  };

  // 处理位置选择
  const handlePositionSelect = (position: any) => {
    if (deploymentMode.enabled && deploymentMode.position) {
      message.success(`已选择位置: X=${position.x.toFixed(1)}, Y=${position.y.toFixed(1)}, Z=${position.z.toFixed(1)}`);
    }
  };

  // 处理部署完成
  const handleDeploymentComplete = () => {
    dispatch(setDeploymentMode({ enabled: false }));
    message.success('设备部署成功');
  };

  // 处理取消部署
  const handleCancelDeployment = () => {
    dispatch(setDeploymentMode({ enabled: false }));
  };

  // 处理重置空域
  const handleResetAirspace = () => {
    dispatch(resetAirspace());
    message.success('空域已重置');
  };

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <Alert
          message="加载失败"
          description={error}
          type="error"
          showIcon
          action={
            <Button size="small" onClick={() => dispatch(fetchAirspace())}>
              重试
            </Button>
          }
        />
      </div>
    );
  }

  if (!airspace || loading) {
    return (
      <div className={styles.loadingContainer}>
        <Spin size="large" tip="加载空域数据中..." />
      </div>
    );
  }

  return (
    <Layout className={styles.layout}>
      <Content className={styles.content}>
        {/* 3D场景区域 */}
        <div className={styles.sceneContainer}>
          <ThreeScene
            boundaryMin={airspace.boundaryMin}
            boundaryMax={airspace.boundaryMax}
            entities={entities}
            selectedEntityId={selectedEntity?.id || null}
            deploymentMode={deploymentMode}
            onPositionSelect={handlePositionSelect}
          />
          
          {/* 场景控制按钮 */}
          <div className={styles.sceneControls}>
            <Space>
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={() => setDeploymentDrawerVisible(true)}
                disabled={deploymentMode.enabled}
              >
                部署设备
              </Button>
              <Button
                icon={<SettingOutlined />}
                onClick={() => setEnvironmentDrawerVisible(true)}
              >
                环境设置
              </Button>
              <Button
                icon={<ReloadOutlined />}
                onClick={handleResetAirspace}
              >
                重置空域
              </Button>
            </Space>
          </div>

          {/* 部署模式提示 */}
          {deploymentMode.enabled && (
            <div className={styles.deploymentHint}>
              <Card size="small">
                <Space>
                  <span>正在部署 {deploymentMode.deviceType} 设备，请在场景中点击选择位置</span>
                  <Button size="small" onClick={handleCancelDeployment}>
                    取消
                  </Button>
                </Space>
              </Card>
            </div>
          )}
        </div>

        {/* 控制面板 */}
        <div className={styles.controlPanel}>
          <ControlPanel />
        </div>
      </Content>

      {/* 右侧实体列表 */}
      <Sider width={300} className={styles.sider}>
        <EntityList 
          entities={entities}
          selectedEntityId={selectedEntity?.id}
        />
      </Sider>

      {/* 部署设备抽屉 */}
      <Drawer
        title="部署探测设备"
        placement="left"
        width={400}
        open={deploymentDrawerVisible}
        onClose={() => setDeploymentDrawerVisible(false)}
      >
        <DeploymentForm
          onStartDeployment={handleStartDeployment}
          deploymentPosition={deploymentMode.position}
          onDeploymentComplete={handleDeploymentComplete}
        />
      </Drawer>

      {/* 环境设置抽屉 */}
      <Drawer
        title="环境参数设置"
        placement="right"
        width={400}
        open={environmentDrawerVisible}
        onClose={() => setEnvironmentDrawerVisible(false)}
      >
        <EnvironmentPanel
          environmentParameters={airspace.environmentParameters}
          onClose={() => setEnvironmentDrawerVisible(false)}
        />
      </Drawer>
    </Layout>
  );
};

export default AirspaceView; 