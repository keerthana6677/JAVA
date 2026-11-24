# A1 Record Layout

## Delimited text format
Each record is one line. Fields are separated by `|`:

`pass_id|register_number|name|route_number|boarding_stop|validity_date|amount_paid`

Example:
`P1003|192411103|Bhargavi N.|R12|Engineering Block|2027-12-31|500.00`

Rules:
- Exactly seven fields.
- Pass ID and register number are nonblank.
- Amount is parsed with `Double.parseDouble` and must be finite.
- One record occupies one line.
- UTF-8 text is used.
- A damaged line is reported and skipped; valid surrounding records remain usable.

## Binary format
Using `DataOutputStream`, each record is written in the following order:
1. UTF pass ID
2. UTF register number
3. UTF name
4. UTF route number
5. UTF boarding stop
6. UTF validity date
7. Double amount

`writeUTF` prefixes each string with its byte length. Binary records are compact and unambiguous but require a matching reader and are not human-readable.

## Text versus fixed-length RandomAccessFile

| Topic | Delimited text | Fixed-length random access |
|---|---|---|
| Search | Sequential scan or in-memory index | Direct offset calculation |
| Updates | Rewrite or append | In-place fixed slot |
| Variable text | Easy | Requires padding/truncation policy |
| Corruption | Whole damaged line can be skipped | Bad slot corrupts a known span |
| Portability | High | Lower due to encoding/byte-order concerns |

For 8,000 records, text scanning is simple and sufficient. Fixed-length `RandomAccessFile` is useful when frequent direct updates are required, but it needs a slot for the longest allowed value and a deleted-record policy.

## Failure handling
- Missing file: return a clear `IOException` and do not create silently for a read operation.
- Incomplete write: write to a temporary file and atomically replace the final file.
- Power failure: close/flush streams, preferably use a temporary file plus atomic move and an application-level record count or checksum.
- Damaged line: skip and count it; never discard previously valid lines.
