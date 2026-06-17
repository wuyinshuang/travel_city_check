#!/bin/bash
echo "=== Testing Frontend Page ==="
echo ""
echo "1. Testing index.html access:"
curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://localhost:8080/travel-city-checkin/

echo ""
echo "2. Testing JS resource:"
curl -s -o /dev/null -w "JS Asset HTTP Status: %{http_code}\n" http://localhost:8080/travel-city-checkin/assets/index-Dbp7tI3N.js

echo ""
echo "3. Testing API - Login:"
curl -s -X POST http://localhost:8080/travel-city-checkin/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

echo ""
echo ""
echo "4. Testing API - Register:"
curl -s -X POST http://localhost:8080/travel-city-checkin/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"123456"}'