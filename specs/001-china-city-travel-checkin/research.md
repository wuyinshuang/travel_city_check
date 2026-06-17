# Research: China City Travel Check-in Application

**Date**: 2026-06-14

**Purpose**: Resolve technical unknowns and document best practices for the implementation.

## 1. Map Visualization Technology

### Decision: ECharts with GeoJSON

**Rationale**: 
- ECharts 是百度开源的可视化库，对中国地图有原生支持
- 内置省份地图数据，支持省市级地图下钻
- 性能优秀，支持大数据量渲染
- 社区活跃，文档完善（中文文档）
- 支持响应式设计和触摸事件

**Alternatives Considered**:
| Option | Pros | Cons | Why Rejected |
|--------|------|------|--------------|
| Leaflet | 轻量级，插件丰富 | 需要额外配置中国地图数据 | 需要更多配置工作 |
| D3.js | 高度可定制 | 学习曲线陡峭，开发效率低 | 对于标准地图场景过于复杂 |
| Mapbox | 功能强大，地图美观 | 商业服务，需要API Key | 增加外部依赖和成本 |

**Implementation Notes**:
- 使用 ECharts 的 `registerMap` API 注册省份地图
- 地图数据使用 GeoJSON 格式存储在 `frontend/src/assets/map-data/`
- 城市点击事件通过 `geo` 组件的 `onclick` 事件处理

## 2. Authentication Strategy

### Decision: Session-based Authentication with Spring Security

**Rationale**:
- 简单可靠，适合单体应用
- Spring Security 原生支持
- 会话存储在服务器端，安全性高
- 支持会话超时配置（30分钟）

**Alternatives Considered**:
| Option | Pros | Cons | Why Rejected |
|--------|------|------|--------------|
| JWT | 无状态，适合分布式 | 需要前端存储Token，安全性较低 | 单体应用不需要JWT的复杂性 |
| OAuth2 | 支持第三方登录 | 配置复杂，需要外部服务 | 用户需求仅用户名密码登录 |

**Implementation Notes**:
- 使用 `HttpSession` 管理会话
- 密码使用 BCrypt 哈希存储
- 会话超时配置：`server.servlet.session.timeout=30m`
- 前端使用 Axios 拦截器处理401响应

## 3. Image Storage Strategy

### Decision: File System Storage with Database References

**Rationale**:
- 简单直接，无需额外服务
- 成本低，适合中小规模应用
- 便于备份和迁移
- 符合用户需求（不使用云存储）

**Alternatives Considered**:
| Option | Pros | Cons | Why Rejected |
|--------|------|------|--------------|
| Cloud Storage (OSS/S3) | 可扩展，CDN加速 | 需要额外配置和费用 | 用户需求明确使用服务器文件系统 |
| Database BLOB | 事务一致性 | 性能差，数据库膨胀 | 不适合存储大量图片 |

**Implementation Notes**:
- 图片存储路径：`/var/www/travelcity/uploads/{userId}/{noteId}/`
- 文件命名：UUID + 原始扩展名
- 数据库存储相对路径
- 文件大小限制：10MB
- 支持的格式：jpg, jpeg, png, gif, webp

## 4. Database Schema Design

### Decision: Normalized Relational Schema

**Rationale**:
- 符合MySQL关系型数据库特性
- 数据一致性有保障
- 支持复杂查询（如统计用户打卡数量）
- 便于扩展

**Key Tables**:
- `users` - 用户信息
- `provinces` - 省份数据（预置）
- `cities` - 城市数据（预置）
- `checkins` - 打卡记录
- `notes` - 备注记录
- `images` - 图片记录

**Implementation Notes**:
- 使用 Spring Data JPA 自动生成表结构
- 添加适当的索引优化查询性能
- 使用外键约束保证数据完整性

## 5. Frontend State Management

### Decision: Pinia with Vue 3 Composition API

**Rationale**:
- Vue 3 官方推荐的状态管理库
- 支持 TypeScript
- 开发工具支持完善
- 模块化设计，便于代码组织

**Store Modules**:
- `userStore` - 用户登录状态、用户信息
- `mapStore` - 当前省份、城市数据
- `checkinStore` - 打卡记录缓存
- `noteStore` - 备注数据缓存

**Implementation Notes**:
- 使用 `pinia-plugin-persistedstate` 持久化关键状态
- API 调用封装在 store actions 中
- 组件通过 `storeToRefs` 响应式访问状态

## 6. API Response Format

### Decision: Unified JSON Response Structure

**Format**:
```json
{
  "code": 200,
  "message": "Success",
  "data": { ... },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Error Response**:
```json
{
  "code": 400,
  "message": "Invalid request parameters",
  "errors": ["Image size exceeds 10MB limit"],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Rationale**:
- 统一格式便于前端处理
- 包含时间戳便于调试
- 错误信息数组支持多条错误

## 7. Progressive Enhancement Strategy

### Decision: Core Functionality First, Enhanced Features Second

**Implementation Layers**:
1. **Base Layer (所有浏览器支持)**:
   - HTML 表单提交
   - 基本的地图显示（静态图片）
   - 文字备注功能

2. **Enhanced Layer (现代浏览器)**:
   - Vue SPA 单页应用
   - 交互式地图（ECharts）
   - 图片上传预览
   - 拖拽排序

3. **Optimal Layer (高性能浏览器)**:
   - Service Worker 离线缓存
   - 图片懒加载
   - 虚拟滚动

**Implementation Notes**:
- 使用 `<noscript>` 提供基础内容
- 检测浏览器能力渐进加载功能
- 核心功能不依赖 JavaScript

## 8. Responsive Design Strategy

### Decision: Mobile-First with CSS Grid/Flexbox

**Breakpoints**:
- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px

**Implementation Notes**:
- 使用 CSS Grid 布局地图区域
- 移动端：全屏地图，底部操作栏
- 桌面端：左侧地图，右侧详情面板
- 触摸事件和鼠标事件统一处理

## Summary

All technical decisions have been made with clear rationale and documented alternatives. The implementation can proceed to Phase 1 (Design & Contracts) with confidence.
