# ECS Logging - Access

Access log functionality for Tomcat, emitting one JSON log entry per HTTP request in the
[ECS format](https://www.elastic.co/guide/en/ecs/current/index.html).

## Basic usage

Add the dependency and configure a `logback-access.xml` with an ECS encoder (see
`EcsAccessEncoder`). By default this logs one entry per request with method, path, status
code, timing, client address, session id and transaction id - no request/response bodies.

## Configuration (`raynigon.logging.access.*`)

| Property | Default | Description |
|---|---|---|
| `export-body` | `false` | Enables request/response body capture. Registers a servlet filter that duplicates ("tees") the request `InputStream` and response `OutputStream` so their content can be included in the log entry as `http.request.body.content` / `http.response.body.content`. |
| `body-size-limit` | `10000` | Maximum number of characters kept per body field; longer bodies are truncated. |
| `exclude-endpoints` | `[]` | Exact request URIs (e.g. `/actuator/health`) for which no access log entry is written at all. Matched by exact string equality against the resolved request URI - **no wildcards or path variables**, so it can't target routes like `/api/products/{id}`. |
| `exclude-multipart-body` | `true` | See [Multipart requests](#multipart-requests) below. |

### Multipart requests

Body export works by reading the entire request body into memory before the servlet
container parses it. For `multipart/form-data` requests this is actively unsafe: it
consumes the raw stream that the container's own multipart parser needs afterwards to
resolve `@RequestPart`/`MultipartFile` arguments, causing those requests to fail instead
of ever reaching the controller. Even if that weren't an issue, the captured content
would be a mix of MIME boundaries, part headers and (for file parts) raw binary bytes
forced through `new String(bytes)` - not something worth logging.

For that reason, whenever `export-body` is enabled, `multipart/form-data` requests are
**excluded from body capture by default** (`exclude-multipart-body=true`). The access log
entry itself is still written as normal - only `http.request.body.content` and
`http.response.body.content` come back empty for that call.

```yaml
raynigon.logging.access:
  export-body: true
  exclude-multipart-body: true # default; set to false to opt back into the legacy,
                                # crash-prone behaviour
```

Note: because the underlying `TeeFilter` tees the request and response together in a
single pass, skipping body capture for a multipart request skips it for **both** sides -
you also won't get that call's response body logged, even if the response itself is
plain JSON.

## Annotations (`com.raynigon.ecs.logging.access.annotation`)

Both annotations can be placed on an individual `@RequestMapping` handler method, or on
the controller class (applying to every handler method in it). They only have an effect
when `export-body` is enabled, and only apply to genuine Spring MVC handler methods -
annotating a static resource handler, a WebSocket endpoint, a security filter-handled
route, or a functional `RouterFunction` endpoint has no effect, since none of those are
resolved as an annotatable `HandlerMethod`.

### `@EcsSkipBodyLogging`

Skips body export for the annotated endpoint. The access log entry is still written -
only `http.request.body.content` / `http.response.body.content` come back empty.

Use this for endpoints whose body shouldn't appear in logs even though it's regular text
(e.g. it carries credentials, tokens, or other sensitive fields), regardless of content
type.

```java
@PostMapping("/login")
@EcsSkipBodyLogging
public TokenResponse login(@RequestBody LoginRequest request) { ... }
```

### `@EcsSkipAccessLogging`

Suppresses the access log entry for the annotated endpoint entirely - no line is written
at all. Implies `@EcsSkipBodyLogging`.

This is an annotation-driven alternative to `exclude-endpoints` for routes that property
can't express, since it only does exact string matching and has no support for path
variables:

```java
@RequestMapping("/api/products")
class ProductController {

    @PostMapping("{id}/images")
    @EcsSkipAccessLogging // exclude-endpoints can't match "/api/products/{id}/images"
    public ImageResponse uploadImage(@PathVariable String id, ...) { ... }
}
```

### How resolution works

Both annotations are read from the `HandlerMethod` that will serve the request, resolved
via `RequestMappingHandlerMapping` *before* the body is touched - handler matching only
inspects the request line and headers (path, method, `consumes`/`produces`), never the
body, so this is safe to do ahead of body capture. If no handler can be resolved yet (for
example the request doesn't match any mapping, or matches only on some conditions), the
lookup fails open: no annotation is assumed, and the request falls back to whatever the
`exclude-multipart-body` / global `export-body` settings would otherwise do.
