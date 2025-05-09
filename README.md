```markdown
# CTE-Project

## LexicalAnalyzer

### Overview

The **LexicalAnalyzer** is a standalone Java class that implements the **Lexical Analysis** stage of a simple compiler for a toy “V” language. It reads a single line of source code and tokenizes it into:

- **KEYWORD**
- **IDENTIFIER**
- **OPERATOR**
- **SYMBOL**
- **LEXICAL_ERROR** (for illegal or unrecognized characters)

This tool is intended for integration into a larger **line-by-line** compiler pipeline, where each input line is first passed through the lexical analyzer before proceeding to parsing, semantic checks, etc.
```