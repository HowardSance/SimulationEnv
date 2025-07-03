/**
 * 3D场景组件
 * 使用Three.js渲染空域、设备、无人机的3D视图
 */

import React, { useRef, useEffect, useState } from 'react';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { EntityStateDTO, Position } from '../../../types/api.types';
import { useAppSelector, useAppDispatch } from '../../../hooks/reduxHooks';
import { selectEntity, setDeploymentPosition } from '../../../store/slices/airspaceSlice';

interface ThreeSceneProps {
  boundaryMin: Position;
  boundaryMax: Position;
  entities: EntityStateDTO[];
  selectedEntityId: string | null;
  deploymentMode: {
    enabled: boolean;
    deviceType: string | null;
  };
  onPositionSelect?: (position: Position) => void;
}

const ThreeScene: React.FC<ThreeSceneProps> = ({
  boundaryMin,
  boundaryMax,
  entities,
  selectedEntityId,
  deploymentMode,
  onPositionSelect
}) => {
  const mountRef = useRef<HTMLDivElement>(null);
  const sceneRef = useRef<THREE.Scene>();
  const rendererRef = useRef<THREE.WebGLRenderer>();
  const cameraRef = useRef<THREE.PerspectiveCamera>();
  const controlsRef = useRef<OrbitControls>();
  const raycasterRef = useRef<THREE.Raycaster>(new THREE.Raycaster());
  const mouseRef = useRef<THREE.Vector2>(new THREE.Vector2());
  const entityMeshesRef = useRef<Map<string, THREE.Mesh>>(new Map());
  const groundRef = useRef<THREE.Mesh>();
  
  const dispatch = useAppDispatch();

  // 初始化场景
  useEffect(() => {
    if (!mountRef.current) return;

    // 创建场景
    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0x87CEEB); // 天空蓝
    scene.fog = new THREE.Fog(0x87CEEB, 1000, 10000);
    sceneRef.current = scene;

    // 创建渲染器
    const renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(mountRef.current.clientWidth, mountRef.current.clientHeight);
    renderer.shadowMap.enabled = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;
    mountRef.current.appendChild(renderer.domElement);
    rendererRef.current = renderer;

    // 创建相机
    const camera = new THREE.PerspectiveCamera(
      60,
      mountRef.current.clientWidth / mountRef.current.clientHeight,
      0.1,
      20000
    );
    camera.position.set(1000, 1000, 1000);
    cameraRef.current = camera;

    // 创建控制器
    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.05;
    controls.maxPolarAngle = Math.PI / 2 - 0.1;
    controlsRef.current = controls;

    // 添加光源
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.position.set(1000, 2000, 1000);
    directionalLight.castShadow = true;
    directionalLight.shadow.camera.left = -5000;
    directionalLight.shadow.camera.right = 5000;
    directionalLight.shadow.camera.top = 5000;
    directionalLight.shadow.camera.bottom = -5000;
    directionalLight.shadow.camera.near = 0.1;
    directionalLight.shadow.camera.far = 10000;
    scene.add(directionalLight);

    // 创建地面
    const groundWidth = boundaryMax.x - boundaryMin.x;
    const groundDepth = boundaryMax.z - boundaryMin.z;
    const groundGeometry = new THREE.PlaneGeometry(groundWidth, groundDepth, 100, 100);
    const groundMaterial = new THREE.MeshStandardMaterial({ 
      color: 0x3a7c3a,
      roughness: 0.8,
      metalness: 0.2
    });
    const ground = new THREE.Mesh(groundGeometry, groundMaterial);
    ground.rotation.x = -Math.PI / 2;
    ground.position.set(
      (boundaryMax.x + boundaryMin.x) / 2,
      0,
      (boundaryMax.z + boundaryMin.z) / 2
    );
    ground.receiveShadow = true;
    scene.add(ground);
    groundRef.current = ground;

    // 添加网格
    const gridHelper = new THREE.GridHelper(
      Math.max(groundWidth, groundDepth),
      50,
      0x000000,
      0x333333
    );
    gridHelper.position.y = 0.1;
    scene.add(gridHelper);

    // 动画循环
    const animate = () => {
      requestAnimationFrame(animate);
      controls.update();
      renderer.render(scene, camera);
    };
    animate();

    // 处理窗口大小变化
    const handleResize = () => {
      if (!mountRef.current) return;
      camera.aspect = mountRef.current.clientWidth / mountRef.current.clientHeight;
      camera.updateProjectionMatrix();
      renderer.setSize(mountRef.current.clientWidth, mountRef.current.clientHeight);
    };
    window.addEventListener('resize', handleResize);

    // 鼠标事件处理
    const handleMouseMove = (event: MouseEvent) => {
      if (!mountRef.current) return;
      const rect = mountRef.current.getBoundingClientRect();
      mouseRef.current.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
      mouseRef.current.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;
    };

    const handleClick = (event: MouseEvent) => {
      if (!sceneRef.current || !cameraRef.current) return;

      raycasterRef.current.setFromCamera(mouseRef.current, cameraRef.current);

      if (deploymentMode.enabled && groundRef.current) {
        // 部署模式：选择地面位置
        const intersects = raycasterRef.current.intersectObject(groundRef.current);
        if (intersects.length > 0) {
          const point = intersects[0].point;
          const position: Position = {
            x: point.x,
            y: 10, // 默认部署高度10米
            z: point.z
          };
          dispatch(setDeploymentPosition(position));
          if (onPositionSelect) {
            onPositionSelect(position);
          }
        }
      } else {
        // 选择模式：选择实体
        const meshes = Array.from(entityMeshesRef.current.values());
        const intersects = raycasterRef.current.intersectObjects(meshes);
        if (intersects.length > 0) {
          const mesh = intersects[0].object as THREE.Mesh;
          const entityId = mesh.userData.entityId;
          dispatch(selectEntity(entityId));
        } else {
          dispatch(selectEntity(null));
        }
      }
    };

    renderer.domElement.addEventListener('mousemove', handleMouseMove);
    renderer.domElement.addEventListener('click', handleClick);

    // 清理函数
    return () => {
      window.removeEventListener('resize', handleResize);
      renderer.domElement.removeEventListener('mousemove', handleMouseMove);
      renderer.domElement.removeEventListener('click', handleClick);
      mountRef.current?.removeChild(renderer.domElement);
      renderer.dispose();
    };
  }, [boundaryMin, boundaryMax, deploymentMode, dispatch, onPositionSelect]);

  // 更新实体
  useEffect(() => {
    if (!sceneRef.current) return;

    // 清除旧的实体
    entityMeshesRef.current.forEach((mesh, id) => {
      sceneRef.current!.remove(mesh);
      mesh.geometry.dispose();
      (mesh.material as THREE.Material).dispose();
    });
    entityMeshesRef.current.clear();

    // 添加新的实体
    entities.forEach(entity => {
      let geometry: THREE.BufferGeometry;
      let material: THREE.Material;
      let mesh: THREE.Mesh;

      // 根据类型创建不同的几何体
      switch (entity.type) {
        case 'RADAR':
          geometry = new THREE.ConeGeometry(20, 40, 8);
          material = new THREE.MeshStandardMaterial({ 
            color: 0x0000ff,
            emissive: 0x0000ff,
            emissiveIntensity: 0.2
          });
          mesh = new THREE.Mesh(geometry, material);
          mesh.rotation.x = Math.PI;
          break;

        case 'OPTICAL_CAMERA':
          geometry = new THREE.BoxGeometry(30, 20, 30);
          material = new THREE.MeshStandardMaterial({ 
            color: 0x00ff00,
            emissive: 0x00ff00,
            emissiveIntensity: 0.2
          });
          mesh = new THREE.Mesh(geometry, material);
          break;

        case 'RADIO_DETECTOR':
          geometry = new THREE.CylinderGeometry(15, 15, 40, 16);
          material = new THREE.MeshStandardMaterial({ 
            color: 0xff00ff,
            emissive: 0xff00ff,
            emissiveIntensity: 0.2
          });
          mesh = new THREE.Mesh(geometry, material);
          break;

        case 'UAV':
          geometry = new THREE.SphereGeometry(15, 16, 16);
          material = new THREE.MeshStandardMaterial({ 
            color: 0xff0000,
            emissive: 0xff0000,
            emissiveIntensity: 0.3
          });
          mesh = new THREE.Mesh(geometry, material);
          break;

        default:
          geometry = new THREE.BoxGeometry(20, 20, 20);
          material = new THREE.MeshStandardMaterial({ color: 0x808080 });
          mesh = new THREE.Mesh(geometry, material);
      }

      // 设置位置
      mesh.position.set(entity.position.x, entity.position.y, entity.position.z);
      mesh.castShadow = true;
      mesh.receiveShadow = true;
      mesh.userData.entityId = entity.id;

      // 如果是选中的实体，高亮显示
      if (entity.id === selectedEntityId) {
        (material as THREE.MeshStandardMaterial).emissiveIntensity = 0.5;
      }

      // 添加探测范围（仅设备）
      if (['RADAR', 'OPTICAL_CAMERA', 'RADIO_DETECTOR'].includes(entity.type) && 
          entity.properties?.detectionRange) {
        const rangeGeometry = new THREE.RingGeometry(
          0,
          entity.properties.detectionRange,
          32
        );
        const rangeMaterial = new THREE.MeshBasicMaterial({ 
          color: material.color,
          opacity: 0.1,
          transparent: true,
          side: THREE.DoubleSide
        });
        const rangeMesh = new THREE.Mesh(rangeGeometry, rangeMaterial);
        rangeMesh.rotation.x = -Math.PI / 2;
        rangeMesh.position.copy(mesh.position);
        rangeMesh.position.y = 1;
        sceneRef.current!.add(rangeMesh);
      }

      sceneRef.current!.add(mesh);
      entityMeshesRef.current.set(entity.id, mesh);
    });
  }, [entities, selectedEntityId]);

  return (
    <div 
      ref={mountRef} 
      style={{ 
        width: '100%', 
        height: '100%',
        cursor: deploymentMode.enabled ? 'crosshair' : 'grab'
      }} 
    />
  );
};

export default ThreeScene; 