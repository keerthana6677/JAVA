# Task 6 Testing and Pseudocode

## Test Matrix

| Test | Input | Expected | Actual | Pass/Fail |
|---|---|---|---|---|
| Successful registration | Valid student and event | Saved once, success dialog | Not executed: MySQL unavailable | N/A |
| Empty name | Blank name | Clear error | Not executed | N/A |
| Invalid email | missing @ | Clear error | Not executed | N/A |
| Invalid phone | letters/too short | Clear error | Not executed | N/A |
| Missing event | no event selected | Clear error | Not executed | N/A |
| Duplicate | same student and event twice | database/UI rejection | Not executed | N/A |
| Different event | same student, new event | accepted | Not executed | N/A |
| Committee login | configured account | dashboard opens | Not executed | N/A |
| View | login then refresh | table populated | Not executed | N/A |
| Filter | event selected | matching rows | Not executed | N/A |
| Export | filtered rows | CSV file | Not executed | N/A |
| DB failure | missing config/service | clear database error | Not executed | N/A |
| Empty results | no matching rows | no-results message | Not executed | N/A |

## Pseudocode
```
register(input):
 validate(input)
 begin SQL transaction
 upsert student using PreparedStatement
 insert registration using PreparedStatement
 commit
 if duplicate constraint error: rollback; reject
 return success
```
