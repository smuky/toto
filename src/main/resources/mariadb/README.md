# MariaDB Liquibase Migrations

This directory contains all Liquibase changelog files for database schema management.

## Structure

- `db.changelog-master.xml` - Master changelog file that includes all other changelogs
- `changelog/` - Directory containing individual changelog files

## Adding New Migrations

1. Create a new changelog file in the `changelog/` directory
   - Follow naming convention: `db.changelog-XXX-description.xml`
   - Increment the XXX number sequentially

2. Add the new changelog to `db.changelog-master.xml`:
   ```xml
   <include file="classpath:mariadb/changelog/db.changelog-XXX-description.xml"/>
   ```

3. Each changeSet should have:
   - Unique `id` attribute
   - `author` attribute
   - Clear, descriptive changes

## Example ChangeSet

```xml
<changeSet id="002-add-new-column" author="your-name">
    <addColumn tableName="predictions_history">
        <column name="new_column" type="VARCHAR(100)"/>
    </addColumn>
</changeSet>
```

## Best Practices

- Never modify existing changeSets after they've been deployed
- Always create new changeSets for schema changes
- Use descriptive IDs and comments
- Test migrations on development environment first
- Keep changeSets atomic and focused

## Rollback

To rollback the last migration:
```bash
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## Verification

Liquibase automatically runs on application startup and logs migration status.
Check the `DATABASECHANGELOG` table to see applied migrations.
