# Tasks: China City Travel Check-in Application

**Feature**: China City Travel Check-in Application  
**Branch**: `001-china-city-travel-checkin`  
**Date**: 2026-06-14  
**Based On**: [spec.md](./spec.md), [plan.md](./plan.md), [data-model.md](./data-model.md), [contracts/api.md](./contracts/api.md)

---

## Implementation Strategy

采用**分层迭代**方式实现：
1. **Phase 1 (Setup)**: 项目初始化，创建基础设施
2. **Phase 2 (Foundational)**: 核心基础设施，安全配置
3. **Phase 3-5 (User Stories)**: 按优先级实现用户故事
4. **Phase 6 (Polish)**: 完善和优化

**并行机会**: 前后端可并行开发，测试可与实现并行

---

## Phase 1: Setup (项目初始化)

| Task | Description |
|------|-------------|
| - [ ] T001 | Create backend Maven project structure with pom.xml |
| - [ ] T002 | Create frontend Vue project structure with package.json |
| - [ ] T003 [P] | Create database initialization script (schema + province/city data) |
| - [ ] T004 [P] | Configure application.yml with MySQL connection |
| - [ ] T005 [P] | Configure Vite and TypeScript for frontend |

---

## Phase 2: Foundational (基础架构)

| Task | Description |
|------|-------------|
| - [ ] T006 | Configure Spring Security with session-based authentication |
| - [ ] T007 | Implement response wrapper for unified API format |
| - [ ] T008 | Create global exception handler |
| - [ ] T009 [P] | Configure Vue Router with route guards |
| - [ ] T010 [P] | Configure Pinia store with persisted state |

---

## Phase 3: User Story 1 - Province Navigation (P1)

**Story Goal**: 用户可以从全国地图导航到省份详细地图

**Independent Test**: 打开应用→点击省份→验证跳转→返回全国地图

### Backend Tasks

| Task | Description |
|------|-------------|
| - [ ] T011 [US1] | Create Province entity in `backend/src/main/java/com/travelcity/entity/Province.java` |
| - [ ] T012 [US1] | Create City entity in `backend/src/main/java/com/travelcity/entity/City.java` |
| - [ ] T013 [US1] | Create ProvinceRepository in `backend/src/main/java/com/travelcity/repository/ProvinceRepository.java` |
| - [ ] T014 [US1] | Create CityRepository in `backend/src/main/java/com/travelcity/repository/CityRepository.java` |
| - [ ] T015 [US1] | Create ProvinceService in `backend/src/main/java/com/travelcity/service/ProvinceService.java` |
| - [ ] T016 [US1] | Create MapController in `backend/src/main/java/com/travelcity/controller/MapController.java` |
| - [ ] T017 [US1] | Implement GET /provinces endpoint |
| - [ ] T018 [US1] | Implement GET /provinces/{id}/geojson endpoint |
| - [ ] T019 [US1] | Implement GET /provinces/{id}/cities endpoint |

### Frontend Tasks

| Task | Description |
|------|-------------|
| - [ ] T020 [P] [US1] | Install ECharts and @types/echarts |
| - [ ] T021 [P] [US1] | Create NationalMap component in `frontend/src/components/map/NationalMap.vue` |
| - [ ] T022 [P] [US1] | Create ProvinceMap component in `frontend/src/components/map/ProvinceMap.vue` |
| - [ ] T023 [P] [US1] | Create Home view in `frontend/src/views/Home.vue` |
| - [ ] T024 [P] [US1] | Create Province view in `frontend/src/views/Province.vue` |
| - [ ] T025 [US1] | Create mapService in `frontend/src/services/mapService.ts` |
| - [ ] T026 [US1] | Integrate map components with router navigation |

---

## Phase 4: User Story 4 - Multi-user Data Isolation (P1)

**Story Goal**: 用户可以注册登录，数据相互隔离

**Independent Test**: 注册用户→登录→验证会话→切换用户验证数据隔离

### Backend Tasks

