# HTTP Caching Proxy

A lightweight HTTP proxy server implemented in Java that provides caching capabilities using an LRU (Least Recently Used) caching strategy.

## Features

- HTTP proxy server with response caching
- LRU (Least Recently Used) cache implementation
- Configurable cache capacity (default: 3 entries)
- Custom port configuration
- Origin server URL configuration
- Cache clearing functionality
- Cache hit/miss tracking via response headers

## Requirements

- Java 17 or higher
- Maven 3.8+
- Windows/Linux/macOS

## Building the Project

```bash
mvn clean package
```

## Usage

Run the proxy server using the following command:

```bash
java -jar target/caching-proxy-1.0-SNAPSHOT.jar --port <port> --origin <origin-url>
```

### Command Line Arguments

| Argument | Description | Required | Default |
|----------|-------------|----------|---------|
| `--port` | Port number for the proxy server | No | 4000 |
| `--origin` | Origin server URL (must start with http:// or https://) | Yes | - |
| `--clear-cache` | Clear the cache and exit | No | false |

### Examples

Start the proxy server on port 3000:
```bash
java -jar target/caching-proxy-1.0-SNAPSHOT.jar --port 3000 --origin https://api.example.com
```

Clear the cache:
```bash
java -jar target/caching-proxy-1.0-SNAPSHOT.jar --clear-cache
```

## How It Works

1. The proxy server accepts incoming HTTP requests
2. For each request, it:
   - Checks if the response is in the cache
   - If found (HIT): Returns cached response with `X-Cache: HIT` header
   - If not found (MISS):
     - Forwards request to origin server
     - Caches the response
     - Returns response with `X-Cache: MISS` header
3. Uses LRU eviction policy when cache reaches capacity

## Project Structure

```
src/main/java/org/muchiri/
├── Main.java           # Entry point and argument handling
├── Server.java         # Proxy server implementation
├── ProxyHandler.java   # Request/response handler
├── Cache.java          # Cache interface
├── LRUCache.java       # LRU cache implementation
└── ArgType.java        # Command line argument types
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
