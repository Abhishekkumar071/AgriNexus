# AgriNexus
Enterprise-grade AI-powered agriculture platform featuring intelligent crop advisory, farm management, weather analytics, pest detection, secure authentication, and cloud deployment.


# AgriNexus — V1 Requirements (Foundation)
Status: **Frozen** (changes only for critical reasons, explicitly called out)
```

┌─────────────────────────────────────────┐
│  1. Controller Layer                     │  ← HTTP request/response, routing
│     (@RestController)                    │
├─────────────────────────────────────────┤
│  2. Service Layer                        │  ← Business logic (interfaces!)
│     (@Service, interface + impl)         │
├─────────────────────────────────────────┤
│  3. Repository Layer                     │  ← Data access
│     (MongoRepository)                    │
├─────────────────────────────────────────┤
│  4. Domain/Model Layer                   │  ← MongoDB documents (@Document)
├─────────────────────────────────────────┤
│  5. Database (MongoDB)                   │
└─────────────────────────────────────────┘

Cross-cutting (har layer ke through guzarti hain):
  - Security Filter Chain (JWT validation)
  - Exception Handling (@ControllerAdvice)
  - Validation (Bean Validation on DTOs)
```

---

## Functional Requirements

| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-1 | User registration with email + password | Must | - Duplicate email → 409 Conflict<br>- Password stored as BCrypt hash, never plain<br>- Invalid email format → 400 with field-level error<br>- Success → 201 with user id (no password in response) |
| FR-2 | User login → JWT access + refresh token | Must | - Wrong credentials → 401, generic message (no "email not found" leak)<br>- Success → access token (~15 min) + refresh token (~7 days) returned |
| FR-3 | `POST /api/v1/auth/refresh` — issue new access token | Must | - Valid, non-expired refresh token → new access token<br>- Expired/invalid/reused refresh token → 401<br>- Refresh token itself never re-issues another refresh token silently (rotation discussed in Module 4) |
| FR-4 | `POST /api/v1/auth/logout` — invalidate refresh token | Must | - After logout, same refresh token used on `/refresh` → 401<br>- Logout with no/invalid token → 401, not 500 |
| FR-5 | CRUD on Farm (`/api/v1/farms`) | Must | - User can only access their own farm (ownership check)<br>- List endpoint supports pagination, sorting, filtering (by soilType, state)<br>- Delete = hard delete in V1 (explicit; soft delete deferred to later version)<br>- Not-owner access attempt → 403, not 404 (avoid info leak — *decision to revisit in security review*) |
| FR-6 | Plot management within Farm | Must | - Add/update/remove plot within farm's plots array<br>- Plot number must be unique within a farm |
| FR-7 | Log farm activities (planting, watering, expenses) | Must | - Activity requires type + date + description (validated)<br>- Activities list supports pagination |
| FR-8 | Role-based access — Farmer role only in V1 | Should | - Admin role scaffolded but not enforced with real admin endpoints yet (V2) |
| FR-9 | User profile view/update | Must | - Password not returned in any profile response |

---

## Non-Functional Requirements

| ID | Requirement | Priority |
|---|---|---|
| NFR-1 | Passwords hashed with BCrypt (strength ≥ 10) | Must |
| NFR-2 | JWT access ~15 min, refresh ~7 days, refresh token invalidatable (DB-backed) | Must |
| NFR-3 | Consistent API response envelope + error format across all endpoints | Must |
| NFR-4 | Input validation via Bean Validation on every request DTO | Must |
| NFR-5 | API response time < 300ms for CRUD (excludes AI calls — out of scope V1) | Should |
| NFR-6 | Service layer defined via interfaces (extensibility for V2+) | Must |
| NFR-7 | Environment-based config, zero hardcoded secrets | Must |
| NFR-8 | Basic structured logging on every request | Should |
| NFR-9 | API versioned from day one (`/api/v1/...`) | Must |
| NFR-10 | List endpoints support pagination, sorting, filtering | Must |

---

## Assumptions & Constraints

- Single currency assumed: INR (multi-currency out of scope for V1).
- Backend responses in English only; multi-language is a later-version concern (frontend may localize independently).
- Single farm-owner model in V1 — collaborators/shared-access (mentioned in original KhetSetu schema) deferred to a later version.
- Hard delete in V1 means data is unrecoverable once deleted — acceptable since V1 is a learning/foundation build, not handling real farmer production data yet.
- No file/image upload in V1 (pest detection image upload is a V3 AI-module concern).
- Deployment target and cloud provider not decided yet — deferred to V5 (Cloud & DevOps); V1 architecture must not assume a specific provider.
- Assume single-instance deployment for V1 (no horizontal scaling concerns yet — that's V6).

---

## Explicitly Out of Scope for V1

AI advisory, chatbot, pest detection, weather integration, market data, notifications, admin panel, soft delete, microservices split — all deferred to their mapped versions (V2–V6) per the frozen roadmap.