| Task | Description |
|------|-------------|
| - [ ] T027 [US4] | Create User entity in `backend/src/main/java/com/travelcity/entity/User.java` |
| - [ ] T028 [US4] | Create UserRepository in `backend/src/main/java/com/travelcity/repository/UserRepository.java` |
| - [ ] T029 [US4] | Create UserService in `backend/src/main/java/com/travelcity/service/UserService.java` |
| - [ ] T030 [US4] | Create AuthController in `backend/src/main/java/com/travelcity/controller/AuthController.java` |
| - [ ] T031 [US4] | Implement POST /auth/register endpoint with BCrypt password hashing |
| - [ ] T032 [US4] | Implement POST /auth/login endpoint |
| - [ ] T033 [US4] | Implement POST /auth/logout endpoint |
| - [ ] T034 [US4] | Implement GET /auth/me endpoint |

### Frontend Tasks

| Task | Description |
|------|-------------|
| - [ ] T035 [P] [US4] | Create Login view in `frontend/src/views/Login.vue` |
| - [ ] T036 [P] [US4] | Create userStore in `frontend/src/stores/userStore.ts` |
| - [ ] T037 [P] [US4] | Create authService in `frontend/src/services/authService.ts` |
| - [ ] T038 [US4] | Add route guards for protected routes |
| - [ ] T039 [US4] | Integrate Axios interceptor for session handling |

---

## Phase 5: User Story 2 - City Check-in (P1)

**Story Goal**: 用户可以点击城市进行打卡，打卡后城市变为浅蓝色

**Independent Test**: 进入省份地图→点击城市→验证颜色变化→刷新验证持久化

### Backend Tasks

| Task | Description |
|------|-------------|
| - [ ] T040 [US2] | Create Checkin entity in `backend/src/main/java/com/travelcity/entity/Checkin.java` |
| - [ ] T041 [US2] | Create CheckinRepository in `backend/src/main/java/com/travelcity/repository/CheckinRepository.java` |
| - [ ] T042 [US2] | Create CheckinService in `backend/src/main/java/com/travelcity/service/CheckinService.java` |
| - [ ] T043 [US2] | Create CheckinController in `backend/src/main/java/com/travelcity/controller/CheckinController.java` |
| - [ ] T044 [US2] | Implement GET /checkins endpoint (with user filtering) |
| - [ ] T045 [US2] | Implement POST /checkins endpoint |
| - [ ] T046 [US2] | Implement DELETE /checkins/{id} endpoint |

### Frontend Tasks

| Task | Description |
|------|-------------|
| - [ ] T047 [P] [US2] | Create checkinStore in `frontend/src/stores/checkinStore.ts` |
| - [ ] T048 [P] [US2] | Create checkinService in `frontend/src/services/checkinService.ts` |
| - [ ] T049 [US2] | Update ProvinceMap component with checkin functionality |
| - [ ] T050 [US2] | Implement city color change (white → light blue) on checkin |
| - [ ] T051 [US2] | Add checkin status persistence on page reload |

---

## Phase 6: User Story 3 - City Notes Management (P2)

**Story Goal**: 用户可以为城市添加备注（文字+图片）

**Independent Test**: 选择城市→添加文字备注→上传图片→验证保存→刷新验证持久化

### Backend Tasks

| Task | Description |
|------|-------------|
| - [ ] T052 [US3] | Create Note entity in `backend/src/main/java/com/travelcity/entity/Note.java` |
| - [ ] T053 [US3] | Create Image entity in `backend/src/main/java/com/travelcity/entity/Image.java` |
| - [ ] T054 [US3] | Create NoteRepository in `backend/src/main/java/com/travelcity/repository/NoteRepository.java` |
| - [ ] T055 [US3] | Create ImageRepository in `backend/src/main/java/com/travelcity/repository/ImageRepository.java` |
| - [ ] T056 [US3] | Create NoteService in `backend/src/main/java/com/travelcity/service/NoteService.java` |
| - [ ] T057 [US3] | Create NoteController in `backend/src/main/java/com/travelcity/controller/NoteController.java` |
| - [ ] T058 [US3] | Implement GET /notes and GET /notes/city/{cityId} endpoints |
| - [ ] T059 [US3] | Implement POST /notes and PUT /notes/{id} endpoints |
| - [ ] T060 [US3] | Implement DELETE /notes/{id} endpoint |
| - [ ] T061 [US3] | Implement POST /notes/{noteId}/images endpoint (file upload, 10MB limit) |
| - [ ] T062 [US3] | Implement DELETE /images/{id} endpoint |

