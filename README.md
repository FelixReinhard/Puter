# Puter
## Registers - 16
- `0x00`: Always zero
- `0x01`: Program Counter `$pc`
- 
- `0x0d`: Jump Flag used when using compare.
- `0x0e`: Low bits of multiplication result `$LO`
- `0x0f`: High bits of multiplication result `$HI`
## Instruction Set
We will be using something similar to MIPS. For simplicity, we will fix the len of 
a instruction to one word (32 bit).
- `li $x {number}`: 4 byte long instruction to load an immediate to into a register.
  - `0x01`: 8 bits, optcode
  - `$x`: 4 bits, register
  - `{number}`: 20 bits, immediate number. 
- `lw $x [$y + {number}] `4 byte long instruction to load from memory at $y into register $x with an offset. 4 byte value
  - `0x02`: 8 bits, optcode
  - `$x`: 4 bits, register
  - `$y`: 4 bits, register
  - `{number}`: 16 bits, immediate number. 
- `lhw $x [$y + {number}] `4 byte long instruction to load from memory at $y into register $x with an offset. 2 byte value
  - `0x03`: 8 bits, optcode
  - `$x`: 4 bits, register
  - `$y`: 4 bits, register
  - `{number}`: 16 bits, immediate number.
- `lb $x [$y + {number}] `4 byte long instruction to load from memory at $y into register $x with an offset. 2 byte value
  - `0x04`: 8 bits, optcode
  - `$x`: 4 bits, register
  - `$y`: 4 bits, register
  - `{number}`: 16 bits, immediate number.
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
  - `0x53`: 8 bits optcode
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `mult $1 $2`: Multiply `$1` and `$2`. Saves the result in `$HI, $LO`
  - `0x54`: 8 bits register
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `div $1 $2`: Divide `$1` and `$2`. `$HI = $1 / $2, $LO = $1 MOD $2`
  - `0x55`: 8 bits register
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `and $1 $2`: Bitwise and for `$1` and `$2`.
  - `0x56`: 8 bits 
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `or $1 $2`: Bitwise or for `$1` and `$2`.
  - `0x57`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `xor $1 $2`: Bitwise exclusive orfor `$1` and `$2`.
  - `0x58`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `sl $1 $2`: Bitwise shift left for `$1` and `$2`.
  - `0x59`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `sr $1 $2`: Bitwise shift r for `$1` and `$2`.
  - `0x5a`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `land $1 $2`: Logical and for `$1` and `$2`.
  - `0x5b`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `lor $1 $2`: Logical and for `$1` and `$2`.
  - `0x5c`: 8 bits
  - `$1`: 4 bits register
  - `$2`: 4 bits register
- `flp $1`: Flip all bits in this register
  - `0x60`: 8 bits
  - `$1`: 4 bits register
  - 4 bits padding
- `not $1`: Logical not
  - `0x61`: 8 bits
  - `$1`: 4 bits register
  - 4 bits padding
### Branching:

- `jmp {number}` Jump to absolute address
  - `0x70` 8 bits 
  - `{number}`: immediate 24 bits (3 bytes)
- `jmp [$1 + {number}]` : Jump to address of register + offset
  - `0x71`: optcode 8 bits 
  - `$1`: 4 bit register
  - `{number}`: 20 bit immediate
### Conditional Branching
- `cmp $1 $2`: Compare two registers. The result is saved in `$CP`
  - `0x80`: 8bits
  - `$1`: 4bits
  - `$2`: 4bits


## Configs
- The `$pc` starts at `0x00000400`