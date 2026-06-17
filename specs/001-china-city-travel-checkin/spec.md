# Feature Specification: China City Travel Check-in Application

**Feature Branch**: `001-china-city-travel-checkin`

**Created**: 2026-06-14

**Status**: Draft

**Input**: User description: "构建一个中国地图城市旅游打卡应用，用户可以先在全国地图上选择省，再在放大版省的地图上选择城市，每个城市有清晰的界限，且是闭合完整的区域。用户点击城市后可以打卡，打卡前为白色，打卡后为浅蓝色。用户也可以选择城市后添加备注，备注中可以编辑文字，也可以上传图片。用户编辑的内容存储到mysql数据库中持久化，整个项目可以在linux服务器上用tomcat部署，部署成功后通过http地址访问，且该应用支持多个用户使用，各个用户数据隔离。"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Province Selection and Navigation (Priority: P1)

用户打开应用后，看到中国全国地图，可以点击任意省份进入该省份的详细地图视图。这是应用的核心入口功能，用户必须能够顺利导航到目标省份。

**Why this priority**: 这是用户使用应用的第一步，没有这个功能，后续所有打卡和备注功能都无法使用。这是整个应用的基础入口。

**Independent Test**: 可以通过打开应用、点击任意省份、验证是否正确跳转到省份地图来独立测试。即使没有打卡功能，用户也能浏览各省份地图。

**Acceptance Scenarios**:

1. **Given** 用户打开应用首页，**When** 用户看到中国全国地图，**Then** 地图应清晰显示所有省份边界，每个省份可点击
2. **Given** 用户在全国地图页面，**When** 用户点击某个省份区域，**Then** 页面应跳转到该省份的放大地图视图
3. **Given** 用户在省份地图页面，**When** 用户点击返回按钮，**Then** 页面应返回到全国地图视图

---

### User Story 2 - City Check-in (Priority: P1)

用户在省份地图上可以看到所有城市边界，点击城市可以进行打卡操作。打卡后城市颜色从白色变为浅蓝色，表示已打卡状态。

**Why this priority**: 打卡是应用的核心功能，是用户记录旅游足迹的主要方式。与省份导航同为P1，因为它们共同构成核心用户旅程。

**Independent Test**: 可以通过进入省份地图、点击城市、验证颜色变化来独立测试。即使没有备注功能，用户也能完成基本的打卡记录。

**Acceptance Scenarios**:

1. **Given** 用户在省份地图页面，**When** 地图加载完成，**Then** 所有城市应显示为闭合完整的区域边界
2. **Given** 用户在省份地图页面，**When** 用户点击某个未打卡城市，**Then** 该城市颜色应从白色变为浅蓝色，表示打卡成功
3. **Given** 用户已打卡某个城市，**When** 用户再次点击该城市，**Then** 系统应显示已打卡状态，可选择取消打卡
4. **Given** 用户打卡某城市后刷新页面，**When** 页面重新加载，**Then** 该城市应保持浅蓝色打卡状态

---

### User Story 3 - City Notes Management (Priority: P2)

用户可以选择城市后添加备注，备注支持文字编辑和图片上传。用户可以记录旅行心得、照片等个人内容。

**Why this priority**: 备注功能丰富了打卡的意义，让用户可以记录更多旅行细节。但即使没有备注，核心打卡功能仍然可用。

**Independent Test**: 可以通过选择城市、添加文字备注、上传图片、验证保存来独立测试。

**Acceptance Scenarios**:

1. **Given** 用户点击某个城市，**When** 系统显示城市详情面板，**Then** 应提供"添加备注"入口
2. **Given** 用户打开备注编辑界面，**When** 用户输入文字内容，**Then** 文字应正确显示并可保存
3. **Given** 用户在备注编辑界面，**When** 用户上传图片，**Then** 图片应正确显示在备注中
4. **Given** 用户编辑完备注，**When** 用户点击保存，**Then** 备注内容应持久化存储
5. **Given** 用户之前保存过备注，**When** 用户再次打开该城市，**Then** 应显示之前保存的备注内容

---

### User Story 4 - Multi-user Data Isolation (Priority: P1)

系统支持多个用户注册登录，每个用户的打卡记录和备注数据相互隔离，用户只能看到自己的数据。

**Why this priority**: 多用户数据隔离是系统安全的基本要求，确保用户隐私和数据安全。这是系统架构的基础功能。

**Independent Test**: 可以通过创建多个测试用户、分别打卡不同城市、验证数据隔离来独立测试。

**Acceptance Scenarios**:

1. **Given** 新用户访问应用，**When** 用户完成注册流程，**Then** 用户账户应创建成功并可登录
2. **Given** 用户A和用户B都打卡了同一城市，**When** 用户A查看自己的地图，**Then** 只应显示用户A自己的打卡记录
3. **Given** 用户A在某城市添加了备注，**When** 用户B查看同一城市，**Then** 不应看到用户A的备注内容

---

### User Story 5 - System Deployment and Access (Priority: P2)

系统部署在Linux服务器上，通过Tomcat运行，用户可以通过HTTP地址访问应用。