### Frontend Tasks

| Task | Description |
|------|-------------|
| - [ ] T063 [P] [US3] | Create noteStore in `frontend/src/stores/noteStore.ts` |
| - [ ] T064 [P] [US3] | Create noteService in `frontend/src/services/noteService.ts` |
| - [ ] T065 [P] [US3] | Create NoteEditor component in `frontend/src/components/note/NoteEditor.vue` |
| - [ ] T066 [P] [US3] | Create ImageUploader component in `frontend/src/components/note/ImageUploader.vue` |
| - [ ] T067 [US3] | Integrate note editor into ProvinceMap component |
| - [ ] T068 [US3] | Implement image preview and gallery display |

---

## Phase 7: User Story 5 - System Deployment (P2)

**Story Goal**: 系统可部署在Linux服务器，通过HTTP访问

**Independent Test**: 构建WAR→部署到Tomcat→通过HTTP访问→验证功能正常

| Task | Description |
|------|-------------|
| - [ ] T069 [US5] | Configure Maven WAR packaging in pom.xml |
| - [ ] T070 [US5] | Create Dockerfile for containerized deployment |
| - [ ] T071 [US5] | Create docker-compose.yml for MySQL + Tomcat |
| - [ ] T072 [US5] | Build frontend and copy to backend static resources |
| - [ ] T073 [US5] | Test deployment on Linux server |

---

## Phase 8: Polish & Cross-Cutting Concerns

| Task | Description |
|------|-------------|
| - [ ] T074 | Implement responsive design for mobile devices |
| - [ ] T075 | Add loading states and error handling |
| - [ ] T076 | Implement 30-minute session timeout |
| - [ ] T077 | Add rate limiting middleware |
| - [ ] T078 | Create stats endpoint (GET /stats) |
| - [ ] T079 | Add logout button and session management UI |
| - [ ] T080 | Optimize map rendering performance |
| - [ ] T081 | Add favicon and page titles |

---

## Testing Tasks (Optional)

Uncomment to enable TDD approach:

```
# Backend Tests
# - [ ] T082 Write unit tests for UserService
# - [ ] T083 Write unit tests for CheckinService
# - [ ] T084 Write integration tests for AuthController
# - [ ] T085 Write integration tests for CheckinController

# Frontend Tests
# - [ ] T086 Write unit tests for userStore
# - [ ] T087 Write unit tests for checkinStore
# - [ ] T088 Write Cypress E2E tests for login flow
# - [ ] T089 Write Cypress E2E tests for checkin flow
```

---

## Task Dependency Graph

```
Phase 1 (Setup)
    ↓
Phase 2 (Foundational)
    ↓
Phase 3 (US1) ←→ Phase 4 (US4)  [Can run in parallel]
    ↓               ↓
Phase 5 (US2) ←───┘
    ↓
Phase 6 (US3)
    ↓
Phase 7 (US5)
    ↓
Phase 8 (Polish)
```

**Parallel Opportunities**:
- Backend and Frontend tasks within each story can run in parallel
- US1 and US4 can be developed simultaneously (no dependencies)
- Infrastructure tasks (T009, T010) can run in parallel with backend setup

---

## MVP Scope

For minimum viable product, implement:
- Phase 1 + Phase 2
- Phase 3 (US1 - Province Navigation)
- Phase 4 (US4 - User Registration/Login)
- Phase 5 (US2 - City Check-in)

This provides: ✅ Map navigation ✅ User authentication ✅ Check-in functionality

---

## Completion Checklist

- [ ] All core user stories implemented (P1)
- [ ] Frontend builds successfully
- [ ] Backend builds successfully
- [ ] All API endpoints tested
- [ ] Database migration complete
- [ ] Security configuration in place
- [ ] Session timeout working (30min)
- [ ] Rate limiting configured
- [ ] Deployment package created

---

**Generated**: 2026-06-14 | **Total Tasks**: 81 | **Stories**: 5 | **Phases**: 8
