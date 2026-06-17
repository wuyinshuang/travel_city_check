# Quickstart: China City Travel Check-in Application

**Purpose**: End-to-end validation guide to verify the feature works correctly.

## Prerequisites

### System Requirements
- Java 17 LTS
- Node.js 18+ and npm
- MySQL 8.x
- Maven 3.8+

### Environment Setup
```bash
# Clone the repository
git clone <repository-url>
cd travel_city_check

# Create MySQL database
mysql -u root -p
CREATE DATABASE travel_city CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'travelcity'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON travel_city.* TO 'travelcity'@'localhost';
FLUSH PRIVILEGES;
```

## Backend Setup

### 1. Configure Database Connection

Edit `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/travel_city
    username: travelcity
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

### 2. Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

### 3. Verify Backend Health

```bash
curl http://localhost:8080/api/v1/provinces
```

Expected: JSON response with list of provinces

## Frontend Setup

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Configure API Base URL

Edit `frontend/.env`:
```
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. Run Frontend

```bash
npm run dev
```

Frontend will start at `http://localhost:5173`

## Validation Scenarios

### Scenario 1: User Registration and Login

**Steps**:
1. Open `http://localhost:5173` in browser
2. Click "注册" button
3. Enter username: `testuser`, password: `password123`
4. Click "注册" button
5. Verify redirect to login page
6. Enter credentials and click "登录"
7. Verify redirect to home page with map displayed

**Expected Result**: User can register and login successfully

### Scenario 2: Province Navigation

**Steps**:
1. After login, verify national map is displayed
2. Click on any province (e.g., 北京市)
3. Verify province detail map is displayed with cities
4. Click "返回" button
5. Verify return to national map

**Expected Result**: User can navigate between national and province maps

### Scenario 3: City Check-in

**Steps**:
1. Navigate to a province map
2. Click on any city
3. Verify city color changes from white to light blue
4. Refresh the page
5. Verify city remains light blue

**Expected Result**: Check-in persists after page refresh

### Scenario 4: Add Note with Image

**Steps**:
1. Click on a checked-in city
2. Click "添加备注" button
3. Enter text: "Beautiful city!"
4. Click "上传图片" button
5. Select an image file (< 10MB)
6. Click "保存" button
7. Verify note is saved with image displayed
8. Refresh the page and verify note persists

**Expected Result**: Note with image is saved and persists

### Scenario 5: Multi-user Data Isolation

**Steps**:
1. Register a second user: `testuser2`
2. Login as `testuser2`
3. Navigate to the same province and city as `testuser`
4. Verify `testuser2` sees no check-ins or notes from `testuser`
5. Check-in the same city
6. Add a different note
7. Login as `testuser` again
8. Verify only `testuser`'s data is visible

**Expected Result**: Each user only sees their own data

### Scenario 6: Session Timeout

**Steps**:
1. Login as any user
2. Wait 30 minutes without any activity
3. Try to perform any action
4. Verify redirect to login page

**Expected Result**: Session expires after 30 minutes of inactivity

### Scenario 7: Image Upload Validation

**Steps**:
1. Try to upload an image larger than 10MB
2. Verify error message: "Image size exceeds 10MB limit"
3. Try to upload a non-image file (e.g., .txt)
4. Verify error message: "Invalid file format"

**Expected Result**: Invalid uploads are rejected with appropriate error messages

## API Validation (Optional)

### Using curl

```bash
# Register user
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"apiuser","password":"password123"}'

# Login (creates session cookie)
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"apiuser","password":"password123"}' \
  -c cookies.txt

# Get provinces
curl http://localhost:8080/api/v1/provinces -b cookies.txt

# Create check-in
curl -X POST http://localhost:8080/api/v1/checkins \
  -H "Content-Type: application/json" \
  -d '{"cityId":1}' \
  -b cookies.txt

# Get user check-ins
curl http://localhost:8080/api/v1/checkins -b cookies.txt
```

## Performance Validation

### Load Testing (Optional)

Using Apache Bench or similar tool:

```bash
# Test concurrent users (100 requests, 10 concurrent)
ab -n 100 -c 10 http://localhost:8080/api/v1/provinces
```

**Expected**: All requests complete within 200ms (p95)

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL is running
   - Check credentials in `application.yml`
   - Verify database exists

2. **Frontend Cannot Connect to Backend**
   - Verify backend is running on port 8080
   - Check CORS configuration
   - Verify `.env` file has correct API URL

3. **Map Not Displaying**
   - Check browser console for errors
   - Verify GeoJSON data files exist
   - Check network requests for map data

4. **Image Upload Fails**
   - Check file size (< 10MB)
   - Verify file format (jpg, png, gif, webp)
   - Check upload directory permissions

## Next Steps

After successful validation:
1. Run `/speckit-tasks` to generate implementation tasks
2. Begin TDD implementation following the task list
3. Write unit tests for each component
4. Implement features incrementally
