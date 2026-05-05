# Framework Libraries — Security Updates (Java 21 / Jakarta EE 10 Baseline)

The Java 21 / Jakarta EE 10 release (`21.0.0-SNAPSHOT`) does not itself introduce new CVE-tagged fixes, but it is built on the full `17.x` release history. The security vulnerabilities below were all resolved in the `17.x` series and are included in the Java 21 baseline. The `21.0.0-SNAPSHOT` upgrade also transitively eliminates the javax namespace surface area (replacing it with Jakarta EE 10 APIs), which removes entire categories of vulnerability exposure in the older `javax.*` ecosystem.

---

## CVE-Referenced Security Fixes Included in the Java 21 Baseline

### Fixed in [17.104.0] — 2025-12-16

| CVE | Component | Fixed Version | Detail |
|-----|-----------|---------------|--------|
| CVE-2025-48734 | `commons-beanutils` | 1.11.0 | https://cwe.mitre.org/data/definitions/284.html |
| CVE-2023-0482 | `resteasy` | 3.15.5.Final | https://cwe.mitre.org/data/definitions/378.html |
| CVE-2021-47621 | `classgraph` | 4.8.112 | https://cwe.mitre.org/data/definitions/611.html |
| CVE-2025-48924 | `commons-lang` | 3.18.0 | https://cwe.mitre.org/data/definitions/674.html |

### Fixed in [17.101.2] — 2025-01-09

| CVE | Component | Fixed Version | Detail |
|-----|-----------|---------------|--------|
| CWE-787 | `com.jayway.json-path` | 2.9.0 | https://cwe.mitre.org/data/definitions/787.html |
| CVE-2024-47554 | `commons-io` | 2.18.0 | https://nvd.nist.gov/vuln/detail/CVE-2024-47554 |

### Fixed in [17.0.2] — 2023-06-14

| CVE | Component | Fixed Version | Detail |
|-----|-----------|---------------|--------|
| CVE-2022-45688 | `org.json` | 20230227 | https://nvd.nist.gov/vuln/detail/CVE-2022-45688 |

---

## Additional Security Updates (No CVE Assigned)

These versions applied security-related dependency upgrades that were not associated with a specific CVE number in the changelog.

| Version | Components Updated |
|---------|--------------------|
| [17.2.0] — 2023-11-03 | `org.json`, `plexus-codehaus`, `apache-tika`, `google-guava` (via `maven-common-bom 17.2.0`) |
| [7.1.3] — 2020-10-14 | `apache-tika`, `commons-beanutils`, `commons-guava`, `junit` (via `maven-common-bom 7.1.1`) |

---

## Jakarta EE 10 Namespace Migration (21.0.0-SNAPSHOT)

The `21.0.0-SNAPSHOT` upgrade replaces all `javax.*` APIs with their `jakarta.*` equivalents. While not assigned individual CVEs, this migration removes exposure to vulnerabilities in the unmaintained `javax` namespace and aligns the platform with actively maintained Jakarta EE 10 security patches.

Key changes in `21.0.0-SNAPSHOT`:

- Migrated `javax.jms.*` → `jakarta.jms.*`
- Replaced `javax.xml.bind:jaxb-api` → `jakarta.xml.bind:jakarta.xml.bind-api`
- Upgraded OpenEJB `8.0.13` → `10.0.0` (Jakarta EE 10 compatible)
