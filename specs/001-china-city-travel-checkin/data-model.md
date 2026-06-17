# Data Model: China City Travel Check-in Application

**Date**: 2026-06-14

**Purpose**: Define the database schema, entities, and relationships for the application.

## Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    User     │       │   Province  │       │    City     │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)     │       │ id (PK)     │
│ username    │       │ name        │       │ name        │
│ password    │       │ code        │       │ province_id │
│ created_at  │       │ geojson     │       │ code        │
│ last_login  │       └─────────────┘       │ geojson     │
└─────────────┘                             └─────────────┘
       │                                           │
       │                                           │
       ▼                                           ▼
┌─────────────┐                             ┌─────────────┐
│  Checkin    │                             │    Note     │
├─────────────┤                             ├─────────────┤
│ id (PK)     │                             │ id (PK)     │
│ user_id(FK) │                             │ user_id(FK) │
│ city_id(FK) │                             │ city_id(FK) │
│ created_at  │                             │ content     │
└─────────────┘                             │ created_at  │
                                            │ updated_at  │
                                            └─────────────┘
                                                   │
                                                   ▼
                                            ┌─────────────┐
                                            │    Image    │
                                            ├─────────────┤
                                            │ id (PK)     │
                                            │ note_id(FK) │
                                            │ file_path   │
                                            │ file_name   │
                                            │ file_size   │
                                            │ created_at  │
                                            └─────────────┘
```

## Entity Definitions

### 1. User (用户)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 用户唯一标识 |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码哈希值 (BCrypt) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 注册时间 |
| last_login | DATETIME | | 最后登录时间 |

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE INDEX idx_username (username)

**Validation Rules**:
- username: 3-50字符，仅允许字母、数字、下划线
- password: 最少6个字符

### 2. Province (省份)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 省份唯一标识 |
| name | VARCHAR(50) | NOT NULL | 省份名称 |
| code | VARCHAR(10) | UNIQUE, NOT NULL | 行政区划代码 |
| geojson | TEXT | NOT NULL | 地理边界数据 (GeoJSON) |

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE INDEX idx_code (code)

**Predefined Data**: 34个省级行政区（23省、5自治区、4直辖市、2特别行政区）

### 3. City (城市)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 城市唯一标识 |
| name | VARCHAR(50) | NOT NULL | 城市名称 |
| province_id | BIGINT | FK, NOT NULL | 所属省份ID |
| code | VARCHAR(10) | UNIQUE, NOT NULL | 行政区划代码 |
| geojson | TEXT | NOT NULL | 地理边界数据 (GeoJSON) |

**Indexes**:
- PRIMARY KEY (id)
- INDEX idx_province (province_id)
- UNIQUE INDEX idx_code (code)

**Foreign Keys**:
- FOREIGN KEY (province_id) REFERENCES provinces(id)

**Predefined Data**: 约340个地级行政区

### 4. Checkin (打卡记录)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 记录唯一标识 |
| user_id | BIGINT | FK, NOT NULL | 用户ID |
| city_id | BIGINT | FK, NOT NULL | 城市ID |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 打卡时间 |

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE INDEX idx_user_city (user_id, city_id) -- 确保每用户每城市仅一条打卡记录
- INDEX idx_user (user_id)
- INDEX idx_city (city_id)

**Foreign Keys**:
- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
- FOREIGN KEY (city_id) REFERENCES cities(id)

**Business Rules**:
- 每用户每城市只能有一条打卡记录
- 打卡后可取消（删除记录）

### 5. Note (备注)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 备注唯一标识 |
| user_id | BIGINT | FK, NOT NULL | 用户ID |
| city_id | BIGINT | FK, NOT NULL | 城市ID |
| content | TEXT | | 备注文字内容 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |
| updated_at | DATETIME | ON UPDATE NOW | 更新时间 |

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE INDEX idx_user_city (user_id, city_id) -- 确保每用户每城市仅一条备注
- INDEX idx_user (user_id)
- INDEX idx_city (city_id)

**Foreign Keys**:
- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
- FOREIGN KEY (city_id) REFERENCES cities(id)

**Business Rules**:
- 每用户每城市只能有一条备注
- 备注可以独立于打卡存在（用户可以先写备注再打卡）

### 6. Image (图片)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | 图片唯一标识 |
| note_id | BIGINT | FK, NOT NULL | 所属备注ID |
| file_path | VARCHAR(500) | NOT NULL | 文件存储路径 |
| file_name | VARCHAR(255) | NOT NULL | 原始文件名 |
| file_size | BIGINT | NOT NULL | 文件大小(字节) |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 上传时间 |

**Indexes**:
- PRIMARY KEY (id)
- INDEX idx_note (note_id)

**Foreign Keys**:
- FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE

**Business Rules**:
- 单张图片最大10MB
- 支持格式：jpg, jpeg, png, gif, webp
- 图片数量不限

## Data Volume Estimates

| Entity | Initial Count | Growth Rate |
|--------|--------------|-------------|
| User | 0 | +100/月 (预估) |
| Province | 34 | 固定 |
| City | ~340 | 固定 |
| Checkin | 0 | +1000/月 (预估) |
| Note | 0 | +500/月 (预估) |
| Image | 0 | +2000/月 (预估) |

## SQL Schema (MySQL 8.x)

```sql
-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login DATETIME,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 省份表
CREATE TABLE provinces (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    geojson TEXT NOT NULL,
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 城市表
CREATE TABLE cities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    province_id BIGINT NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    geojson TEXT NOT NULL,
    INDEX idx_province (province_id),
    INDEX idx_code (code),
    FOREIGN KEY (province_id) REFERENCES provinces(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 打卡记录表
CREATE TABLE checkins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_user_city (user_id, city_id),
    INDEX idx_user (user_id),
    INDEX idx_city (city_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 备注表
CREATE TABLE notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    content TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_user_city (user_id, city_id),
    INDEX idx_user (user_id),
    INDEX idx_city (city_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 图片表
CREATE TABLE images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_id BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_note (note_id),
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## JPA Entity Mappings

See implementation in `backend/src/main/java/com/travelcity/entity/`:
- `User.java`
- `Province.java`
- `City.java`
- `Checkin.java`
- `Note.java`
- `Image.java`
