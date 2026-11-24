# A1 Pseudocode

## SIMATS Engineering Standard

### AddPass(pass)
```text
BEGIN
  IF pass is NULL THEN
    THROW IllegalArgumentException
  END IF
  VALIDATE passId, registerNumber, name, routeNumber, boardingStop, validityDate
  IF amountPaid is not numeric or is negative THEN
    THROW InvalidRecordException
  END IF
  open file in UTF-8 append mode
  create one line from exactly seven fields
  write line
  flush and close file
  IF close/write fails THEN
    report incomplete write
  END IF
END
```

### FindPassById(passId)
```text
BEGIN
  IF file does not exist THEN
    THROW MissingFileException
  END IF
  open file in UTF-8 line-reading mode
  WHILE a line exists DO
    IF line is blank THEN
      count damaged line
      CONTINUE
    END IF
    split line by "|" preserving empty fields
    IF field count != 7 THEN
      count damaged line
      CONTINUE
    END IF
    TRY parse amount
    CATCH NumberFormatException
      count damaged line
      CONTINUE
    END TRY
    IF passId field equals requested passId THEN
      close file
      RETURN complete BusPass record
    END IF
  END WHILE
  close file
  RETURN NULL
END
```

### ListPassesByRoute(route)
```text
BEGIN
  create empty result list
  open file in UTF-8 line-reading mode
  WHILE a line exists DO
    validate exactly seven fields
    parse amount safely
    IF valid AND route field equals route THEN
      add record to result
    END IF
  END WHILE
  RETURN result and damaged-record count
END
```
