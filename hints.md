# Hints for adding code to this repo

The code is laid out in modules.

The wallet module is a good module to copy for layout etc.

Our DB entities are generally stored under a "model" package.

## Flyway
We are using Flyway for database migrations. 
When adding a new entity don't forget to add the corresponding new flyway migration script.
Remember that you cannot change existing migrations. If you need to rename a column or table etc then it must be done in a new migration.