# Etapa 1: build
FROM oven/bun:1-alpine AS builder
WORKDIR /app

# Copy package files
COPY package.json bun.lock ./

# Install dependencies
RUN bun install

# Copy source code
COPY . .

# Build arguments
ARG VITE_SERVER_URL=http://localhost:8080/pos/server/api
ENV VITE_SERVER_URL=$VITE_SERVER_URL

# Build application
RUN bun run build

# Etapa 2: production serve
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Copy built application
COPY --from=builder /app/dist .

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Create non-root user
RUN addgroup -g 1000 nginx-group && \
    adduser -D -s /bin/sh -u 1000 -G nginx-group nginx-user

# Change ownership of nginx directories
RUN chown -R nginx-user:nginx-group /var/cache/nginx && \
    chown -R nginx-user:nginx-group /var/log/nginx && \
    chown -R nginx-user:nginx-group /etc/nginx/conf.d && \
    touch /var/run/nginx.pid && \
    chown -R nginx-user:nginx-group /var/run/nginx.pid

# Health check (install curl first)
RUN apk add --no-cache curl
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost/ || exit 1

# Switch to non-root user
USER nginx-user

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]