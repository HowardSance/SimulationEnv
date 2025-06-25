# IDE热键配置指南

## Ctrl+鼠标左键跳转功能修复

### 问题描述
Ctrl+鼠标左键方法跳转功能不起作用，可能是热键冲突导致的。

### 解决步骤

#### 1. 检查当前热键设置
1. 打开IDE (IntelliJ IDEA/Cursor)
2. 进入 `File` → `Settings` (Windows) 或 `IntelliJ IDEA` → `Preferences` (Mac)
3. 导航到 `Keymap`
4. 搜索以下功能并检查快捷键：
   - "Go to Declaration or Usages"
   - "Navigate" → "Declaration or Usages"
   - "Go to Implementation(s)"

#### 2. 重置热键设置
1. 在Keymap设置页面
2. 点击右上角的齿轮图标
3. 选择 "Reset to Default" 或 "Restore Defaults"
4. 重启IDE

#### 3. 手动设置热键
如果重置后仍有问题，可以手动设置：
1. 在Keymap中搜索 "Go to Declaration or Usages"
2. 右键点击该功能
3. 选择 "Add Mouse Shortcut"
4. 按住Ctrl键并点击鼠标左键
5. 点击OK保存

#### 4. 检查系统热键冲突
常见冲突源：
- 浏览器扩展程序
- 系统工具软件
- 其他开发工具
- 鼠标驱动程序

#### 5. 替代快捷键
如果Ctrl+Click仍有问题，可以使用以下替代快捷键：
- `Ctrl + B` - 跳转到声明
- `Ctrl + Alt + B` - 跳转到实现
- `Ctrl + Shift + B` - 跳转到类型声明

#### 6. 验证功能
1. 打开任意Java文件
2. 找到方法调用或类引用
3. 使用设置的热键进行跳转测试

### 注意事项
- 确保项目已正确索引
- 检查Java插件是否已启用
- 确保源代码路径配置正确 