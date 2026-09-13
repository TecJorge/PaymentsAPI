# Payments API

Small Spring Boot service built for a technical challenge (schedule/consult/update/delete bank transactions, each one with a transfer fee attached). Not production-grade, just meant to show how I'd approach it.

## Stack

- Java 21
- Spring Boot 4.1.1 (Web, Data JPA, Validation)
- Maven wrapper included, so no need to install Maven
- SQL Server (`mssql-jdbc`)
- OpenAPI Generator - generates the request/response DTOs from `src/main/resources/spec.yaml` at build time, so you don't touch those classes by hand
- Lombok + MapStruct
- Jollyday - used to calculate fees based on business days (see below)

## Before you start

- JDK 21
- A SQL Server instance up somewhere reachable at `localhost:1433` - no Dockerfile in here yet, sorry, so you'll need to run one yourself (local install, an existing instance, whatever you've got)

## Database

Connection info lives in `src/main/resources/application.yml`:

```
host: localhost:1433
database: Payments_API
username: admin
password: admin
```

Create the `Payments_API` database (or just point the values above at whatever instance you're using), then run the schema script by hand:

```
src/main/resources/db/scripts/initial_schema.sql
```

Sets up `payments_transaction` and `payments_transaction_deleted`, plus a trigger that keeps `updatedAt` current.

There's also `insertStatements.sql` in the same folder if you want some sample rows to query against instead of starting from an empty table.

`ddl-auto` is `none`, so nothing gets created for you automatically - run the schema script first or the app will just fail on startup.

Didn't get to unit tests for this one (the challenge said they're welcome but not required) - if I revisit this, that's the first thing I'd add, especially around the fee calculation.

## Endpoints

| Method | Endpoint | What it does |
|--------|----------|---------------|
| POST   | `/transaction/create` | Schedule a transaction |
| GET    | `/transaction/retrieve?userId={id}` | Get all transactions for a user |
| GET    | `/transaction?userId={id}&transactionId={id}` | Get one transaction |
| PUT    | `/transaction/update` | Update a scheduled transaction |
| DELETE | `/transaction/delete?transactionId={id}&userId={id}` | Delete a transaction |

Full schemas are in `src/main/resources/spec.yaml`.

## Fee rules

Each transaction gets a fee based on the amount and how far out it's scheduled:

| Amount | Scheduled for | Fee |
|--------|----------------|-----|
| Up to €1000 | Today | 3% + €3 |
| €1001 - €2000 | 1 to 10 days out | 9% |
| Over €2000 | 11 to 20 days out | 8.2% |
| Over €2000 | 21 to 30 days out | 6.9% |
| Over €2000 | 31 to 40 days out | 4.7% |
| Over €2000 | more than 40 days out | 1.7% |

"Days out" only counts business days - weekends and Portuguese public holidays (via Jollyday) don't count towards the total.

A couple of things the original spec didn't cover, so I had to make a judgment call:

- **Amounts over €2000 scheduled for today (or anything under 11 days out)** aren't covered by any rule in the original challenge. Right now that throws a validation error instead of silently applying a fee tier that isn't specified - felt safer than guessing.
- **Scheduling a transaction for a date that's already passed** isn't addressed either. I added a validation that just rejects it, since letting someone "schedule" something in the past didn't make sense.
