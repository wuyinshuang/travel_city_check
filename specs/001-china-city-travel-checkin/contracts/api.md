# API Contract: China City Travel Check-in Application

**Version**: 1.0.0

**Base URL**: `/api/v1`

**Authentication**: Session-based (Spring Security)

## Response Format

### Success Response

```json
{
  "code": 200,
  "message": "Success",
  "data": { ... },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

### Error Response

```json
{
  "code": 400,
  "message": "Invalid request parameters",
  "errors": ["Image size exceeds 10MB limit"],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

### HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content (successful deletion) |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## 1. Authentication APIs

### 1.1 User Registration

**POST** `/auth/register`

**Request Body**:
```json
{
  "username": "string (3-50 chars, alphanumeric + underscore)",
  "password": "string (min 6 chars)"
}
```

**Response** (201):
```json
{
  "code": 201,
  "message": "Registration successful",
  "data": {
    "userId": 1,
    "username": "testuser"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 400: Username already exists
- 400: Invalid username format
- 400: Password too short

---

### 1.2 User Login

**POST** `/auth/login`

**Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response** (200):
```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "userId": 1,
    "username": "testuser"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 401: Invalid credentials

---

### 1.3 User Logout

**POST** `/auth/logout`

**Response** (200):
```json
{
  "code": 200,
  "message": "Logout successful",
  "data": null,
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 1.4 Get Current User

**GET** `/auth/me`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "userId": 1,
    "username": "testuser",
    "createdAt": "2026-06-14T10:00:00Z",
    "lastLogin": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 401: Not authenticated

---

## 2. Map Data APIs

### 2.1 Get All Provinces

**GET** `/provinces`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "北京市",
      "code": "110000"
    },
    {
      "id": 2,
      "name": "天津市",
      "code": "120000"
    }
  ],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 2.2 Get Province GeoJSON

**GET** `/provinces/{provinceId}/geojson`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "type": "FeatureCollection",
    "features": [...]
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 2.3 Get Cities by Province

**GET** `/provinces/{provinceId}/cities`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "北京市",
      "code": "110100"
    },
    {
      "id": 2,
      "name": "天津市",
      "code": "120100"
    }
  ],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 2.4 Get City GeoJSON

**GET** `/cities/{cityId}/geojson`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "type": "Feature",
    "geometry": {
      "type": "Polygon",
      "coordinates": [...]
    },
    "properties": {
      "name": "北京市",
      "code": "110100"
    }
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

## 3. Check-in APIs

### 3.1 Get User Check-ins

**GET** `/checkins`

**Query Parameters**:
- `provinceId` (optional): Filter by province

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "cityId": 1,
      "cityName": "北京市",
      "provinceId": 1,
      "provinceName": "北京市",
      "createdAt": "2026-06-14T10:00:00Z"
    }
  ],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 3.2 Create Check-in

**POST** `/checkins`

**Request Body**:
```json
{
  "cityId": 1
}
```

**Response** (201):
```json
{
  "code": 201,
  "message": "Check-in successful",
  "data": {
    "id": 1,
    "cityId": 1,
    "cityName": "北京市",
    "createdAt": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 400: Already checked in
- 404: City not found

---

### 3.3 Delete Check-in

**DELETE** `/checkins/{checkinId}`

**Response** (204): No Content

**Errors**:
- 404: Check-in not found
- 403: Not your check-in

---

## 4. Note APIs

### 4.1 Get User Notes

**GET** `/notes`

**Query Parameters**:
- `provinceId` (optional): Filter by province

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "cityId": 1,
      "cityName": "北京市",
      "provinceId": 1,
      "provinceName": "北京市",
      "content": "Great city!",
      "images": [
        {
          "id": 1,
          "filePath": "/uploads/1/1/abc123.jpg",
          "fileName": "photo.jpg",
          "fileSize": 1024000
        }
      ],
      "createdAt": "2026-06-14T10:00:00Z",
      "updatedAt": "2026-06-14T10:30:00Z"
    }
  ],
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

### 4.2 Get Note by City

**GET** `/notes/city/{cityId}`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "cityId": 1,
    "content": "Great city!",
    "images": [...],
    "createdAt": "2026-06-14T10:00:00Z",
    "updatedAt": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 404: Note not found

---

### 4.3 Create Note

**POST** `/notes`

**Request Body**:
```json
{
  "cityId": 1,
  "content": "Great city!"
}
```

**Response** (201):
```json
{
  "code": 201,
  "message": "Note created",
  "data": {
    "id": 1,
    "cityId": 1,
    "content": "Great city!",
    "images": [],
    "createdAt": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 400: Note already exists for this city
- 404: City not found

---

### 4.4 Update Note

**PUT** `/notes/{noteId}`

**Request Body**:
```json
{
  "content": "Updated content"
}
```

**Response** (200):
```json
{
  "code": 200,
  "message": "Note updated",
  "data": {
    "id": 1,
    "content": "Updated content",
    "updatedAt": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 404: Note not found
- 403: Not your note

---

### 4.5 Delete Note

**DELETE** `/notes/{noteId}`

**Response** (204): No Content

**Errors**:
- 404: Note not found
- 403: Not your note

---

## 5. Image APIs

### 5.1 Upload Image

**POST** `/notes/{noteId}/images`

**Content-Type**: `multipart/form-data`

**Request Body**:
- `file`: File (max 10MB, formats: jpg, jpeg, png, gif, webp)

**Response** (201):
```json
{
  "code": 201,
  "message": "Image uploaded",
  "data": {
    "id": 1,
    "filePath": "/uploads/1/1/abc123.jpg",
    "fileName": "photo.jpg",
    "fileSize": 1024000,
    "createdAt": "2026-06-14T10:30:00Z"
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

**Errors**:
- 400: File too large (max 10MB)
- 400: Invalid file format
- 404: Note not found
- 403: Not your note

---

### 5.2 Delete Image

**DELETE** `/images/{imageId}`

**Response** (204): No Content

**Errors**:
- 404: Image not found
- 403: Not your image

---

## 6. Statistics APIs

### 6.1 Get User Statistics

**GET** `/stats`

**Response** (200):
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "totalCheckins": 50,
    "totalNotes": 30,
    "totalImages": 100,
    "provincesVisited": 10,
    "citiesVisited": 50
  },
  "timestamp": "2026-06-14T10:30:00Z"
}
```

---

## Rate Limiting

| Endpoint | Limit |
|----------|-------|
| All APIs | 100 requests/minute per user |
| Image Upload | 10 uploads/minute per user |

---

## Session Management

- Session timeout: 30 minutes of inactivity
- Session cookie: `JSESSIONID`
- Secure flag enabled in production
- HttpOnly flag enabled
