#data
.values = {'a', 22, 33, 44, 55}
.hello = "Hello World"

#main
lw $a [.hello]
dbg $a //test

li $d 10
li $a 2
sub $d $a

li $a 0
li $b 1
li $c 0

#loop
mov $11 $a
add $a $b
mov $b $11

li $9 1
sub $d $9
cmp $d $0
jls #finish

jmp #loop


#finish
add $b $a
dbg $b