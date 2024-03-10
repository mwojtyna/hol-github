# Database diagram

### Notes:

- All primary keys are UUIDs
- All columns are NOT NULL, unless specified otherwise

```mermaid
erDiagram
    user {
        uuid id PK
        text username UK
        text password
    }

    session {
        uuid id PK
        uuid user_id FK
        timestamp expire_date
    }

    repo {
        uuid id PK
        text name UK
        integer star_amount
        bytea image
    }

    game {
        uuid id PK
        uuid user_id FK
        integer score
    }

    user 1--0+ game: "games"
    user 1--0+ session: "session"
```