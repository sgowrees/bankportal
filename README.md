# Testing Suite

Five test types, each using the tool best suited for it — all example around
the login flow so you can see how they relate.

| Layer | Tool | Location | Run command |
|---|---|---|---|
| 

## Prerequisites

- Your Spring Boot app running on `http://localhost:8080` (for API, Selenium, k6)
- Your frontend running on `http://localhost:5173` (for Selenium, Cypress)
- k6 installed: https://k6.io/docs/get-started/installation/
- Node.js installed (for Cypress)

## Quick start

```bash
# Unit tests (no server needed — everything's mocked)
cd unit-api-tests && mvn test

# API tests (needs backend running)
cd unit-api-tests && mvn verify

# Selenium UI tests (needs backend + frontend running)
cd selenium-tests && mvn test

# Cypress E2E (needs backend + frontend running)
cd cypress-tests && npm install && npm run cy:run

# k6 load test (needs backend running)
cd k6-tests && k6 run scripts/load-test-login.js

# k6 stress test
cd k6-tests && k6 run scripts/stress-test-api.js
```

## Notes

- Adjust every selector in `selenium-tests/.../pages/LoginPage.java` and
  `cypress-tests/cypress/support/commands.js` to match your actual frontend's
  real HTML (input names, button text, etc.).
- `unit-api-tests` unit tests assume a `UserService.login()` method exists
  with roughly the shape shown — adjust to match your real service.
- k6's `data/users.json` should contain real seeded test users in whatever
  environment you're pointing `BASE_URL` at.
