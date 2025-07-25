#!/bin/bash

# Development startup script for POS System

set -e

echo "🚀 Starting POS System in Development Mode"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if Bun is installed for local development
if ! command -v bun &> /dev/null; then
    echo "⚠️  Bun is not installed. Install it from https://bun.sh for local development."
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo "📝 Creating .env file from .env.example"
    cp .env.example .env
    echo "⚠️  Please edit .env file with your configuration before running again"
    exit 1
fi

# Load environment variables
export $(cat .env | grep -v '^#' | xargs)

echo "🔧 Building and starting services..."

# Start services
docker-compose up --build -d

echo "⏳ Waiting for services to be healthy..."

# Wait for database
echo "🗄️  Waiting for database..."
until docker-compose exec -T postgres pg_isready -U posuser -d posdb > /dev/null 2>&1; do
    sleep 2
done

# Wait for backend
echo "🔙 Waiting for backend..."
until curl -f http://localhost:8080/pos/server/api/actuator/health > /dev/null 2>&1; do
    sleep 2
done

# Wait for frontend
echo "🖥️  Waiting for frontend..."
until curl -f http://localhost:3000 > /dev/null 2>&1; do
    sleep 2
done

echo "✅ All services are up and running!"
echo ""
echo "🌐 Frontend: http://localhost:3000"
echo "🔙 Backend API: http://localhost:8080/pos/server/api"
echo "📖 API Documentation: http://localhost:8080/pos/server/api/swagger-ui/index.html"
echo "🗄️  Database: localhost:5432 (posdb)"
echo ""
echo "📊 To view logs:"
echo "   docker-compose logs -f backend"
echo "   docker-compose logs -f frontend"
echo "   docker-compose logs -f postgres"
echo ""
echo "🛑 To stop all services:"
echo "   docker-compose down"