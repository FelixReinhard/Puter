# Puter
## Registers - 16
- `0x00`: Always zero
- `0x01`: Program Counter `$pc`
- 

## Instruction Set
We will be using something similar to MIPS.
- `li $x {number}`: 5 byte long instruction to load an immediate to into a register.
  - `0x01`: 8 bits, optcode
  - `$x`: 4 bits, register
  - `{number}`: 28 bits, immediate number. [0;268,435,456]
- `add $1 $2`: 2 byte Adds `$1 = $1 + $2`
  - `0x50`: 8 bits optcode
  - `$1`: 4 bits register
  - `$2`: 4 bits register

- `sub $1 $2`: 2 byte Subtracts `$1 = $1 - $2`
  - `0x51`: 8 bits optcode
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `mov $1 $2`: 2 byte Moves `$1 = $2`

  - `0x52`: 8 bits optcode
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `swt $1 $2`: 3 byte Switches `$1 = $2` and `$2 = $1`
  - `0x52`: 8 bits optcode
  - `$1`: 5 bits register
  - 3 bits padding
  - `$2`: 5 bits register
## Configs
- The `$pc` starts at `0x00000400`