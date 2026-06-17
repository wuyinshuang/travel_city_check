<!--
## Sync Impact Report
- Version change: 1.0.0 → 1.0.0 (initial)
- Modified principles: None (initial creation)
- Added sections: Core Principles, Security Requirements, Development Workflow, Governance
- Removed sections: None
- Templates requiring updates: ✅ updated
- Follow-up TODOs: None
-->

# Travel City Check Constitution

## Core Principles

### I. Code Quality First
Every line of code must adhere to language-specific style guides and best practices. Static type checking and linting are mandatory in CI/CD pipelines with zero warnings policy. Functions and modules should be small, single-responsibility, and well-documented.

### II. Test-Driven Development
TDD is mandatory: tests written first, user approved, tests fail, then implement. Backend requires >80% unit test coverage. Integration tests for API endpoints and e2e tests for critical user flows are required.

### III. User Experience Consistency
A unified design system must be adopted across the frontend. Responsive design must work on desktop, tablet, and mobile. API response formats and error handling patterns must be standardized.

### IV. Performance Optimization
Backend API response times must be < 200ms for p95 under normal load. Frontend must achieve FCP < 1.8s and TTI < 3.8s on 4G networks. Caching strategies and lazy loading must be implemented appropriately.

### V. Linux Deployment Ready
The project must be deployable on Linux servers (Ubuntu LTS preferred). Docker containerization with multi-stage builds is required. Zero-downtime deployments and graceful shutdown handling must be implemented.

## Security Requirements
- Apply least-privilege principles to server and database access
- Validate and sanitize all user inputs; protect against OWASP Top 10 risks
- Use dependency scanning and vulnerability alerts in CI/CD
- Implement rate limiting and TLS termination at the reverse proxy level
- Manage secrets via environment variables; never commit secrets to version control

## Development Workflow
- Use Git with trunk-based workflow and conventional commits
- CI/CD pipeline must include: lint → test → build → security scan → deploy
- All PRs require peer review; no direct pushes to main branch
- Infrastructure as Code (Terraform/Ansible) is preferred for server provisioning

## Governance
This constitution supersedes all other practices. Amendments require documentation, approval, and migration plan. All PRs/reviews must verify compliance with these principles. Complexity must be justified.

**Version**: 1.0.0 | **Ratified**: 2026-06-14 | **Last Amended**: 2026-06-14