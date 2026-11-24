# Task 5 Event-Style Comparison

| Style | Code Size | Readability | Frame Field Access | Reuse | Maintenance |
|---|---|---|---|---|---|
| implements ActionListener | Small | Clear, central switch needed if expanded | Direct | Moderate | Good |
| Named inner class | Small-medium | Explicit | Direct outer access | Good | Good |
| Anonymous class | Small | Inline, explicit syntax | Direct outer access | Poor | Moderate |
| Lambda | Smallest | Clear for short handlers | Direct outer access | Moderate | Good |

The actual shared frame base keeps conversion logic identical. Lambdas suit short callbacks; method references suit existing methods; named handlers/Swing Actions suit reuse across components and keyboard/menu actions.