**Why this priority**: 部署和访问是系统上线的前提，但可以在核心功能开发完成后进行。

**Independent Test**: 可以通过在Linux服务器上部署、通过浏览器访问HTTP地址来独立测试。

**Acceptance Scenarios**:

1. **Given** 系统部署到Linux服务器，**When** Tomcat服务启动，**Then** 应用应可通过配置的HTTP地址访问
2. **Given** 用户通过浏览器访问应用地址，**When** 页面加载完成，**Then** 应正确显示中国地图首页
3. **Given** 系统正在运行，**When** 多个用户同时访问，**Then** 系统应稳定响应各用户请求

---

### Edge Cases

- 当用户网络不稳定时，打卡操作如何处理？系统应提供离线提示并在网络恢复后同步数据
- 当用户上传的图片过大时如何处理？系统应限制图片大小在10MB以内，超过限制时提示用户压缩或选择其他图片
- 当用户尝试打卡不存在的城市区域时如何处理？系统应忽略无效点击或提供错误提示
- 当用户长时间未操作导致会话过期时如何处理？系统应在30分钟无操作后会话过期，提示用户重新登录
- 当数据库连接失败时如何处理？系统应显示友好的错误提示并记录日志

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST display a complete map of China with all provinces clearly bounded and clickable
- **FR-002**: System MUST allow users to click on any province to navigate to the province detail map
- **FR-003**: System MUST display all cities within a province with clear, closed, and complete boundary areas
- **FR-004**: System MUST allow users to click on any city to perform check-in operation
- **FR-005**: System MUST change city color from white to light blue after successful check-in
- **FR-006**: System MUST persist check-in status so it remains after page refresh or re-login
- **FR-007**: System MUST allow users to add one text note per city (users can edit or update the existing note)
- **FR-008**: System MUST allow users to upload images as part of city notes, with maximum size of 10MB per image (no limit on number of images per note)
- **FR-009**: System MUST support user registration and login using username and password (password stored as hash)
- **FR-010**: System MUST isolate data between different users - each user can only see their own check-ins and notes
- **FR-011**: System MUST store all user data (check-ins, notes, images) in MySQL database
- **FR-012**: System MUST be deployable on Linux server using Tomcat
- **FR-013**: System MUST be accessible via HTTP URL after deployment
- **FR-014**: System MUST support concurrent access from multiple users
- **FR-015**: System MUST expire user sessions after 30 minutes of inactivity

### Key Entities

- **User**: Represents a registered user of the application. Key attributes include user ID, username, password (hashed), registration date, last login time. Each user has isolated data.

- **Province**: Represents a Chinese province on the map. Key attributes include province ID, name, geographic boundary data, list of cities. Used for navigation and display.

- **City**: Represents a city within a province. Key attributes include city ID, name, province ID, geographic boundary data (closed polygon). Used for check-in and note association.

- **Check-in Record**: Represents a user's check-in for a specific city. Key attributes include record ID, user ID, city ID, check-in timestamp. Used to track which cities a user has visited.

- **Note**: Represents a user's note for a specific city. Key attributes include note ID, user ID, city ID, text content, creation time, last update time. Each user can have at most one note per city. Used to store user's travel memories.

- **Image**: Represents an image uploaded as part of a note. Key attributes include image ID, note ID, file path/storage reference, upload time. Used to store visual memories.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can navigate from national map to any province map within 3 clicks
- **SC-002**: Check-in operation completes within 2 seconds after user clicks a city
- **SC-003**: System supports at least 100 concurrent users without performance degradation
- **SC-004**: Page load time for map display is under 3 seconds on standard broadband connection
- **SC-005**: 95% of users can complete their first check-in within 1 minute of first use without guidance
- **SC-006**: User data isolation is verified with 100% accuracy - no user can access another user's data
- **SC-007**: System maintains 99.5% uptime during normal operation hours
- **SC-008**: Image upload completes within 10 seconds for images under 10MB

## Clarifications

### Session 2026-06-14

- Q: 用户应该使用什么方式进行注册登录？ → A: 用户名 + 密码
- Q: 单张图片的最大允许大小是多少？ → A: 10MB
- Q: 每个用户对每个城市可以添加多少条备注？ → A: 每个城市仅一条备注
- Q: 每条备注最多可以上传多少张图片？ → A: 不限制
- Q: 用户登录会话的超时时间是多少？ → A: 30分钟

## Assumptions

- Users have stable internet connectivity for map loading and data synchronization
- Users access the application via modern web browsers (Chrome, Firefox, Edge, Safari) with JavaScript enabled
- China map data (province and city boundaries) is available in a suitable vector format (e.g., GeoJSON)
- MySQL database is pre-installed and configured on the Linux server
- Tomcat is pre-installed on the Linux server or will be installed during deployment
- Image storage will use server file system with database references (not cloud storage)
- User authentication will use standard session-based authentication
- Chinese language is the primary interface language
- Mobile responsive design is not required for initial version (desktop-focused)
- Province and city boundary data is accurate and up-to-date with Chinese administrative divisions