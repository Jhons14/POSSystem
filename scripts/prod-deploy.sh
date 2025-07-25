#!/bin/bash

# Production deployment script for POS System

set -e

echo "🚀 Deploying POS System to Production"

# Check if running as root
if [ "$EUID" -eq 0 ]; then
    echo "❌ Do not run this script as root for security reasons"
    exit 1
fi

# Check required environment variables
required_vars=("POSTGRES_PASSWORD" "JWT_SECRET" "REDIS_PASSWORD")
for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ Required environment variable $var is not set"
        exit 1
    fi
done

# Check if .env file exists
if [ ! -f .env ]; then
    echo "❌ .env file not found. Please create it with production values."
    exit 1
fi

# Set production profile
export SPRING_PROFILES_ACTIVE=pdn
export ENVIRONMENT=production

echo "🔧 Building production images..."

# Build images
docker-compose -f docker-compose.yml -f docker-compose.prod.yml build --no-cache

echo "🔄 Stopping existing containers..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml down

echo "🗄️  Starting database and cache..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d postgres redis

echo "⏳ Waiting for database to be ready..."
until docker-compose exec -T postgres pg_isready -U posuser -d posdb > /dev/null 2>&1; do
    sleep 2
done

echo "🔙 Starting backend..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d backend

echo "⏳ Waiting for backend to be ready..."
until curl -f http://localhost:8080/pos/server/api/actuator/health > /dev/null 2>&1; do
    sleep 2
done

echo "🖥️  Starting frontend and nginx..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d frontend nginx

echo "🔍 Running health checks..."
sleep 10

# Health checks
services=("postgres" "redis" "backend" "frontend" "nginx")
for service in "${services[@]}"; do
    if docker-compose ps "$service" | grep -q "Up"; then
        echo "✅ $service is running"
    else
        echo "❌ $service failed to start"
        docker-compose logs "$service"
        exit 1
    fi
done

echo ""
echo "🎉 Production deployment completed successfully!"
echo ""
echo "🌐 Application: https://yourdomain.com"
echo "🔙 API Health: https://yourdomain.com/api/actuator/health"
echo ""
echo "📊 To monitor logs:"
echo "   docker-compose -f docker-compose.yml -f docker-compose.prod.yml logs -f"
echo ""
echo "🔄 To update application:"
echo "   git pull && ./scripts/prod-deploy.sh"