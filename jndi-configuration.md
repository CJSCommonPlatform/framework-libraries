# JNDI Configuration Reference — cp-framework-libraries

JNDI values are injected at application startup via CDI producers:

- `@GlobalValue` — resolved from `java:global/<key>`. Shared across all deployed applications in the WildFly instance.
- `@Value` — resolved first from `java:app/<service-context-name>/<key>`, falling back to `java:global/<key>`. Intended for per-service overrides.

If a key is not present in JNDI and no `defaultValue` is set, the application will fail to start with a `MissingPropertyException`.

---

## Job Store

All values are **app-specific** (`@Value`). Configured in `JobStoreConfiguration`.

| JNDI Key | Scope | Default | Description |
|---|---|---|---|
| `jobstore.timer.start.wait.milliseconds` | App | `20000` | Delay in milliseconds before the job-store timer fires for the first time after application startup. |
| `jobstore.timer.interval.milliseconds` | App | `20000` | Polling interval in milliseconds between job-store timer ticks. |
| `jobstore.job.priority.percentage.high` | App | `70` | Percentage of available worker slots reserved for high-priority jobs. |
| `jobstore.job.priority.percentage.low` | App | `10` | Percentage of available worker slots reserved for low-priority jobs. |
| `worker.job.count` | App | `10` | Maximum number of concurrent job-worker slots. |
