### **Step 0 – Setup baseline**

* Chose build tool (**Maven**, beginner-friendly).
* Chose Java version (**21 LTS**).
* Initialized Spring Boot project with Web, JPA, Actuator, PostgreSQL Driver, Flyway.
* Verified project runs (before DB integration).

---

### **Step 1 – Domain & API design**

* Wrote **`docs/domain.md`** → described entities (`User`, `Organization`, `Project`, `Issue`, `Comment`), invariants, relationships, constraints.
* Wrote **`docs/api-contract.md`** → described REST endpoints, request/response schemas, pagination, filtering, error model (Problem Details).
* Drew an **ER diagram** for reference.

---

### **Step 2 – Infrastructure setup**

This is what you just finished:

* Installed **PostgreSQL 16** locally via Homebrew.
* Created **role + database** (`servicedesk`).
* Configured `application.properties` with datasource URL, username, password.
* Integrated **Flyway** for schema migrations.
* Fixed Flyway issues (added `flyway-database-postgresql` dependency, upgraded Flyway version, relaxed unsupported-database flag).
* ✅ Verified app starts cleanly with DB connection.

---

### **Step 3 – Domain implementation (next)**

* Translate `domain.md` → JPA `@Entity` classes.
* Add **repositories** (Spring Data JPA interfaces).
* Create **Flyway migration V1\_\_init.sql** to create all baseline tables.
* Run app → Flyway auto-applies migration → schema created in Postgres.
* Verify with `psql \dt`.

---
